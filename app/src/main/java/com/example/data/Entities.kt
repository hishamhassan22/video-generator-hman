package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "video_projects")
data class VideoProject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val prompt: String,
    val narrativeScript: String,
    val voicePersona: String,
    val resolutionSetting: String = "Full HD (1080p)",
    val musicTrack: String = "Cinematic Ambient",
    val videoDurationSeconds: Int = 15,
    val characterId: Int? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "video_scenes",
    foreignKeys = [
        ForeignKey(
            entity = VideoProject::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["projectId"])]
)
data class VideoScene(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,
    val sceneNumber: Int,
    val scenePrompt: String,
    val narrationText: String,
    val imageUrl: String? = null,
    val motionType: String = "Zoom In",
    val durationSeconds: Int = 4
)

@Entity(tableName = "ai_characters")
data class AICharacter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val nameAr: String = "",
    val gender: String, // "Male", "Female", "Child", "Cartoon"
    val avatarSeed: String, // seed for generating beautiful avatars or predefined index
    val description: String,
    val descriptionAr: String = "",
    val defaultVoice: String,
    val isCustom: Boolean = false
)
