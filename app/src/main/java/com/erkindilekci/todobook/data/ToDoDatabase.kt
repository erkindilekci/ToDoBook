package com.erkindilekci.todobook.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erkindilekci.todobook.data.models.TodoTask

@Database(entities = [TodoTask::class], version = 3, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}
