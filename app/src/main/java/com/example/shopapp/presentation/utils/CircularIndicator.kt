package com.example.shopapp.presentation.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularIndicator(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Spacer(modifier = Modifier.padding(8.dp))
        Text("Sorry unable to get Information")
    }
}