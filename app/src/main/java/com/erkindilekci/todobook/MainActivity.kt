package com.erkindilekci.todobook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.erkindilekci.todobook.navigation.SetupNavigation
import com.erkindilekci.todobook.ui.theme.ToDoBookTheme
import com.erkindilekci.todobook.ui.viewmodel.SharedViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        MobileAds.initialize(this) {}
        setContent {
            ToDoBookTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    //val navController = rememberNavController()
                    val navController = rememberAnimatedNavController()
                    val sharedViewModel: SharedViewModel = hiltViewModel()

                    SetupNavigation(
                        navController = navController,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }
}

/*
@Composable
fun MyTextField() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(
                    "Username",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.secondaryVariant
                )
            },
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.secondary,
                focusedBorderColor = MaterialTheme.colors.secondary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.42f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun MyTextField2() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = {
            Text(
                "Username",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.87f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Username icon",
                tint = MaterialTheme.colors.secondary
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info icon",
                tint = MaterialTheme.colors.secondary
            )
        },
        textStyle = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            cursorColor = MaterialTheme.colors.secondary,
            leadingIconColor = MaterialTheme.colors.secondary,
            trailingIconColor = MaterialTheme.colors.secondary,
            focusedBorderColor = MaterialTheme.colors.secondary,
            unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.42f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    )

}

@Preview
@Composable
fun d() {
    MyTextField2()
}*/
