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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.shopapp.R
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.presentation.viewModels.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrDeleteBannerDialog(
    onDismiss: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel(),
    bannerId: String? = null,
    refreshBannerListData: () -> Unit
) {
    val context = LocalContext.current
    val addBannerState = viewModel.addBannerState.collectAsStateWithLifecycle()
    val deleteBannerState = viewModel.deleteBannerState.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf("") }
    var image by remember { mutableStateOf<Uri?>(null) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                image = uri
            }
        }

    fun createBanner() {
        if (name.isBlank() || image == null) {
            Toast.makeText(context, "Please fill all the field", Toast.LENGTH_SHORT).show()
        } else {
            val bannerData = BannerDataModels(
                name = name,
                image = image.toString()
            )
            viewModel.addBannerView(context, bannerData, image!!)
        }
    }
    fun deleteBanner(){
        viewModel.deleteBannerView(bannerId!!)
    }

    if (addBannerState.value.isLoading) {
        CircularIndicator()
    } else if (addBannerState.value.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = addBannerState.value.errorMessage.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else if (addBannerState.value.data != null) {
        Toast.makeText(
            context,
            addBannerState.value.data ?: "Banner is created",
            Toast.LENGTH_SHORT
        ).show()
        refreshBannerListData()
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
                    if (bannerId == null) {
                        Text(
                            text = "Upload a banner",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.teal_200)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = {
                                Text(
                                    "Banner Name",
                                    color = colorResource(R.color.teal_700)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(R.color.teal_700),
                                unfocusedBorderColor = colorResource(R.color.teal_700)
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
                                contentDescription = "Banner Image",
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .border(
                                        2.dp,
                                        color = colorResource(R.color.teal_200),
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
                                colors = ButtonDefaults.buttonColors(
                                    colorResource(R.color.teal_200).copy(
                                        0.3f
                                    )
                                )
                            ) {
                                Text("Add")
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { createBanner() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.teal_700),
                                contentColor = Color.White
                            )
                        ) { Text(text = "Create") }
                    } else {
                        Column {
                            Text(
                                "You sure want to delete this banner?",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.background
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            if(deleteBannerState.value.isLoading){
                               Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                   CircularProgressIndicator()
                               }
                            }else if(deleteBannerState.value.errorMessage != null){
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = deleteBannerState.value.errorMessage.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Red,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }else if(deleteBannerState.value.data != null){
                                Toast.makeText(
                                    context,
                                    deleteBannerState.value.data ?: "Banner is deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                refreshBannerListData()
                                onDismiss()
                            }else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    OutlinedButton(onClick = { deleteBanner() }) {
                                        Text("Yes")
                                    }
                                    OutlinedButton(onClick = { onDismiss() }) {
                                        Text("No")
                                    }

                                }
                            }
                        }
                    }
                }
            }

        )
    }
}