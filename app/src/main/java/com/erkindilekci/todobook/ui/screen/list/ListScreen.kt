package com.erkindilekci.todobook.ui.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.ui.theme.*
import com.erkindilekci.todobook.ui.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.Action
import com.erkindilekci.todobook.util.SearchAppBarState
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
    }

    val action by sharedViewModel.action

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action) },
        onUndoClicked = { sharedViewModel.action.value = it },
        taskTitle = sharedViewModel.title.value,
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
                    .background(
                        /*brush = Brush.verticalGradient(
                            colors = listOf(AppBar, Color.White),
                            startY = -600f,
                            endY = 2500f
                        )*/
                        color = Background
                    )
            ) {
                ListContent(
                    allTasks = allTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    searchedTasks = searchedTasks,
                    searchAppBarState = searchAppBarState
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
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    handleDatabaseActions()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${action.name.substring(0, 1).toUpperCase(Locale.getDefault()) + action.name.substring(1).toLowerCase(Locale.getDefault())}: $taskTitle",
                    actionLabel = setActionLabel(action = action)
                )
                undoDeleteTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") "UNDO" else "OK"
}

private fun undoDeleteTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit,
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) onUndoClicked(Action.UNDO)
}