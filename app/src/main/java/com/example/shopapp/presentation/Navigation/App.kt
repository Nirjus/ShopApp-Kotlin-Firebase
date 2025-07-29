package com.example.shopapp.presentation.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.shopapp.presentation.Screens.AdminScreens.CreateOrEditProductScreenUI
import com.example.shopapp.presentation.Screens.AdminScreens.ManageCategoryScreenUI
import com.example.shopapp.presentation.Screens.AdminScreens.ManageOrderScreenUI
import com.example.shopapp.presentation.Screens.AdminScreens.ManageProductScreenUI
import com.example.shopapp.presentation.Screens.AdminScreens.ManageUI
import com.example.shopapp.presentation.Screens.AllCategoriesScreen
import com.example.shopapp.presentation.Screens.CartScreen
import com.example.shopapp.presentation.Screens.CheckoutScreen
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
    firebaseAuth: FirebaseAuth,
    startPayment:()-> Unit,
    ) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val showUserBottomBar = remember { mutableStateOf(false) }
    val showAdminBottomBar = remember { mutableStateOf(false) }

    val userBottomRoutes = listOf(
        Routes.HomeScreen,
        Routes.WishListScreen,
        Routes.CartScreen,
        Routes.ProfileScreen
    )

    val adminBottomRoutes = listOf(
        AdminRoutes.ManageProductScreen,
        AdminRoutes.ManageCategoryScreen,
        AdminRoutes.ManageOrderScreen,
        AdminRoutes.ManageUI
    )
    LaunchedEffect(currentDestination) {

         showUserBottomBar.value = currentDestination in userBottomRoutes.map { it::class.qualifiedName}
         showAdminBottomBar.value = currentDestination in adminBottomRoutes.map { it::class.qualifiedName }
    }


    val userBottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, unSelectedIcon = Icons.Outlined.Home),
        BottomNavItem("WishList", Icons.Default.Favorite, unSelectedIcon = Icons.Outlined.Favorite),
        BottomNavItem(
            "Cart",
            Icons.Default.ShoppingCart,
            unSelectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavItem("Profile", Icons.Default.Person, unSelectedIcon = Icons.Outlined.Person)
    )
    val adminBottomNavItems = listOf(
        BottomNavItem("Products", Icons.Default.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavItem("Category", Icons.Default.Menu, Icons.Outlined.Menu),
        BottomNavItem("Orders", Icons.Default.Info, Icons.Outlined.Info),
        BottomNavItem("UI", Icons.Default.Build, Icons.Outlined.Build)
    )
    var startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.LoginSignUpScreen
    } else {
        SubNavigation.MainScreen
    }
    Scaffold(
        modifier = Modifier,
        bottomBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateTopPadding()
                        )
                ) {
                    when {

                        showUserBottomBar.value -> AnimatedBottomBar(
                            selectedItem = selectedItem,
                            itemSize = userBottomNavItems.size,
                            containerColor = Color.Transparent,
                            indicatorColor = colorResource(id = R.color.purple_300),
                            indicatorDirection = IndicatorDirection.BOTTOM,
                            indicatorStyle = IndicatorStyle.FILLED
                        ) {
                            userBottomNavItems.forEachIndexed { index, item ->
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
                        showAdminBottomBar.value -> AnimatedBottomBar(
                            selectedItem = selectedItem,
                            itemSize = adminBottomNavItems.size,
                            containerColor = Color.Transparent,
                            indicatorColor = colorResource(id = R.color.teal_200),
                            indicatorDirection = IndicatorDirection.BOTTOM,
                            indicatorStyle = IndicatorStyle.FILLED
                        ) {
                            adminBottomNavItems.forEachIndexed { index, item ->
                                BottomBarItem(
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        when (index) {
                                            0 -> navController.navigate(AdminRoutes.ManageProductScreen)
                                            1 -> navController.navigate(AdminRoutes.ManageCategoryScreen)
                                            2 -> navController.navigate(AdminRoutes.ManageOrderScreen)
                                            3 -> navController.navigate(AdminRoutes.ManageUI)
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
                .padding(innerPadding)
        ) {
            NavHost(navController = navController, startDestination = startScreen) {
                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.LoginScreen) {
                    composable<Routes.LoginScreen> {
                        SignInScreenUI(navController = navController)
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreen(navController = navController)
                    }
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
                navigation<SubNavigation.AdminScreen>(startDestination = AdminRoutes.ManageProductScreen) {
                    composable<AdminRoutes.ManageProductScreen> {
                        ManageProductScreenUI(navController = navController)
                    }

                    composable<AdminRoutes.ManageCategoryScreen> {
                        ManageCategoryScreenUI(navController = navController)
                    }
                    composable<AdminRoutes.ManageOrderScreen> {
                        ManageOrderScreenUI(navController = navController)
                    }
                    composable<AdminRoutes.ManageUI> {
                        ManageUI(navController = navController)
                    }
                    composable<AdminRoutes.CreateOrEditProductScreen> {
                        var product: AdminRoutes.CreateOrEditProductScreen = it.toRoute()
                        CreateOrEditProductScreenUI(navController= navController, productId = product.productId)
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
                    CheckoutScreen(navController = navController, productId = product.productId, pay = startPayment)
                }
            }
        }
    }
}