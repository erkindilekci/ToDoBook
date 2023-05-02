package com.erkindilekci.todobook.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erkindilekci.todobook.data.models.Priority
import com.erkindilekci.todobook.ui.theme.TextColor
import com.erkindilekci.todobook.ui.theme.TextColorDark

@Composable
fun PriorityItem(
    priority: Priority
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = priority.color)
        }

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = priority.name, style = typography.subtitle2, fontWeight = FontWeight.SemiBold)
    }
}

@Preview
@Composable
fun PriorityItemPrev() {
    PriorityItem(priority = Priority.HIGH)
}