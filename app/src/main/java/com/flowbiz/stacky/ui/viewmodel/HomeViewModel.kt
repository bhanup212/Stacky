package com.flowbiz.stacky.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowbiz.stacky.data.model.local.Question
import com.flowbiz.stacky.data.repository.RemoteRepository
import com.flowbiz.stacky.di.module.StorageModule.Companion.IO_DISPATCHER
import com.flowbiz.stacky.di.module.StorageModule.Companion.MAIN_DISPATCHER
import com.flowbiz.stacky.utils.ResponseWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class HomeViewModel @Inject constructor(
    @Named(IO_DISPATCHER) private val ioDispatcher: CoroutineDispatcher,
    @Named(MAIN_DISPATCHER) private val mainDispatcher: CoroutineDispatcher,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _questionList = MutableLiveData<ArrayList<Question>>()
    val questionList: LiveData<ArrayList<Question>> = _questionList

    private val _averageViewAnswerCount = MutableLiveData<Pair<Double, Double>?>()
    val averageViewAnswerCount: LiveData<Pair<Double, Double>?> = _averageViewAnswerCount

    private val _tagList = MutableLiveData<HashSet<String>>()
    val tagList: LiveData<HashSet<String>> = _tagList

    fun getQuestionList() {
        _isLoading.value = true
        viewModelScope.launch(ioDispatcher) {
            _averageViewAnswerCount.postValue(null)
            val res = remoteRepository.getQuestions()
            if (res is ResponseWrapper.Success) {
                calculateAverageCount(res.data)
                withContext(mainDispatcher) {
                    _questionList.value = res.data
                }
            }
            _isLoading.postValue(false)
        }
    }

    fun calculateAverageCount(data: ArrayList<Question>) {
        viewModelScope.launch(ioDispatcher) {
            var answerCount: Double = 0.0
            var viewCount: Double = 0.0

            data.forEach { question ->
                answerCount += question.answerCount
                viewCount += question.viewCount
            }
            if (answerCount < 1 || data.isEmpty()) {
                _averageViewAnswerCount.postValue(null)
            } else {
                _averageViewAnswerCount.postValue(
                    Pair(
                        viewCount / data.size,
                        answerCount / data.size
                    )
                )
            }
        }
    }

    fun onFilter() {
        _isLoading.value = (true)
        viewModelScope.launch(ioDispatcher) {
            val tags = HashSet<String>()
            _questionList.value?.let {
                it.forEach {
                    tags.addAll(it.tags.toHashSet())
                }
            }
            _tagList.postValue(tags)
            _isLoading.postValue(false)
        }
    }
}
