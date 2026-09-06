/**
 * Loads the real content (legal-corrected, verbatim from the same source the
 * Android build used — see android/app/.../content/*.kt and
 * docs/caregiver_upgrade/) as JSON. Fetched once and cached in memory; the
 * service worker (sw.js) caches the JSON files themselves for offline use.
 */
const DataStore = (() => {
  let cache = null;

  async function loadAll() {
    if (cache) return cache;
    const files = [
      "pathways",
      "legal_requirements",
      "official_resources",
      "disclaimers",
      "curriculum",
      "module_titles",
      "flashcards",
      "scenarios",
      "rubrics",
      "glossary",
      "audio_episodes",
      "lesson_visual_manifest",
      "banner_manifest",
      "brand_manifest",
    ];
    const entries = await Promise.all(
      files.map(async (name) => {
        const res = await fetch(`data/${name}.json`);
        if (!res.ok) throw new Error(`No se pudo cargar data/${name}.json`);
        return [name, await res.json()];
      }),
    );
    cache = Object.fromEntries(entries);
    return cache;
  }

  function pathwayById(data, id) {
    return data.pathways.find((p) => p.id === id) || null;
  }

  function legalRequirementsFor(data, pathwayId) {
    return data.legal_requirements.filter(
      (r) => r.pathwayIds === "ALL" || r.pathwayIds.includes(pathwayId),
    );
  }

  function officialResourcesFor(data, pathwayId) {
    return data.official_resources.filter(
      (r) => r.pathwayIds === "ALL" || r.pathwayIds.includes(pathwayId),
    );
  }

  function curriculumDayFor(data, day) {
    return data.curriculum.find((d) => d.day === day) || null;
  }

  function curriculumWeek(data, week) {
    return data.curriculum.filter((d) => d.week === week);
  }

  function scenarioFor(data, week) {
    return data.scenarios.find((s) => s.week === week) || null;
  }

  function audioEpisodeFor(data, week) {
    return data.audio_episodes.find((e) => e.week === week) || null;
  }

  function rubricCriterion(data, id) {
    return data.rubrics.find((r) => r.id === id) || null;
  }

  /** The comic-style step-by-step lámina covering a given day, if any (a lámina can cover several days). */
  function visualForDay(data, day) {
    return data.lesson_visual_manifest.visuals.find((v) => v.days.includes(day)) || null;
  }

  /** The per-lesson banner image for a given day. */
  function bannerForDay(data, day) {
    return data.banner_manifest.banners.find((b) => b.day === day) || null;
  }

  return {
    loadAll,
    pathwayById,
    legalRequirementsFor,
    officialResourcesFor,
    curriculumDayFor,
    curriculumWeek,
    scenarioFor,
    audioEpisodeFor,
    rubricCriterion,
    visualForDay,
    bannerForDay,
  };
})();
