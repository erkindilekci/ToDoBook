package com.erkindilekci.todobook.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.data.models.TodoTask
import com.erkindilekci.todobook.data.repository.DataStoreRepository
import com.erkindilekci.todobook.data.repository.ToDoRepository
import com.erkindilekci.todobook.util.Action
import com.erkindilekci.todobook.util.Constants.MAX_TITLE_LENGTH
import com.erkindilekci.todobook.util.RequestState
import com.erkindilekci.todobook.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var id by mutableStateOf(0)
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(Priority.LOW)
        private set

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    private val _allTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<TodoTask>>> = _allTasks

    private val _selectedTask: MutableStateFlow<TodoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<TodoTask?> = _selectedTask

    private val _searchedTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<TodoTask>>> = _searchedTasks

    private val _filterState =
        MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val filterState: StateFlow<RequestState<Priority>> = _filterState

    init {
        getAllTasks()
        readFilterState()
    }

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.Default) {
                repository.searchDatabase(searchQuery = "%$searchQuery%").collect {
                    _searchedTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.Default) {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect {
                _selectedTask.value = it
            }
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val newToDoTask = TodoTask(
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(newToDoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val newToDoTask = TodoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.updateTask(newToDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val newToDoTask = TodoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.deleteTask(newToDoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> { addTask() }
            Action.UPDATE -> { updateTask() }
            Action.DELETE -> { deleteTask() }
            Action.DELETE_ALL -> { deleteAllTasks() }
            Action.UNDO -> { addTask() }
            else -> {  }
        }
    }

    fun updateTaskFields(selectedTask: TodoTask?) {
        if (selectedTask != null) {
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.trim().isNotEmpty() && description.trim().isNotEmpty()
    }

    val lowPriorityTasks: StateFlow<List<TodoTask>> =
        repository.getLowPriorityTasks().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val mediumPriorityTasks: StateFlow<List<TodoTask>> =
        repository.getMediumPriorityTasks().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<TodoTask>> =
        repository.getHighPriorityTasks().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val nonePriorityTasks: StateFlow<List<TodoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun persistFilterState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistFilterState(priority)
        }
    }

    private fun readFilterState() {
        _filterState.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.Default) {
                dataStoreRepository.readFilterState
                    .map { Priority.valueOf(it) }
                    .collect {
                        _filterState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _filterState.value = RequestState.Error(e)
        }
    }

    fun updateAction(newAction: Action) {
        action = newAction
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    fun updateSearchAppState(newState: SearchAppBarState) {
        searchAppBarState = newState
    }

    fun updateSearchText(newText: String) {
        searchTextState = newText
    }
}   