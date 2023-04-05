package com.littlebit.jetreader.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.littlebit.jetreader.R

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