package com.erkindilekci.todobook.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.ui.theme.Background
import com.erkindilekci.todobook.ui.theme.BackgroundDark

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.background(if (isSystemInDarkTheme()) BackgroundDark else Background)
            //.background(Background)
            .height(60.dp)
            .clickable { isExpanded = true }
            .border(
                width = 1.dp,
                MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                shape = RoundedCornerShape(10.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier
            .size(16.dp)
            .padding(start = 3.dp)
            .weight(1f)) {
            drawCircle(color = priority.color)
        }

        Text(text = priority.name, style = MaterialTheme.typography.subtitle2, modifier = Modifier.weight(8f))

        IconButton(onClick = { isExpanded = true }, modifier = Modifier
            .weight(1.5f)
            .alpha(ContentAlpha.medium)
            .rotate(degrees = angle)) {
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = stringResource(id = R.string.arrow_down))
        }
        
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }, modifier = Modifier
            .fillMaxWidth(fraction = 0.94f)
            //.background(if (isSystemInDarkTheme()) BackgroundDark else Background)
            .background(Background)
        ) {
            DropdownMenuItem(onClick = {
                isExpanded = false
                onPrioritySelected(Priority.LOW)
            }) {
                PriorityItem(priority = Priority.LOW)
            }

            //Divider(thickness = 0.3.dp, modifier = Modifier.fillMaxWidth())

            DropdownMenuItem(onClick = {
                isExpanded = false
                onPrioritySelected(Priority.MEDIUM)
            }) {
                PriorityItem(priority = Priority.MEDIUM)
            }

            //Divider(thickness = 0.3.dp, modifier = Modifier.fillMaxWidth())

            DropdownMenuItem(onClick = {
                isExpanded = false
                onPrioritySelected(Priority.HIGH)
            }) {
                PriorityItem(priority = Priority.HIGH)
            }
        }
    }
}

@Preview
@Composable
fun PreviewPDD() {
    PriorityDropDown(priority = Priority.MEDIUM, onPrioritySelected = {})
}