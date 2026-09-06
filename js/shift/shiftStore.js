/**
 * Firestore data layer for Modo Turno — the on-shift assistant module
 * (patients, geofenced check-in/out, hours, agency panel). Mirrors
 * js/data.js's role for the 90-day course, but reads/writes a shared
 * Firestore database instead of a local JSON bundle, because this data has
 * to be visible to more than one person (a caregiver and their agency).
 *
 * Collections (see firestore.rules for the full model + access rules):
 *   users/{uid}                          role, agencyId, displayName
 *   agencies/{agencyId}                  name, ownerUid
 *   agencies/{agencyId}/patients/{id}     geofence + care-plan fields
 *   shifts/{id}                          one caregiver's visit to one patient
 */
var ShiftStore = (() => {
  function db() {
    return FirebaseApp.db;
  }

  // ---- distance / geofence -------------------------------------------------

  /** Haversine distance in meters between two lat/lng points. */
  function distanceMeters(lat1, lng1, lat2, lng2) {
    const R = 6371000;
    const toRad = (deg) => (deg * Math.PI) / 180;
    const dLat = toRad(lat2 - lat1);
    const dLng = toRad(lng2 - lng1);
    const a = Math.sin(dLat / 2) ** 2 + Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * Math.sin(dLng / 2) ** 2;
    return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }

  function isWithinGeofence(patient, lat, lng) {
    return distanceMeters(patient.lat, patient.lng, lat, lng) <= (patient.geofenceRadiusMeters || 150);
  }

  // ---- patients --------------------------------------------------------

  async function listPatients(agencyId) {
    const snap = await db().collection("agencies").doc(agencyId).collection("patients").get();
    return snap.docs.map((d) => ({ id: d.id, ...d.data() }));
  }

  async function getPatient(agencyId, patientId) {
    const doc = await db().collection("agencies").doc(agencyId).collection("patients").doc(patientId).get();
    return doc.exists ? { id: doc.id, ...doc.data() } : null;
  }

  /** Agency admin only (enforced by firestore.rules). */
  async function createPatient(agencyId, patient) {
    const ref = await db().collection("agencies").doc(agencyId).collection("patients").add({ ...patient, createdAt: Date.now() });
    return ref.id;
  }

  async function updatePatient(agencyId, patientId, fields) {
    await db().collection("agencies").doc(agencyId).collection("patients").doc(patientId).update(fields);
  }

  // ---- shifts ------------------------------------------------------------

  function startOfDay(date = new Date()) {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return d.getTime();
  }

  function endOfDay(date = new Date()) {
    const d = new Date(date);
    d.setHours(23, 59, 59, 999);
    return d.getTime();
  }

  /** Shifts scheduled for one caregiver today, soonest first. */
  async function listTodayShiftsForCaregiver(caregiverUid) {
    const snap = await db()
      .collection("shifts")
      .where("caregiverUid", "==", caregiverUid)
      .where("scheduledStart", ">=", startOfDay())
      .where("scheduledStart", "<=", endOfDay())
      .orderBy("scheduledStart")
      .get();
    return snap.docs.map((d) => ({ id: d.id, ...d.data() }));
  }

  /** The one shift currently checked in (status 'active') for a caregiver, if any. */
  async function getActiveShift(caregiverUid) {
    const snap = await db().collection("shifts").where("caregiverUid", "==", caregiverUid).where("status", "==", "active").limit(1).get();
    return snap.empty ? null : { id: snap.docs[0].id, ...snap.docs[0].data() };
  }

  async function getShift(shiftId) {
    const doc = await db().collection("shifts").doc(shiftId).get();
    return doc.exists ? { id: doc.id, ...doc.data() } : null;
  }

  /** Agency admin (or a caregiver scheduling their own visit) creates a planned shift. */
  async function createShift({ agencyId, patientId, caregiverUid, scheduledStart, scheduledEnd, tasks }) {
    const ref = await db()
      .collection("shifts")
      .add({
        agencyId,
        patientId,
        caregiverUid,
        scheduledStart,
        scheduledEnd,
        status: "scheduled",
        tasksCompleted: [],
        tasks: tasks || [],
        createdAt: Date.now(),
      });
    return ref.id;
  }

  /** Caregiver checks in — records geolocation and whether it was inside the patient's geofence. */
  async function checkIn(shiftId, { lat, lng, accuracy, withinGeofence }) {
    await db().collection("shifts").doc(shiftId).update({
      status: "active",
      checkIn: { at: Date.now(), lat, lng, accuracy, withinGeofence },
    });
  }

  async function toggleShiftTask(shiftId, taskId, done) {
    const ref = db().collection("shifts").doc(shiftId);
    await db().runTransaction(async (tx) => {
      const doc = await tx.get(ref);
      const current = new Set(doc.data().tasksCompleted || []);
      if (done) current.add(taskId);
      else current.delete(taskId);
      tx.update(ref, { tasksCompleted: Array.from(current) });
    });
  }

  /** Caregiver checks out — records geolocation, total minutes, and closes the shift. */
  async function checkOut(shiftId, { lat, lng, accuracy }) {
    const shift = await getShift(shiftId);
    const at = Date.now();
    const totalMinutes = shift?.checkIn?.at ? Math.round((at - shift.checkIn.at) / 60000) : null;
    await db().collection("shifts").doc(shiftId).update({
      status: "in_review",
      checkOut: { at, lat, lng, accuracy },
      totalMinutes,
    });
  }

  /** Family PIN confirmation + mood reflection, submitted to the agency. */
  async function submitShiftSummary(shiftId, { familyPin, familyConfirmed, mood, hardParts }) {
    await db().collection("shifts").doc(shiftId).update({
      status: "closed",
      familyPin: familyPin || null,
      familyConfirmed: !!familyConfirmed,
      mood: mood ?? null,
      hardParts: hardParts || [],
      closedAt: Date.now(),
    });
  }

  /** A caregiver's shift history for "Mis Horas" (most recent first). */
  async function listShiftsForCaregiver(caregiverUid, limit = 30) {
    const snap = await db().collection("shifts").where("caregiverUid", "==", caregiverUid).orderBy("scheduledStart", "desc").limit(limit).get();
    return snap.docs.map((d) => ({ id: d.id, ...d.data() }));
  }

  /** Agency-wide shift list for today, for the agency panel's live roster. */
  async function listTodayShiftsForAgency(agencyId) {
    const snap = await db()
      .collection("shifts")
      .where("agencyId", "==", agencyId)
      .where("scheduledStart", ">=", startOfDay())
      .where("scheduledStart", "<=", endOfDay())
      .get();
    return snap.docs.map((d) => ({ id: d.id, ...d.data() }));
  }

  async function listCaregivers(agencyId) {
    const snap = await db().collection("users").where("agencyId", "==", agencyId).where("role", "==", "caregiver").get();
    return snap.docs.map((d) => ({ uid: d.id, ...d.data() }));
  }

  return {
    distanceMeters,
    isWithinGeofence,
    listPatients,
    getPatient,
    createPatient,
    updatePatient,
    listTodayShiftsForCaregiver,
    getActiveShift,
    getShift,
    createShift,
    checkIn,
    toggleShiftTask,
    checkOut,
    submitShiftSummary,
    listShiftsForCaregiver,
    listTodayShiftsForAgency,
    listCaregivers,
  };
})();
window.ShiftStore = ShiftStore;
