/**
 * Web equivalent of ui/screens/RutinaDiariaScreen.kt: a real 75-minute
 * session in 5 timed blocks. Marks the day completed in AppState
 * (localStorage) when finished. Shows the real teaching lesson
 * (data.curriculum[].lessonEs, generated per
 * docs/caregiver_upgrade/CURRICULUM_LESSON_CONTENT_PROMPT.md) before the
 * practice activity, closing the "practice with no teaching" gap this
 * screen originally had.
 */
var Screens = window.Screens || {};

const SESSION_BLOCKS = [
  { name: "Recuerdo activo", minutes: 10 },
  { name: "Estudio enfocado", minutes: 15 },
  { name: "Práctica deliberada", minutes: 25 },
  { name: "Explicación Feynman", minutes: 10 },
  { name: "Repetición espaciada", minutes: 15 },
];
const TOTAL_MINUTES = SESSION_BLOCKS.reduce((s, b) => s + b.minutes, 0);

let hoyTimer = null;

Screens.hoy = async (data) => {
  const state = AppState.get();
  const day = state.currentPlanDay;
  const curriculumDay = DataStore.curriculumDayFor(data, day) || data.curriculum[0];
  const moduleTitle = data.module_titles[curriculumDay.moduleId] || curriculumDay.moduleId;
  const alreadyDone = AppState.isDayCompleted(day);
  const illustration = data.illustrations[String(day)];

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      <div class="relative overflow-hidden rounded-xl bg-gradient-to-br from-primary-container to-primary p-space-md text-on-primary shadow-sm">
        <div class="flex flex-col gap-space-xxs z-10 relative">
          <div class="flex items-center gap-space-xs">
            ${UI.statusPill(`Semana ${curriculumDay.week} · ${moduleTitle}`, { bg: "bg-primary-fixed", color: "text-on-primary-fixed-variant" })}
            <span class="text-on-primary-container font-label-sm text-label-sm">Día ${day} de 90</span>
          </div>
          <h2 class="font-headline-md text-headline-md text-on-primary leading-tight pt-1">${UI.escapeHtml(curriculumDay.titleEs)}</h2>
          <p class="font-body-sm text-body-sm text-on-primary-container">${UI.escapeHtml(curriculumDay.objectiveEs)}</p>
        </div>
      </div>

      ${UI.sectionCard(`
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-space-xs">
            <div class="w-10 h-10 rounded-full bg-secondary-fixed flex items-center justify-center text-on-secondary-fixed-variant">${UI.icon("timer")}</div>
            <div>
              <span class="font-label-sm text-label-sm text-outline uppercase tracking-wider block">Sesión Guiada</span>
              <h3 class="font-headline-sm text-headline-sm text-on-surface">Rutina de Hoy</h3>
            </div>
          </div>
          <div class="text-right">
            <span class="font-label-lg text-label-lg font-semibold text-primary" id="routine-time-text">0 / ${TOTAL_MINUTES} min</span>
            <span class="block font-label-sm text-label-sm text-outline" id="routine-block-text">${alreadyDone ? "Día completado" : "Sin iniciar"}</span>
          </div>
        </div>
        <div class="flex flex-col gap-1.5">
          <div class="w-full bg-surface-container-high h-2.5 rounded-full overflow-hidden">
            <div class="h-full bg-secondary transition-all duration-500 rounded-full" id="routine-progress-fill" style="width: ${alreadyDone ? 100 : 0}%"></div>
          </div>
        </div>
        <div class="flex items-center justify-between gap-space-sm pt-1">
          <div class="flex items-center gap-1.5 text-on-surface-variant font-body-sm text-body-sm">
            ${UI.icon("verified_user", "text-primary text-base")}
            <span>5 bloques estructurados</span>
          </div>
          <button id="start-session-btn" class="h-12 px-space-lg bg-secondary text-on-secondary rounded-lg font-headline-sm text-headline-sm flex items-center justify-center gap-2" ${alreadyDone ? "disabled" : ""}>
            ${UI.icon("play_arrow")}<span>${alreadyDone ? "Completado" : "Iniciar Sesión"}</span>
          </button>
        </div>
      `)}

      ${UI.sectionCard(`
        <div class="flex items-center gap-space-xs">
          ${UI.icon("menu_book", "text-primary")}
          <h3 class="font-headline-sm text-headline-sm text-on-surface">Lección de hoy</h3>
        </div>
        <p class="font-body-md text-body-md text-on-surface leading-relaxed">${UI.escapeHtml(curriculumDay.lessonEs || "")}</p>
      `)}

      ${
        illustration
          ? UI.sectionCard(`
              <div class="flex items-center gap-space-xs">
                ${UI.icon("auto_stories", "text-primary")}
                <h3 class="font-headline-sm text-headline-sm text-on-surface">Paso a paso ilustrado</h3>
              </div>
              <img src="assets/illustrations/${illustration.file}" alt="${UI.escapeHtml(illustration.title)}" class="w-full rounded-lg border border-outline-variant/40"/>
            `)
          : ""
      }

      ${UI.sectionCard(`
        <h3 class="font-headline-sm text-headline-sm text-on-surface">Actividad de práctica</h3>
        <p class="font-body-md text-body-md text-on-surface-variant">${UI.escapeHtml(curriculumDay.practiceEs)}</p>
      `)}

      ${curriculumDay.criticalSafety ? UI.disclaimerBanner("Emergency", data.disclaimers) : ""}

      <div class="flex flex-col gap-space-sm">
        <h3 class="font-headline-md text-headline-md text-on-surface">Bloques de Entrenamiento</h3>
        ${SESSION_BLOCKS.map(
          (b, i) => `<div class="flex items-center justify-between bg-surface-container-lowest rounded-lg p-space-sm" data-block-row="${i}">
            <span class="font-body-md text-body-md text-on-surface">${i + 1}. ${b.name}</span>
            <span class="font-label-sm text-label-sm text-outline">${b.minutes} min</span>
          </div>`,
        ).join("")}
      </div>
    </div>`;

  return {
    body,
    afterRender: () => {
      if (hoyTimer) {
        clearInterval(hoyTimer);
        hoyTimer = null;
      }
      const startBtn = document.getElementById("start-session-btn");
      if (!startBtn || alreadyDone) return;

      startBtn.onclick = () => {
        startBtn.disabled = true;
        startBtn.innerHTML = `${UI.icon("hourglass_top")}<span>En progreso…</span>`;
        let elapsedSeconds = 0;
        const totalSeconds = TOTAL_MINUTES * 60;

        hoyTimer = setInterval(() => {
          const timeText = document.getElementById("routine-time-text");
          const blockText = document.getElementById("routine-block-text");
          const fill = document.getElementById("routine-progress-fill");
          if (!timeText) {
            clearInterval(hoyTimer);
            hoyTimer = null;
            return;
          }

          elapsedSeconds += 1;
          const elapsedMinutes = elapsedSeconds / 60;
          let cumulative = 0;
          let currentBlockIndex = SESSION_BLOCKS.length - 1;
          for (let i = 0; i < SESSION_BLOCKS.length; i++) {
            cumulative += SESSION_BLOCKS[i].minutes;
            if (elapsedMinutes < cumulative) {
              currentBlockIndex = i;
              break;
            }
          }
          document.querySelectorAll("[data-block-row]").forEach((row, i) => {
            row.classList.toggle("bg-secondary-fixed/40", i === currentBlockIndex && elapsedSeconds < totalSeconds);
          });

          timeText.textContent = `${Math.floor(elapsedMinutes)} / ${TOTAL_MINUTES} min`;
          blockText.textContent = elapsedSeconds >= totalSeconds ? "Completado" : SESSION_BLOCKS[currentBlockIndex].name;
          fill.style.width = `${Math.min((elapsedSeconds / totalSeconds) * 100, 100)}%`;

          if (elapsedSeconds >= totalSeconds) {
            clearInterval(hoyTimer);
            hoyTimer = null;
            AppState.markDayCompleted(day);
          }
        }, 1000);
      };
    },
  };
};

window.Screens = Screens;
