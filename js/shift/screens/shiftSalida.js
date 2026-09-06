/**
 * Salida y Resumen — screen 15 in Caregiver Pro v3.dc.html, folded together
 * with screen 10's mood/hard-parts reflection so check-out is one screen
 * instead of two. Captures a real checkout geolocation fix, then an
 * optional family PIN and a personal shift reflection, and closes the
 * shift (ShiftStore.submitShiftSummary).
 */
var Screens = window.Screens || {};

const MOOD_DEFS = [
  { key: "bien", icon: "sentiment_very_satisfied", label: "BIEN" },
  { key: "normal", icon: "sentiment_neutral", label: "NORMAL" },
  { key: "pesado", icon: "sentiment_dissatisfied", label: "PESADO" },
];
const HARD_PART_LABELS = ["El baño", "Se resistió", "Mi espalda", "El inglés", "El tiempo"];

Screens.shiftSalida = async (data, params) => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };
  const user = AuthStore.get();
  const shift = await ShiftStore.getShift(params.shiftId);
  if (!shift) return { body: `<div class="p-8 text-center text-outline">No se encontró este turno.</div>` };
  const patient = await ShiftStore.getPatient(user.agencyId, shift.patientId);

  const body = `
    <div id="salida-step-checkout">
      <div class="rounded-b-3xl px-margin-mobile pt-space-xl pb-space-lg flex flex-col items-center gap-space-sm text-center" style="background:var(--teal-dark)">
        ${UI.icon("logout", "text-4xl text-white")}
        <div class="font-display text-2xl text-white">REGISTRAR SALIDA</div>
        <div class="font-body-sm text-body-sm" style="color:var(--mint)">${UI.escapeHtml(patient?.name || "")}</div>
        <span id="checkout-gps-status" class="font-body-sm text-body-sm text-white/70">Confirmando tu ubicación…</span>
      </div>
      <div class="px-margin-mobile pt-space-lg pb-space-2xl">
        <div id="btn-checkout" class="text-center font-display text-lg tracking-wide py-space-md rounded-2xl text-white opacity-50" style="background:var(--coral-dark)">CONFIRMAR SALIDA</div>
      </div>
    </div>

    <div id="salida-step-summary" class="hidden px-margin-mobile pt-space-xl pb-space-2xl flex flex-col gap-space-lg">
      <div>
        <div class="text-[11px] font-black tracking-wide text-primary">CIERRE DE TURNO</div>
        <div class="font-display text-3xl text-primary leading-tight mt-1">¿Cómo te fue?</div>
      </div>

      <div class="flex gap-2.5">
        ${MOOD_DEFS.map((m) => `<button data-mood="${m.key}" class="mood-btn flex-1 aspect-square rounded-3xl flex flex-col items-center justify-center gap-1.5 border-2 border-surface-container-high bg-white text-outline">${UI.icon(m.icon, "text-4xl")}<span class="text-xs font-black">${m.label}</span></button>`).join("")}
      </div>

      <div class="flex flex-col gap-2.5">
        <div class="text-[11px] font-black tracking-wide text-outline">¿QUÉ COSTÓ MÁS? (OPCIONAL)</div>
        <div class="flex gap-2 flex-wrap">
          ${HARD_PART_LABELS.map((label) => `<button data-hard="${UI.escapeHtml(label)}" class="hard-btn font-body-sm text-body-sm font-extrabold px-3.5 py-2.5 rounded-full bg-white border-2 border-surface-container-high text-outline">${UI.escapeHtml(label)}</button>`).join("")}
        </div>
      </div>

      <label class="flex flex-col gap-1">
        <span class="font-label-sm text-label-sm text-outline uppercase">PIN de confirmación familiar (opcional)</span>
        <input id="family-pin" maxlength="4" inputmode="numeric" class="border-2 border-surface-container-high rounded-xl px-4 py-3 text-center tracking-[0.5em] font-display text-2xl" placeholder="----"/>
        <span class="font-body-sm text-body-sm text-outline">Si no hay nadie en casa, puedes enviarlo sin PIN y la agencia lo revisa.</span>
      </label>

      ${UI.bigButton("Enviar a la agencia", { color: "teal", id: "btn-submit-summary" })}
    </div>`;

  return {
    body,
    afterRender: () => {
      let checkoutFix = null;
      let selectedMood = null;
      const hardParts = new Set();

      const gpsStatus = document.getElementById("checkout-gps-status");
      const checkoutBtn = document.getElementById("btn-checkout");
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          checkoutFix = { lat: pos.coords.latitude, lng: pos.coords.longitude, accuracy: pos.coords.accuracy };
          gpsStatus.textContent = `Ubicación confirmada (±${Math.round(pos.coords.accuracy)} m).`;
          checkoutBtn.classList.remove("opacity-50");
        },
        (err) => {
          gpsStatus.textContent = "Sin señal GPS (" + err.message + "). Puedes salir sin ella.";
          checkoutFix = { lat: null, lng: null, accuracy: null };
          checkoutBtn.classList.remove("opacity-50");
        },
        { enableHighAccuracy: true, timeout: 12000 },
      );

      checkoutBtn.onclick = async () => {
        if (!checkoutFix) return;
        checkoutBtn.textContent = "Registrando…";
        try {
          await ShiftStore.checkOut(shift.id, checkoutFix);
          document.getElementById("salida-step-checkout").classList.add("hidden");
          document.getElementById("salida-step-summary").classList.remove("hidden");
        } catch (err) {
          checkoutBtn.textContent = "CONFIRMAR SALIDA";
          alert("No se pudo registrar la salida: " + err.message);
        }
      };

      document.querySelectorAll(".mood-btn").forEach((btn) => {
        btn.onclick = () => {
          selectedMood = btn.dataset.mood;
          document.querySelectorAll(".mood-btn").forEach((b) => {
            const active = b === btn;
            b.classList.toggle("bg-primary", active);
            b.classList.toggle("text-white", active);
            b.classList.toggle("border-primary", active);
            b.classList.toggle("bg-white", !active);
            b.classList.toggle("text-outline", !active);
            b.classList.toggle("border-surface-container-high", !active);
          });
        };
      });

      document.querySelectorAll(".hard-btn").forEach((btn) => {
        btn.onclick = () => {
          const label = btn.dataset.hard;
          if (hardParts.has(label)) {
            hardParts.delete(label);
            btn.classList.remove("bg-primary", "text-white", "border-primary");
            btn.classList.add("bg-white", "text-outline", "border-surface-container-high");
          } else {
            hardParts.add(label);
            btn.classList.add("bg-primary", "text-white", "border-primary");
            btn.classList.remove("bg-white", "text-outline", "border-surface-container-high");
          }
        };
      });

      document.getElementById("btn-submit-summary").onclick = async () => {
        const pin = document.getElementById("family-pin").value.trim();
        try {
          await ShiftStore.submitShiftSummary(shift.id, {
            familyPin: pin || null,
            familyConfirmed: pin.length === 4,
            mood: selectedMood,
            hardParts: Array.from(hardParts),
          });
          window.location.hash = "#/shift";
        } catch (err) {
          alert("No se pudo enviar: " + err.message);
        }
      };
    },
  };
};

window.Screens = Screens;
