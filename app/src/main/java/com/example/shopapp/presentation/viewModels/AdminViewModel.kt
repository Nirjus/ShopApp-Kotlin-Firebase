package com.example.shopapp.presentation.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.adminUsecase.*
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val createProductUseCase: CreateProduct,
    private val createCategoryUseCase: CreateCategory,
    private val updateProductUseCase: UpdateProduct,
    private val deleteProductUseCase: DeleteProduct,
    private val deleteCategoryUseCase: DeleteCategory,
    private val updateCategoryUseCase: UpdateCategory
) : ViewModel() {
    private val _createProductState = MutableStateFlow(CreateProductState())
    val createProductState = _createProductState.asStateFlow()
    private val _createCategoryState = MutableStateFlow(CreateCategoryState())
    val createCategoryState = _createCategoryState.asStateFlow()
    private val _updateProductState = MutableStateFlow(UpdateProductState())
    val updateProductState = _updateProductState.asStateFlow()
    private val _deleteProductState = MutableStateFlow(DeleteProductState())
    val deleteProductState = _deleteProductState.asStateFlow()
    private val _deleteCategoryState = MutableStateFlow(DeleteCategoryState())
    val deleteCategoryState = _deleteCategoryState.asStateFlow()
    private val _updateCategoryState = MutableStateFlow(UpdateCategoryState())
    val updateCategoryState = _updateCategoryState.asStateFlow()

    fun createProductView(context: Context, productsDataModel: ProductsDataModel, imageUri: Uri) {
        viewModelScope.launch {
            createProductUseCase.createProducts(context, productsDataModel, imageUri).collect {
                when (it) {
                    is ResultState.Error -> {
                        _createProductState.value = _createProductState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _createProductState.value = _createProductState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _createProductState.value = _createProductState.value.copy(
                            isLoading = false,
                            data = it.data
                        )
                    }
                }
            }
        }
    }

    fun createCategoryView(context: Context, categoryDataModel: CategoryDataModel, imageUri: Uri) {
        viewModelScope.launch {
            createCategoryUseCase.createCategories(context, categoryDataModel, imageUri).collect {
                when (it) {
                    is ResultState.Error -> {
                        _createCategoryState.value = _createCategoryState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _createCategoryState.value = _createCategoryState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _createCategoryState.value = _createCategoryState.value.copy(
                            isLoading = false,
                            data = it.data
                        )
                    }
                }
            }
        }
    }

    fun updateProductView(productsDataModel: ProductsDataModel) {
        viewModelScope.launch {
            updateProductUseCase.updateProducts(productsDataModel).collect {
                when (it) {
                    is ResultState.Error -> {
                        _updateProductState.value = _updateProductState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _updateProductState.value = _updateProductState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _updateProductState.value = _updateProductState.value.copy(
                            isLoading = false,
                            data = it.data
                        )
                    }
                }
            }
        }
    }

    fun deleteProductView(productId: String) {
        viewModelScope.launch {
            deleteProductUseCase.deleteProducts(productId).collect {
                when (it) {
                    is ResultState.Error -> {
                        _deleteProductState.value = _deleteProductState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _deleteProductState.value = _deleteProductState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _deleteProductState.value = _deleteProductState.value.copy(
                            isLoading = false,
                            data = it.data
                        )
                    }
                }
            }
        }
    }

    fun deleteCategoryView(categoryId: String) {
        viewModelScope.launch {
            deleteCategoryUseCase.deleteCategories(categoryId).collect {
                when (it) {
                    is ResultState.Error -> {
                        _deleteCategoryState.value = _deleteCategoryState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _deleteCategoryState.value = _deleteCategoryState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _deleteCategoryState.value = _deleteCategoryState.value.copy(
                            isLoading = false,
                            data = it.data
                        )
                    }
                }
            }
        }
    }

    fun updateCategoryView(categoryDataModel: CategoryDataModel) {
        viewModelScope.launch {
            updateCategoryUseCase.updateCategories(categoryDataModel).collect {
                when (it) {
                    is ResultState.Error -> {
                        _updateCategoryState.value = _updateCategoryState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _updateCategoryState.value = _updateCategoryState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _updateCategoryState.value = _updateCategoryState.value.copy(
                            isLoading = false,
                            data = it.data
                        )
                    }
                }
            }
        }
    }
}

data class CreateProductState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: String? = null
)

data class CreateCategoryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: String? = null
)

data class UpdateCategoryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: String? = null
)

data class UpdateProductState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: String? = null
)

data class DeleteCategoryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: String? = null
)

data class DeleteProductState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: String? = null
)