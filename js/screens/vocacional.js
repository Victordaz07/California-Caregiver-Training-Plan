/**
 * Web equivalent of ui/screens/AnalizadorVocacionalScreen.kt. The Android
 * build already had to retire fabricated claims here ("MATCH 94%",
 * "Potencial de ingreso hasta $38/h" with no source) — this version never
 * introduces them: honest self-reflection against the six real pathway
 * descriptions, plus a private notes field (saved only in your browser).
 */
var Screens = window.Screens || {};

const VOCATIONAL_NOTES_KEY = "caregiverProCA.vocationalNotes";

Screens.vocacional = async (data) => {
  const savedNotes = localStorage.getItem(VOCATIONAL_NOTES_KEY) || "";

  const body = `
    <div class="flex flex-col w-full px-margin-mobile pb-space-2xl gap-space-lg">
      ${UI.sectionCard(`
        <h2 class="font-headline-sm text-headline-sm text-on-surface">Reflexiona, no adivines</h2>
        <p class="font-body-sm text-body-sm text-outline">Esta app no calcula un "match" ni cifras de salario — eso requeriría datos que no tenemos con fuente verificable. En su lugar, compara honestamente tu situación contra las seis rutas reales.</p>
      `)}
      <div class="flex flex-col gap-space-sm">
        ${data.pathways
          .map(
            (p) => `${UI.sectionCard(`
              <h3 class="font-headline-sm text-headline-sm text-on-surface">${UI.escapeHtml(p.titleEs)}</h3>
              <p class="font-body-sm text-body-sm text-on-surface-variant">${UI.escapeHtml(p.summaryEs)}</p>
              <p class="font-label-sm text-label-sm text-outline italic">${UI.escapeHtml(p.routeNoticeEs)}</p>
            `)}`,
          )
          .join("")}
      </div>
      ${UI.sectionCard(`
        <h3 class="font-headline-sm text-headline-sm text-on-surface">Tus notas privadas</h3>
        <p class="font-body-sm text-body-sm text-outline">Solo se guardan en este navegador, en tu dispositivo.</p>
        <textarea id="vocational-notes" rows="6" class="w-full rounded-lg border border-outline-variant p-space-sm font-body-md text-body-md" placeholder="¿Qué ruta se parece más a tu situación? ¿Qué te falta averiguar?">${UI.escapeHtml(savedNotes)}</textarea>
      `)}
    </div>`;

  return {
    body,
    afterRender: () => {
      const textarea = document.getElementById("vocational-notes");
      let saveTimeout = null;
      textarea.addEventListener("input", () => {
        clearTimeout(saveTimeout);
        saveTimeout = setTimeout(() => localStorage.setItem(VOCATIONAL_NOTES_KEY, textarea.value), 400);
      });
    },
  };
};

window.Screens = Screens;
