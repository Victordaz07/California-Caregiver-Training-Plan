/**
 * App bootstrap: loads real content data, registers the service worker,
 * and renders header/main/bottomnav on every hash change. Mirrors
 * CaregiverNavHost.kt's onboarding gate + 5-tab shell.
 */
(async function main() {
  if ("serviceWorker" in navigator) {
    navigator.serviceWorker.register("sw.js").catch((err) => console.warn("SW no registrado:", err));
  }

  let data;
  try {
    data = await DataStore.loadAll();
  } catch (err) {
    document.getElementById("app-view").innerHTML = `<div class="p-6 text-center text-error">No se pudo cargar el contenido. Revisa tu conexión e intenta de nuevo.<br/><span class="text-xs">${UI.escapeHtml(err.message)}</span></div>`;
    console.error(err);
    return;
  }

  function renderHeader(route) {
    const header = document.getElementById("app-header");
    if (route.kind === "detail") {
      header.innerHTML = UI.detailHeader(route.title, route.back);
    } else if (route.kind === "onboarding") {
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
    nav.innerHTML = `<div class="flex">${TABS.map(
      (t) => `<a href="${t.path}" class="nav-tab ${t.id === route.tab ? "active" : ""}">
        ${UI.icon(t.icon)}
        <span class="nav-tab-label">${t.label}</span>
      </a>`,
    ).join("")}</div>`;
  }

  async function render() {
    const state = AppState.get();
    const path = currentPath();

    if (!state.onboardingCompleted && path !== "#/welcome" && path !== "#/pathway") {
      window.location.hash = "#/welcome";
      return;
    }
    if (state.onboardingCompleted && (path === "#/welcome" || path === "#/pathway")) {
      window.location.hash = "#/hoy";
      return;
    }

    const route = ROUTES[path] || ROUTES["#/hoy"];
    renderHeader(route);
    renderBottomNav(route);

    const view = document.getElementById("app-view");
    const result = await route.render(data);
    view.innerHTML = result.body;
    window.scrollTo(0, 0);
    if (result.afterRender) result.afterRender();
  }

  window.addEventListener("hashchange", render);
  // Re-render on any state change triggered from within a screen (e.g.
  // completing a day, rating a flashcard) so header/body reflect it live.
  AppState.subscribe(() => render());
  render();
})();
