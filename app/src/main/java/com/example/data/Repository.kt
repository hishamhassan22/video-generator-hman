package com.example.data

import kotlinx.coroutines.flow.Flow

class StudioRepository(private val db: AppDatabase) {
    val allProjects: Flow<List<VideoProject>> = db.videoProjectDao().getAllProjectsFlow()
    val allCharacters: Flow<List<AICharacter>> = db.aiCharacterDao().getAllCharactersFlow()

    suspend fun getProjectById(id: Int): VideoProject? = db.videoProjectDao().getProjectById(id)
    suspend fun insertProject(project: VideoProject): Long = db.videoProjectDao().insertProject(project)
    suspend fun updateProject(project: VideoProject) = db.videoProjectDao().updateProject(project)
    suspend fun deleteProject(id: Int) = db.videoProjectDao().deleteProjectById(id)

    fun getScenesForProjectFlow(projectId: Int): Flow<List<VideoScene>> = db.videoSceneDao().getScenesForProjectFlow(projectId)
    suspend fun getScenesForProject(projectId: Int): List<VideoScene> = db.videoSceneDao().getScenesForProject(projectId)
    suspend fun insertScene(scene: VideoScene) = db.videoSceneDao().insertScene(scene)
    suspend fun insertScenes(scenes: List<VideoScene>) = db.videoSceneDao().insertScenes(scenes)
    suspend fun deleteScenesForProject(projectId: Int) = db.videoSceneDao().deleteScenesForProject(projectId)

    suspend fun insertCharacter(character: AICharacter) = db.aiCharacterDao().insertCharacter(character)
    suspend fun deleteCharacter(id: Int) = db.aiCharacterDao().deleteCharacterById(id)
    suspend fun getCharacterById(id: Int): AICharacter? = db.aiCharacterDao().getCharacterById(id)
    suspend fun getAllCharacters(): List<AICharacter> = db.aiCharacterDao().getAllCharacters()
}
