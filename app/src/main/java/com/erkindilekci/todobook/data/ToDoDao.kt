package com.erkindilekci.todobook.data

import androidx.room.*
import com.erkindilekci.todobook.data.models.TodoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTasks(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<TodoTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(todoTask: TodoTask)

    @Update
    suspend fun updateTask(todoTask: TodoTask)

    @Delete
    suspend fun deleteTask(todoTask: TodoTask)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table WHERE priority LIKE 'L%'")
    fun getLowPriorityTasks(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table WHERE priority LIKE 'M%'")
    fun getMediumPriorityTasks(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table WHERE priority LIKE 'H%'")
    fun getHighPriorityTasks(): Flow<List<TodoTask>>

    @Query("UPDATE todo_table SET isDone =:newBoolean WHERE id=:todoId")
    suspend fun updateTaskIsDone(newBoolean: Boolean, todoId: Int)
}