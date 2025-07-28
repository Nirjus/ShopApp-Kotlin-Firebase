package com.example.shopapp.presentation.Screens.AdminScreens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.shopapp.R
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.presentation.Navigation.AdminRoutes
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.utils.BasicAlertBox
import com.example.shopapp.presentation.viewModels.AdminViewModel
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditProductScreenUI(
    navController: NavController,
    productId: String? = null,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        if (productId != null) {
            viewModel.getProductByIdView(productId)
        }
    }
    val createProductState = viewModel.createProductState.collectAsStateWithLifecycle()
    val updateProductState = viewModel.updateProductState.collectAsStateWithLifecycle()
    val getProductByIdState = viewModel.getProductByIdState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var finalPrice by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var availableUnites by remember { mutableStateOf("") }
    var image by remember { mutableStateOf<Uri?>(null) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                image = uri
            }
        }
    LaunchedEffect(getProductByIdState.value.data) {
        if (getProductByIdState.value.data != null) {
            name = getProductByIdState.value.data!!.name
            description = getProductByIdState.value.data!!.description
            price = getProductByIdState.value.data!!.price
            finalPrice = getProductByIdState.value.data!!.finalPrice
            category = getProductByIdState.value.data!!.category
            availableUnites = getProductByIdState.value.data!!.availableUnites.toString()
            image = getProductByIdState.value.data!!.image.toUri()
        }
    }
    fun createProduct() {
        if (name.isBlank() || description.isBlank() || price.isBlank() || finalPrice.isBlank() || category.isBlank() || availableUnites.isBlank() || image == null) {
            Toast.makeText(context, "Fill all the fields", Toast.LENGTH_SHORT).show()
        } else {
            val productData = ProductsDataModel(
                name = name,
                description = description,
                price = price,
                finalPrice = finalPrice,
                category = category,
                availableUnites = availableUnites.toInt(),
                image = image.toString(),
                createdAt = System.currentTimeMillis().toString()
            )
            viewModel.createProductView(context, productData, image!!)
        }
    }
    fun updateProduct(){
        if (name.isBlank() || description.isBlank() || price.isBlank() || finalPrice.isBlank() || category.isBlank() || availableUnites.isBlank() || image == null) {
            Toast.makeText(context, "Fill all the fields", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Product is updated", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(
                    text = if (productId == null) "Create Product" else "Edit product",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 10.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.purple_200),
                titleContentColor = MaterialTheme.colorScheme.background
            )
        )
    }) { innerPadding ->
        if (createProductState.value.isLoading) {
            CircularIndicator()
        } else if (createProductState.value.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = createProductState.value.errorMessage ?: "Error in product creating",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        } else if (createProductState.value.data != null) {
            BasicAlertBox(onDismiss = {}, text = "Product created successfully") {
                Button(onClick = {navController.navigate(AdminRoutes.ManageProductScreen)},
                    modifier = Modifier
                        .fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_300)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Product added in the list, visit now", color = Color.White)
                }
            }
        }else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(top = 10.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = Color.LightGray.copy(0.5f)),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "info",
                            tint = Color.DarkGray
                        )
                        Text(
                            "Fill all the fields to create or edit a product",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.purple_300),
                            unfocusedBorderColor = colorResource(R.color.purple_200)
                        )
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Product Description") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.purple_300),
                            unfocusedBorderColor = colorResource(R.color.purple_200)
                        )
                    )
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Product Price") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.purple_300),
                            unfocusedBorderColor = colorResource(R.color.purple_200)
                        )
                    )
                    OutlinedTextField(
                        value = finalPrice,
                        onValueChange = { finalPrice = it },
                        label = { Text("Final Price") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.purple_300),
                            unfocusedBorderColor = colorResource(R.color.purple_200)
                        )
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.purple_300),
                            unfocusedBorderColor = colorResource(R.color.purple_200)
                        )
                    )
                    OutlinedTextField(
                        value = availableUnites,
                        onValueChange = { availableUnites = it },
                        label = { Text("Available Units") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.purple_300),
                            unfocusedBorderColor = colorResource(R.color.purple_200)
                        )
                    )
                    // image upload
                    Box(modifier = Modifier.fillMaxWidth()) {
                        SubcomposeAsyncImage(
                            model = image.toString(),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .border(
                                    2.dp,
                                    color = colorResource(R.color.purple_500),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentScale = ContentScale.Crop
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
                                is AsyncImagePainter.State.Error ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize() // Make this Box fill the 200.dp space
                                            .background(Color.LightGray.copy(alpha = 0.3f)), // Background for the error state container
                                        contentAlignment = Alignment.Center // Center content within this Box
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.image),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .align(Alignment.Center)
                                        )
                                    }

                                else -> SubcomposeAsyncImageContent()
                            }
                        }
                        IconButton(
                            onClick = {
                                pickMedia.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .size(60.dp, 40.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Change Picture",
                            )
                        }
                    }
                    // save or create
                    Button(
                        onClick = { if(productId == null) createProduct() else updateProduct() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple_300))
                    ) {
                        Text(
                            text = if (productId == null) "Create product" else "Save",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateOrEditProductScreenPreview() {
    val navController = NavController(LocalContext.current)
    CreateOrEditProductScreenUI(navController = navController, productId = null)
}