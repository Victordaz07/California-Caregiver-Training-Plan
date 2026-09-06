/**
 * Buscar por Problema — screen 4 in Caregiver Pro v3.dc.html, the "Ayuda"
 * tab of Modo Turno. Each card maps to a real curriculum day that already
 * covers exactly that situation (see docs/CURRICULUM_V2_SOURCES.md) — this
 * is a fast-access lens onto content that's already vetted, not new
 * clinical guidance invented for this screen.
 */
var Screens = window.Screens || {};

const PROBLEMS = [
  { id: "se_cayo", title: "SE CAYÓ", meta: "Qué hacer ahora", icon: "personal_injury", category: "Caídas", day: 41, tileBg: "bg-secondary-container", tileFg: "text-on-secondary-container" },
  { id: "no_banarse", title: "NO QUIERE BAÑARSE", meta: "Cómo pedirlo", icon: "shower", category: "Baño", day: 30, tileBg: "bg-primary-container", tileFg: "text-on-primary-container" },
  { id: "no_come", title: "NO COME", meta: "Señales y qué hacer", icon: "no_meals", category: "Comida", day: 47, tileBg: "bg-tertiary-container", tileFg: "text-tertiary" },
  { id: "agresiva", title: "ESTÁ AGRESIVA", meta: "Calmar sin discutir", icon: "psychology_alt", category: "Ánimo", day: 53, tileBg: "bg-primary-container", tileFg: "text-on-primary-container" },
  { id: "medicina", title: "OLVIDA LA MEDICINA", meta: "Apoyo permitido", icon: "medication", category: "Medicinas", day: 80, tileBg: "bg-secondary-container", tileFg: "text-on-secondary-container" },
  { id: "piel", title: "PIEL ENROJECIDA", meta: "Prevenir llagas", icon: "healing", category: "Piel", day: 33, tileBg: "bg-primary-container", tileFg: "text-on-primary-container" },
];

const CATEGORIES = ["Todo", "Caídas", "Baño", "Comida", "Ánimo", "Medicinas", "Piel"];

Screens.shiftAyuda = async () => {
  const body = `
    <div class="bg-primary px-margin-mobile pt-space-xl pb-space-md flex flex-col gap-space-sm">
      <h1 class="font-display text-3xl text-white leading-tight">¿QUÉ ESTÁ<br>PASANDO?</h1>
      <div class="flex items-center gap-2.5 bg-white rounded-2xl px-4 py-3">
        ${UI.icon("search", "text-outline")}
        <input id="ayuda-search" type="text" placeholder="Escribe o toca un tema" class="flex-1 outline-none font-body-md text-body-md text-on-surface"/>
      </div>
      <div id="ayuda-chips" class="flex gap-2 flex-wrap">
        ${CATEGORIES.map((c, i) => `<span data-cat="${UI.escapeHtml(c)}" class="chip-cat font-label-sm text-label-sm font-black px-3 py-2 rounded-full cursor-pointer ${i === 0 ? "bg-white text-primary" : "text-white"}" style="${i === 0 ? "" : "background:rgba(255,255,255,.14)"}">${UI.escapeHtml(c)}</span>`).join("")}
      </div>
    </div>
    <div id="ayuda-grid" class="px-margin-mobile pt-space-md pb-space-2xl grid grid-cols-2 gap-space-sm">
      ${PROBLEMS.map(problemCardHtml).join("")}
    </div>
    <div id="ayuda-empty" class="hidden px-margin-mobile pt-space-2xl text-center font-body-md text-body-md text-outline">Ningún tema coincide. Prueba con otra palabra.</div>`;

  return {
    body,
    afterRender: () => {
      const search = document.getElementById("ayuda-search");
      const chips = document.querySelectorAll(".chip-cat");
      let activeCategory = "Todo";

      function applyFilter() {
        const q = search.value.trim().toLowerCase();
        let visibleCount = 0;
        document.querySelectorAll("[data-problem-id]").forEach((card) => {
          const p = PROBLEMS.find((x) => x.id === card.dataset.problemId);
          const matchesCategory = activeCategory === "Todo" || p.category === activeCategory;
          const matchesQuery = !q || p.title.toLowerCase().includes(q) || p.meta.toLowerCase().includes(q);
          const show = matchesCategory && matchesQuery;
          card.classList.toggle("hidden", !show);
          if (show) visibleCount++;
        });
        document.getElementById("ayuda-empty").classList.toggle("hidden", visibleCount > 0);
      }

      search.oninput = applyFilter;
      chips.forEach((chip) => {
        chip.onclick = () => {
          activeCategory = chip.dataset.cat;
          chips.forEach((c) => {
            const active = c === chip;
            c.classList.toggle("bg-white", active);
            c.classList.toggle("text-primary", active);
            c.classList.toggle("text-white", !active);
            c.style.background = active ? "" : "rgba(255,255,255,.14)";
          });
          applyFilter();
        };
      });
    },
  };
};

function problemCardHtml(p) {
  return `<a href="#/shift/problema/${p.id}" data-problem-id="${p.id}" class="bg-white border-2 border-surface-container-high rounded-2xl p-space-sm flex flex-col gap-space-xs">
    <div class="w-11 h-11 rounded-xl flex items-center justify-center ${p.tileBg} ${p.tileFg}">${UI.icon(p.icon, "text-2xl")}</div>
    <div class="font-headline-sm text-headline-sm uppercase text-on-surface leading-tight">${UI.escapeHtml(p.title)}</div>
    <div class="font-body-sm text-body-sm text-outline">${UI.escapeHtml(p.meta)}</div>
  </a>`;
}

window.Screens = Screens;
window.PROBLEMS = PROBLEMS;
