package com.erkindilekci.todobook.presentation.listscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.presentation.theme.AppBar
import com.erkindilekci.todobook.presentation.theme.Background
import com.erkindilekci.todobook.presentation.theme.FabColor
import com.erkindilekci.todobook.presentation.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.Action
import com.erkindilekci.todobook.util.SearchAppBarState
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
    action: Action
) {
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action)
    }

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val filterState by sharedViewModel.filterState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val mediumPriorityTasks by sharedViewModel.mediumPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()
    val nonePriorityTasks by sharedViewModel.nonePriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = { sharedViewModel.updateAction(it) },
        onUndoClicked = { sharedViewModel.updateAction(it) },
        taskTitle = sharedViewModel.title,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) {
                Snackbar(
                    backgroundColor = AppBar,
                    contentColor = Color.White,
                    actionColor = Color.White,
                    snackbarData = it
                )
            }
        },
        topBar = {
            ListAppBar(
                sharedViewModel, searchAppBarState, searchTextState
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(color = Background)
            ) {
                ListContent(
                    allTasks = allTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    searchedTasks = searchedTasks,
                    searchAppBarState = searchAppBarState,
                    lowPriorityTasks = lowPriorityTasks,
                    mediumPriorityTasks = mediumPriorityTasks,
                    highPriorityTasks = highPriorityTasks,
                    nonePriorityTasks = nonePriorityTasks,
                    filterState = filterState,
                    onSwipeToDelete = { action, todoTask ->
                        sharedViewModel.updateAction(action)
                        sharedViewModel.updateTaskFields(todoTask)
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                )
            }
        },
        floatingActionButton = {
            MyFabButton(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun MyFabButton(
    onFabClicked: (taskId: Int) -> Unit,
) {
    ExtendedFloatingActionButton(
        contentColor = Color.White, backgroundColor = FabColor,
        text = { Text(text = stringResource(id = R.string.add), color = Color.White) },
        onClick = { onFabClicked(-1) },
        icon = { Icon(Icons.Filled.Add, stringResource(id = R.string.add)) },
    )
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    val scope = rememberCoroutineScope()
    val string = stringResource(id = R.string.all_task)
    val undoText = stringResource(id = R.string.undo)
    val okText = stringResource(id = R.string.ok)

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action, taskTitle, string),
                    actionLabel = setActionLabel(action, undoText, okText)
                )
                undoDeleteTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setMessage(
    action: Action,
    taskTitle: String,
    string: String
): String {
    return when (action) {
        Action.DELETE_ALL -> string
        else -> "${
            action.name.substring(0, 1).toUpperCase(Locale.getDefault()) + action.name.substring(1)
                .toLowerCase(Locale.getDefault())
        }: $taskTitle"
    }
}

private fun setActionLabel(action: Action, undoText: String, okText: String): String {
    return if (action.name == "DELETE") undoText else okText
}

private fun undoDeleteTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit,
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) onUndoClicked(
        Action.UNDO
    )
}
