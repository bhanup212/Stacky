package com.flowbiz.stacky.data.repository

import com.flowbiz.stacky.data.db.QuestionDao
import com.flowbiz.stacky.data.model.local.Question
import java.util.ArrayList
import javax.inject.Inject

class LocalRepository @Inject constructor(private val questionDao: QuestionDao) {

    fun insertListToDb(list: ArrayList<Question>) {
        questionDao.insertAll(list)
    }

    fun getAll(): List<Question> {
        return questionDao.getAll()
    }
}
