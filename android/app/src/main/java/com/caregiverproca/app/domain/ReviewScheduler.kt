package com.caregiverproca.app.domain

import java.time.Clock
import java.time.Duration
import java.time.Instant
import kotlin.math.roundToLong

/**
 * A transparent, deterministic spaced-repetition heuristic — NOT FSRS, and the
 * app must never claim it is (per Fase 4 of the master prompt: "no afirmar
 * 'FSRS' si solo se implementa una heurística distinta. Documenta el
 * algoritmo real."). Adapted from
 * 03_ANDROID_TECHNICAL/reference_kotlin/ReviewScheduler.kt.example.
 *
 * The [clock] is injected so scheduling is testable without wall-clock time.
 */
enum class ReviewRating { AGAIN, HARD, GOOD, EASY }

data class ReviewState(
    val itemId: String,
    val repetitions: Int = 0,
    val lapses: Int = 0,
    val ease: Double = 2.5,
    val intervalDays: Double = 0.0,
    val dueAt: Instant = Instant.EPOCH,
    val lastReviewedAt: Instant? = null,
) {
    companion object {
        /** Bump this if the formula below changes shape, so stored states can be migrated or reset. */
        const val ALGORITHM_VERSION = 1
    }
}

class ReviewScheduler(
    private val clock: Clock,
    private val maxIntervalDays: Double = 365.0,
) {
    fun schedule(current: ReviewState, rating: ReviewRating): ReviewState {
        val now = Instant.now(clock)
        val priorDays = current.intervalDays.coerceAtLeast(1.0)

        val nextEase = when (rating) {
            ReviewRating.AGAIN -> (current.ease - 0.20).coerceIn(1.30, 3.00)
            ReviewRating.HARD -> (current.ease - 0.05).coerceIn(1.30, 3.00)
            ReviewRating.GOOD -> current.ease.coerceIn(1.30, 3.00)
            ReviewRating.EASY -> (current.ease + 0.10).coerceIn(1.30, 3.00)
        }

        val nextDays = when (rating) {
            ReviewRating.AGAIN -> 0.0
            ReviewRating.HARD -> (priorDays * 1.2).coerceAtMost(maxIntervalDays)
            ReviewRating.GOOD -> (priorDays * nextEase).coerceAtMost(maxIntervalDays)
            ReviewRating.EASY -> (priorDays * nextEase * 1.3).coerceAtMost(maxIntervalDays)
        }

        val dueAt = if (rating == ReviewRating.AGAIN) {
            now.plus(Duration.ofMinutes(10))
        } else {
            now.plus(Duration.ofHours((nextDays * 24.0).roundToLong().coerceAtLeast(1)))
        }

        return current.copy(
            repetitions = if (rating == ReviewRating.AGAIN) 0 else current.repetitions + 1,
            lapses = current.lapses + if (rating == ReviewRating.AGAIN) 1 else 0,
            ease = nextEase,
            intervalDays = nextDays,
            dueAt = dueAt,
            lastReviewedAt = now,
        )
    }
}
