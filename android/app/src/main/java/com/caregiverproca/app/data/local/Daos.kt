package com.caregiverproca.app.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CurriculumProgressDao {
    @Upsert
    suspend fun upsert(entity: CurriculumProgressEntity)

    @Query("SELECT * FROM curriculum_progress")
    fun observeAll(): Flow<List<CurriculumProgressEntity>>

    @Query("SELECT COUNT(*) FROM curriculum_progress WHERE completed = 1")
    fun observeCompletedCount(): Flow<Int>
}

@Dao
interface ReviewStateDao {
    @Upsert
    suspend fun upsert(entity: ReviewStateEntity)

    @Query("SELECT * FROM review_state")
    fun observeAll(): Flow<List<ReviewStateEntity>>

    @Query("SELECT * FROM review_state WHERE cardId = :cardId")
    suspend fun getByCardId(cardId: String): ReviewStateEntity?

    @Query("SELECT COUNT(*) FROM review_state WHERE dueAtEpochMillis <= :nowEpochMillis")
    fun observeDueCount(nowEpochMillis: Long): Flow<Int>
}
