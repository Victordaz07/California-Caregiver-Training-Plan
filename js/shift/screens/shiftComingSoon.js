/** Shared placeholder for Modo Turno tabs not built yet (Ayuda, Horas, Constancia). */
var Screens = window.Screens || {};

Screens.shiftComingSoon = (title, subtitle) => ({
  body: `
    <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-md">
      <div class="font-display text-2xl text-white uppercase">${UI.escapeHtml(title)}</div>
    </div>
    <div class="px-margin-mobile pt-space-2xl flex flex-col items-center text-center gap-space-sm">
      ${UI.icon("construction", "text-5xl text-outline")}
      <p class="font-body-md text-body-md text-outline max-w-xs">${UI.escapeHtml(subtitle)} Todavía se está construyendo.</p>
    </div>`,
});

window.Screens = Screens;
