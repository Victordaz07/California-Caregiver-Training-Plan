/** Web equivalent of ui/screens/BibliotecaAudiosScreen.kt: honest "already offline" framing, grouped by phase. */
var Screens = window.Screens || {};

const PHASES = [
  { label: "FASE 1", title: "Fundamentos y Rutas", weeks: [1, 2, 3, 4] },
  { label: "FASE 2", title: "Seguridad, ADL y Movilidad", weeks: [5, 6, 7, 8] },
  { label: "FASE 3", title: "Comunicación y Documentación", weeks: [9, 10, 11] },
  { label: "FASE 4", title: "Medicación y Carrera", weeks: [12, 13] },
];

Screens.biblioteca = async (data) => {
  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${UI.sectionCard(`
        <h2 class="font-headline-sm text-headline-sm text-on-surface">Guiones Narrados · 13 Semanas</h2>
        <div class="flex items-center gap-space-xs">
          ${UI.icon("check_circle", "text-primary")}
          <p class="font-body-sm text-body-sm text-outline">Los ${data.audio_episodes.length} episodios están incluidos en la app con audio narrado real: disponibles sin conexión, sin nada que descargar.</p>
        </div>
      `)}
      ${PHASES.map(
        (phase) => `${UI.sectionCard(`
          <h3 class="font-headline-sm text-headline-sm text-on-surface">${phase.label} · ${phase.title}</h3>
          ${data.audio_episodes
            .filter((e) => phase.weeks.includes(e.week))
            .map(
              (e) => `<div class="flex items-center justify-between" data-ep-row="${e.episodeId}">
                <div class="min-w-0 flex-1">
                  <p class="font-body-md text-body-md text-on-surface truncate">Semana ${e.week} · ${UI.escapeHtml(e.titleEs)}</p>
                  <p class="font-body-sm text-body-sm text-outline truncate">${e.targetMinutes} min · "${UI.escapeHtml(e.keyPhraseEn)}"</p>
                </div>
                <button data-play="${e.episodeId}" class="w-11 h-11 flex-shrink-0 flex items-center justify-center rounded-full text-primary">${UI.icon("play_circle")}</button>
              </div>`,
            )
            .join("")}
        `)}`,
      ).join("")}
    </div>`;

  return {
    body,
    afterRender: () => {
      document.querySelectorAll("[data-play]").forEach((btn) => {
        btn.onclick = () => {
          const id = btn.dataset.play;
          if (Narration.isPlayingId(id)) {
            Narration.stop();
            btn.innerHTML = UI.icon("play_circle");
          } else {
            document.querySelectorAll("[data-play]").forEach((b) => (b.innerHTML = UI.icon("play_circle")));
            btn.innerHTML = UI.icon("stop_circle");
            Narration.play(id, () => {
              btn.innerHTML = UI.icon("play_circle");
              AppState.markAudioListened(id);
            });
          }
        };
      });
    },
  };
};

window.Screens = Screens;
