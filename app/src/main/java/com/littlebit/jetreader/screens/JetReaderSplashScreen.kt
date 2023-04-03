package com.littlebit.jetreader.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.littlebit.jetreader.navigation.JetScreens
import kotlinx.coroutines.delay
import com.littlebit.jetreader.R

@Composable
fun JetReaderSplashScreen(navController: NavController) {

    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(8f).getInterpolation(it)
                }
            )
        )
        delay(2000L)
        if(FirebaseAuth.getInstance().currentUser != null)
            navController.navigate(JetScreens.HomeScreen.name)
        else
            navController.navigate(JetScreens.LoginSignUpScreen.name)
    }



    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(
            width = 2.dp,
            color = Color.LightGray
        )
    ){
        Column (
            modifier = Modifier
                .padding(1.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            JetReaderLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Readers' favorite",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red.copy(0.5f)
            )
        }
    }
}

@Composable
fun JetReaderLogo(modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier.size(150.dp).clip(CircleShape),
        shape = CircleShape,
        color = Color.Black
    ){
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }

}
