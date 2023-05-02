package com.erkindilekci.todobook.ui.screen.task

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.data.models.TodoTask
import com.erkindilekci.todobook.ui.theme.AppBar
import com.erkindilekci.todobook.util.Action

@Composable
fun TaskAppBar(
    navigateToListScreen: (Action) -> Unit,
    selectedTask: TodoTask?
) {
    if (selectedTask  == null){
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
    }
}

@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = { BackAction(onBackClicked = navigateToListScreen) },
        backgroundColor = AppBar,
        contentColor = Color.White,
        title = { Text(text = stringResource(id = R.string.app_bar_task)) },
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back), tint = Color.White)
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(imageVector = Icons.Filled.Check, contentDescription = stringResource(id = R.string.back), tint = Color.White
        )
    }
}

@Composable
fun ExistingTaskAppBar(
    selectedTask: TodoTask,
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = { CloseAction(onCloseClicked = navigateToListScreen) },
        backgroundColor = AppBar,
        contentColor = Color.White,
        title = { Text(text = selectedTask.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        actions = {
            DeleteAction(onDeleteClicked = navigateToListScreen)
            UpdateAction(onUpdateClicked = navigateToListScreen)
        }
    )
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = stringResource(id = R.string.close), tint = Color.White)
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: (Action) -> Unit
) {
    IconButton(onClick = { onDeleteClicked(Action.DELETE) }) {
        Icon(imageVector = Icons.Filled.Delete, contentDescription = stringResource(id = R.string.delete), tint = Color.White)
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(imageVector = Icons.Filled.Check, contentDescription = stringResource(id = R.string.update), tint = Color.White)
    }
}

@Preview
@Composable
fun NewTaskAppBarPreview() {
    NewTaskAppBar(navigateToListScreen = {})
}
@Preview
@Composable
fun ExistingTaskAppBarPreview() {
    ExistingTaskAppBar(navigateToListScreen = {}, selectedTask = TodoTask(5, "Hepinize Merhaba Arkadaslar", "Degerli kardeslerim, degerli dostlarim bugun burada", Priority.MEDIUM))
}