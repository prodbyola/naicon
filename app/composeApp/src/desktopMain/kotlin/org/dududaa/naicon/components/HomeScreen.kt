package org.dududaa.naicon.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.dududaa.naicon.poppinsFontFamily

@Composable
fun HomeScreen() {
    val colorScheme = MaterialTheme.colorScheme
    var filePickerOpened by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        AnimatedPlayIcon()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .offset(0.dp, 150.dp)
        ) {
            val brush = Brush.linearGradient(
                listOf(
                    colorScheme.tertiary,
                    colorScheme.secondary,
                    colorScheme.surfaceVariant,
                )
            )

            Text(
                "Native Converter".uppercase(),
                style = TextStyle(
                    brush = brush
                ),
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontFamily = poppinsFontFamily
            )
            Button(
                onClick = { filePickerOpened = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = colorScheme.background
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.width(240.dp).height(48.dp)
            ) {
                Text("Get Started", fontFamily = poppinsFontFamily)
            }

            if (filePickerOpened) {
                FilePicker(
                    onCloseRequest = {
                        filePickerOpened = false
                    },
                    allowedExtensions = listOf(".mkv"),
                    allowMultiple = false
                )
            }
        }
    }
}