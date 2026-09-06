/**
 * Web equivalent of ui/screens/EscenariosFlashcardsScreen.kt: a real due-card
 * queue driven by the ported ReviewScheduler (js/scheduler.js + AppState),
 * plus the branching scenario for the learner's current week.
 */
var Screens = window.Screens || {};

Screens.escenarios = async (data) => {
  const state = AppState.get();
  const currentDay = DataStore.curriculumDayFor(data, state.currentPlanDay);
  const currentWeek = currentDay ? currentDay.week : 1;

  const allCardIds = data.flashcards.map((c) => c.id);
  const dueIds = AppState.dueCardIds(allCardIds);
  const dueCard = dueIds.length > 0 ? data.flashcards.find((c) => c.id === dueIds[0]) : null;

  const scenario = DataStore.scenarioFor(data, currentWeek);

  const flashcardSection = dueCard
    ? `<div id="flashcard-block">
        ${UI.sectionCard(`
          <div class="flex items-center justify-between">
            <span class="font-label-sm text-label-sm text-outline uppercase">Semana ${dueCard.week}${dueCard.critical ? " · Seguridad" : ""}</span>
            <span class="font-label-sm text-label-sm text-outline">${dueIds.length} pendiente${dueIds.length === 1 ? "" : "s"}</span>
          </div>
          <p class="font-headline-sm text-headline-sm text-on-surface" id="fc-question">${UI.escapeHtml(dueCard.questionEs)}</p>
          <p class="font-body-md text-body-md text-on-surface-variant hidden" id="fc-answer">${UI.escapeHtml(dueCard.answerEs)}</p>
          <button id="fc-reveal" class="h-11 bg-primary-container text-on-primary-container rounded-lg font-label-lg text-label-lg">Mostrar respuesta</button>
          <div id="fc-rating" class="hidden grid grid-cols-4 gap-2">
            <button data-rating="AGAIN" class="h-11 bg-error-container text-on-error-container rounded-lg font-label-sm text-label-sm">Otra vez</button>
            <button data-rating="HARD" class="h-11 bg-secondary-fixed text-on-secondary-fixed-variant rounded-lg font-label-sm text-label-sm">Difícil</button>
            <button data-rating="GOOD" class="h-11 bg-primary-fixed text-on-primary-fixed-variant rounded-lg font-label-sm text-label-sm">Bien</button>
            <button data-rating="EASY" class="h-11 bg-tertiary-container text-on-tertiary-container rounded-lg font-label-sm text-label-sm">Fácil</button>
          </div>
        `)}
      </div>`
    : UI.sectionCard(`<p class="font-body-md text-body-md text-on-surface-variant">No hay tarjetas pendientes de repaso ahora mismo. Vuelve más tarde.</p>`);

  const scenarioSection = scenario
    ? `<div id="scenario-block">
        ${UI.sectionCard(`
          <div class="flex items-center gap-space-xs">
            ${UI.icon("theater_comedy", "text-outline")}
            <span class="font-label-sm text-label-sm text-outline uppercase">Escenario ficticio · Semana ${scenario.week}</span>
          </div>
          <h3 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(scenario.titleEs)}</h3>
          <p class="font-body-md text-body-md text-on-surface-variant">${UI.escapeHtml(scenario.stemEs)}</p>
          <div class="flex flex-col gap-2" id="scenario-choices">
            ${scenario.choices
              .map(
                (c) => `<button data-choice="${c.id}" class="text-left h-auto py-3 px-space-sm bg-surface-container rounded-lg font-body-sm text-body-sm text-on-surface">${UI.escapeHtml(c.textEs)}</button>`,
              )
              .join("")}
          </div>
          <div id="scenario-feedback" class="hidden"></div>
        `)}
      </div>`
    : "";

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${flashcardSection}
      ${scenarioSection}
    </div>`;

  return {
    body,
    afterRender: () => {
      const reveal = document.getElementById("fc-reveal");
      if (reveal) {
        reveal.onclick = () => {
          document.getElementById("fc-answer").classList.remove("hidden");
          document.getElementById("fc-rating").classList.remove("hidden");
          reveal.classList.add("hidden");
        };
      }
      document.querySelectorAll("#fc-rating [data-rating]").forEach((btn) => {
        btn.onclick = () => {
          AppState.rateCard(dueCard.id, ReviewRating[btn.dataset.rating]);
        };
      });

      const OUTCOME_STYLE = {
        Safe: { bg: "bg-primary-fixed", color: "text-on-primary-fixed-variant", label: "Seguro" },
        CriticalFail: { bg: "bg-error-container", color: "text-on-error-container", label: "Fallo crítico" },
        NeedsCorrection: { bg: "bg-secondary-fixed", color: "text-on-secondary-fixed-variant", label: "Necesita corrección" },
      };
      document.querySelectorAll("#scenario-choices [data-choice]").forEach((btn) => {
        btn.onclick = () => {
          const choice = scenario.choices.find((c) => c.id === btn.dataset.choice);
          const style = OUTCOME_STYLE[choice.outcome];
          AppState.markScenarioCompleted(scenario.id);
          const feedback = document.getElementById("scenario-feedback");
          feedback.className = `${style.bg} ${style.color} rounded-lg p-space-sm flex flex-col gap-1`;
          feedback.innerHTML = `<span class="font-label-sm text-label-sm uppercase font-semibold">${style.label}</span><p class="font-body-sm text-body-sm">${UI.escapeHtml(choice.feedbackEs)}</p>`;
          document.querySelectorAll("#scenario-choices [data-choice]").forEach((b) => (b.disabled = true));
        };
      });
    },
  };
};

window.Screens = Screens;
