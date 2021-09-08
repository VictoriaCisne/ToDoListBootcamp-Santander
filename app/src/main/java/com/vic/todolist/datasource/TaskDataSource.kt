package com.vic.todolist.datasource

import android.content.Context
import com.vic.todolist.model.Task

class TaskDataSource(context: Context) {

    private val mDatabase = TaskDatabase.getDatabase(context).taskDAO()

    fun findById(taskId: Int) = mDatabase.get(taskId)

    fun insertTask(task: Task) {
        if (task.id == 0) {
            mDatabase.save(task)
        } else {
            mDatabase.update(task)
        }
    }

    fun getList() = mDatabase.getAll()

    fun deleteTask(task: Task) {
        mDatabase.delete(task)
    }
}