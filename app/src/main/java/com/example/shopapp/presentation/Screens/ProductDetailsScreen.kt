package com.example.shopapp.presentation.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopapp.R
import com.example.shopapp.domain.models.CartDataModels
import com.example.shopapp.presentation.Navigation.Routes
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    productId: String,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val getProductById = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val scrollVehavour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    var selectedSized by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(0) }
    var isFavourite by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productId)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollVehavour.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                },
                scrollBehavior = scrollVehavour
            )
        }
    ) { innerPadding ->
        when {
            getProductById.value.isLoading -> {
                CircularIndicator()
            }

            getProductById.value.errorMessage != null -> {
                Text(getProductById.value.errorMessage!!)
            }

            getProductById.value.userData != null -> {
                val product = getProductById.value.userData!!.copy(productId = productId)
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    Box(modifier = Modifier.height(300.dp)) {
                        AsyncImage(
                            model = product.image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Rs.${product.price}",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Size",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(
                                "S",
                                "R",
                                "L",
                                "XL"
                            ).forEach {
                                OutlinedButton(
                                    onClick = { selectedSized = it },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (selectedSized == it) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        contentColor = if (selectedSized == it) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                ) { Text(it) }
                            }
                        }
                        Text(
                            text = "Quantity",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                Text("-")
                            }
                            Text(quantity.toString(), style = MaterialTheme.typography.bodyLarge)
                            IconButton(onClick = { quantity++ }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "add button"
                                )
                            }
                        }
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                        Text(product.description)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val cartDatamodel = CartDataModels(
                                    productId = productId,
                                    name = product.name,
                                    image = product.name,
                                    price = product.price,
                                    quantity = quantity.toString(),
                                    size = selectedSized,
                                    description = product.description,
                                    category = product.category
                                )
                                viewModel.addToCart(cartDatamodel)
                            }, modifier = Modifier.fillMaxWidth(), colors = ButtonColors(
                                containerColor = colorResource(R.color.purple_300),
                                contentColor = colorResource(R.color.white),
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            )
                        ) { Text("Add To Cart") }
                        Button(
                            onClick = {
                                navController.navigate(Routes.CheckoutScreen(productId))
                            }, modifier = Modifier.fillMaxWidth(), colors = ButtonColors(
                                containerColor = colorResource(R.color.purple_300),
                                contentColor = colorResource(R.color.white),
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            )
                        ) { Text("Buy Now") }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add to Wishlist", style = MaterialTheme.typography.labelLarge)
                            IconButton(onClick = {
                                isFavourite = !isFavourite
                                viewModel.addToFavourite(product)
                            }) {
                                Icon(
                                    imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "wishlist icon",
                                    tint = if (isFavourite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}