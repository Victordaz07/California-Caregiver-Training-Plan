/**
 * Auth + profile state for Modo Turno, mirroring AppState's pub/sub shape
 * (js/state.js) but backed by Firebase Auth + a `users/{uid}` Firestore
 * profile document instead of localStorage — this module is shared across
 * devices and, for agency admins, across the whole team, so it can't live
 * in one browser's storage the way the 90-day course does.
 */
var AuthStore = (() => {
  let user = null; // null (signed out) | { uid, email, role, agencyId, displayName }
  let ready = false;
  const listeners = new Set();

  function notify() {
    listeners.forEach((fn) => fn(user));
  }

  function init() {
    FirebaseApp.init();
    if (!FirebaseApp.isConfigured()) {
      ready = true;
      return;
    }
    FirebaseApp.auth.onAuthStateChanged(async (fbUser) => {
      if (!fbUser) {
        user = null;
        ready = true;
        notify();
        return;
      }
      const snap = await FirebaseApp.db.collection("users").doc(fbUser.uid).get();
      const profile = snap.exists ? snap.data() : {};
      user = { uid: fbUser.uid, email: fbUser.email, role: profile.role || null, agencyId: profile.agencyId || null, displayName: profile.displayName || "" };
      ready = true;
      notify();
    });
  }

  async function signUpCaregiver({ email, password, displayName, agencyId }) {
    const cred = await FirebaseApp.auth.createUserWithEmailAndPassword(email, password);
    await FirebaseApp.db.collection("users").doc(cred.user.uid).set({
      role: "caregiver",
      agencyId: agencyId.trim(),
      displayName: displayName.trim(),
      createdAt: Date.now(),
    });
  }

  async function signUpAgency({ email, password, displayName, agencyName }) {
    const cred = await FirebaseApp.auth.createUserWithEmailAndPassword(email, password);
    const agencyRef = FirebaseApp.db.collection("agencies").doc();
    await agencyRef.set({ name: agencyName.trim(), ownerUid: cred.user.uid, createdAt: Date.now() });
    await FirebaseApp.db.collection("users").doc(cred.user.uid).set({
      role: "agency",
      agencyId: agencyRef.id,
      displayName: displayName.trim(),
      createdAt: Date.now(),
    });
  }

  /**
   * Someone who works alone, with no agency employing them, needs no code
   * from anyone else. Under the hood this is the same shape as an agency
   * account (role 'agency', owns a one-person "agency" record) — that's
   * exactly the permissions a solo worker needs: add their own patients,
   * manage their own shifts, nothing shared with anyone else.
   */
  async function signUpIndependent({ email, password, displayName }) {
    return signUpAgency({ email, password, displayName, agencyName: `${displayName.trim()} (independiente)` });
  }

  async function signIn({ email, password }) {
    await FirebaseApp.auth.signInWithEmailAndPassword(email, password);
  }

  async function signOut() {
    await FirebaseApp.auth.signOut();
  }

  function subscribe(fn) {
    listeners.add(fn);
    return () => listeners.delete(fn);
  }

  return {
    init,
    signUpCaregiver,
    signUpAgency,
    signUpIndependent,
    signIn,
    signOut,
    subscribe,
    get: () => user,
    isReady: () => ready,
  };
})();
window.AuthStore = AuthStore;
