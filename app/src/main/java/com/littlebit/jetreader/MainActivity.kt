package com.littlebit.jetreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlebit.jetreader.navigation.JetReaderNavigation
import com.littlebit.jetreader.ui.theme.JetReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetReaderTheme {
                // A surface container using the 'background' color from the theme
                JetReaderApp()
            }
        }
    }
}

@Composable
fun JetReaderApp() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            JetReaderNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetReaderTheme {
        JetReaderApp()
    }
}