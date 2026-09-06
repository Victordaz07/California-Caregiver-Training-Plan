/**
 * localStorage-backed state — the web equivalent of
 * UserPreferencesRepository (DataStore) + LearningRepository (Room) in the
 * Android build. Single JSON blob under one key, with a tiny pub/sub so
 * screens can react to changes without a framework.
 */
const AppState = (() => {
  const STORAGE_KEY = "caregiverProCA.v1";

  function defaultState() {
    return {
      pathwayId: null,
      onboardingCompleted: false,
      currentPlanDay: 1,
      completedBlocksToday: 0,
      curriculumProgress: {}, // { [day: number]: true }
      reviewState: {}, // { [cardId: string]: ReviewState }
    };
  }

  function load() {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (!raw) return defaultState();
      return { ...defaultState(), ...JSON.parse(raw) };
    } catch (e) {
      console.warn("No se pudo leer el estado guardado, usando valores por defecto.", e);
      return defaultState();
    }
  }

  let state = load();
  const listeners = new Set();

  function save() {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
    } catch (e) {
      console.warn("No se pudo guardar el estado (localStorage lleno o bloqueado).", e);
    }
    listeners.forEach((fn) => fn(state));
  }

  function get() {
    return state;
  }

  function subscribe(fn) {
    listeners.add(fn);
    return () => listeners.delete(fn);
  }

  function setPathway(pathwayId) {
    state.pathwayId = pathwayId;
    save();
  }

  function completeOnboarding() {
    state.onboardingCompleted = true;
    save();
  }

  function markDayCompleted(day) {
    state.curriculumProgress[day] = true;
    if (day === state.currentPlanDay) {
      state.currentPlanDay = Math.min(day + 1, 90);
    }
    save();
  }

  function completedDayCount() {
    return Object.keys(state.curriculumProgress).length;
  }

  function isDayCompleted(day) {
    return !!state.curriculumProgress[day];
  }

  /** Cards due now, or never reviewed, from the given id list — same semantics as LearningRepository.dueCardIds. */
  function dueCardIds(allCardIds, nowMs = Date.now()) {
    return allCardIds.filter((id) => {
      const rs = state.reviewState[id];
      return !rs || rs.dueAtMs <= nowMs;
    });
  }

  function reviewStateFor(cardId) {
    return state.reviewState[cardId] || newReviewState(cardId);
  }

  function rateCard(cardId, rating) {
    const current = reviewStateFor(cardId);
    const next = scheduleReview(current, rating);
    state.reviewState[cardId] = next;
    save();
    return next;
  }

  function dueReviewCount(allCardIds, nowMs = Date.now()) {
    return dueCardIds(allCardIds, nowMs).length;
  }

  function reset() {
    state = defaultState();
    save();
  }

  return {
    get,
    subscribe,
    setPathway,
    completeOnboarding,
    markDayCompleted,
    completedDayCount,
    isDayCompleted,
    dueCardIds,
    dueReviewCount,
    reviewStateFor,
    rateCard,
    reset,
  };
})();
