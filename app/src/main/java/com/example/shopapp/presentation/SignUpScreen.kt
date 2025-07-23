package com.example.shopapp.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.presentation.Navigation.Routes
import com.example.shopapp.presentation.Navigation.SubNavigation
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.utils.CustomTextField
import com.example.shopapp.presentation.utils.SuccessAlertBox
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    val state = viewModel.signUpScreenState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    if (state.value.isLoading) {
        CircularIndicator()
    } else if (state.value.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = state.value.errorMessage!!)
        }
    } else if (state.value.userData != null) {
        SuccessAlertBox(
            onDismiss = {},
            onConfirm = { navController.navigate(SubNavigation.MainScreen) })
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SignUp ",
                fontSize = 16.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            CustomTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            )
            CustomTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            )
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions =
            )
            CustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions =
            )
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            )
            CustomTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                leadingIcon = Icons.Default.Phone,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            )

            Button(
                onClick = {
                    if (firstName.isNotEmpty() && lastName.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() && confirmPassword.isNotEmpty() && phoneNumber.isNotEmpty()) {

                        if (password == confirmPassword) {
                            val userData = UserData(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                password = password,
                                phoneNumber = phoneNumber
                            )
                            viewModel.createUser(userData)

                            Toast.makeText(context, "Sign up successfully", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "Password and confirm password not matched",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_300))
            ) {
                Text(
                    "Save",
                    style = TextStyle(
                        color = colorResource(R.color.white),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
//     Already have an account
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?")
                TextButton(onClick = {navController.navigate(Routes.LoginScreen)}) {
                    Text("Login", style = TextStyle(color = colorResource(R.color.purple_300)))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(text = "Or", modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = {}, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = "google icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "SignUp with Google",
//                color = colorResource(R.color.black)
                )
            }
        }
    }
}