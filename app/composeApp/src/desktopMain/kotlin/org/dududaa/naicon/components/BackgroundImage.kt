package org.dududaa.naicon.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import naicon.composeapp.generated.resources.Res
import naicon.composeapp.generated.resources.bg
import org.jetbrains.compose.resources.painterResource

@Composable
fun BackgroundImage(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}