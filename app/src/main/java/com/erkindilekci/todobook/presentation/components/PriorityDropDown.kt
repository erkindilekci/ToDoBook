package com.erkindilekci.todobook.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.presentation.theme.Background

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    var parentSize by remember { mutableStateOf(IntSize.Zero) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { parentSize = it.size }
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
        Canvas(
            modifier = Modifier
                .size(16.dp)
                .padding(start = 3.dp)
                .weight(1f)
        ) {
            drawCircle(color = priority.color)
        }

        Text(
            text = priority.name,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(8f)
        )

        IconButton(
            onClick = { isExpanded = true }, modifier = Modifier
                .weight(1.5f)
                .alpha(ContentAlpha.medium)
                .rotate(degrees = angle)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.arrow_down)
            )
        }

        DropdownMenu(
            expanded = isExpanded, onDismissRequest = { isExpanded = false }, modifier = Modifier
                //.fillMaxWidth(fraction = 0.94f)
                .width(with(LocalDensity.current) { parentSize.width.toDp() })
                //.background(if (isSystemInDarkTheme()) BackgroundDark else Background)
                .background(Background)
        ) {
            Priority.values().slice(0..2).forEach {
                DropdownMenuItem(onClick = {
                    isExpanded = false
                    onPrioritySelected(it)
                }) {
                    PriorityItem(priority = it)
                }
            }
        }
    }
}
