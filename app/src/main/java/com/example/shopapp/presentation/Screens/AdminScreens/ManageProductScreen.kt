package com.example.shopapp.presentation.Screens.AdminScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shopapp.R
import com.example.shopapp.presentation.Navigation.AdminRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreenUI(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.teal_200),
                    titleContentColor = MaterialTheme.colorScheme.background)
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.size(180.dp, 100.dp).border(width = 2.dp, color= Color.Gray, shape = RoundedCornerShape(10.dp)).background(color = colorResource(R.color.teal_200).copy(0.2f)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No. of products", style = MaterialTheme.typography.bodyLarge)
                    Text("${10}", style = MaterialTheme.typography.bodyMedium)
                }
                OutlinedButton(onClick = {navController.navigate(AdminRoutes.CreateOrEditProductScreen())}) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add icon", modifier = Modifier.size(20.dp), tint = colorResource(R.color.teal_700))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Add Product", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.teal_700))
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(10){
//
//                }
//            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProductPreview(){
    val navController: NavController = NavController(LocalContext.current)
    ManageProductScreenUI(navController)
}