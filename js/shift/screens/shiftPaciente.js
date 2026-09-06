/**
 * Ficha del Paciente — screen 12 in Caregiver Pro v3.dc.html. Loaded from a
 * shiftId (not a bare patientId) because every entry point into this screen
 * already comes from a specific scheduled shift (Mis Pacientes today).
 */
var Screens = window.Screens || {};

Screens.shiftPaciente = async (data, params) => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };
  const user = AuthStore.get();
  const shift = await ShiftStore.getShift(params.id);
  if (!shift) return { body: `<div class="p-8 text-center text-outline">No se encontró este turno.</div>` };
  const patient = await ShiftStore.getPatient(user.agencyId, shift.patientId);
  if (!patient) return { body: `<div class="p-8 text-center text-outline">No se encontró el paciente.</div>` };

  const initial = (patient.name || "?").trim().charAt(0).toUpperCase();
  const pills = [];
  if (patient.allergies?.length) pills.push(`ALERGIA: ${patient.allergies.join(", ").toUpperCase()}`);
  if (patient.fallRisk) pills.push("RIESGO DE CAÍDA");

  const preferencesHtml = (patient.preferences || [])
    .map((p) => `<div class="flex gap-2.5 items-center"><span class="text-outline">${UI.icon(p.icon || "info")}</span><span class="font-body-md text-body-md font-extrabold text-on-surface">${UI.escapeHtml(p.label)}</span></div>`)
    .join("");

  const tasksHtml = (patient.tasks || [])
    .map((t) => `<div class="flex gap-2.5 items-center bg-white border-2 border-surface-container-high rounded-2xl px-3 py-2.5"><span class="text-primary">${UI.icon(t.icon || "task_alt")}</span><span class="flex-1 font-body-md text-body-md font-extrabold text-on-surface">${UI.escapeHtml(t.label)}</span></div>`)
    .join("");

  const body = `
    <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-xl pb-space-md flex flex-col gap-space-sm">
      <div class="flex items-center gap-3 pl-12">
        <div class="w-14 h-14 flex-shrink-0 rounded-2xl flex items-center justify-center font-display text-2xl" style="background:var(--tan); color:var(--tan-text)">${UI.escapeHtml(initial)}</div>
        <div class="flex-1 min-w-0">
          <div class="font-display text-2xl text-white leading-tight">${UI.escapeHtml(patient.name)}</div>
          ${patient.ageYears || patient.mobilityAid ? `<div class="font-body-sm text-body-sm" style="color:var(--mint)">${[patient.ageYears ? patient.ageYears + " años" : "", patient.mobilityAid, ...(patient.conditions || [])].filter(Boolean).join(" · ")}</div>` : ""}
        </div>
      </div>
      ${pills.length ? `<div class="flex gap-2 flex-wrap">${pills.map((p) => `<span class="bg-white/15 text-white text-[11px] font-black px-2.5 py-1.5 rounded-full">${UI.escapeHtml(p)}</span>`).join("")}</div>` : ""}
    </div>

    <div class="px-margin-mobile pt-space-md flex flex-col gap-space-md pb-space-2xl">
      ${
        preferencesHtml
          ? `<div class="rounded-2xl p-space-sm flex flex-col gap-2.5" style="background:var(--cream); border:2px solid var(--cream-border)">
        <div class="text-[11px] font-black tracking-wide" style="color:var(--tan-text)">CÓMO LE GUSTA</div>
        ${preferencesHtml}
      </div>`
          : ""
      }

      <div class="flex flex-col gap-2">
        <div class="text-[11px] font-black tracking-wide text-outline">TAREAS ASIGNADAS${patient.tasks?.length ? " · " + patient.tasks.length : ""}</div>
        ${tasksHtml || `<p class="font-body-sm text-body-sm text-outline">Tu agencia todavía no asignó tareas para este paciente.</p>`}
      </div>
    </div>

    <div class="fixed bottom-0 left-0 right-0 border-t border-surface-container-high bg-white px-margin-mobile py-space-sm pb-safe">
      ${UI.bigButton("Entrar al turno", { color: "coral", id: "btn-entrar-turno" })}
    </div>`;

  return {
    body,
    afterRender: () => {
      document.getElementById("btn-entrar-turno").onclick = () => {
        window.location.hash = `#/shift/llegada/${shift.id}`;
      };
    },
  };
};

window.Screens = Screens;
