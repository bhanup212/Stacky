package com.flowbiz.stacky.di

import com.flowbiz.stacky.BuildConfig
import com.flowbiz.stacky.data.model.remote.QuestionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/2.2/questions/")
    suspend fun getQuestions(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "activity",
        @Query("site") site: String = "stackoverflow",
    ): Response<QuestionsResponse>
}