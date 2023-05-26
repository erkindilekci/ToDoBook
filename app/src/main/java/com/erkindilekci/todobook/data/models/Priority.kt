package com.erkindilekci.todobook.data.models

import androidx.compose.ui.graphics.Color
import com.erkindilekci.todobook.presentation.theme.HighPriorityColor
import com.erkindilekci.todobook.presentation.theme.LowPriorityColor
import com.erkindilekci.todobook.presentation.theme.MediumPriorityColor
import com.erkindilekci.todobook.presentation.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}
