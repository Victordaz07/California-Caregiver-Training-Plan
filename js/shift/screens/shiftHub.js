/**
 * Placeholder landing for Modo Turno while the real screens (Mis Pacientes,
 * Ficha, Geocerca, Turno Activo, etc. — tasks #37-40) are built. Confirms
 * the auth + routing plumbing works end to end: signed-in users land here
 * with their real role/agency, signed-out users never reach this route.
 */
var Screens = window.Screens || {};

Screens.shiftHub = async () => {
  const user = AuthStore.get();
  const body = `
    <div class="bg-primary rounded-b-3xl px-margin-mobile pt-space-lg pb-space-lg flex flex-col gap-space-xs">
      <span class="font-label-sm text-label-sm text-white/70 uppercase tracking-wide">Modo Turno</span>
      <h1 class="font-display text-2xl text-white">Hola, ${UI.escapeHtml(user?.displayName || "")}</h1>
      <span class="font-body-sm text-body-sm" style="color:var(--mint)">${user?.role === "agency" ? "Cuenta de agencia" : "Cuidadora"}</span>
    </div>
    <div class="px-margin-mobile pt-space-lg flex flex-col gap-space-sm">
      ${UI.sectionCard(`<p class="font-body-md text-body-md text-on-surface">Las pantallas de pacientes, turno activo y horas se están construyendo. Tu cuenta ya funciona.</p>`)}
      <button id="btn-shift-logout" class="font-label-md text-label-md text-secondary underline self-start">Cerrar sesión</button>
    </div>`;

  return {
    body,
    afterRender: () => {
      document.getElementById("btn-shift-logout").onclick = async () => {
        await AuthStore.signOut();
        window.location.hash = "#/shift/login";
      };
    },
  };
};

window.Screens = Screens;
