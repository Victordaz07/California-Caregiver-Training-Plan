/** Web equivalent of ui/screens/RecursosSemanaScreen.kt: real official links filtered by pathway. */
var Screens = window.Screens || {};

Screens.recursos = async (data) => {
  const state = AppState.get();
  const pathway = state.pathwayId ? DataStore.pathwayById(data, state.pathwayId) : null;
  const resources = state.pathwayId ? DataStore.officialResourcesFor(data, state.pathwayId) : data.official_resources;

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${UI.sectionCard(`
        <span class="font-label-sm text-label-sm text-outline uppercase">Línea estatal APS</span>
        <p class="font-headline-sm text-headline-sm text-on-surface">${data.legal_requirements.find((r) => r.id === "mandated_reporting_home") ? "1-833-401-0832" : ""}</p>
        <p class="font-body-sm text-body-sm text-outline">Ante peligro inmediato, llama primero al 911.</p>
      `, { bg: "bg-secondary-fixed/50" })}
      <h3 class="font-headline-md text-headline-md text-on-surface">Recursos oficiales${pathway ? ` para ${UI.escapeHtml(pathway.titleEs)}` : ""}</h3>
      <div class="flex flex-col gap-space-sm">
        ${resources
          .map(
            (r) => `${UI.sectionCard(`
              <p class="font-label-sm text-label-sm text-outline uppercase">${UI.escapeHtml(r.agency)}${r.geography ? ` · ${UI.escapeHtml(r.geography)}` : ""}</p>
              <h4 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(r.titleEs)}</h4>
              <a href="${r.url}" target="_blank" rel="noopener" class="font-label-sm text-label-sm text-primary underline break-all">${UI.escapeHtml(r.url)}</a>
              ${r.phone ? `<p class="font-body-sm text-body-sm text-on-surface-variant">Teléfono: ${UI.escapeHtml(r.phone)}</p>` : ""}
            `)}`,
          )
          .join("")}
      </div>
      ${UI.disclaimerBanner("Sources", data.disclaimers)}
    </div>`;

  return { body };
};

window.Screens = Screens;
