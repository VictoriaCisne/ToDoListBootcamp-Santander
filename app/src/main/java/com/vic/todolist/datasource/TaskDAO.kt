package com.vic.todolist.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vic.todolist.model.Task

@Dao
interface TaskDAO {

    @Insert
    fun save(task: Task) : Long

    @Update
    fun update(task: Task) : Int

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM Task")
    fun getAll() : List<Task>

    @Query("SELECT * FROM Task WHERE id = :id")
    fun get(id: Int) : Task
}