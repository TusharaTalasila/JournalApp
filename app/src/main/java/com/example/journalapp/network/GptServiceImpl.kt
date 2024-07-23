package com.example.journalapp.network

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.journalapp.BuildConfig
import com.example.journalapp.model.GptBody
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class GptServiceImpl() : GptService {
    private val client = OkHttpClient()


    @Throws(IOException::class, JSONException::class)
    override suspend fun getGptResponse(question: String): String {
        val apiKey = BuildConfig.gpt_api
        val url = "https://api.openai.com/v1/chat/completions"
        //initialize the json parameters
        val requestBody = """{   
        "model": "gpt-3.5-turbo",
        "messages": [
          {
            "role": "system",
            "content": "You are a helpful assistant."
          },
          {
            "role": "user",
            "content": "$question"
          },
          {
            "role": "user",
            "content": "Task 2: Use your answer from task 1 to create a cohesive and concise narrative summary that encapsulates the listed key elements, emotions, and themes, and reflects the selected feelings, weather, and artistic style. The summary should be rich with imagery and suitable for being translated into a visual representation that resembles the style of (Artist).Task 3: Use your answer from task 2 to craft a succinct, visually rich prompt of no more than 60 words that distills the essence of the narrative summary. Incorporate key visual elements and the distinctive artistic style selected. This prompt is intended for the DALL-E API to produce an image that embodies the user's day through the lens of the chosen art form. Clarity and focus are paramount to ensure the resulting image is cohesive and representative. Only return the answer to task 3 and do so in this format: Prompt: Answer"
    
            }
            
        ]
      }
        """.trimMargin()//stores the request

        val request = Request.Builder() //issue w/ request body
            .url(url)//pass in open ai url
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", " Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute() //executes the api request
            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    val jsonObject = JSONObject(responseBody)
                    val choicesArray = jsonObject.getJSONArray("choices")
                    val firstChoiceObject = choicesArray.getJSONObject(0)
                    val messageObject = firstChoiceObject.getJSONObject("message")
                    messageObject.getString("content")
                        .trim() // Trim used to remove leading and trailing newlines
                } ?: throw Exception("No response body")
            } else {
                throw Exception("Unsuccessful network call: ${response.message}")
            }
        }
    }
}

