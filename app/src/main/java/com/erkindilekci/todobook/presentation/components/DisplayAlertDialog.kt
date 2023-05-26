package com.erkindilekci.todobook.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.presentation.theme.AppBar

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            backgroundColor = AppBar,
            title = {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            onDismissRequest = { closeDialog() },
            text = {
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onYesClicked()
                    closeDialog()
                }) {
                    Text(text = stringResource(id = R.string.yes), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { closeDialog() }) {
                    Text(text = stringResource(id = R.string.no), color = Color.White)
                }
            }
        )
    }
}
