package com.example.shopapp.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shopapp.R
import com.example.shopapp.presentation.Navigation.SubNavigation
import com.example.shopapp.presentation.utils.CustomTextField
import com.example.shopapp.presentation.utils.SuccessAlertBox
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel

@Composable
fun SignInScreenUI(
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val state = viewModel.loginScreenState.collectAsStateWithLifecycle()
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (state.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (state.value.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = state.value.errorMessage!!)
        }
    } else if (state.value.userData != null) {
        SuccessAlertBox(
            onConfirm = { navController.navigate(SubNavigation.MainScreen) },
            onDismiss = {}
        )
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign In",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                leadingIcon = Icons.Default.Email
            )
            Spacer(modifier = Modifier.padding(8.dp))
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "Forgot password?",
                modifier = Modifier.align(Alignment.End),
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please fill all the field", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_300))
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have any account?", fontSize = 15.sp)
                TextButton(onClick = {}) {
                    Text("SignUp", color = colorResource(R.color.purple_300), fontSize = 15.sp)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text("Or", modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.padding(8.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = "Google icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Sign In with Google")

            }
        }
    }
}