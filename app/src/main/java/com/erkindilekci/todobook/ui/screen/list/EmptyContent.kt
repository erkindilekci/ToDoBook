package com.erkindilekci.todobook.ui.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erkindilekci.todobook.R
import com.erkindilekci.todobook.ui.theme.Background
import com.erkindilekci.todobook.ui.theme.TextColor

@Composable
fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.sad),
            contentDescription = stringResource(id = R.string.empty_screen_icon),
            modifier = Modifier.size(200.dp),
            tint = TextColor,
        )

        Text(
            text = stringResource(id = R.string.add_task),
            color = TextColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.h5.fontSize
        )
    }
}