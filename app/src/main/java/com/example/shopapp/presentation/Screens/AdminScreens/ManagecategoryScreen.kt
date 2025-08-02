package com.example.shopapp.presentation.Screens.AdminScreens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopapp.R
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.presentation.Navigation.AdminRoutes
import com.example.shopapp.presentation.Navigation.Routes
import com.example.shopapp.presentation.utils.CategoryEditDialog
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryScreenUI(
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val getAllCategoryState = viewModel.getAllCategoryState.collectAsStateWithLifecycle()
    val categoryData: List<CategoryDataModel> = getAllCategoryState.value.userData.filterNotNull()
    var showAlert by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCategories()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Manage Categories",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.teal_200),
                    titleContentColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.ProfileScreen) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                }
            )
        }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 10.dp)
            ) {
                if (getAllCategoryState.value.isLoading) {
                    CircularIndicator()
                } else if (getAllCategoryState.value.errorMessage != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = getAllCategoryState.value.errorMessage ?: "Something went wrong")
                    }
                } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total number of \n categories ${categoryData.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    VerticalDivider(modifier = Modifier.height(30.dp), color = Color.Gray)
                    Button(
                        onClick = { showAlert = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_200))
                    ) {
                        Text("Add more")
                    }
                }
                if (showAlert) {
                    CategoryEditDialog(
                        onDismiss = { showAlert = false },
                        refreshCategoryListData = { viewModel.getAllCategories() })
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("List of categories", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(10.dp))
                    HorizontalDivider()
                }
                Spacer(modifier = Modifier.size(16.dp))
                if (categoryData.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "No category available!",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(.6f)
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categoryData) { category ->
                        CategoryCard(
                            category,
                            refreshCategoryListData = { viewModel.getAllCategories() })
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: CategoryDataModel, refreshCategoryListData: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = category.image,
                contentDescription = "Category Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "♦ " + category.name,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            isEdit = true
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = { expanded = false }
                    )
                }
            }
        }
    }
    if (isEdit) {
        CategoryEditDialog(
            onDismiss = { isEdit = false },
            categoryId = category.categoryId,
            refreshCategoryListData = refreshCategoryListData
        )

    }
}