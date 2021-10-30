package com.flowbiz.stacky.data.model.remote

import com.flowbiz.stacky.data.model.local.Question
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class QuestionsResponse(
    @SerializedName("items")
    val items: ArrayList<Item>
) {
    data class Item(
        @SerializedName("tags")
        val tags: ArrayList<String>,
        @SerializedName("owner")
        val owner: Owner,
        @SerializedName("is_answered")
        val isAnswered: Boolean,
        @SerializedName("view_count")
        val viewCount: Int,
        @SerializedName("answer_count")
        val answerCount: Int,
        @SerializedName("score")
        val score: Int,
        @SerializedName("last_activity_date")
        val lastActivityDate: Long,
        @SerializedName("creation_date")
        val creationDate: Long,
        @SerializedName("last_edit_date")
        val lastEditDate: Long,
        @SerializedName("question_id")
        val questionId: Long,
        @SerializedName("link")
        val link: String,
        @SerializedName("title")
        val title: String,
    ) {
        data class Owner(
            @SerializedName("reputation")
            val reputation: Int,
            @SerializedName("user_id")
            val userId: Int,
            @SerializedName("user_type")
            val userType: String,
            @SerializedName("profile_image")
            val profileImage: String,
            @SerializedName("display_name")
            val displayName: String,
            @SerializedName("link")
            val link: String
        )

        fun toQuestion(): Question {
            return Question(
                questionId,
                tags,
                isAnswered,
                viewCount,
                answerCount,
                score,
                getFormattedDate(lastActivityDate),
                getFormattedDate(creationDate),
                getFormattedDate(lastEditDate),
                link,
                title,
                owner.reputation,
                owner.userId,
                owner.userType,
                owner.profileImage,
                owner.displayName,
                owner.link
            )
        }

        private fun getFormattedDate(longDate: Long): String {
            val outSdf = SimpleDateFormat("dd-MM-yyyy")
            val date = Date().apply {
                time = longDate
            }
            return outSdf.format(date)
        }
    }

    fun toQuestionList(): ArrayList<Question> {
        val list = ArrayList<Question>()
        items.forEach {
            list.add(it.toQuestion())
        }
        return list
    }
}
