package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.HomeScreen
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.OnboardingScreen
import com.example.myapplication.view.SolicitudScreen
import com.example.myapplication.view.SplashScreen
import com.example.myapplication.viewmodel.SolicitudViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val solicitudViewModel: SolicitudViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }


        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToForm = {
                    navController.navigate(Routes.SOLICITUD)
                }
            )
        }


        composable(Routes.SOLICITUD) {
            SolicitudScreen(viewModel = solicitudViewModel)
        }


        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME)
                }
            )
        }
        composable(Routes.HOME) { HomeScreen() }
    }
}