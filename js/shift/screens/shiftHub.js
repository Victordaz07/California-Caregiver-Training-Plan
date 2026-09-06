/**
 * "Mis Pacientes" — the Modo Turno hub (tab root). Lists today's real
 * scheduled shifts (Firestore) for the signed-in caregiver, with an "EN
 * CURSO" card for whichever one is currently checked in. Web equivalent of
 * screen 11 in Caregiver Pro v3.dc.html.
 *
 * There's no "add patient" screen yet (that's the agency panel, task #40),
 * so an agency admin gets a one-time inline setup card here to create their
 * first patient and self-assign a test shift — enough to walk the whole
 * pipeline (ficha → geocerca → turno activo → salida) with real data instead
 * of fabricated placeholders.
 */
var Screens = window.Screens || {};

function shiftEmptyStateBody() {
  return `
    <div class="flex flex-col w-full px-margin-mobile pt-space-2xl pb-space-2xl gap-space-lg">
      <div class="flex flex-col gap-1">
        <h1 class="font-display text-4xl leading-none text-primary">¿Qué<br>necesitas hoy?</h1>
        <p class="font-body-md text-body-md text-outline mt-2">No tienes turnos programados todavía.</p>
      </div>
      <div class="flex flex-col gap-space-sm">
        <a href="#/shift/ayuda" class="flex gap-4 items-center bg-secondary rounded-2xl p-space-md shadow-[0_5px_0_var(--coral-dark)]">
          ${UI.icon("emergency_home", "text-4xl text-white")}
          <div class="flex-1"><div class="font-display text-xl text-white leading-tight">AYUDA AHORA</div><div class="font-body-sm text-body-sm text-white/85">Se cayó, no come, está agresiva…</div></div>
        </a>
        <a href="#/practica" class="flex gap-4 items-center bg-white border-[3px] border-primary rounded-2xl p-space-md shadow-[0_5px_0_var(--teal-dark)]">
          ${UI.icon("timer", "text-4xl text-primary")}
          <div class="flex-1"><div class="font-display text-xl text-primary leading-tight">APRENDER 5 MIN</div><div class="font-body-sm text-body-sm text-outline">Una lección corta, con audio</div></div>
        </a>
      </div>
    </div>`;
}

function firstSetupCardHtml() {
  return `
    <div class="mx-margin-mobile mt-space-md bg-primary-container border-2 border-primary/30 rounded-2xl p-space-md flex flex-col gap-space-sm">
      <div class="font-headline-sm text-headline-sm uppercase text-on-primary-container">Configuración inicial (agencia)</div>
      <p class="font-body-sm text-body-sm text-on-primary-container">Crea tu primer paciente para probar el módulo completo. Los pacientes reales y los turnos de tu equipo se administran desde el Panel de la Agencia.</p>
      <label class="flex flex-col gap-1"><span class="font-label-sm text-label-sm text-outline uppercase">Nombre del paciente</span><input id="setup-name" class="border-2 border-surface-container-high rounded-xl px-3 py-2 bg-white" placeholder="Doña Elena"/></label>
      <label class="flex flex-col gap-1"><span class="font-label-sm text-label-sm text-outline uppercase">Dirección</span><input id="setup-address" class="border-2 border-surface-container-high rounded-xl px-3 py-2 bg-white" placeholder="1420 W Olive Ave, Fresno"/></label>
      <button id="setup-use-location" class="font-label-md text-label-md text-primary underline self-start">Usar mi ubicación actual como geocerca</button>
      <span id="setup-location-status" class="font-body-sm text-body-sm text-outline"></span>
      ${UI.bigButton("Crear paciente y turno de prueba", { color: "teal", id: "setup-submit" })}
    </div>`;
}

Screens.shiftHub = async (data) => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };
  const user = AuthStore.get();
  const today = new Date();
  let shifts = [];
  let patientsById = {};
  try {
    shifts = await ShiftStore.listTodayShiftsForCaregiver(user.uid);
    const patients = await Promise.all(shifts.map((s) => ShiftStore.getPatient(user.agencyId, s.patientId)));
    patients.forEach((p, i) => {
      if (p) patientsById[shifts[i].patientId] = p;
    });
  } catch (err) {
    console.error("No se pudieron cargar los turnos de hoy:", err);
  }

  if (shifts.length === 0) {
    const setup = user.role === "agency" ? firstSetupCardHtml() : "";
    return {
      body: shiftEmptyStateBody() + setup,
      afterRender: () => wireSetupCard(user),
    };
  }

  const weekday = today.toLocaleDateString("es-ES", { weekday: "long", day: "numeric", month: "long" });

  const cardsHtml = shifts
    .map((shift) => {
      const patient = patientsById[shift.patientId];
      if (!patient) return "";
      const initial = (patient.name || "?").trim().charAt(0).toUpperCase();
      const timeLabel = `${new Date(shift.scheduledStart).toLocaleTimeString("es-ES", { hour: "2-digit", minute: "2-digit" })}–${new Date(shift.scheduledEnd).toLocaleTimeString("es-ES", { hour: "2-digit", minute: "2-digit" })}`;

      if (shift.status === "active") {
        const elapsedMin = shift.checkIn?.at ? Math.round((Date.now() - shift.checkIn.at) / 60000) : 0;
        return `<div class="border-[3px] rounded-3xl p-space-sm flex flex-col gap-space-sm" style="border-color:#2F9E5F; background:#F1FBF5; box-shadow:0 5px 0 #2F9E5F">
          <div class="flex gap-3 items-center">
            <div class="w-13 h-13 rounded-2xl flex items-center justify-center font-display text-xl" style="width:52px;height:52px;background:var(--tan);color:var(--tan-text)">${UI.escapeHtml(initial)}</div>
            <div class="flex-1 min-w-0"><div class="font-headline-sm text-headline-sm uppercase text-on-surface">${UI.escapeHtml(patient.name)}</div><div class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(patient.address || "")} · ${timeLabel}</div></div>
            <span class="flex items-center gap-1 text-white text-[10px] font-black uppercase px-2 py-1.5 rounded-full" style="background:#2F9E5F">${UI.icon("radio_button_checked", "text-sm")}En curso</span>
          </div>
          <div class="flex gap-2 items-center">
            <span class="flex-1 font-body-sm text-body-sm font-extrabold" style="color:#2F7A52">${elapsedMin} min en turno</span>
            <a href="#/shift/activo/${shift.id}" class="bg-primary text-white font-display text-sm tracking-wide px-4 py-2.5 rounded-xl">ABRIR TURNO</a>
          </div>
        </div>`;
      }

      return `<a href="#/shift/paciente/${shift.id}" class="flex gap-3 items-center bg-white border-2 border-surface-container-high rounded-2xl p-space-sm">
        <div class="w-12 h-12 rounded-2xl flex items-center justify-center font-display text-lg bg-primary-container text-on-primary-container">${UI.escapeHtml(initial)}</div>
        <div class="flex-1 min-w-0"><div class="font-headline-sm text-headline-sm uppercase text-on-surface">${UI.escapeHtml(patient.name)}</div><div class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(patient.address || "")} · ${timeLabel}</div></div>
        ${UI.icon("chevron_right", "text-outline-variant")}
      </a>`;
    })
    .join("");

  const body = `
    <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-md flex flex-col gap-space-sm">
      <div class="flex justify-between items-end">
        <div><div class="font-display text-2xl text-white leading-none">MIS PACIENTES</div><div class="font-body-sm text-body-sm capitalize" style="color:var(--mint)">${UI.escapeHtml(weekday)}</div></div>
      </div>
    </div>
    <div class="px-margin-mobile pt-space-md flex flex-col gap-space-sm pb-space-lg">
      ${cardsHtml}
      <div class="flex gap-space-sm items-start bg-primary-container rounded-2xl p-space-md mt-space-sm">
        ${UI.icon("schedule", "text-primary flex-shrink-0")}
        <p class="font-body-sm text-body-sm text-on-primary-container">El turno se abre solo a la hora asignada. No tienes que buscar nada.</p>
      </div>
    </div>`;

  return { body };
};

function wireSetupCard(user) {
  const btn = document.getElementById("setup-use-location");
  const submit = document.getElementById("setup-submit");
  if (!btn || !submit) return;
  let coords = null;
  const status = document.getElementById("setup-location-status");

  btn.onclick = () => {
    status.textContent = "Buscando tu ubicación…";
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        coords = { lat: pos.coords.latitude, lng: pos.coords.longitude };
        status.textContent = `Ubicación capturada (precisión ${Math.round(pos.coords.accuracy)} m).`;
      },
      (err) => {
        status.textContent = "No se pudo obtener tu ubicación: " + err.message;
      },
      { enableHighAccuracy: true, timeout: 10000 },
    );
  };

  submit.onclick = async () => {
    const name = document.getElementById("setup-name").value.trim();
    const address = document.getElementById("setup-address").value.trim();
    if (!name || !coords) {
      status.textContent = "Escribe un nombre y captura la ubicación primero.";
      return;
    }
    submit.textContent = "Creando…";
    try {
      const patientId = await ShiftStore.createPatient(user.agencyId, {
        name,
        address,
        lat: coords.lat,
        lng: coords.lng,
        geofenceRadiusMeters: 150,
      });
      const now = Date.now();
      await ShiftStore.createShift({
        agencyId: user.agencyId,
        patientId,
        caregiverUid: user.uid,
        scheduledStart: now,
        scheduledEnd: now + 4 * 60 * 60 * 1000,
        tasks: [],
      });
      window.location.hash = "#/shift";
      window.location.reload();
    } catch (err) {
      status.textContent = "No se pudo crear: " + err.message;
    }
  };
}

window.Screens = Screens;
