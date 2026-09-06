/**
 * Web equivalent of ui/screens/onboarding/WelcomeScreen.kt and
 * PathwaySelectionScreen.kt.
 */
var Screens = window.Screens || {};

Screens.welcome = async (data) => {
  const order = ["General", "Emergency", "Scope", "FictionalCase", "HandsOn", "Certificate", "Voice", "Sources"];
  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg pt-space-xl">
      <div class="flex flex-col items-center text-center gap-space-sm">
        <img alt="Caregiver Pro California" class="h-16 w-16" src="assets/logo.svg"/>
        <h1 class="font-display-lg-mobile text-display-lg-mobile text-primary">Caregiver Pro CA</h1>
        <p class="font-body-md text-body-md text-outline max-w-md">Antes de empezar, lee estos avisos importantes.</p>
      </div>
      <div class="flex flex-col gap-space-sm">
        ${order.map((k) => UI.disclaimerBanner(k, data.disclaimers)).join("")}
      </div>
      <button id="btn-continue" class="h-14 bg-primary text-on-primary rounded-lg font-headline-sm text-headline-sm mt-space-md">Entiendo, continuar</button>
    </div>`;
  return {
    body,
    afterRender: () => {
      document.getElementById("btn-continue").onclick = () => {
        window.location.hash = "#/pathway";
      };
    },
  };
};

Screens.pathwaySelection = async (data) => {
  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg pt-space-xl">
      <div class="flex flex-col gap-space-xxs">
        <h1 class="font-headline-lg text-headline-lg text-on-surface">¿Cuál es tu ruta?</h1>
        <p class="font-body-sm text-body-sm text-outline">Elige la que mejor describe tu situación. No hay una ruta por defecto — cada una tiene requisitos distintos.</p>
      </div>
      <div class="flex flex-col gap-space-sm">
        ${data.pathways
          .map(
            (p) => `<button data-pathway="${p.id}" class="text-left bg-surface-container-lowest rounded-xl p-space-md shadow-sm hover:shadow-md transition flex flex-col gap-1">
              <h3 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(p.titleEs)}</h3>
              <p class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(p.summaryEs)}</p>
            </button>`,
          )
          .join("")}
      </div>
    </div>`;
  return {
    body,
    afterRender: () => {
      document.querySelectorAll("[data-pathway]").forEach((btn) => {
        btn.onclick = () => {
          AppState.setPathway(btn.dataset.pathway);
          AppState.completeOnboarding();
          window.location.hash = "#/hoy";
        };
      });
    },
  };
};

window.Screens = Screens;
