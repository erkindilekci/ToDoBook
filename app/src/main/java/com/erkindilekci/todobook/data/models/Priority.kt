package com.erkindilekci.todobook.data.models

import androidx.compose.ui.graphics.Color
import com.erkindilekci.todobook.ui.theme.HighPriorityColor
import com.erkindilekci.todobook.ui.theme.LowPriorityColor
import com.erkindilekci.todobook.ui.theme.MediumPriorityColor
import com.erkindilekci.todobook.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}