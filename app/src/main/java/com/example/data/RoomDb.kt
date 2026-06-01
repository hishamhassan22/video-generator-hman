package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoProjectDao {
    @Query("SELECT * FROM video_projects ORDER BY createdAt DESC")
    fun getAllProjectsFlow(): Flow<List<VideoProject>>

    @Query("SELECT * FROM video_projects WHERE id = :projectId LIMIT 1")
    suspend fun getProjectById(projectId: Int): VideoProject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: VideoProject): Long

    @Update
    suspend fun updateProject(project: VideoProject)

    @Query("DELETE FROM video_projects WHERE id = :projectId")
    suspend fun deleteProjectById(projectId: Int)
}

@Dao
interface VideoSceneDao {
    @Query("SELECT * FROM video_scenes WHERE projectId = :projectId ORDER BY sceneNumber ASC")
    fun getScenesForProjectFlow(projectId: Int): Flow<List<VideoScene>>

    @Query("SELECT * FROM video_scenes WHERE projectId = :projectId ORDER BY sceneNumber ASC")
    suspend fun getScenesForProject(projectId: Int): List<VideoScene>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScene(scene: VideoScene): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenes(scenes: List<VideoScene>)

    @Query("DELETE FROM video_scenes WHERE projectId = :projectId")
    suspend fun deleteScenesForProject(projectId: Int)
}

@Dao
interface AICharacterDao {
    @Query("SELECT * FROM ai_characters")
    fun getAllCharactersFlow(): Flow<List<AICharacter>>

    @Query("SELECT * FROM ai_characters")
    suspend fun getAllCharacters(): List<AICharacter>

    @Query("SELECT * FROM ai_characters WHERE id = :characterId LIMIT 1")
    suspend fun getCharacterById(characterId: Int): AICharacter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: AICharacter): Long

    @Query("DELETE FROM ai_characters WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Int)
}

@Database(entities = [VideoProject::class, VideoScene::class, AICharacter::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoProjectDao(): VideoProjectDao
    abstract fun videoSceneDao(): VideoSceneDao
    abstract fun aiCharacterDao(): AICharacterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "video_studio_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
