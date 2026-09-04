package com.caregiverproca.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** One row per curriculum day (1-90) the learner has marked complete. Absence of a row means "not started". */
@Entity(tableName = "curriculum_progress")
data class CurriculumProgressEntity(
    @PrimaryKey val day: Int,
    val completed: Boolean,
    val completedAtEpochMillis: Long?,
)

/**
 * Spaced-repetition state for one flashcard, per [com.caregiverproca.app.domain.ReviewScheduler].
 * `dueAtEpochMillis` / `lastReviewedAtEpochMillis` store [java.time.Instant.toEpochMilli] —
 * plain Long instead of a Room TypeConverter for java.time.Instant, to keep this schema simple.
 */
@Entity(tableName = "review_state")
data class ReviewStateEntity(
    @PrimaryKey val cardId: String,
    val repetitions: Int,
    val lapses: Int,
    val ease: Double,
    val intervalDays: Double,
    val dueAtEpochMillis: Long,
    val lastReviewedAtEpochMillis: Long?,
    val algorithmVersion: Int,
)
