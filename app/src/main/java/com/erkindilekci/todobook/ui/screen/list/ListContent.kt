package com.erkindilekci.todobook.ui.screen.list

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.data.models.TodoTask
import com.erkindilekci.todobook.ui.theme.*
import com.erkindilekci.todobook.ui.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.Action
import com.erkindilekci.todobook.util.RequestState
import com.erkindilekci.todobook.util.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    allTasks: RequestState<List<TodoTask>>,
    searchedTasks: RequestState<List<TodoTask>>,
    lowPriorityTasks: List<TodoTask>,
    mediumPriorityTasks: List<TodoTask>,
    highPriorityTasks: List<TodoTask>,
    nonePriorityTasks: List<TodoTask>,
    filterState: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, TodoTask) -> Unit
) {
    if (filterState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
            filterState.data == Priority.NONE -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = nonePriorityTasks,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
            filterState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
            filterState.data == Priority.MEDIUM -> {
                HandleListContent(
                    tasks = mediumPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
            filterState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
        }
    }
}

@Composable
fun HandleListContent(
    tasks: List<TodoTask>,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        DisplayTasks(
            todoTasks = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayTasks(
    todoTasks: List<TodoTask>,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp, start = 4.dp, end = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = todoTasks, key = { it.id }) {
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)

            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(200)
                    onSwipeToDelete(Action.DELETE, it)
                }
            }

            val degrees by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0f else -45f)

            var itemAppeared by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = true) {
                itemAppeared = true
            }

            AnimatedVisibility(
                visible = itemAppeared && !isDismissed,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.2f) },
                    background = { RedBackground(degrees = degrees) },
                    dismissContent = {
                        TodoItem(
                            todoTask = it,
                            navigateToTaskScreen = navigateToTaskScreen
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun RedBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete),
            tint = Color.White
        )
    }
}

@Composable
fun TodoItem(
    todoTask: TodoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    viewModel: SharedViewModel = hiltViewModel()
) {
    Column(modifier = Modifier
        .clickable {
            navigateToTaskScreen(todoTask.id)
        }
        .background(Background)) {
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
                    color = if (todoTask.isDone) CheckedText else TextColor,
                    //color = TextColor,
                    textDecoration = if (todoTask.isDone) TextDecoration.LineThrough else TextDecoration.None,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 1
                )
                Text(
                    text = todoTask.description,
                    fontSize = 18.sp,
                    color = if (todoTask.isDone) CheckedText else TextColor,
                    //color = TextColor,
                    //modifier = Modifier.padding(bottom = 4.dp),
                    textDecoration = if (todoTask.isDone) TextDecoration.LineThrough else TextDecoration.None,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier.weight(0.19f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(todoTask.priority.color, CircleShape)
                )

                Checkbox(
                    checked = todoTask.isDone,
                    onCheckedChange = {
                        viewModel.updateIsDone(it, todoTask.id)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppBar,
                        checkmarkColor = Color.White
                    )
                )
                /*Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = todoTask.priority.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )*/
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}