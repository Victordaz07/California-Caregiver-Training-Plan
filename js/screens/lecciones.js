/** Web equivalent of ui/screens/AudioLeccionesScreen.kt: real playback + real recording. */
var Screens = window.Screens || {};

Screens.lecciones = async (data) => {
  const state = AppState.get();
  const currentDay = DataStore.curriculumDayFor(data, state.currentPlanDay);
  const currentWeek = currentDay ? currentDay.week : 1;
  const episode = DataStore.audioEpisodeFor(data, currentWeek) || data.audio_episodes[0];

  function episodeRow(ep) {
    return `<div class="flex items-center justify-between bg-surface-container-lowest rounded-lg p-space-sm" data-ep-row="${ep.episodeId}">
      <div class="min-w-0 flex-1">
        <p class="font-body-md text-body-md text-on-surface truncate">Semana ${ep.week} · ${UI.escapeHtml(ep.titleEs)}</p>
        <p class="font-body-sm text-body-sm text-outline truncate">"${UI.escapeHtml(ep.keyPhraseEn)}"</p>
      </div>
      <button data-play="${ep.episodeId}" class="w-11 h-11 flex-shrink-0 flex items-center justify-center rounded-full text-primary">${UI.icon("play_circle")}</button>
      ${UI.statusPill(`${ep.targetMinutes} min`)}
    </div>`;
  }

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${UI.sectionCard(`
        <div class="flex items-center gap-space-xs">
          ${UI.icon("auto_awesome", "text-primary")}
          <h2 class="font-headline-sm text-headline-sm text-on-surface">Orientación Guiada: Audio Conversacional</h2>
        </div>
        <p class="font-body-sm text-body-sm text-outline">Guiones narrados bilingües, ya incluidos sin conexión. Escúchalos y luego grábate practicando.</p>
      `)}
      ${UI.sectionCard(`
        <span class="font-label-sm text-label-sm text-outline uppercase">Semana ${currentWeek} · Episodio de hoy</span>
        <h3 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(episode.titleEs)}</h3>
        <div class="flex items-center gap-space-sm">
          <button data-play="${episode.episodeId}" class="w-12 h-12 flex-shrink-0 flex items-center justify-center rounded-full bg-primary-container text-on-primary-container">${UI.icon("play_circle")}</button>
          <p class="font-body-sm text-body-sm text-outline">${episode.targetMinutes} min · ${UI.escapeHtml(episode.learnerPromptEs)}</p>
        </div>
        <p class="font-body-lg text-body-lg text-on-surface">"${UI.escapeHtml(episode.keyPhraseEn)}"</p>
        <p class="font-body-sm text-body-sm text-outline">Pronunciación aproximada: ${UI.escapeHtml(episode.pronunciationEs)}</p>
      `)}
      <div id="vrc"></div>
      <h3 class="font-headline-md text-headline-md text-on-surface">Biblioteca de Guiones · 13 Semanas</h3>
      <div class="flex flex-col gap-1.5">
        ${data.audio_episodes.map(episodeRow).join("")}
      </div>
    </div>`;

  return {
    body,
    afterRender: () => {
      document.getElementById("vrc").innerHTML = voiceRecordCardHtml("vrc-lecciones");
      attachVoiceRecordCard("vrc-lecciones", "Escucha la narración y luego grábate repitiendo la frase clave de esta semana.");

      function wirePlayButtons() {
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
              });
            }
          };
        });
      }
      wirePlayButtons();
    },
  };
};

window.Screens = Screens;
