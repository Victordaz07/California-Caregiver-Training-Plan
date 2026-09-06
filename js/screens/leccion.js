/**
 * Lección completa — full reading view for one day's lessonEs (692-834
 * words each, from CAREGIVER_V2_PARTE_1/2/3, docs/CURRICULUM_V2_SOURCES.md).
 * Reached from the Hoy card's "Leer lección completa" link. Shows the
 * day's banner, the full lesson as Markdown, and — when this day has one —
 * the step-by-step comic lámina with its alt text and safety stop, exactly
 * per the integration rules in the content pack's README (don't gate
 * progress on opening the image; allow a full-screen view).
 */
var Screens = window.Screens || {};

Screens.leccion = async (data, params) => {
  const day = Number(params.day);
  const curriculumDay = DataStore.curriculumDayFor(data, day);
  if (!curriculumDay) return { body: `<div class="p-8 text-center text-outline">No se encontró esta lección.</div>` };

  const banner = DataStore.bannerForDay(data, day);
  const visual = DataStore.visualForDay(data, day);
  const moduleTitle = data.module_titles[curriculumDay.moduleId] || curriculumDay.moduleId;

  const body = `
    <div class="flex flex-col w-full pb-space-2xl">
      ${banner ? `<img src="${banner.file}" alt="${UI.escapeHtml(banner.altEs)}" class="w-full h-auto block"/>` : ""}

      <div class="px-margin-mobile pt-space-md flex flex-col gap-space-md">
        <div class="flex flex-col gap-1">
          ${UI.statusPill(`Día ${day} · Semana ${curriculumDay.week} · ${moduleTitle}`)}
          <h1 class="font-display text-3xl leading-tight text-primary">${UI.escapeHtml(curriculumDay.titleEs)}</h1>
        </div>

        <div class="flex flex-col gap-space-sm">${UI.renderMarkdown(curriculumDay.lessonEs)}</div>

        ${
          visual
            ? `<div class="rounded-2xl overflow-hidden border-[3px] border-primary" style="box-shadow:0 5px 0 var(--teal-dark)">
                <div class="bg-primary px-space-sm py-space-xs flex items-center gap-2">
                  ${UI.icon("auto_stories", "text-white")}<span class="font-display text-sm tracking-wide text-white uppercase">Paso a paso ilustrado</span>
                </div>
                <img id="leccion-lamina" src="${visual.file}" alt="${UI.escapeHtml(visual.altEs)}" class="w-full h-auto block cursor-zoom-in"/>
                ${visual.criticalStopEs ? `<div class="flex gap-2 items-start px-space-sm py-space-sm" style="background:var(--coral-bg)"><span style="color:var(--coral-dark)">${UI.icon("emergency")}</span><p class="font-body-sm text-body-sm font-extrabold" style="color:var(--coral-dark)">${UI.escapeHtml(visual.criticalStopEs)}</p></div>` : ""}
              </div>`
            : ""
        }

        ${UI.sectionCard(`
          <h3 class="font-headline-sm text-headline-sm uppercase text-on-surface">Actividad de práctica</h3>
          <p class="font-body-md text-body-md text-on-surface-variant">${UI.escapeHtml(curriculumDay.practiceEs)}</p>
        `)}

        ${curriculumDay.criticalSafety ? UI.disclaimerBanner("Emergency", data.disclaimers) : ""}
      </div>
    </div>

    <div id="lamina-lightbox" class="hidden fixed inset-0 z-50 bg-black/90 flex items-center justify-center p-4" role="dialog" aria-modal="true">
      <img src="${visual ? visual.file : ""}" alt="${visual ? UI.escapeHtml(visual.altEs) : ""}" class="max-w-full max-h-full object-contain"/>
    </div>`;

  return {
    body,
    afterRender: () => {
      const lamina = document.getElementById("leccion-lamina");
      const lightbox = document.getElementById("lamina-lightbox");
      if (lamina && lightbox) {
        lamina.onclick = () => lightbox.classList.remove("hidden");
        lightbox.onclick = () => lightbox.classList.add("hidden");
      }
    },
  };
};

window.Screens = Screens;
