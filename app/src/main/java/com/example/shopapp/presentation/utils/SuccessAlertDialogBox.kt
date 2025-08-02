package com.example.shopapp.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlertBox(
    onDismiss: () -> Unit,
    success: Boolean = true,
    text: String = "Congratulation, you have \n completed your registration",
    customContent: @Composable () -> Unit? = {null}
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.background(shape = RoundedCornerShape(16.dp), color = Color.White),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(color = colorResource(R.color.purple_300), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if(success) Icons.Default.Check else Icons.Default.Close,
                        contentDescription =null,
                        tint = if(success) Color.Green else Color.Red,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if(success) "Success" else "Error",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.purple_500)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = colorResource(R.color.purple_300)
                )

                Spacer(modifier = Modifier.height(24.dp))
                customContent()

            }
        }
    )
}