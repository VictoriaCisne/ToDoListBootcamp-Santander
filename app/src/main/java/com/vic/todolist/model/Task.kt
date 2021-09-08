package com.vic.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Task {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var title: String = ""
    var description: String = ""
    var date: String = ""
    var time: String = ""
}