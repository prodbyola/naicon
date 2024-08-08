package org.dududaa.naicon

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedPlayIcon() {
    val lineColor = Color.White

    val innerX = 60F
    val innerY = 46F


    Spacer(
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    topLeft = Offset(innerX, innerY),
                    color = lineColor,
                    size = size.copy(
                        height = size.height - (innerY * 2),
                        width = size.width - (innerX * 2)
                    ),
                    style = Stroke(width = 10F),
                    cornerRadius = CornerRadius(12F)
                )
            }
            .height(154.dp)
            .width(204.dp)
            .border(
                width = 6.dp,
                color = lineColor,
                shape = RoundedCornerShape(12.dp)
            )
    )
}