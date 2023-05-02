package com.erkindilekci.todobook.ui.screen.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.components.PriorityDropDown
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.ui.theme.Background
import com.erkindilekci.todobook.ui.theme.TextColor

@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {

    Column(modifier = Modifier
        .fillMaxSize()
        //.background(if (isSystemInDarkTheme()) BackgroundDark else Background)
        .background(Background)
        .padding(all = 12.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(id = R.string.title), color = Color.DarkGray) },
            textStyle = MaterialTheme.typography.body1,
            placeholder = { Text(text = stringResource(id = R.string.max_character), color = Color.DarkGray) },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                textColor = TextColor,
                unfocusedLabelColor = Color.Black,
                disabledLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black,
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        
        PriorityDropDown(priority = priority, onPrioritySelected = onPrioritySelected)


        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(id = R.string.description), color = Color.DarkGray) },
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                textColor = TextColor,
                unfocusedLabelColor = Color.Black,
                disabledLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}