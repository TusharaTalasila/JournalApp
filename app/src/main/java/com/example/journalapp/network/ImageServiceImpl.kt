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

   /* @Throws(IOException::class, JSONException::class)
     override suspend fun getImageResponse(question: String): String {
         val url = "https://modelslab.com/api/v6/images/text2img"
         val requestBody = """
     {
         "key": "$apiKey",
         "model_id": "sdxl-unstable-diffusers-y",
         "prompt": "$question",
         "negative_prompt": "",
         "width": "512",
         "height": "512",
         "samples": "1",
         "num_inference_steps": "31",
         "safety_checker": "no",
         "enhance_prompt": "yes",
         "seed": null,
         "guidance_scale": 7.5,
         "panorama": "no",
         "self_attention": "no",
         "upscale": "no",
         "embeddings_model": null,
         "lora_model": null,
         "tomesd": "yes",
         "clip_skip": "2",
         "use_karras_sigmas": "yes",
         "vae": null,
         "lora_strength": null,
         "scheduler": "UniPCMultistepScheduler",
         "webhook": null,
         "track_id": null
     }
     """.trimIndent()
         val request = Request.Builder()
             .url(url)
             .addHeader("Content-Type", "application/json")
             .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
             .build()
         val client = OkHttpClient()

         // Perform the network operation on an IO-optimized dispatcher
         return withContext(Dispatchers.IO) {
             val response = client.newCall(request).execute() // Execute the request
             if (response.isSuccessful) {
                 response.body?.string()?.let { responseBody ->
                     val jsonObject = JSONObject(responseBody)
                     val status = jsonObject.getString("status")
                     when (status) {
                         "processing" -> {
                             val fetchUrl = jsonObject.getString("fetch_result")
                             val eta = jsonObject.getDouble("eta").times(1000).toLong()
                             val id = jsonObject.getString("id")
                             kotlinx.coroutines.delay(eta) // Wait the estimated time before trying to fetch the image
                             fetchGeneratedImage(fetchUrl, id) // Fetch the generated image
                         }

                         "success" -> {
                             val outputArray = jsonObject.getJSONArray("output")
                             outputArray.getString(0) // Return the first URL in the output array
                         }

                         else -> throw Exception("Unhandled status: $status")
                     }
                 } ?: throw Exception("No response body")
             } else {
                 throw Exception("Unsuccessful network call: ${response.message}")
             }
         }
     }

    // Method to get the generated image
    suspend fun fetchGeneratedImage(fetchUrl: String, id: String): String =
        withContext(Dispatchers.IO) {
            val requestBody = """
        {"key": "$apiKey",
        "request_id": "$id"}"""
            var wait = 2000
            var isSuccess = false
            var imageUrl: String = "error"
            while (!isSuccess) {
                // Make the request
                val request = Request.Builder()
                    .url(fetchUrl)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody)
                    val status = jsonObject.getString("status")

                    when (status) {
                        "success" -> {
                            val outputArray = jsonObject.getJSONArray("output")
                            imageUrl = outputArray.getString(0)
                            isSuccess = true // Exit the loop
                        }

                        "processing" -> {
                            // If the status is "processing", wait for some time before retrying
                            kotlinx.coroutines.delay(wait.toLong())
                            wait *= 2//double waiting time for the next time
                        }

                        else -> {
                            // If the status is neither "success" nor "processing", throw an exception or handle accordingly
                            throw Exception("Unhandled status: $status")
                        }
                    }
                } else {
                    throw Exception("Unsuccessful network call: ${response.message}")
                }
            }

            return@withContext imageUrl
        }
}*/

