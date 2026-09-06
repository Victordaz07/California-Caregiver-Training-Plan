/**
 * App bootstrap: loads real content data, registers the service worker,
 * and renders header/main/bottomnav on every hash change. Mirrors
 * CaregiverNavHost.kt's onboarding gate + 5-tab shell.
 */
(async function main() {
  if ("serviceWorker" in navigator) {
    navigator.serviceWorker
      .register("sw.js")
      .then((reg) => {
        // Browsers don't always re-check for a new sw.js promptly on their
        // own; ask explicitly every time the app opens so a fixed/updated
        // service worker (like the cache-versioning fix that prompted this)
        // reaches an already-installed PWA on its very next open.
        reg.update().catch(() => {});
      })
      .catch((err) => console.warn("SW no registrado:", err));

    // Once a *new* service worker takes over an already-open page, its old
    // cached JS/CSS/JSON is stale until the page is reloaded — do that
    // reload automatically, once, instead of leaving the user stuck on
    // stale code until they notice and refresh themselves. "controllerchange"
    // also fires once on a page's very first-ever load (no update involved),
    // so only auto-reload when a controller already existed before that —
    // i.e. this really is a worker replacing an earlier one.
    const hadControllerAtLoad = !!navigator.serviceWorker.controller;
    let reloadedForNewSw = false;
    navigator.serviceWorker.addEventListener("controllerchange", () => {
      if (!hadControllerAtLoad || reloadedForNewSw) return;
      reloadedForNewSw = true;
      window.location.reload();
    });
  }

  AuthStore.init();

  let data;
  try {
    data = await DataStore.loadAll();
  } catch (err) {
    document.getElementById("app-view").innerHTML = `<div class="p-6 text-center text-error">No se pudo cargar el contenido. Revisa tu conexión e intenta de nuevo.<br/><span class="text-xs">${UI.escapeHtml(err.message)}</span></div>`;
    console.error(err);
    return;
  }

  function backHref(route, params) {
    return typeof route.back === "function" ? route.back(params) : route.back;
  }

  function renderHeader(route, params) {
    const header = document.getElementById("app-header");
    if (route.noHeader) {
      // Screen renders its own full-bleed banner; only float a back button
      // over it for pushed (detail) screens — hub tabs need no back arrow.
      header.innerHTML =
        route.kind === "detail"
          ? `<a href="${backHref(route, params)}" aria-label="Volver" class="absolute top-4 left-4 w-10 h-10 rounded-full bg-white/90 flex items-center justify-center" style="color:var(--teal-dark)">${UI.icon("arrow_back")}</a>`
          : "";
    } else if (route.kind === "detail") {
      header.innerHTML = UI.detailHeader(route.title, backHref(route, params));
    } else if (route.kind === "onboarding" || route.kind === "shift-auth") {
      header.innerHTML = "";
    } else {
      const state = AppState.get();
      const day = state.currentPlanDay;
      const week = DataStore.curriculumDayFor(data, day)?.week ?? 1;
      header.innerHTML = UI.hubHeader("Caregiver Pro CA", `Día ${day} / 90 (Semana ${week})`);
    }
  }

  function renderBottomNav(route) {
    const nav = document.getElementById("app-bottomnav");
    if (route.kind !== "hub") {
      nav.classList.add("hidden");
      return;
    }
    nav.classList.remove("hidden");
    const tabs = route.navGroup === "shift" ? SHIFT_TABS : TRAINING_TABS;
    nav.innerHTML = `<div class="flex">${tabs.map(
      (t) => `<a href="${t.path}" class="nav-tab ${t.id === route.tab ? "active" : ""}">
        ${UI.icon(t.icon)}
        <span class="nav-tab-label">${t.label}</span>
      </a>`,
    ).join("")}</div>`;
  }

  async function render() {
    const state = AppState.get();
    const path = currentPath();

    if (!state.onboardingCompleted && path !== "#/welcome" && path !== "#/pathway" && !path.startsWith("#/shift")) {
      window.location.hash = "#/welcome";
      return;
    }
    if (state.onboardingCompleted && (path === "#/welcome" || path === "#/pathway")) {
      window.location.hash = "#/hoy";
      return;
    }

    // Modo Turno has its own account system (Firebase), separate from the
    // 90-day course's onboarding gate above.
    if (path.startsWith("#/shift") && path !== "#/shift/login" && path !== "#/shift/signup") {
      if (!AuthStore.isReady()) {
        // Wait for the pending onAuthStateChanged callback; it re-triggers
        // render() via the AuthStore.subscribe() call below once resolved.
        document.getElementById("app-view").innerHTML = `<div class="p-8 text-center font-body-md text-body-md text-outline">Cargando…</div>`;
        return;
      }
      if (FirebaseApp.isConfigured() && !AuthStore.get()) {
        window.location.hash = "#/shift/login";
        return;
      }
    }

    const matched = matchRoute(path) || { route: ROUTES["#/hoy"], params: {} };
    const { route, params } = matched;
    renderHeader(route, params);
    renderBottomNav(route);

    const view = document.getElementById("app-view");
    view.classList.toggle("pt-16", !route.noHeader);
    const result = await route.render(data, params);
    view.innerHTML = result.body;
    window.scrollTo(0, 0);
    if (result.afterRender) result.afterRender();
  }

  window.addEventListener("hashchange", render);
  // Re-render on any state change triggered from within a screen (e.g.
  // completing a day, rating a flashcard) so header/body reflect it live.
  AppState.subscribe(() => render());
  // Re-render once Firebase resolves the signed-in user (Modo Turno) and on
  // every subsequent sign-in/sign-out.
  AuthStore.subscribe(() => render());
  render();
})();
