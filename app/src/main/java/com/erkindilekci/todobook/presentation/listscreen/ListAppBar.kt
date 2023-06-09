package com.erkindilekci.todobook.presentation.listscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.presentation.components.DisplayAlertDialog
import com.erkindilekci.todobook.presentation.components.PriorityItem
import com.erkindilekci.todobook.presentation.theme.AppBar
import com.erkindilekci.todobook.presentation.theme.Background
import com.erkindilekci.todobook.presentation.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.Action
import com.erkindilekci.todobook.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = { sharedViewModel.updateSearchAppState(SearchAppBarState.OPENED) },
                onFilterClicked = { sharedViewModel.persistFilterState(it) },
                onDeleteAllConfirmed = { sharedViewModel.updateAction(Action.DELETE_ALL) }
            )
        }

        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { sharedViewModel.updateSearchText(it) },
                onCloseClicked = {
                    sharedViewModel.updateSearchAppState(SearchAppBarState.CLOSED)
                    sharedViewModel.updateSearchText("")
                },
                onSearchClicked = { sharedViewModel.searchDatabase(it) }
            )
        }
    }
}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onFilterClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        backgroundColor = AppBar,
        contentColor = Color.White,
        title = { Text(text = stringResource(id = R.string.tasks)) },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onFilterClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all),
        message = stringResource(id = R.string.sure_all),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteAllConfirmed() }
    )

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onFilterClicked = onSortClicked)
    DeleteAllAction(onDeleteAllConfirmed = { openDialog = true })
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search),
            tint = Color.White
        )
    }
}

@Composable
fun SortAction(
    onFilterClicked: (Priority) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    IconButton(onClick = { isExpanded = !isExpanded }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(id = R.string.filter),
            tint = Color.White
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            Modifier.background(Background)
        ) {
            Priority.values().forEach {
                DropdownMenuItem(onClick = {
                    isExpanded = false
                    onFilterClicked(it)
                }) {
                    PriorityItem(priority = it)
                }
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    IconButton(onClick = { isExpanded = !isExpanded }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.delete_all),
            tint = Color.White
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(onClick = {
                onDeleteAllConfirmed()
                isExpanded = false
            }) {
                Text(
                    text = stringResource(id = R.string.delete_all),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = AppBar,
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = text,
                onValueChange = { onTextChange(it) },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        color = Color.White.copy(alpha = ContentAlpha.medium)
                    )
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                ),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = AppBar,
                    textColor = Color.White,
                    cursorColor = Color.White,
                    placeholderColor = Color.White.copy(alpha = ContentAlpha.medium),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    IconButton(onClick = { }, modifier = Modifier.alpha(ContentAlpha.disabled)) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search),
                            tint = Color.White
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        if (text.trim().isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.clear),
                            tint = Color.White
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearchClicked(text) }
                )
            )
        }
    }
}
