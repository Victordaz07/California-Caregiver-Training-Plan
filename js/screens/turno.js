/**
 * Web equivalent of ui/screens/SimulacionTurnoScreen.kt: practice a
 * 60-second structured handoff (situación, hechos, acciones, pendiente) with
 * real self-recording — no fake real-time percentages.
 */
var Screens = window.Screens || {};

Screens.turno = async (data) => {
  const handoffRubrics = ["objective_facts", "chronology", "action_and_notification"]
    .map((id) => DataStore.rubricCriterion(data, id))
    .filter(Boolean);

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${UI.sectionCard(`
        <h2 class="font-headline-sm text-headline-sm text-on-surface">Estructura de una entrega de turno</h2>
        ${UI.numberedStep(1, "Situación: hora y contexto breve.")}
        ${UI.numberedStep(2, "Hechos observados: lo que viste, oíste o hiciste, sin diagnosticar.")}
        ${UI.numberedStep(3, "Acción tomada: qué hiciste al respecto.")}
        ${UI.numberedStep(4, "Pendiente: qué queda por hacer o a quién se notificó.")}
      `)}
      ${UI.disclaimerBanner("FictionalCase", data.disclaimers)}
      <div id="vrc"></div>
      ${UI.sectionCard(`
        <h3 class="font-headline-sm text-headline-sm text-on-surface">Autorrevisión (basada en los criterios reales de la app)</h3>
        ${handoffRubrics.map((r) => UI.checklistRow(`${r.labelEs}: ${r.descriptionEs}`)).join("")}
      `)}
    </div>`;

  return {
    body,
    afterRender: () => {
      document.getElementById("vrc").innerHTML = voiceRecordCardHtml("vrc-turno");
      attachVoiceRecordCard("vrc-turno", "Grábate haciendo una entrega de turno de 60 segundos sobre un caso ficticio.");
    },
  };
};

window.Screens = Screens;
