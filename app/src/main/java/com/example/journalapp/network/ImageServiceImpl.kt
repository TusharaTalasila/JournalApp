package com.example.journalapp.network

import android.util.Log
import com.example.journalapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class ImageServiceImpl : ImageService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();
    private val apiKey = BuildConfig.dalle_api

    @Throws(IOException::class, JSONException::class)
    override suspend fun getImageResponse(question: String): String? {
        val url = "https://api.openai.com/v1/images/generations"
        val requestBody = """
    {
        "model": "dall-e-3",
        "prompt": "$question",
        "n": 1,
        "size": "1024x1024"
    }
    """.trimIndent()
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute() // Execute the request
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val jsonObject = JSONObject(responseBody)
                        val optionsArray = jsonObject.getJSONArray("data")
                        val firstObject = optionsArray.getJSONObject(0)
                        return@withContext firstObject.getString("url")
                    }
                } else {
                    Log.e(
                        "NetworkError",
                        "Unsuccessful network call: ${response.message}, HTTP code: ${response.code}"
                    )
                    return@withContext "Request failed"
                }
            } catch (e: Exception) {
                // Log the exception
                Log.e("NetworkError", "Exception occurred during network call", e)
                return@withContext "Exception occurred"
            }
        }

    }
}

