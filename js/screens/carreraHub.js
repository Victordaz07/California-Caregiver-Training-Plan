/** Web equivalent of ui/screens/hub/CareerHubScreen.kt. */
var Screens = window.Screens || {};

Screens.carreraHub = async (data) => {
  const state = AppState.get();
  const pathway = DataStore.pathwayById(data, state.pathwayId);
  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-md">
      ${UI.brandScene(data, "career-portfolio")}
      <h1 class="font-headline-lg text-headline-lg uppercase text-on-surface">Carrera</h1>
      ${pathway ? UI.sectionCard(`<p class="font-label-sm text-label-sm text-outline uppercase">Tu ruta</p><p class="font-headline-sm text-headline-sm uppercase text-on-surface">${UI.escapeHtml(pathway.titleEs)}</p>`) : ""}
      ${UI.screenNavCard({ title: "Progreso", subtitle: "Días, audios, escenarios e insignias", iconName: "insights", href: "#/carrera/progreso", tileBg: "bg-secondary-container", tileFg: "text-secondary" })}
      ${UI.screenNavCard({ title: "Requisitos y Certificación", subtitle: "Lo que aplica a tu ruta, con fuente oficial", iconName: "fact_check", href: "#/carrera/requisitos" })}
      ${UI.screenNavCard({ title: "Recursos por Semana", subtitle: "Enlaces oficiales de California", iconName: "link", href: "#/carrera/recursos" })}
      ${UI.screenNavCard({ title: "Analizador Vocacional", subtitle: "Reflexiona sobre tus metas de carrera", iconName: "explore", href: "#/carrera/vocacional" })}
      ${UI.screenNavCard({ title: "Perfil", subtitle: "Ajustes, grabaciones, reiniciar progreso", iconName: "settings", href: "#/carrera/perfil", tileBg: "bg-tertiary-container", tileFg: "text-tertiary" })}
    </div>`;
  return { body };
};

window.Screens = Screens;
