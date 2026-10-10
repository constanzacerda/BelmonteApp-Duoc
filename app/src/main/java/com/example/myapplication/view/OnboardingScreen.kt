package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 segundos de animación/carga
        onSplashFinished()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(id = R.drawable.logo_duoc), contentDescription = "Logo Duoc")
    }
}

@Composable
fun OnboardingScreen(onNavigateToForm: () -> Unit) {
    val fleetImages = listOf(R.drawable.flota1, R.drawable.flota2, R.drawable.flota3)
    val pagerState = rememberPagerState(pageCount = { fleetImages.size })

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Image(
                painter = painterResource(id = fleetImages[page]),
                contentDescription = "Vehículo ${page + 1}",
                modifier = Modifier.fillMaxSize()
            )
        }

        Button(
            onClick = onNavigateToForm,
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Text("Solicitar Traslado")
        }
    }
}