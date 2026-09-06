/**
 * Ficha de Problema — screen 5 in Caregiver Pro v3.dc.html ("SE CAYÓ" in the
 * mockup), generalized to all 6 cards from shiftAyuda.js. Built entirely
 * from the real curriculum day mapped in PROBLEMS: the "Cómo se hace, paso
 * a paso" steps and (since every mapped day is criticalSafety) the "Punto
 * crítico de seguridad" section, both pulled straight out of the real
 * lessonEs Markdown — nothing on this screen is new advice invented for
 * quick reference; it's the same vetted content, just surfaced fast.
 */
var Screens = window.Screens || {};

Screens.shiftProblema = async (data, params) => {
  const problem = window.PROBLEMS.find((p) => p.id === params.id);
  if (!problem) return { body: `<div class="p-8 text-center text-outline">No se encontró este tema.</div>` };

  const curriculumDay = DataStore.curriculumDayFor(data, problem.day);
  const visual = DataStore.visualForDay(data, problem.day);
  const stepsBlock = UI.findMarkdownBlock(curriculumDay.lessonEs, "Cómo se hace, paso a paso");
  const criticalBlock = UI.findMarkdownBlock(curriculumDay.lessonEs, "Punto crítico de seguridad");

  const steps = (stepsBlock || "")
    .split("\n")
    .map((l) => l.trim())
    .filter(Boolean)
    .map((line) => line.replace(/^\d+\.\s+/, ""));

  const body = `
    <div class="bg-secondary px-margin-mobile pt-space-xl pb-space-md flex flex-col gap-space-sm">
      <div class="flex items-center gap-3">
        <a href="#/shift/ayuda" aria-label="Volver" class="w-10 h-10 rounded-xl bg-white/20 text-white flex items-center justify-center">${UI.icon("arrow_back")}</a>
        <div class="font-display text-2xl text-white">${UI.escapeHtml(problem.title)}</div>
      </div>
    </div>

    <div class="px-margin-mobile pt-space-md pb-space-2xl flex flex-col gap-space-sm">
      ${steps.map((s, i) => UI.numberedStep(i + 1, s)).join("")}

      ${
        criticalBlock
          ? `<div class="rounded-2xl p-space-md flex flex-col gap-2" style="background:var(--coral-bg); border:2px solid #F6C6BE">
              <div class="flex items-center gap-2">${UI.icon("emergency", "")}<span class="font-display text-sm tracking-wide" style="color:var(--coral-dark)">PUNTO CRÍTICO DE SEGURIDAD</span></div>
              <p class="font-body-sm text-body-sm" style="color:var(--coral-dark)">${UI.inlineMarkdown(criticalBlock)}</p>
            </div>`
          : ""
      }

      ${
        visual
          ? `<div class="rounded-2xl overflow-hidden border-[3px] border-primary" style="box-shadow:0 5px 0 var(--teal-dark)">
              <div class="bg-primary px-space-sm py-space-xs flex items-center gap-2">
                ${UI.icon("auto_stories", "text-white")}<span class="font-display text-sm tracking-wide text-white uppercase">Paso a paso ilustrado</span>
              </div>
              <img src="${visual.file}" alt="${UI.escapeHtml(visual.altEs)}" class="w-full h-auto block"/>
            </div>`
          : ""
      }

      <a href="#/leccion/${problem.day}" class="text-center font-label-md text-label-md text-primary underline py-space-xs">Ver la lección completa (día ${problem.day})</a>
    </div>`;

  return { body };
};

window.Screens = Screens;
