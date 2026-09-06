/**
 * Web equivalent of ui/screens/SimulacionTurnoScreen.kt. Visual language:
 * "Caregiver Pro v2" redesign — dark header with live progress, tappable
 * checklist rows (real toggle state, not just static text), warning banner,
 * plus real self-recording — no fake real-time percentages.
 */
var Screens = window.Screens || {};

const HANDOFF_TASKS = [
  { id: "situacion", label: "Di la hora y el contexto breve", icon: "schedule" },
  { id: "hechos", label: "Describe solo hechos observados, sin diagnosticar", icon: "visibility" },
  { id: "accion", label: "Explica qué acción tomaste", icon: "task_alt" },
  { id: "pendiente", label: "Di qué queda pendiente o a quién avisaste", icon: "forward_to_inbox" },
  { id: "privacidad", label: "Comparte solo con quien está autorizado", icon: "lock" },
  { id: "sin_omitir", label: "No omitiste ningún hecho relevante", icon: "playlist_add_check" },
];

let turnoChecked = new Set();

Screens.turno = async (data) => {
  const handoffRubrics = ["objective_facts", "chronology", "action_and_notification"]
    .map((id) => DataStore.rubricCriterion(data, id))
    .filter(Boolean);

  const pct = Math.round((turnoChecked.size / HANDOFF_TASKS.length) * 100);

  const body = `
    <div class="flex flex-col w-full gap-space-lg pb-space-2xl">
      <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-md flex flex-col gap-space-sm">
        <div class="flex items-center gap-2 text-xs font-extrabold tracking-wide" style="color:rgba(255,255,255,.65)">
          ${UI.icon("schedule", "text-base")}<span>ENTREGA DE TURNO</span>
        </div>
        <div class="font-display text-3xl text-white leading-tight">Checklist de<br>Entrega</div>
        <div class="flex items-center gap-space-sm">
          <div class="flex-1 h-2.5 rounded-full overflow-hidden" style="background:rgba(255,255,255,.18)">
            <div id="turno-fill" class="h-full rounded-full bg-secondary transition-all duration-300" style="width:${pct}%"></div>
          </div>
          <span id="turno-count" class="text-white text-sm font-black">${turnoChecked.size}/${HANDOFF_TASKS.length}</span>
        </div>
      </div>

      <div class="px-margin-mobile flex flex-col gap-space-sm">
        <div id="turno-tasks" class="flex flex-col gap-2">
          ${HANDOFF_TASKS.map(
            (t) => `<button data-task="${t.id}" class="flex items-center gap-space-sm bg-white border-2 border-surface-container-high rounded-2xl p-space-sm text-left">
              <span data-check class="w-8 h-8 flex-shrink-0 rounded-lg flex items-center justify-center ${turnoChecked.has(t.id) ? "" : "border-2"}" style="${turnoChecked.has(t.id) ? "background:var(--success)" : "border-color:var(--lock)"}">
                ${turnoChecked.has(t.id) ? `<span class="text-white">${UI.icon("check", "text-lg")}</span>` : ""}
              </span>
              <span class="flex-1 font-body-md text-body-md font-extrabold text-on-surface">${UI.escapeHtml(t.label)}</span>
              <span class="text-outline-variant">${UI.icon(t.icon)}</span>
            </button>`,
          ).join("")}
        </div>

        ${UI.sectionCard(`
          <h3 class="font-headline-sm text-headline-sm uppercase text-on-surface">Criterios reales de esta app</h3>
          ${handoffRubrics.map((r) => `<p class="font-body-sm text-body-sm text-outline"><strong class="text-on-surface">${UI.escapeHtml(r.labelEs)}:</strong> ${UI.escapeHtml(r.descriptionEs)}</p>`).join("")}
        `)}

        <div id="vrc"></div>

        <button id="turno-finish" class="btn-solid btn-teal py-4 text-lg" ${turnoChecked.size < HANDOFF_TASKS.length ? "disabled style='opacity:.5'" : ""}>Terminar Entrega</button>
      </div>

      <div class="mx-margin-mobile flex items-center gap-space-sm rounded-2xl p-space-md" style="background:var(--coral)">
        ${UI.icon("warning", "text-white text-2xl flex-shrink-0")}
        <p class="text-white font-extrabold text-sm leading-tight">Si hay peligro inmediato, llama al 911 antes de terminar esta entrega.</p>
      </div>
    </div>`;

  return {
    body,
    afterRender: () => {
      document.getElementById("vrc").innerHTML = voiceRecordCardHtml("vrc-turno");
      attachVoiceRecordCard("vrc-turno", "Grábate haciendo una entrega de turno de 60 segundos sobre un caso ficticio.");

      document.querySelectorAll("[data-task]").forEach((btn) => {
        btn.onclick = () => {
          const id = btn.dataset.task;
          if (turnoChecked.has(id)) turnoChecked.delete(id);
          else turnoChecked.add(id);
          const pctNow = Math.round((turnoChecked.size / HANDOFF_TASKS.length) * 100);
          document.getElementById("turno-fill").style.width = `${pctNow}%`;
          document.getElementById("turno-count").textContent = `${turnoChecked.size}/${HANDOFF_TASKS.length}`;
          const check = btn.querySelector("[data-check]");
          if (turnoChecked.has(id)) {
            check.style.background = "var(--success)";
            check.style.borderWidth = "0";
            check.innerHTML = `<span class="text-white">${UI.icon("check", "text-lg")}</span>`;
          } else {
            check.style.background = "transparent";
            check.style.borderWidth = "2px";
            check.style.borderColor = "var(--lock)";
            check.innerHTML = "";
          }
          const finishBtn = document.getElementById("turno-finish");
          if (turnoChecked.size >= HANDOFF_TASKS.length) {
            finishBtn.removeAttribute("disabled");
            finishBtn.style.opacity = "1";
          } else {
            finishBtn.setAttribute("disabled", "");
            finishBtn.style.opacity = ".5";
          }
        };
      });

      document.getElementById("turno-finish").onclick = () => {
        if (turnoChecked.size < HANDOFF_TASKS.length) return;
        turnoChecked = new Set();
        window.location.hash = "#/practica";
      };
    },
  };
};

window.Screens = Screens;
