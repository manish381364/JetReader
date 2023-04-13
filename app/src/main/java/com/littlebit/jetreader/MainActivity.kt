package com.littlebit.jetreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.littlebit.jetreader.navigation.JetReaderNavigation
import com.littlebit.jetreader.ui.theme.JetReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme = isSystemInDarkTheme()
            val isDarkTheme = rememberSaveable { mutableStateOf(theme) }
            JetReaderTheme(darkTheme = isDarkTheme.value) {
                JetReaderApp(isDarkTheme)
            }
        }
    }
}

@Composable
fun JetReaderApp(isDarkTheme: MutableState<Boolean>) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            JetReaderNavigation(isDarkTheme)
        }
    }
}

