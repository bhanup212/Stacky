package com.flowbiz.stacky.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flowbiz.stacky.data.model.local.Question

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Question>)

    @Query("SELECT * FROM questions")
    fun getAll(): List<Question>

    @Insert
    fun insert(question: Question)

    @Query("DELETE FROM questions")
    fun deleteAll()
}
