package com.example.shopapp.presentation.utils

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.shopapp.R
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.presentation.viewModels.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditDialog(
    onDismiss: () -> Unit,
    categoryId: String? = null,
    viewModel: AdminViewModel = hiltViewModel(),
    refreshCategoryListData: () -> Unit
    ) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        if (categoryId != null) {
            viewModel.getCategoryByIdView(categoryId)
        }
    }
    val createCategoryState = viewModel.createCategoryState.collectAsStateWithLifecycle()
    val updateCategoryState = viewModel.updateCategoryState.collectAsStateWithLifecycle()
    val getCategoryByIdState = viewModel.getCategoryByIdState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var image by remember { mutableStateOf<Uri?>(null) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                image = uri
            }
        }

    fun createCategory() {
        if (name.isBlank() || image == null) {
            Toast.makeText(context, "Please fill all the field", Toast.LENGTH_SHORT).show()
        } else {
            val categoryData = CategoryDataModel(
                name = name,
                image = image.toString(),
                createdBy = System.currentTimeMillis().toString()
            )
            viewModel.createCategoryView(context, categoryData, image!!)
        }
    }

    fun updateCategory() {
        if (name.isBlank() || image == null) {
            Toast.makeText(context, "Please fill all the field", Toast.LENGTH_SHORT).show()
        } else {
            // LOGIC
        }
    }
    LaunchedEffect(getCategoryByIdState.value.data) {
        if (categoryId != null) {
            getCategoryByIdState.value.data?.let {
                name = it.name
                image = it.image.toUri()
            }
        }
    }
    if(createCategoryState.value.isLoading){
        CircularIndicator()
    }else if(createCategoryState.value.errorMessage != null){
        Toast.makeText(context, createCategoryState.value.errorMessage ?: "Category not created", Toast.LENGTH_SHORT).show()
    }else if(createCategoryState.value.data != null){
        Toast.makeText(context, createCategoryState.value.data ?: "Category created successfully", Toast.LENGTH_SHORT).show()
        refreshCategoryListData()
        onDismiss()
    }
    if(updateCategoryState.value.isLoading){
        CircularIndicator()
    }else if(updateCategoryState.value.errorMessage != null){
        Toast.makeText(context, updateCategoryState.value.errorMessage ?: "Error in updating category", Toast.LENGTH_SHORT).show()
    }else if(updateCategoryState.value.data != null){
        Toast.makeText(context, updateCategoryState.value.data ?: "Category updated successfully", Toast.LENGTH_SHORT).show()
        refreshCategoryListData()
        onDismiss()
    }
    else {
        BasicAlertDialog(
            onDismissRequest = onDismiss,
            modifier = Modifier.background(shape = RoundedCornerShape(16.dp), color = Color.White),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = if (categoryId == null) "Create a category" else "Edit here",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.purple_500)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = {
                            Text(
                                "Category Name",
                                color = colorResource(R.color.purple_300)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.purple_300),
                            unfocusedBorderColor = colorResource(R.color.purple_300)
                        ),
                        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SubcomposeAsyncImage(
                            model = image.toString(),
                            contentDescription = "Category Image",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .border(
                                    2.dp,
                                    color = colorResource(R.color.purple_300),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentScale = ContentScale.Crop
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> CircularIndicator()
                                is AsyncImagePainter.State.Error -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(), // Make this Box fill the 200.dp space

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
                                }

                                else -> SubcomposeAsyncImageContent()
                            }
                        }
                        OutlinedButton(
                            onClick = {
                                pickMedia.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors()
                        ) {
                            Text("Add", color = MaterialTheme.colorScheme.secondaryContainer)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { if (categoryId == null) createCategory() else updateCategory() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.purple_300),
                            contentColor = Color.White
                        )
                    ) { Text(text = if (categoryId == null) "Create" else "Save") }

                }
            }

        )
    }
}