/**
 * NEW screen (didn't exist before the v2 redesign): settings/profile,
 * wired to real state — real recordings count, real pathway, a real
 * (if destructive) reset action. No name field: this app never asked for
 * one, so the header shows the chosen pathway instead of inventing an
 * identity field just to match the mockup's avatar-initial layout.
 */
var Screens = window.Screens || {};

Screens.perfil = async (data) => {
  const state = AppState.get();
  const pathway = state.pathwayId ? DataStore.pathwayById(data, state.pathwayId) : null;

  const settings = [
    { icon: "translate", label: "Idioma", value: "Español · audio bilingüe" },
    { icon: "mic", label: "Mis grabaciones", value: `${state.recordingsCount} practicada${state.recordingsCount === 1 ? "" : "s"}` },
    { icon: "headphones", label: "Audios escuchados", value: `${state.audiosListened.length} de ${data.audio_episodes.length}` },
    { icon: "badge", label: "Cambiar de ruta", value: pathway ? pathway.titleEs : "Sin elegir", action: "change-pathway" },
    { icon: "work_history", label: "Modo Turno", value: "Pacientes, entrada/salida, horas", action: "shift-mode" },
    { icon: "gavel", label: "Avisos legales", value: "Alcance, fuentes, emergencias", action: "legal" },
    { icon: "restart_alt", label: "Reiniciar progreso", value: "Borra todo lo guardado en este navegador", action: "reset", danger: true },
  ];

  const body = `
    <div class="flex flex-col w-full gap-space-lg pb-space-2xl">
      <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-md flex items-center gap-space-sm">
        <div class="w-[62px] h-[62px] flex-shrink-0 rounded-2xl flex items-center justify-center" style="background:var(--tan); color:var(--tan-text)">${UI.icon("person", "text-3xl")}</div>
        <div class="flex-1 min-w-0">
          <div class="font-display text-2xl text-white leading-tight">${pathway ? UI.escapeHtml(pathway.titleEs) : "Elige tu ruta"}</div>
          ${pathway ? UI.statusPill(pathway.stateCredentialFromApp ? "Certificación estatal" : "Formación interna") : ""}
        </div>
      </div>

      <div class="px-margin-mobile flex flex-col gap-space-sm">
        ${settings
          .map(
            (s) => `<button data-action="${s.action || ""}" class="w-full flex items-center gap-space-sm bg-white border-2 border-surface-container-high rounded-2xl p-space-sm text-left">
              <div class="w-10 h-10 flex-shrink-0 rounded-xl flex items-center justify-center" style="background:${s.danger ? "var(--coral-bg)" : "var(--teal-bg)"}; color:${s.danger ? "var(--coral-dark)" : "var(--teal-dark)"}">${UI.icon(s.icon)}</div>
              <div class="flex-1 min-w-0">
                <div class="font-body-md text-body-md font-extrabold ${s.danger ? "" : "text-on-surface"}" style="${s.danger ? "color:var(--coral-dark)" : ""}">${UI.escapeHtml(s.label)}</div>
                <div class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(s.value)}</div>
              </div>
              ${s.action ? UI.icon("chevron_right", "text-outline-variant") : ""}
            </button>`,
          )
          .join("")}
        <div class="text-center font-hand text-xl text-outline pt-space-md">Cuidar también es amor ♥</div>
        <div class="text-center text-xs font-extrabold tracking-wide" style="color:var(--lock-2)">CAREGIVER PRO CA · v2.0</div>
      </div>
    </div>`;

  return {
    body,
    afterRender: () => {
      document.querySelectorAll('[data-action="change-pathway"]').forEach((b) => (b.onclick = () => (window.location.hash = "#/pathway")));
      document.querySelectorAll('[data-action="shift-mode"]').forEach((b) => (b.onclick = () => (window.location.hash = "#/shift")));
      document.querySelectorAll('[data-action="legal"]').forEach((b) => (b.onclick = () => (window.location.hash = "#/carrera/requisitos")));
      document.querySelectorAll('[data-action="reset"]').forEach(
        (b) =>
          (b.onclick = () => {
            if (confirm("¿Borrar todo tu progreso guardado en este navegador? Esto no se puede deshacer.")) {
              AppState.reset();
              window.location.hash = "#/welcome";
            }
          }),
      );
    },
  };
};

window.Screens = Screens;
