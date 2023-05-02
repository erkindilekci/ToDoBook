package com.erkindilekci.todobook.ui.screen.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
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
import com.erkindilekci.todobook.components.PriorityItem
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.ui.theme.AppBar
import com.erkindilekci.todobook.ui.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.SearchAppBarState
import com.erkindilekci.todobook.util.TrailingIconState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = { sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED },
                onFilterClicked = {},
                onDeleteClicked = {}
            )
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { sharedViewModel.searchTextState.value = it },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
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
    onDeleteClicked: () -> Unit
) {
    TopAppBar(
        backgroundColor = AppBar,
        contentColor = Color.White,
        title = { Text(text = stringResource(id = R.string.tasks)) },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onFilterClicked,
                onDeleteClicked = onDeleteClicked
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit
) {
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onFilterClicked = onSortClicked)
    DeleteAllAction(onDeleteClicked = onDeleteClicked)
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
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(onClick = {
                isExpanded = false
                onFilterClicked(Priority.HIGH)
            }) {
                PriorityItem(priority = Priority.HIGH)
            }

            DropdownMenuItem(onClick = {
                isExpanded = false
                onFilterClicked(Priority.MEDIUM)
            }) {
                PriorityItem(priority = Priority.MEDIUM)
            }

            DropdownMenuItem(onClick = {
                isExpanded = false
                onFilterClicked(Priority.LOW)
            }) {
                PriorityItem(priority = Priority.LOW)
            }

            DropdownMenuItem(onClick = {
                isExpanded = false
                onFilterClicked(Priority.NONE)
            }) {
                PriorityItem(priority = Priority.NONE)
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteClicked: () -> Unit
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
                onDeleteClicked()
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
    var trailingIconState by remember { mutableStateOf(TrailingIconState.READY_TO_DELETE) }

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
                        when (trailingIconState) {
                            TrailingIconState.READY_TO_DELETE -> {
                                if (text.trim().isNotEmpty()) {
                                    onTextChange("")
                                    trailingIconState = TrailingIconState.READY_TO_CLOSE
                                } else {
                                    onCloseClicked()
                                }
                            }
                            TrailingIconState.READY_TO_CLOSE -> {
                                if (text.trim().isNotEmpty()) {
                                    onTextChange("")
                                } else {
                                    onCloseClicked()
                                    trailingIconState = TrailingIconState.READY_TO_DELETE
                                }
                            }
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