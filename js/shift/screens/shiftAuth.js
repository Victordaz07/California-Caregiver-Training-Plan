/**
 * Login/signup for Modo Turno (patients, geofenced check-in/out, hours,
 * agency panel). Separate from the 90-day course's onboarding — that one
 * needs no account because its data lives only in this browser; this
 * module needs a real account because an agency admin has to see a
 * caregiver's shift data too.
 */
var Screens = window.Screens || {};

function shiftNotConfiguredBody() {
  return `
    <div class="flex flex-col w-full px-margin-mobile pt-space-2xl pb-space-2xl gap-space-md items-center text-center">
      ${UI.icon("cloud_off", "text-5xl text-outline")}
      <h1 class="font-headline-md text-headline-md uppercase text-on-surface">Modo Turno no está configurado todavía</h1>
      <p class="font-body-md text-body-md text-outline max-w-md">Este módulo necesita un proyecto de Firebase (gratis). Sigue los pasos de <code class="font-mono text-body-sm">docs/FIREBASE_SETUP.md</code> y pega tu configuración en <code class="font-mono text-body-sm">js/shift/firebase-config.js</code>.</p>
      <a href="#/carrera" class="font-label-md text-label-md text-primary underline mt-space-sm">Volver a Carrera</a>
    </div>`;
}

function authField(id, label, type = "text", placeholder = "") {
  return `<label class="flex flex-col gap-1">
    <span class="font-label-sm text-label-sm text-outline uppercase">${UI.escapeHtml(label)}</span>
    <input id="${id}" type="${type}" placeholder="${UI.escapeHtml(placeholder)}" class="border-2 border-surface-container-high rounded-xl px-4 py-3 font-body-md text-body-md text-on-surface bg-white focus:border-primary outline-none"/>
  </label>`;
}

Screens.shiftLogin = async () => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pt-space-2xl pb-space-2xl gap-space-lg">
      <div class="flex flex-col items-center text-center gap-space-sm">
        <img alt="Caregiver Pro California" class="h-14 w-14" src="assets/logo.svg"/>
        <h1 class="font-display text-3xl leading-none text-primary">Modo Turno</h1>
        <p class="font-body-md text-body-md text-outline">Inicia sesión para ver tus pacientes y registrar tu turno.</p>
      </div>
      <div id="shift-error" class="hidden font-body-sm text-body-sm text-white bg-secondary rounded-xl p-space-sm"></div>
      <div class="flex flex-col gap-space-sm">
        ${authField("shift-email", "Correo", "email", "tucorreo@ejemplo.com")}
        ${authField("shift-password", "Contraseña", "password", "••••••••")}
      </div>
      ${UI.bigButton("Entrar", { color: "teal", id: "btn-shift-login" })}
      <a href="#/shift/signup" class="text-center font-label-md text-label-md text-primary underline">Crear una cuenta nueva</a>
    </div>`;

  return {
    body,
    afterRender: () => {
      const errorBox = document.getElementById("shift-error");
      document.getElementById("btn-shift-login").onclick = async () => {
        errorBox.classList.add("hidden");
        const email = document.getElementById("shift-email").value.trim();
        const password = document.getElementById("shift-password").value;
        try {
          await AuthStore.signIn({ email, password });
          window.location.hash = "#/shift";
        } catch (err) {
          errorBox.textContent = "No pudimos iniciar sesión: " + err.message;
          errorBox.classList.remove("hidden");
        }
      };
    },
  };
};

const SITUATION_OPTIONS = [
  { id: "independent", icon: "work_history", label: "TRABAJO INDEPENDIENTE", meta: "No necesitas ningún código" },
  { id: "employee", icon: "badge", label: "TRABAJO PARA UNA AGENCIA", meta: "Pide el código a quien te contrató" },
  { id: "owner", icon: "apartment", label: "ADMINISTRO UNA AGENCIA", meta: "Creas la cuenta para tu equipo" },
];

function situationCardHtml(opt, active) {
  return `<button data-situation="${opt.id}" class="situation-card flex items-center gap-space-sm text-left rounded-2xl p-space-sm border-2 ${active ? "border-primary bg-primary-container" : "border-surface-container-high bg-white"}">
    <div class="w-11 h-11 flex-shrink-0 rounded-xl flex items-center justify-center ${active ? "bg-primary text-white" : "bg-surface-container text-outline"}">${UI.icon(opt.icon)}</div>
    <div class="flex-1 min-w-0">
      <div class="font-headline-sm text-headline-sm uppercase ${active ? "text-primary" : "text-on-surface"}">${opt.label}</div>
      <div class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(opt.meta)}</div>
    </div>
    ${active ? UI.icon("check_circle", "text-primary") : ""}
  </button>`;
}

Screens.shiftSignup = async () => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pt-space-2xl pb-space-2xl gap-space-lg">
      <div class="flex flex-col items-center text-center gap-space-sm">
        <img alt="Caregiver Pro California" class="h-14 w-14" src="assets/logo.svg"/>
        <h1 class="font-display text-3xl leading-none text-primary">Crear cuenta</h1>
        <p class="font-body-sm text-body-sm text-outline">¿Cuál describe mejor tu situación?</p>
      </div>

      <div id="situation-cards" class="flex flex-col gap-space-xs">
        ${SITUATION_OPTIONS.map((opt, i) => situationCardHtml(opt, i === 0)).join("")}
      </div>

      <div id="shift-error" class="hidden font-body-sm text-body-sm text-white bg-secondary rounded-xl p-space-sm"></div>

      <div class="flex flex-col gap-space-sm">
        ${authField("shift-name", "Tu nombre", "text", "Tu nombre completo")}
        ${authField("shift-email", "Correo", "email", "tucorreo@ejemplo.com")}
        ${authField("shift-password", "Contraseña", "password", "Mínimo 6 caracteres")}
        <div id="employee-fields" class="hidden">${authField("shift-agency-code", "Código de agencia", "text", "Pídeselo a quien te contrató")}</div>
        <div id="owner-fields" class="hidden">${authField("shift-agency-name", "Nombre de tu agencia", "text", "Cuidado Familiar LLC")}</div>
      </div>

      ${UI.bigButton("Crear cuenta", { color: "coral", id: "btn-shift-signup" })}
      <a href="#/shift/login" class="text-center font-label-md text-label-md text-primary underline">Ya tengo cuenta</a>
    </div>`;

  return {
    body,
    afterRender: () => {
      let situation = "independent";
      const cards = document.getElementById("situation-cards");
      const employeeFields = document.getElementById("employee-fields");
      const ownerFields = document.getElementById("owner-fields");

      function selectSituation(id) {
        situation = id;
        cards.innerHTML = SITUATION_OPTIONS.map((opt) => situationCardHtml(opt, opt.id === situation)).join("");
        wireCards();
        employeeFields.classList.toggle("hidden", situation !== "employee");
        ownerFields.classList.toggle("hidden", situation !== "owner");
      }

      function wireCards() {
        cards.querySelectorAll(".situation-card").forEach((card) => {
          card.onclick = () => selectSituation(card.dataset.situation);
        });
      }
      wireCards();

      const errorBox = document.getElementById("shift-error");
      document.getElementById("btn-shift-signup").onclick = async () => {
        errorBox.classList.add("hidden");
        const displayName = document.getElementById("shift-name").value.trim();
        const email = document.getElementById("shift-email").value.trim();
        const password = document.getElementById("shift-password").value;
        try {
          if (!displayName) throw new Error("Escribe tu nombre.");
          if (situation === "owner") {
            const agencyName = document.getElementById("shift-agency-name").value.trim();
            if (!agencyName) throw new Error("Escribe el nombre de tu agencia.");
            await AuthStore.signUpAgency({ email, password, displayName, agencyName });
          } else if (situation === "employee") {
            const agencyId = document.getElementById("shift-agency-code").value.trim();
            if (!agencyId) throw new Error("Pide el código de agencia a quien te contrató.");
            await AuthStore.signUpCaregiver({ email, password, displayName, agencyId });
          } else {
            await AuthStore.signUpIndependent({ email, password, displayName });
          }
          window.location.hash = "#/shift";
        } catch (err) {
          errorBox.textContent = "No pudimos crear tu cuenta: " + err.message;
          errorBox.classList.remove("hidden");
        }
      };
    },
  };
};

window.Screens = Screens;
