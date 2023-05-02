package com.erkindilekci.todobook.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.erkindilekci.todobook.ui.screen.list.ListScreen
import com.erkindilekci.todobook.ui.screen.task.TaskScreen
import com.erkindilekci.todobook.ui.viewmodel.SharedViewModel
import com.erkindilekci.todobook.util.Constants.LIST_ARGUMENT_KEY
import com.erkindilekci.todobook.util.Constants.LIST_SCREEN
import com.erkindilekci.todobook.util.Constants.TASK_ARGUMENT_KEY
import com.erkindilekci.todobook.util.Constants.TASK_SCREEN
import com.erkindilekci.todobook.util.toAction


@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = Screens(navController)

    NavHost(navController = navController, startDestination = LIST_SCREEN) {
        composable(
            route = LIST_SCREEN,
            arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
                type = NavType.StringType
            })
        ){ navBackStackEntry ->
            val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()
            LaunchedEffect(key1 = action) {
                sharedViewModel.action.value = action
            }

            ListScreen(navigateToTaskScreen = screen.task, sharedViewModel = sharedViewModel)
        }

        composable(
            route = TASK_SCREEN,
            arguments = listOf(navArgument(TASK_ARGUMENT_KEY){
                type = NavType.IntType
            })
        ){ navBackStackEntry ->
            val taskId = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)
            sharedViewModel.getSelectedTask(taskId)
            val selectedTask by sharedViewModel.selectedTask.collectAsState()

            LaunchedEffect(key1 = selectedTask) {
                if (selectedTask != null || taskId == -1) {
                    sharedViewModel.updateTaskFields(selectedTask)
                }
            }

            TaskScreen(navigateToListScreen = screen.list, sharedViewModel = sharedViewModel, selectedTask = selectedTask)

        }
    }
}



/*LaunchedEffect(key1 = taskId){
               runBlocking {
                   sharedViewModel.updateTaskFields(selectedTask)
               }
}*/
