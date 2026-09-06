/**
 * NEW screen (didn't exist before the v2 redesign): a stats/badges/certificate
 * dashboard, wired to real tracked state — days completed (already tracked),
 * plus audios listened, scenarios completed and recordings made (added to
 * js/state.js specifically to power this screen honestly, no invented numbers).
 */
var Screens = window.Screens || {};

Screens.progreso = async (data) => {
  const state = AppState.get();
  const days = AppState.completedDayCount();
  const audios = state.audiosListened.length;
  const scenarios = state.scenariosCompleted.length;
  const pct = Math.round((days / 90) * 100);
  const pathway = state.pathwayId ? DataStore.pathwayById(data, state.pathwayId) : null;

  const infectionDone = data.curriculum.some((d) => d.moduleId === "infection" && AppState.isDayCompleted(d.day));
  const safetyDone = data.curriculum.some((d) => d.moduleId === "safety" && AppState.isDayCompleted(d.day));
  const recordingDone = state.recordingsCount > 0;
  const audioDone = audios >= 1;
  const scenarioDone = scenarios >= 1;

  const badges = [
    { icon: "clean_hands", earned: infectionDone, bg: "bg-primary", fg: "text-secondary-fixed" },
    { icon: "emergency", earned: safetyDone, bg: "bg-secondary", fg: "text-white" },
    { icon: "record_voice_over", earned: recordingDone, bg: "bg-primary-container", fg: "text-primary" },
    { icon: "headphones", earned: audioDone, bg: "bg-tertiary-container", fg: "text-tertiary" },
    { icon: "theater_comedy", earned: scenarioDone, bg: "bg-secondary-container", fg: "text-secondary" },
  ];

  const body = `
    <div class="flex flex-col w-full gap-space-lg pb-space-2xl">
      <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-lg flex flex-col items-center gap-space-sm">
        <div class="w-[132px] h-[132px] rounded-full flex items-center justify-center" style="background:conic-gradient(var(--coral) 0% ${pct}%, rgba(255,255,255,.14) ${pct}% 100%)">
          <div class="w-[104px] h-[104px] rounded-full bg-primary flex flex-col items-center justify-center">
            <span class="font-display text-4xl text-white leading-none">${pct}%</span>
            <span class="text-[10px] font-black tracking-wide" style="color:var(--mint)">DEL PLAN</span>
          </div>
        </div>
        <div class="font-display text-xl text-white text-center">Vas muy bien${pathway ? `, ${UI.escapeHtml(pathway.titleEs.split(" ")[0])}` : ""}</div>
      </div>

      <div class="px-margin-mobile flex flex-col gap-space-md">
        <div class="grid grid-cols-3 gap-space-sm">
          <div class="bg-primary-container rounded-2xl p-space-sm flex flex-col gap-1">
            ${UI.icon("event_available", "text-primary text-2xl")}
            <span class="font-display text-2xl text-primary leading-none">${days}</span>
            <span class="text-[10px] font-black text-primary tracking-wide">DÍAS</span>
          </div>
          <div class="bg-secondary-container rounded-2xl p-space-sm flex flex-col gap-1">
            ${UI.icon("headphones", "text-secondary text-2xl")}
            <span class="font-display text-2xl text-secondary leading-none">${audios}</span>
            <span class="text-[10px] font-black text-secondary tracking-wide">AUDIOS</span>
          </div>
          <div class="bg-tertiary-container rounded-2xl p-space-sm flex flex-col gap-1">
            ${UI.icon("theater_comedy", "text-tertiary text-2xl")}
            <span class="font-display text-2xl text-tertiary leading-none">${scenarios}</span>
            <span class="text-[10px] font-black text-tertiary tracking-wide">ESCENARIOS</span>
          </div>
        </div>

        <div>
          <div class="text-xs font-extrabold tracking-wide text-outline mb-2">INSIGNIAS</div>
          <div class="flex gap-space-sm">
            ${badges
              .map(
                (b) => `<div class="flex-1 aspect-square rounded-2xl flex items-center justify-center ${b.earned ? `${b.bg} ${b.fg}` : ""}" style="${b.earned ? "" : "background:var(--border-light); color:var(--lock-2)"}">
                  ${UI.icon(b.earned ? b.icon : "lock", "text-2xl")}
                </div>`,
              )
              .join("")}
          </div>
        </div>

        <div class="relative overflow-hidden rounded-2xl p-space-md flex items-center gap-space-sm" style="background:var(--cream); border:3px solid var(--teal-dark)">
          <div class="w-14 h-14 flex-shrink-0 rounded-full bg-primary flex items-center justify-center" style="color:var(--tan)">${UI.icon("workspace_premium", "text-3xl")}</div>
          <div class="flex-1 min-w-0">
            <div class="font-headline-sm text-headline-sm uppercase text-primary leading-tight">Certificado de 90 días</div>
            <div class="font-body-sm text-body-sm text-outline">${90 - days > 0 ? `Faltan ${90 - days} días · se emite al terminar` : "¡Completado! Disponible en Perfil."}</div>
          </div>
        </div>

        <div class="flex gap-space-sm items-start bg-primary-container rounded-2xl p-space-md">
          ${UI.icon("gavel", "text-primary flex-shrink-0")}
          <p class="font-body-sm text-body-sm text-on-primary-container">Este certificado es de práctica interna; no reemplaza una certificación estatal (HHA, CNA, HCA, IHSS).</p>
        </div>
      </div>
    </div>`;

  return { body };
};

window.Screens = Screens;
