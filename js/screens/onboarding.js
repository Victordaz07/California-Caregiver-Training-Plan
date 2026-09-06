/**
 * Web equivalent of ui/screens/onboarding/WelcomeScreen.kt and
 * PathwaySelectionScreen.kt. Visual language: "Caregiver Pro v2" redesign
 * (logo float animation, icon-tile route cards, coral CTA with pressed shadow).
 */
var Screens = window.Screens || {};

const PATHWAY_TILES = {
  FamilyCaregiver: { icon: "family_restroom", bg: "bg-tertiary-container", fg: "text-tertiary", meta: "Sin certificación, en casa" },
  IhssProvider: { icon: "badge", bg: "bg-secondary-container", fg: "text-on-secondary-container", meta: "Registro del condado" },
  HcaAffiliatedHco: { icon: "home_health", bg: "bg-primary-container", fg: "text-on-primary-container", meta: "Registro HCA + formación inicial" },
  DirectPrivateCaregiver: { icon: "handshake", bg: "bg-primary-container", fg: "text-on-primary-container", meta: "Acuerdo directo, fuera de agencia" },
  HomeHealthAide: { icon: "medical_services", bg: "bg-tertiary-container", fg: "text-tertiary", meta: "Programa CDPH · 120 o 40 horas" },
  CertifiedNurseAssistant: { icon: "school", bg: "bg-secondary-container", fg: "text-on-secondary-container", meta: "Programa CDPH · 60h aula + 100h clínica" },
};

Screens.welcome = async (data) => {
  const order = ["General", "Emergency", "Scope", "FictionalCase", "HandsOn", "Certificate", "Voice", "Sources"];
  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg pt-space-xl">
      <div class="flex flex-col items-center text-center gap-space-sm">
        <img alt="Caregiver Pro California" class="h-16 w-16" style="animation: omFloat 4s ease-in-out infinite" src="assets/logo.svg"/>
        <h1 class="font-display text-4xl leading-none text-primary">Caregiver Pro CA</h1>
        <p class="font-body-md text-body-md text-outline max-w-md">Antes de empezar, lee estos avisos importantes.</p>
      </div>
      <div class="flex flex-col gap-space-sm">
        ${order.map((k) => UI.disclaimerBanner(k, data.disclaimers)).join("")}
      </div>
      ${UI.bigButton("Entiendo, continuar", { color: "teal", id: "btn-continue" })}
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
        <img alt="Caregiver Pro California" class="h-14 w-14 mb-2" style="animation: omFloat 4s ease-in-out infinite" src="assets/logo.svg"/>
        <h1 class="font-display text-4xl leading-none text-primary">¿Cuál es<br>tu ruta?</h1>
        <p class="font-body-md text-body-md text-outline mt-2">Cada ruta tiene requisitos distintos en California. Elige una y armamos tu plan de 90 días.</p>
      </div>
      <div class="flex flex-col gap-space-sm">
        ${data.pathways
          .map((p) => {
            const tile = PATHWAY_TILES[p.id] || PATHWAY_TILES.FamilyCaregiver;
            return `<button data-pathway="${p.id}" class="text-left bg-white border-2 border-surface-container-high rounded-2xl p-space-md hover:border-primary transition flex items-center gap-space-sm">
              <div class="w-12 h-12 flex-shrink-0 rounded-2xl ${tile.bg} ${tile.fg} flex items-center justify-center">${UI.icon(tile.icon, "text-2xl")}</div>
              <div class="flex-1 min-w-0">
                <h3 class="font-headline-sm text-headline-sm uppercase text-on-surface">${UI.escapeHtml(p.titleEs)}</h3>
                <p class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(tile.meta)}</p>
              </div>
              ${UI.icon("chevron_right", "text-secondary")}
            </button>`;
          })
          .join("")}
      </div>
      <div class="flex gap-space-sm items-start bg-primary-container rounded-2xl p-space-md">
        ${UI.icon("shield", "text-primary flex-shrink-0")}
        <p class="font-body-sm text-body-sm text-on-primary-container">Esto es capacitación, no consejo médico. En emergencia, llama al 911.</p>
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
