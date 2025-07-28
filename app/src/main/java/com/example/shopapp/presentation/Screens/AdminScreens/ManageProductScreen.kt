package com.example.shopapp.presentation.Screens.AdminScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopapp.R
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.presentation.Navigation.AdminRoutes
import com.example.shopapp.presentation.Navigation.Routes
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreenUI(
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val getAllProductsSate = viewModel.getAllProductsState.collectAsStateWithLifecycle()
    val productData: List<ProductsDataModel> = getAllProductsSate.value.userData.filterNotNull()
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllProducts()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Manage Products",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.teal_200),
                    titleContentColor = MaterialTheme.colorScheme.background
                )
            )
        }) { innerPadding ->
        if(getAllProductsSate.value.isLoading){
            CircularIndicator()
        }else if(getAllProductsSate.value.errorMessage != null){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = getAllProductsSate.value.errorMessage ?: "Something went wrong")
            }
        }else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 10.dp)
//                .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .size(180.dp, 100.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(color = colorResource(R.color.teal_200).copy(0.2f)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No. of products", style = MaterialTheme.typography.bodyLarge)
                        Text("${productData.size}", style = MaterialTheme.typography.bodyMedium)
                    }
                    OutlinedButton(onClick = { navController.navigate(AdminRoutes.CreateOrEditProductScreen()) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add icon",
                                modifier = Modifier.size(20.dp),
                                tint = colorResource(R.color.teal_700)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                "Add Product",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(R.color.teal_700)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("List of products", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(10.dp))
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.size(16.dp))
                LazyColumn(
                    modifier = Modifier.weight(.6f).padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(productData) { product ->
                        ProductItem(product, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: ProductsDataModel,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = {navController.navigate(Routes.ProductDetailsScreen(product.productId))}),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .background(color = Color.White)
                    .size(80.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rs.${product.finalPrice}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Category: ${product.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.weight(1.5f))
            Box(
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = { navController.navigate(AdminRoutes.CreateOrEditProductScreen(product.productId)) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {  }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProductPreview() {
    val navController = NavController(LocalContext.current)
    ManageProductScreenUI(navController)
}