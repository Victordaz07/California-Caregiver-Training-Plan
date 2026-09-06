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

Screens.shiftSignup = async () => {
  if (!FirebaseApp.isConfigured()) return { body: shiftNotConfiguredBody() };

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pt-space-2xl pb-space-2xl gap-space-lg">
      <div class="flex flex-col items-center text-center gap-space-sm">
        <img alt="Caregiver Pro California" class="h-14 w-14" src="assets/logo.svg"/>
        <h1 class="font-display text-3xl leading-none text-primary">Crear cuenta</h1>
      </div>

      <div class="flex gap-space-sm">
        <button data-role="caregiver" class="role-tab flex-1 py-3 rounded-xl border-2 border-primary bg-primary text-white font-label-md text-label-md uppercase">Cuidadora</button>
        <button data-role="agency" class="role-tab flex-1 py-3 rounded-xl border-2 border-surface-container-high bg-white text-on-surface font-label-md text-label-md uppercase">Agencia</button>
      </div>

      <div id="shift-error" class="hidden font-body-sm text-body-sm text-white bg-secondary rounded-xl p-space-sm"></div>

      <div class="flex flex-col gap-space-sm">
        ${authField("shift-name", "Tu nombre", "text", "Marisol Ramírez")}
        ${authField("shift-email", "Correo", "email", "tucorreo@ejemplo.com")}
        ${authField("shift-password", "Contraseña", "password", "Mínimo 6 caracteres")}
        <div id="caregiver-fields">${authField("shift-agency-code", "Código de agencia", "text", "Pídeselo a tu agencia")}</div>
        <div id="agency-fields" class="hidden">${authField("shift-agency-name", "Nombre de tu agencia", "text", "Cuidado Familiar LLC")}</div>
      </div>

      ${UI.bigButton("Crear cuenta", { color: "coral", id: "btn-shift-signup" })}
      <a href="#/shift/login" class="text-center font-label-md text-label-md text-primary underline">Ya tengo cuenta</a>
    </div>`;

  return {
    body,
    afterRender: () => {
      let role = "caregiver";
      const tabs = document.querySelectorAll(".role-tab");
      const caregiverFields = document.getElementById("caregiver-fields");
      const agencyFields = document.getElementById("agency-fields");
      tabs.forEach((tab) => {
        tab.onclick = () => {
          role = tab.dataset.role;
          tabs.forEach((t) => {
            t.classList.toggle("bg-primary", t === tab);
            t.classList.toggle("text-white", t === tab);
            t.classList.toggle("border-primary", t === tab);
            t.classList.toggle("bg-white", t !== tab);
            t.classList.toggle("text-on-surface", t !== tab);
            t.classList.toggle("border-surface-container-high", t !== tab);
          });
          caregiverFields.classList.toggle("hidden", role !== "caregiver");
          agencyFields.classList.toggle("hidden", role !== "agency");
        };
      });

      const errorBox = document.getElementById("shift-error");
      document.getElementById("btn-shift-signup").onclick = async () => {
        errorBox.classList.add("hidden");
        const displayName = document.getElementById("shift-name").value.trim();
        const email = document.getElementById("shift-email").value.trim();
        const password = document.getElementById("shift-password").value;
        try {
          if (role === "agency") {
            const agencyName = document.getElementById("shift-agency-name").value.trim();
            if (!agencyName) throw new Error("Escribe el nombre de tu agencia.");
            await AuthStore.signUpAgency({ email, password, displayName, agencyName });
          } else {
            const agencyId = document.getElementById("shift-agency-code").value.trim();
            if (!agencyId) throw new Error("Pide el código de agencia a tu supervisora.");
            await AuthStore.signUpCaregiver({ email, password, displayName, agencyId });
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
