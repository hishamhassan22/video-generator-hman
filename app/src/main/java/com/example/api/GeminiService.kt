package com.example.api

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiRetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    /**
     * Helper to call the API safely
     */
    suspend fun createVideoData(prompt: String): String? {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            throw IllegalStateException("API key is not configured. Please use the Secrets panel in AI Studio.")
        }

        // We require structured JSON output that conforms to our scene blueprint
        val systemInstructionText = """
            You are an advanced AI Video Producer. Your job is to transform a video idea or prompt into a professional, multi-scene video storyboard with custom narration and camera motions.
            You must respond in rigid JSON format. Do not wrap the JSON output in markdown blocks (like ```json).
            Your JSON must occupy this exact format:
            {
              "title": "A short, engaging title of the video",
              "narrativeScript": "An elegant script summarizing the full video voiceover narration",
              "scenes": [
                {
                  "sceneNumber": 1,
                  "scenePrompt": "A highly detailed image generation prompt (e.g. for SDXL or Flux) of what's happening. Include styling like realistic, cinematic lighting, 3D, cartoon, anime.",
                  "narrationText": "Voiceover narration script for this specific scene",
                  "motionType": "Zoom In",
                  "durationSeconds": 5
                }
              ]
            }
            Ensure you include exactly 3 to 4 chronological scenes to tell a full story.
            If the prompt is in Arabic, render all scripts, titles, and narrations in Arabic, but keep 'scenePrompt' (the image generator prompt) in English for optimal image generation engine capability!
            If the prompt is in English, write all of it in English.
            Ensure 'motionType' is one of: "Zoom In", "Pan Right", "Slow Mo Zoom", "Tilt Up", "Pan Left".
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = "Create a premium video storyboard for: $prompt")))
            ),
            generationConfig = GenerationConfig(
                responseMimeType = "application/json",
                temperature = 0.8f
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemInstructionText)))
        )

        val response = service.generateContent(apiKey, request)
        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
    }
}
