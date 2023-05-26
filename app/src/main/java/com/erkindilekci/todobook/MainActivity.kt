package com.erkindilekci.todobook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.erkindilekci.todobook.navigation.SetupNavigation
import com.erkindilekci.todobook.presentation.theme.ToDoBookTheme
import com.erkindilekci.todobook.presentation.viewmodel.SharedViewModel
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
