package com.erkindilekci.todobook.navigation

//import androidx.navigation.compose.composable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.erkindilekci.todobook.ui.screen.list.ListScreen
import com.erkindilekci.todobook.ui.screen.splash.SplashScreen
import com.erkindilekci.todobook.ui.screen.task.TaskScreen
import com.erkindilekci.todobook.ui.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.Action
import com.erkindilekci.todobook.util.Constants.LIST_ARGUMENT_KEY
import com.erkindilekci.todobook.util.Constants.LIST_SCREEN
import com.erkindilekci.todobook.util.Constants.SPLASH_SCREEN
import com.erkindilekci.todobook.util.Constants.TASK_ARGUMENT_KEY
import com.erkindilekci.todobook.util.Constants.TASK_SCREEN
import com.erkindilekci.todobook.util.toAction
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = Screens(navController)

    AnimatedNavHost(navController = navController, startDestination = SPLASH_SCREEN) {
        composable(
            route = SPLASH_SCREEN,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(2000)
                )
            }
        ){
            SplashScreen(navigateToListScreen = screen.splash)
        }

        composable(
            route = LIST_SCREEN,
            arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
                type = NavType.StringType
            }),
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(600)
                )
            }
        ){ navBackStackEntry ->
            val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

            var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

            LaunchedEffect(key1 = myAction) {
                if (action != myAction) {
                    myAction = action
                    sharedViewModel.action.value = action
                }
            }

            val databaseAction by sharedViewModel.action

            ListScreen(navigateToTaskScreen = screen.list, sharedViewModel = sharedViewModel, action = databaseAction)
        }

        composable(
            route = TASK_SCREEN,
            arguments = listOf(navArgument(TASK_ARGUMENT_KEY){
                type = NavType.IntType
            }),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(600)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(600)
                )
            }
        ){ navBackStackEntry ->
            val taskId = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)
            
            LaunchedEffect(key1 = taskId){
                sharedViewModel.getSelectedTask(taskId)
            }

            val selectedTask by sharedViewModel.selectedTask.collectAsState()

            LaunchedEffect(key1 = selectedTask) {
                if (selectedTask != null || taskId == -1) {
                    sharedViewModel.updateTaskFields(selectedTask)
                }
            }

            TaskScreen(navigateToListScreen = screen.task, sharedViewModel = sharedViewModel, selectedTask = selectedTask)

        }
    }
}