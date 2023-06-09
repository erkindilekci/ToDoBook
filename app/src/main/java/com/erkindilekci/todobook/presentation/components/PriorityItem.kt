package com.erkindilekci.todobook.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.erkindilekci.todobook.data.models.Priority

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
