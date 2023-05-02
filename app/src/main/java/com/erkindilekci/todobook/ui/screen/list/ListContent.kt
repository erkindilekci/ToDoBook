package com.erkindilekci.todobook.ui.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erkindilekci.todobook.data.models.TodoTask
import com.erkindilekci.todobook.ui.theme.*
import com.erkindilekci.todobook.ui.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.RequestState
import com.erkindilekci.todobook.util.SearchAppBarState

@Composable
fun ListContent(
    allTasks: RequestState<List<TodoTask>>,
    searchedTasks: RequestState<List<TodoTask>>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (searchAppBarState == SearchAppBarState.TRIGGERED) {
        if (searchedTasks is RequestState.Success) {
            HandleListContent(tasks = searchedTasks.data, navigateToTaskScreen = navigateToTaskScreen)
        }
    } else {
        if (allTasks is RequestState.Success) {
            HandleListContent(tasks = allTasks.data, navigateToTaskScreen = navigateToTaskScreen)
        }
    }
}

@Composable
fun HandleListContent(
    tasks: List<TodoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        DisplayTasks(
            todoTasks = tasks,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@Composable
fun DisplayTasks(
    todoTasks: List<TodoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp, start = 4.dp, end = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = todoTasks, key = { it.id }){
            TodoItem(todoTask = it, navigateToTaskScreen = navigateToTaskScreen)
        }
    }
}

@Composable
fun TodoItem(
    todoTask: TodoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    var isChecked by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.clickable {
        navigateToTaskScreen(todoTask.id)
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)

        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = todoTask.title,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isChecked) CheckedText else TextColor,
                    //color = TextColor,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 1
                )
                Text(
                    text = todoTask.description,
                    fontSize = 18.sp,
                    color = if (isChecked) CheckedText else TextColor,
                    //color = TextColor,
                    //modifier = Modifier.padding(bottom = 4.dp),
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier.weight(0.1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(3.5.dp))

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(todoTask.priority.color, CircleShape)
                )
                
                //Spacer(modifier = Modifier.height(2.dp))
                
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = !isChecked },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppBar,
                        checkmarkColor = Color.White
                    )
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}