package com.caregiverproca.app.data

import android.content.Context
import com.caregiverproca.app.data.local.AppDatabase
import com.caregiverproca.app.data.local.CurriculumProgressEntity
import com.caregiverproca.app.data.local.ReviewStateEntity
import com.caregiverproca.app.domain.ReviewRating
import com.caregiverproca.app.domain.ReviewScheduler
import com.caregiverproca.app.domain.ReviewState
import java.time.Clock
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room-backed source of truth for durable learning facts (curriculum
 * completion, flashcard review state) — the piece DataStore intentionally
 * does not cover (see docs/caregiver_upgrade/DECISIONS.md ADR-002, now
 * superseded by ADR-007 now that Room is wired up).
 */
class LearningRepository(
    context: Context,
    private val clock: Clock = Clock.systemUTC(),
) {
    private val db = AppDatabase.getInstance(context)
    private val scheduler = ReviewScheduler(clock)

    val completedDays: Flow<Set<Int>> =
        db.curriculumProgressDao().observeAll().map { rows -> rows.filter { it.completed }.map { it.day }.toSet() }

    val completedDayCount: Flow<Int> = db.curriculumProgressDao().observeCompletedCount()

    suspend fun markDayCompleted(day: Int) {
        db.curriculumProgressDao().upsert(
            CurriculumProgressEntity(day = day, completed = true, completedAtEpochMillis = Instant.now(clock).toEpochMilli()),
        )
    }

    val dueReviewCount: Flow<Int> = db.reviewStateDao().observeDueCount(Instant.now(clock).toEpochMilli())

    val allReviewStates: Flow<List<ReviewStateEntity>> = db.reviewStateDao().observeAll()

    /** Applies [rating] to the card's current review state (or a fresh one) and persists the result. */
    suspend fun rateCard(cardId: String, rating: ReviewRating) {
        val dao = db.reviewStateDao()
        val existing = dao.getByCardId(cardId)
        val current = existing?.toDomain() ?: ReviewState(itemId = cardId)
        val next = scheduler.schedule(current, rating)
        dao.upsert(next.toEntity())
    }

    suspend fun dueCardIds(cardIds: List<String>): List<String> {
        val now = Instant.now(clock).toEpochMilli()
        val dao = db.reviewStateDao()
        return cardIds.filter { id ->
            val state = dao.getByCardId(id)
            state == null || state.dueAtEpochMillis <= now
        }
    }
}

private fun ReviewStateEntity.toDomain(): ReviewState = ReviewState(
    itemId = cardId,
    repetitions = repetitions,
    lapses = lapses,
    ease = ease,
    intervalDays = intervalDays,
    dueAt = Instant.ofEpochMilli(dueAtEpochMillis),
    lastReviewedAt = lastReviewedAtEpochMillis?.let(Instant::ofEpochMilli),
)

private fun ReviewState.toEntity(): ReviewStateEntity = ReviewStateEntity(
    cardId = itemId,
    repetitions = repetitions,
    lapses = lapses,
    ease = ease,
    intervalDays = intervalDays,
    dueAtEpochMillis = dueAt.toEpochMilli(),
    lastReviewedAtEpochMillis = lastReviewedAt?.toEpochMilli(),
    algorithmVersion = ReviewState.ALGORITHM_VERSION,
)
