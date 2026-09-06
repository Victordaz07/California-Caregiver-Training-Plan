/**
 * Ported verbatim (same formula, same constants) from
 * android/app/src/main/java/com/caregiverproca/app/domain/ReviewScheduler.kt.
 * A transparent, deterministic spaced-repetition heuristic — NOT FSRS, and
 * this app must never claim it is. clockNowMs is injectable for testing.
 */
const ReviewRating = Object.freeze({ AGAIN: "AGAIN", HARD: "HARD", GOOD: "GOOD", EASY: "EASY" });

const REVIEW_ALGORITHM_VERSION = 1;

function newReviewState(itemId) {
  return {
    itemId,
    repetitions: 0,
    lapses: 0,
    ease: 2.5,
    intervalDays: 0.0,
    dueAtMs: 0,
    lastReviewedAtMs: null,
  };
}

function clamp(value, min, max) {
  return Math.min(Math.max(value, min), max);
}

/**
 * @param {object} current ReviewState-shaped object (see newReviewState)
 * @param {string} rating one of ReviewRating
 * @param {number} nowMs current time in ms (Date.now() by default)
 * @param {number} maxIntervalDays default 365
 */
function scheduleReview(current, rating, nowMs = Date.now(), maxIntervalDays = 365.0) {
  const priorDays = Math.max(current.intervalDays, 1.0);

  let nextEase;
  switch (rating) {
    case ReviewRating.AGAIN:
      nextEase = clamp(current.ease - 0.2, 1.3, 3.0);
      break;
    case ReviewRating.HARD:
      nextEase = clamp(current.ease - 0.05, 1.3, 3.0);
      break;
    case ReviewRating.GOOD:
      nextEase = clamp(current.ease, 1.3, 3.0);
      break;
    case ReviewRating.EASY:
      nextEase = clamp(current.ease + 0.1, 1.3, 3.0);
      break;
    default:
      throw new Error(`Unknown rating: ${rating}`);
  }

  let nextDays;
  switch (rating) {
    case ReviewRating.AGAIN:
      nextDays = 0.0;
      break;
    case ReviewRating.HARD:
      nextDays = Math.min(priorDays * 1.2, maxIntervalDays);
      break;
    case ReviewRating.GOOD:
      nextDays = Math.min(priorDays * nextEase, maxIntervalDays);
      break;
    case ReviewRating.EASY:
      nextDays = Math.min(priorDays * nextEase * 1.3, maxIntervalDays);
      break;
  }

  const dueAtMs =
    rating === ReviewRating.AGAIN
      ? nowMs + 10 * 60 * 1000
      : nowMs + Math.max(Math.round(nextDays * 24.0), 1) * 60 * 60 * 1000;

  return {
    itemId: current.itemId,
    repetitions: rating === ReviewRating.AGAIN ? 0 : current.repetitions + 1,
    lapses: current.lapses + (rating === ReviewRating.AGAIN ? 1 : 0),
    ease: nextEase,
    intervalDays: nextDays,
    dueAtMs,
    lastReviewedAtMs: nowMs,
  };
}
