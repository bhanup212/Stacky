package com.flowbiz.stacky.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flowbiz.stacky.data.model.remote.QuestionsResponse

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey
    val questionId: Long,
    val tags: ArrayList<String>,
    val isAnswered: Boolean,
    val viewCount: Int,
    val answerCount: Int,
    val score: Int,
    val lastActivityDate: String,
    val creationDate: String,
    val lastEditDate: String,
    val link: String,
    val title: String,
    val reputation: Int,
    val userId: Int,
    val userType: String,
    val profileImage: String,
    val displayName: String,
    val profileLink: String
)
