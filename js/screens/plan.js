/** Web equivalent of ui/screens/Plan90DiasScreen.kt. Visual language: "Caregiver Pro v2". */
var Screens = window.Screens || {};

const MODULE_TILES = {
  foundations: { icon: "school", bg: "bg-primary-container", fg: "text-primary" },
  pathways: { icon: "map", bg: "bg-primary-container", fg: "text-primary" },
  infection: { icon: "clean_hands", bg: "bg-primary-container", fg: "text-primary" },
  safety: { icon: "emergency", bg: "bg-secondary-container", fg: "text-secondary" },
  adl: { icon: "shower", bg: "bg-primary-container", fg: "text-primary" },
  mobility: { icon: "accessibility_new", bg: "bg-tertiary-container", fg: "text-tertiary" },
  nutrition: { icon: "restaurant", bg: "bg-primary-container", fg: "text-primary" },
  dementia: { icon: "psychology", bg: "bg-primary-container", fg: "text-primary" },
  communication: { icon: "translate", bg: "bg-primary-container", fg: "text-primary" },
  documentation: { icon: "description", bg: "bg-primary-container", fg: "text-primary" },
  abuse: { icon: "gavel", bg: "bg-secondary-container", fg: "text-secondary" },
  medication_support: { icon: "medication", bg: "bg-secondary-container", fg: "text-secondary" },
  career: { icon: "workspace_premium", bg: "bg-tertiary-container", fg: "text-tertiary" },
};

Screens.plan = async (data) => {
  const state = AppState.get();
  const completed = AppState.completedDayCount();
  const weeks = [...new Set(data.curriculum.map((d) => d.week))];
  const moduleOf = (week) => data.curriculum.find((d) => d.week === week).moduleId;

  const body = `
    <div class="flex flex-col w-full gap-space-lg pb-space-2xl">
      <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-md flex flex-col gap-space-sm">
        <div class="flex justify-between items-end">
          <div class="font-display text-3xl text-white">Mi Plan</div>
          <div class="text-xs font-extrabold tracking-wide" style="color:var(--mint)">${completed}/90 DÍAS</div>
        </div>
        <div class="h-2.5 rounded-full overflow-hidden" style="background:rgba(255,255,255,.16)">
          <div class="h-full rounded-full bg-secondary" style="width:${Math.round((completed / 90) * 100)}%"></div>
        </div>
      </div>

      <div class="px-margin-mobile flex flex-col gap-space-sm">
        ${weeks
          .map((week) => {
            const days = data.curriculum.filter((d) => d.week === week);
            const doneDays = days.filter((d) => AppState.isDayCompleted(d.day)).length;
            const isCurrent = days.some((d) => d.day === state.currentPlanDay);
            const allDone = doneDays === days.length;
            const isLocked = !isCurrent && !allDone && days[0].day > state.currentPlanDay;
            const moduleTitle = data.module_titles[days[0].moduleId] || days[0].moduleId;
            const tile = MODULE_TILES[days[0].moduleId] || MODULE_TILES.foundations;
            return `<div class="flex items-center gap-space-sm bg-white border-2 border-surface-container-high rounded-2xl p-space-sm ${isLocked ? "opacity-60" : ""}">
              <div class="w-11 h-11 flex-shrink-0 rounded-xl ${tile.bg} ${tile.fg} flex items-center justify-center">${UI.icon(tile.icon, "text-xl")}</div>
              <div class="flex-1 min-w-0">
                <div class="font-headline-sm text-headline-sm uppercase text-on-surface leading-tight">${UI.escapeHtml(moduleTitle)}</div>
                <div class="text-[11px] font-extrabold text-outline tracking-wide">SEMANA ${week} · ${days.length} DÍAS</div>
              </div>
              ${allDone ? `<span class="flex-shrink-0" style="color:var(--success)">${UI.icon("check_circle")}</span>` : ""}
              ${isCurrent && !allDone ? UI.statusPill("HOY") : ""}
              ${isLocked ? UI.icon("lock", "flex-shrink-0 text-outline-variant") : ""}
            </div>`;
          })
          .join("")}
      </div>
    </div>`;

  return { body };
};

window.Screens = Screens;
