/** Web equivalent of ui/screens/Plan90DiasScreen.kt. */
var Screens = window.Screens || {};

Screens.plan = async (data) => {
  const state = AppState.get();
  const completed = AppState.completedDayCount();
  const weeks = [...new Set(data.curriculum.map((d) => d.week))];

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      <div>
        <h1 class="font-headline-lg text-headline-lg text-on-surface">Plan de 90 Días</h1>
        <p class="font-body-sm text-body-sm text-outline">13 semanas, un tema por semana. Cada día tiene un objetivo y una práctica — la lección de enseñanza completa está en camino.</p>
      </div>
      ${UI.sectionCard(UI.labeledProgress("Progreso general", completed, 90))}
      <div class="flex flex-col gap-space-md">
        ${weeks
          .map((week) => {
            const days = data.curriculum.filter((d) => d.week === week);
            const moduleTitle = data.module_titles[days[0].moduleId] || days[0].moduleId;
            return `<div class="flex flex-col gap-space-sm">
              <h3 class="font-headline-sm text-headline-sm text-on-surface">Semana ${week} · ${UI.escapeHtml(moduleTitle)}</h3>
              <div class="flex flex-col gap-1">
                ${days
                  .map((d) => {
                    const done = AppState.isDayCompleted(d.day);
                    const isToday = d.day === state.currentPlanDay;
                    return `<div class="flex items-center gap-space-sm bg-surface-container-lowest rounded-lg p-space-sm ${isToday ? "ring-2 ring-primary" : ""}">
                      ${UI.icon(done ? "check_circle" : "radio_button_unchecked", done ? "text-primary" : "text-outline")}
                      <div class="flex-1 min-w-0">
                        <p class="font-body-sm text-body-sm text-on-surface">Día ${d.day} · ${UI.escapeHtml(d.titleEs)}</p>
                      </div>
                      ${d.criticalSafety ? UI.statusPill("Seguridad", { bg: "bg-error-container", color: "text-on-error-container" }) : ""}
                    </div>`;
                  })
                  .join("")}
              </div>
            </div>`;
          })
          .join("")}
      </div>
    </div>`;

  return { body };
};

window.Screens = Screens;
