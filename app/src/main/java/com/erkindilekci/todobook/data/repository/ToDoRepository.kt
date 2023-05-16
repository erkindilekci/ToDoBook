package com.erkindilekci.todobook.data.repository

import com.erkindilekci.todobook.data.ToDoDao
import com.erkindilekci.todobook.data.models.TodoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(
    private val toDoDao: ToDoDao
){
    val getAllTasks: Flow<List<TodoTask>> = toDoDao.getAllTasks()
    val sortByLowPriority: Flow<List<TodoTask>> = toDoDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<TodoTask>> = toDoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<TodoTask> {
        return toDoDao.getSelectedTask(taskId)
    }

    suspend fun addTask(todoTask: TodoTask) {
        toDoDao.addTask(todoTask)
    }
    
    suspend fun updateTask(todoTask: TodoTask) {
        toDoDao.updateTask(todoTask)
    }

    suspend fun deleteTask(todoTask: TodoTask) {
        toDoDao.deleteTask(todoTask)
    }

    suspend fun deleteAllTasks() {
        toDoDao.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String): Flow<List<TodoTask>> {
        return toDoDao.searchDatabase(searchQuery)
    }

    fun getLowPriorityTasks(): Flow<List<TodoTask>> {
        return toDoDao.getLowPriorityTasks()
    }

    fun getMediumPriorityTasks(): Flow<List<TodoTask>> {
        return toDoDao.getMediumPriorityTasks()
    }

    fun getHighPriorityTasks(): Flow<List<TodoTask>> {
        return toDoDao.getHighPriorityTasks()
    }

    suspend fun updateIsDone(newBoolean: Boolean, todoId: Int) {
        return toDoDao.updateTaskIsDone(newBoolean, todoId)
    }
}