package com.caregiverproca.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CurriculumProgressEntity::class, ReviewStateEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun curriculumProgressDao(): CurriculumProgressDao
    abstract fun reviewStateDao(): ReviewStateDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "caregiver_pro_ca.db",
                ).build().also { instance = it }
            }
    }
}
