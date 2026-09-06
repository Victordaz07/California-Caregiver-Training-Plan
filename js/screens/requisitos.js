/** Web equivalent of ui/screens/RequisitosCertificacionScreen.kt: real legal content, filtered by pathway. */
var Screens = window.Screens || {};

const REQUIREMENT_STATUS_LABELS = {
  RequiredForPathway: "Requerido para tu ruta",
  ConditionalByPathway: "Condicional según tu ruta",
  FutureRequirement: "Vigencia futura",
  RecommendedOrExternalRequirement: "Recomendado o exigido externamente",
  RoleAndFactDependent: "Depende de tu rol y los hechos",
  InternalOnly: "Solo interno (no gubernamental)",
};

Screens.requisitos = async (data) => {
  const state = AppState.get();
  if (!state.pathwayId) {
    return { body: `<div class="p-6">${UI.sectionCard('<p class="font-body-md text-body-md">Elige tu ruta primero desde el inicio.</p>')}</div>` };
  }
  const pathway = DataStore.pathwayById(data, state.pathwayId);
  const reqs = DataStore.legalRequirementsFor(data, state.pathwayId);

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${UI.sectionCard(`
        <span class="font-label-sm text-label-sm text-outline uppercase">Tu ruta</span>
        <h2 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(pathway.titleEs)}</h2>
        <p class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(pathway.routeNoticeEs)}</p>
      `)}
      ${UI.sectionCard(`
        <h3 class="font-headline-sm text-headline-sm text-on-surface">Pasos principales</h3>
        ${pathway.coreStepsEs.map((s, i) => UI.numberedStep(i + 1, s)).join("")}
      `)}
      <h3 class="font-headline-md text-headline-md text-on-surface">Requisitos aplicables</h3>
      ${reqs
        .map(
          (r) => `${UI.sectionCard(`
            <div class="flex items-center justify-between gap-space-xs">
              <h4 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(r.titleEs)}</h4>
              ${UI.statusPill(REQUIREMENT_STATUS_LABELS[r.status] || r.status)}
            </div>
            <p class="font-body-md text-body-md text-on-surface-variant">${UI.escapeHtml(r.summaryEs)}</p>
            ${r.effectiveFrom ? `<p class="font-label-sm text-label-sm text-outline">Vigente desde ${r.effectiveFrom}</p>` : ""}
            ${r.feeUsd != null ? `<p class="font-label-sm text-label-sm text-outline">Tarifa verificada: $${r.feeUsd}</p>` : ""}
            ${
              r.sourceUrls.length
                ? `<div class="flex flex-col gap-1">${r.sourceUrls
                    .map((u) => `<a href="${u}" target="_blank" rel="noopener" class="font-label-sm text-label-sm text-primary underline break-all">${UI.escapeHtml(u)}</a>`)
                    .join("")}</div>`
                : ""
            }
          `)}`,
        )
        .join("")}
      ${UI.disclaimerBanner("Sources", data.disclaimers)}
      ${UI.disclaimerBanner("Certificate", data.disclaimers)}
    </div>`;

  return { body };
};

window.Screens = Screens;
