/**
 * Web equivalent of ui/screens/RutinaDiariaScreen.kt. Visual language:
 * "Caregiver Pro v2" redesign — dark teal header with a day-progress ring,
 * 5-block icon row, quick-link cards to Audio/Checklist, illustrated
 * step-by-step card when available, and the real teaching lesson
 * (data.curriculum[].lessonEs) before the practice activity.
 */
var Screens = window.Screens || {};

const SESSION_BLOCKS = [
  { name: "Recuerdo activo", minutes: 10, icon: "psychology", label: "RECUERDO" },
  { name: "Estudio enfocado", minutes: 15, icon: "menu_book", label: "ESTUDIO" },
  { name: "Práctica deliberada", minutes: 25, icon: "fitness_center", label: "PRÁCTICA" },
  { name: "Explicación Feynman", minutes: 10, icon: "record_voice_over", label: "EXPLICA" },
  { name: "Repetición espaciada", minutes: 15, icon: "repeat", label: "REPASO" },
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
  const streak = AppState.completedDayCount();
  const dayPct = Math.round((day / 90) * 100);

  const body = `
    <div class="flex flex-col w-full gap-space-lg pb-space-2xl">
      <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-lg flex gap-space-md items-center">
        <div class="w-[86px] h-[86px] flex-shrink-0 rounded-full flex items-center justify-center" style="background:conic-gradient(var(--coral) 0% ${dayPct}%, rgba(255,255,255,.16) ${dayPct}% 100%)">
          <div class="w-[66px] h-[66px] rounded-full bg-primary flex flex-col items-center justify-center">
            <span class="font-display text-2xl text-white leading-none">${day}</span>
            <span class="text-[10px] font-extrabold tracking-wide" style="color:rgba(255,255,255,.6)">DE 90</span>
          </div>
        </div>
        <div class="flex-1 min-w-0 flex flex-col gap-1.5">
          ${UI.statusPill(`Semana ${curriculumDay.week} · ${moduleTitle}`)}
          <div class="font-display text-2xl text-white leading-tight">${UI.escapeHtml(curriculumDay.titleEs)}</div>
          <div class="flex items-center gap-1.5 text-sm font-extrabold" style="color:var(--mint)">
            ${UI.icon("local_fire_department", "text-base")}<span>${streak} día${streak === 1 ? "" : "s"} completados</span>
          </div>
        </div>
      </div>

      <div class="px-margin-mobile flex flex-col gap-space-md">
        <div class="rounded-xl p-space-md flex flex-col gap-space-sm" style="background:var(--cream); border:2px solid var(--cream-border)">
          <div class="flex items-center gap-space-sm">
            <div class="relative w-16 h-16 flex-shrink-0 flex items-center justify-center">
              ${!alreadyDone ? `<div class="absolute inset-0 rounded-full" style="background:var(--coral); animation:omPulse 2.4s ease-out infinite"></div>` : ""}
              <button id="start-session-btn" ${alreadyDone ? "disabled" : ""} class="relative w-16 h-16 rounded-full text-white flex items-center justify-center" style="background:${alreadyDone ? "var(--success)" : "var(--coral)"}; box-shadow:0 5px 0 ${alreadyDone ? "#1f6f42" : "var(--coral-dark)"}">
                ${UI.icon(alreadyDone ? "check" : "play_arrow", "text-3xl")}
              </button>
            </div>
            <div class="flex-1 min-w-0">
              <div class="font-headline-sm text-headline-sm uppercase text-primary">Rutina de hoy</div>
              <div class="font-body-sm text-body-sm text-outline" id="routine-meta">${alreadyDone ? "Día completado" : `${TOTAL_MINUTES} min · 5 bloques · sin iniciar`}</div>
            </div>
          </div>
          <div class="flex gap-1.5" id="routine-blocks">
            ${SESSION_BLOCKS.map(
              (b) => `<div class="flex-1 flex flex-col items-center gap-1 bg-white rounded-xl py-2 px-1" data-block-tile>
                ${UI.icon(b.icon, "text-primary text-xl")}<span class="text-[9px] font-extrabold text-outline tracking-wide">${b.label}</span>
              </div>`,
            ).join("")}
          </div>
          <div class="h-2 rounded-full overflow-hidden bg-surface-container-high" id="routine-progress-track">
            <div class="h-full rounded-full bg-secondary transition-all duration-500" id="routine-progress-fill" style="width:${alreadyDone ? 100 : 0}%"></div>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-space-sm">
          <a href="#/audio/lecciones" class="bg-primary-container rounded-2xl p-space-md flex flex-col gap-1.5">
            ${UI.icon("headphones", "text-primary text-2xl")}
            <div class="font-headline-sm text-headline-sm uppercase text-primary leading-tight">Audio de<br>la semana</div>
            <div class="text-xs font-extrabold text-primary">Bilingüe · sin conexión</div>
          </a>
          <a href="#/practica/turno" class="bg-secondary-container rounded-2xl p-space-md flex flex-col gap-1.5">
            ${UI.icon("checklist", "text-on-secondary-container text-2xl")}
            <div class="font-headline-sm text-headline-sm uppercase text-on-secondary-container leading-tight">Checklist<br>del turno</div>
            <div class="text-xs font-extrabold text-on-secondary-container">Autorrevisión</div>
          </a>
        </div>

        ${
          illustration
            ? `<a href="#" id="illustration-teaser" class="bg-white border-2 border-surface-container-high rounded-2xl p-space-sm flex items-center gap-space-sm">
                <div class="w-12 h-12 flex-shrink-0 rounded-xl bg-primary-container bg-cover bg-center" style="background-image:url('assets/illustrations/${illustration.file}'); background-size:230% auto; background-position:8% 62%"></div>
                <div class="flex-1 min-w-0">
                  <div class="text-[11px] font-extrabold tracking-wide text-primary">LÁMINA ILUSTRADA</div>
                  <div class="font-headline-sm text-headline-sm uppercase text-on-surface leading-tight">${UI.escapeHtml(illustration.title)}</div>
                </div>
                ${UI.icon("arrow_forward", "text-secondary")}
              </a>`
            : ""
        }

        ${UI.sectionCard(`
          <div class="flex items-center gap-space-xs">
            ${UI.icon("menu_book", "text-primary")}
            <h3 class="font-headline-sm text-headline-sm uppercase text-on-surface">Lección de hoy</h3>
          </div>
          <p class="font-body-md text-body-md text-on-surface leading-relaxed">${UI.escapeHtml(curriculumDay.lessonEs || "")}</p>
        `)}

        ${UI.sectionCard(`
          <h3 class="font-headline-sm text-headline-sm uppercase text-on-surface">Actividad de práctica</h3>
          <p class="font-body-md text-body-md text-on-surface-variant">${UI.escapeHtml(curriculumDay.practiceEs)}</p>
        `)}

        ${curriculumDay.criticalSafety ? UI.disclaimerBanner("Emergency", data.disclaimers) : ""}
      </div>
    </div>`;

  return {
    body,
    afterRender: () => {
      if (hoyTimer) {
        clearInterval(hoyTimer);
        hoyTimer = null;
      }
      const illustrationTeaser = document.getElementById("illustration-teaser");
      if (illustrationTeaser) illustrationTeaser.onclick = (e) => e.preventDefault();

      const startBtn = document.getElementById("start-session-btn");
      if (!startBtn || alreadyDone) return;

      startBtn.onclick = () => {
        startBtn.disabled = true;
        let elapsedSeconds = 0;
        const totalSeconds = TOTAL_MINUTES * 60;

        hoyTimer = setInterval(() => {
          const meta = document.getElementById("routine-meta");
          const fill = document.getElementById("routine-progress-fill");
          if (!meta) {
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
          document.querySelectorAll("[data-block-tile]").forEach((tile, i) => {
            tile.style.background = i === currentBlockIndex && elapsedSeconds < totalSeconds ? "var(--tan)" : "#fff";
          });

          meta.textContent = `${Math.floor(elapsedMinutes)} / ${TOTAL_MINUTES} min · ${elapsedSeconds >= totalSeconds ? "Completado" : SESSION_BLOCKS[currentBlockIndex].name}`;
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
