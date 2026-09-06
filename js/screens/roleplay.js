/**
 * Web equivalent of ui/screens/RolePlayBilingueScreen.kt: the week's key
 * phrase as a bilingual script to read and practice, plus real
 * self-recording — no fake "AI evaluation" score, matching the correction
 * already made in the Android build (see DECISIONS.md / REGULATORY_CONTENT_REPORT.md).
 */
var Screens = window.Screens || {};

Screens.roleplay = async (data) => {
  const state = AppState.get();
  const currentDay = DataStore.curriculumDayFor(data, state.currentPlanDay);
  const currentWeek = currentDay ? currentDay.week : 1;
  const episode = DataStore.audioEpisodeFor(data, currentWeek) || data.audio_episodes[0];

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${UI.sectionCard(`
        <span class="font-label-sm text-label-sm text-outline uppercase">Semana ${episode.week} · Guion de práctica</span>
        <h2 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(episode.titleEs)}</h2>
        <p class="font-body-md text-body-md text-on-surface-variant">${UI.escapeHtml(episode.learnerPromptEs)}</p>
        <div class="bg-surface-container rounded-lg p-space-sm">
          <span class="font-label-md text-label-md text-primary font-semibold block mb-1">Frase clave en inglés</span>
          <p class="font-body-lg text-body-lg text-on-surface">"${UI.escapeHtml(episode.keyPhraseEn)}"</p>
          <p class="font-body-sm text-body-sm text-outline mt-1">Pronunciación aproximada: ${UI.escapeHtml(episode.pronunciationEs)}</p>
        </div>
      `)}
      ${UI.disclaimerBanner("Scope", data.disclaimers)}
      <div id="vrc"></div>
      ${UI.sectionCard(`
        <h3 class="font-headline-sm text-headline-sm text-on-surface">Autorrevisión</h3>
        ${UI.checklistRow("Mantuve un tono calmado y respetuoso")}
        ${UI.checklistRow("Pedí permiso antes de continuar con la tarea")}
        ${UI.checklistRow("Usé la frase clave con claridad, aunque no fuera perfecta")}
        ${UI.checklistRow("No prometí ni diagnostiqué nada fuera de mi función")}
      `)}
    </div>`;

  return {
    body,
    afterRender: () => {
      document.getElementById("vrc").innerHTML = voiceRecordCardHtml("vrc-roleplay");
      attachVoiceRecordCard("vrc-roleplay", "Lee el guion en voz alta y grábate practicando la frase clave.");
    },
  };
};

window.Screens = Screens;
