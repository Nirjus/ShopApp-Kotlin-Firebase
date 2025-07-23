package com.example.shopapp.presentation.Screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopapp.R
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    productId: String,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val state = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val productData = state.value.userData
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalAddress by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Shipping") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back icon")
                }
            })
        }
    ) { innerPadding ->
        when {
            state.value.isLoading -> {
                CircularIndicator()
            }

            state.value.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("Sorry unable to get Information")
                }
            }

            state.value.userData == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No products available")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = state.value.userData?.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .border(
                                    1.dp, color = colorResource(
                                        R.color.purple_300
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = state.value.userData!!.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "$${state.value.userData!!.finalPrice}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "Contact Information",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "Shipping Address",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = country,
                            onValueChange = { country = it },
                            label = { Text("Country/Region") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = { Text("First Name") },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f),
                            )
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = { Text("Last Name") },
                                modifier = Modifier.weight(1f),
                            )
                        }
//                        Spacer(modifier = Modifier.height(8.dp))
//                        OutlinedTextField(
//                            value = postalAddress,
//                            onValueChange = { postalAddress = it },
//                            label = { Text("Postal Address") },
//                            modifier = Modifier.fillMaxWidth(),
//                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("City") },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f),
                            )
                            OutlinedTextField(
                                value = postalAddress,
                                onValueChange = { postalAddress = it },
                                label = { Text("Postal Address") },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedMethod == "Standard Free delivery over Rs.4500", onClick = {
                                selectedMethod = "Standard Free delivery over Rs.4500"
                            })
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Standard Free delivery over Rs.4500")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selectedMethod == "Cash On Delivery", onClick = {
                                selectedMethod = "Cash On Delivery"
                            })
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Cash on Delivery")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick={
                        //pay.invoke()
                    }, modifier = Modifier.fillMaxWidth(), colors = ButtonColors(
                        containerColor = colorResource(R.color.purple_300),
                        contentColor = colorResource(R.color.white),
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )) {
                        Text("PLace order")
                    }
                    }
                }
            }
        }
    }
