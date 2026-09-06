/**
 * Llegada con Geocerca — screen 13 in Caregiver Pro v3.dc.html. Uses the
 * real browser Geolocation API (no fake GPS) and ShiftStore's Haversine
 * distance check against the patient's stored lat/lng. Check-in is allowed
 * even outside the geofence (weak signal happens) — the true/false result
 * is just recorded on the shift so the agency panel can flag it later,
 * never used to block a caregiver from clocking in.
 */
var Screens = window.Screens || {};

Screens.shiftLlegada = async (data, params) => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };
  const user = AuthStore.get();
  const shift = await ShiftStore.getShift(params.shiftId);
  if (!shift) return { body: `<div class="p-8 text-center text-outline">No se encontró este turno.</div>` };
  const patient = await ShiftStore.getPatient(user.agencyId, shift.patientId);

  const body = `
    <div class="min-h-screen flex flex-col" style="background:var(--teal-dark)">
      <div class="px-margin-mobile pt-space-xl flex items-center justify-between">
        <span class="text-[11px] font-black tracking-wide" style="color:var(--mint)">REGISTRAR LLEGADA</span>
        <span id="gps-badge" class="flex items-center gap-1.5 text-[10px] font-black px-2.5 py-1.5 rounded-full" style="background:rgba(47,158,95,.22); color:#8FE3B4">${UI.icon("gps_not_fixed", "text-sm")}<span id="gps-badge-text">Buscando GPS…</span></span>
      </div>

      <div class="flex-1 flex flex-col items-center gap-space-lg pt-space-lg px-margin-mobile text-center">
        <div>
          <div class="font-display text-3xl text-white leading-tight">${UI.escapeHtml(patient?.name || "Paciente")}</div>
          <div class="font-body-sm text-body-sm mt-1" style="color:var(--mint)">${UI.escapeHtml(patient?.address || "")}</div>
        </div>

        <div class="relative w-60 h-60 flex items-center justify-center">
          <div class="absolute inset-0 rounded-full border-2 border-dashed" style="border-color:rgba(143,211,204,.45)"></div>
          <div id="zone-ring-outer" class="absolute rounded-full" style="width:170px;height:170px;background:rgba(138,155,158,.16);border:2px solid rgba(138,155,158,.5)"></div>
          <div id="zone-ring-inner" class="absolute rounded-full" style="width:100px;height:100px;background:rgba(138,155,158,.26)"></div>
          <div id="zone-dot" class="relative w-[74px] h-[74px] rounded-full flex items-center justify-center text-white" style="background:#8A9B9E">${UI.icon("home_pin", "text-4xl")}</div>
          <div id="zone-label" class="absolute bottom-1.5 bg-white text-[11px] font-black px-3 py-1.5 rounded-full" style="color:#5C7176">Ubicando…</div>
        </div>

        <div>
          <div id="arrival-time" class="font-display text-5xl text-white leading-none">--:--</div>
          <div class="text-xs font-black tracking-wide mt-1" style="color:var(--mint)">HORA DE LLEGADA</div>
        </div>
      </div>

      <div class="px-margin-mobile pb-space-xl pt-space-md flex flex-col gap-space-sm">
        <div id="btn-entrar" class="text-center font-display text-xl tracking-wide py-space-md rounded-2xl text-white opacity-50" style="background:var(--coral)">ENTRAR AL TURNO</div>
        <div class="flex gap-2.5 items-start bg-white/10 rounded-2xl p-space-sm">
          ${UI.icon("privacy_tip", "text-white/70 flex-shrink-0")}
          <p class="font-body-sm text-body-sm" style="color:#CFE6EC">Solo se guarda la hora y que estabas en el domicilio. No se registra tu ubicación durante el turno.</p>
        </div>
      </div>
    </div>`;

  return {
    body,
    afterRender: () => {
      let lastFix = null;
      const btn = document.getElementById("btn-entrar");
      const badgeText = document.getElementById("gps-badge-text");
      const zoneLabel = document.getElementById("zone-label");
      const zoneDot = document.getElementById("zone-dot");

      function updateFix(pos) {
        const { latitude: lat, longitude: lng, accuracy } = pos.coords;
        const withinGeofence = patient ? ShiftStore.isWithinGeofence(patient, lat, lng) : false;
        lastFix = { lat, lng, accuracy, withinGeofence };

        badgeText.textContent = `GPS ${accuracy < 30 ? "preciso" : "aproximado"} · ${Math.round(accuracy)} m`;
        zoneLabel.textContent = withinGeofence ? "ESTÁS EN LA ZONA" : "FUERA DE LA ZONA";
        zoneLabel.style.color = withinGeofence ? "#2F7A52" : "#C7422F";
        zoneDot.style.background = withinGeofence ? "#2F9E5F" : "#8A9B9E";

        document.getElementById("arrival-time").textContent = new Date().toLocaleTimeString("es-ES", { hour: "2-digit", minute: "2-digit" });
        btn.classList.remove("opacity-50");
      }

      const watchId = navigator.geolocation.watchPosition(
        updateFix,
        (err) => {
          badgeText.textContent = "Sin señal GPS: " + err.message;
        },
        { enableHighAccuracy: true, maximumAge: 5000, timeout: 15000 },
      );

      btn.onclick = async () => {
        if (!lastFix) return;
        navigator.geolocation.clearWatch(watchId);
        btn.textContent = "Entrando…";
        try {
          await ShiftStore.checkIn(shift.id, lastFix);
          window.location.hash = `#/shift/activo/${shift.id}`;
        } catch (err) {
          btn.textContent = "ENTRAR AL TURNO";
          alert("No se pudo registrar la llegada: " + err.message);
        }
      };
    },
  };
};

window.Screens = Screens;
