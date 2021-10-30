package com.flowbiz.stacky.data.repository

import com.flowbiz.stacky.data.model.local.Question
import com.flowbiz.stacky.di.ApiService
import com.flowbiz.stacky.utils.ResponseWrapper
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val apiService: ApiService,
    private val localRepository: LocalRepository
) {

    suspend fun getQuestions(): ResponseWrapper<ArrayList<Question>> {
        try {
            val res = apiService.getQuestions()
            if (res.isSuccessful) {
                res.body()?.toQuestionList()?.let { list ->
                    localRepository.insertListToDb(list)
                }
            }
        } catch (e: Exception) {}
        val list = localRepository.getAll()
        return ResponseWrapper.Success(list as ArrayList<Question>)
    }
}
