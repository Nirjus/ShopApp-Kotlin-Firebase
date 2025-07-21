package com.example.shopapp.presentation.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.shopapp.R
import com.example.shopapp.presentation.Screens.AllCategoriesScreen
import com.example.shopapp.presentation.Screens.CartScreen
import com.example.shopapp.presentation.Screens.GetAllFavouriteProductScreen
import com.example.shopapp.presentation.Screens.GetAllProductScreen
import com.example.shopapp.presentation.Screens.HomeScreen
import com.example.shopapp.presentation.Screens.ProductDetailsScreen
import com.example.shopapp.presentation.Screens.ProfileScreenUI
import com.example.shopapp.presentation.SignInScreenUI
import com.example.shopapp.presentation.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

data class BottomNavItem(val name: String, val icon: ImageVector, val unSelectedIcon: ImageVector)

@Composable
fun App(
    firebaseAuth: FirebaseAuth
) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = remember { mutableStateOf(false) }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when (currentDestination) {
            Routes.LoginScreen::class.qualifiedName, Routes.SignUpScreen::class.qualifiedName -> false
            else -> true
        }
    }

    val BottomNavItem = listOf(
        BottomNavItem("Home", Icons.Default.Home, unSelectedIcon = Icons.Outlined.Home),
        BottomNavItem("WishList", Icons.Default.Favorite, unSelectedIcon = Icons.Outlined.Favorite),
        BottomNavItem(
            "Cart",
            Icons.Default.ShoppingCart,
            unSelectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavItem("Profile", Icons.Default.Person, unSelectedIcon = Icons.Outlined.Person)
    )
    var startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.LoginSignUpScreen
    } else {
        SubNavigation.MainScreen
    }
    Scaffold(
        modifier = Modifier,
        bottomBar = {
            if (shouldShowBottomBar.value) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateTopPadding()
                        )
                ) {
                    AnimatedBottomBar(
                        selectedItem = selectedItem,
                        itemSize = BottomNavItem.size,
                        containerColor = Color.Transparent,
                        indicatorColor = colorResource(id = R.color.purple_300),
                        indicatorDirection = IndicatorDirection.BOTTOM,
                        indicatorStyle = IndicatorStyle.FILLED
                    ) {
                        BottomNavItem.forEachIndexed { index, item ->
                            BottomBarItem(
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    when (index) {
                                        0 -> navController.navigate(Routes.HomeScreen)
                                        1 -> navController.navigate(Routes.WishListScreen)
                                        2 -> navController.navigate(Routes.CartScreen)
                                        3 -> navController.navigate(Routes.ProfileScreen)
                                    }
                                },
                                imageVector = item.icon,
                                label = item.name,
                                containerColor = Color.Transparent
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .padding(innerPadding)
                .padding(bottom = if (shouldShowBottomBar.value) 60.dp else innerPadding.calculateTopPadding())
        ) {
            NavHost(navController = navController, startDestination = startScreen) {
                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.LoginScreen) {
                    composable<Routes.LoginScreen> {
                        SignInScreenUI(navController = navController)
                    }
                }
                composable<Routes.SignUpScreen> {
                    SignUpScreen(navController = navController)
                }
                navigation<SubNavigation.MainScreen>(startDestination = Routes.HomeScreen) {
                    composable<Routes.HomeScreen> {
                        HomeScreen(navController = navController)
                    }
                    composable<Routes.ProfileScreen> {
                        ProfileScreenUI(navController = navController, firebaseAuth = firebaseAuth)
                    }
                    composable<Routes.WishListScreen> {
                        GetAllFavouriteProductScreen(navController = navController)
                    }
                    composable<Routes.CartScreen> {
                        CartScreen(navController = navController)
                    }
                    composable<Routes.SeeAllProductScreen> {
                        GetAllProductScreen(navController = navController)
                    }
                    composable<Routes.AllCategoryScreen> {
                        AllCategoriesScreen(navController = navController)
                    }
                }
                composable<Routes.ProductDetailsScreen> {
                    var product: Routes.ProductDetailsScreen = it.toRoute()
                    ProductDetailsScreen(navController = navController, productId = product.productId)
                }
                composable<Routes.CategoryWiseProductsScreen> {
                    var category: Routes.CategoryWiseProductsScreen = it.toRoute()
                    ProductDetailsScreen(navController = navController, productId = category.categoryName)
                }
                composable<Routes.CheckoutScreen> {
                    var product: Routes.CheckoutScreen = it.toRoute()
                    ProductDetailsScreen(navController = navController, productId = product.productId)
                }
            }
        }
    }
}