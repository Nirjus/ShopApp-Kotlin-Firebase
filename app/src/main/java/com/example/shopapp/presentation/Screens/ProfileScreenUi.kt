package com.example.shopapp.presentation.Screens

import com.example.shopapp.R
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.models.UserDataParent
import com.example.shopapp.presentation.Navigation.SubNavigation
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.utils.LogoutAlertBox
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreenUI(
    navController: NavController,
    firebaseAuth: FirebaseAuth,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.getUserById(firebaseAuth.currentUser!!.uid)
    }
    val profileScreenState = viewModel.profileScreenState.collectAsStateWithLifecycle()
    val updateScreenState = viewModel.updateScreenState.collectAsStateWithLifecycle()
    val userProfileImageState = viewModel.uploadUserProfileImageState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val isEditing = remember { mutableStateOf(false) }
//    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageUri = remember { mutableStateOf("") }

    val firstname =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.firstName ?: "") }
    val lastname =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.lastName ?: "") }
    val email =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.email ?: "") }
    val phoneNumber =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.phoneNumber ?: "") }
    val address =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.address ?: "") }

    LaunchedEffect(profileScreenState.value.userData) {
        profileScreenState.value.userData?.userData?.let { userData ->
            firstname.value = userData.firstName
            lastname.value = userData.lastName
            email.value = userData.email
            phoneNumber.value = userData.phoneNumber
            address.value = userData.address
            imageUri.value = userData.image
        }
    }
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                viewModel.uploadUserProfileIMage(uri)
                imageUri.value = uri.toString()
            }
        }
    if (updateScreenState.value.userData != null) {
        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
    } else if (updateScreenState.value.errorMessage != null) {
        Toast.makeText(context, updateScreenState.value.errorMessage, Toast.LENGTH_SHORT).show()
    } else if (updateScreenState.value.isLoading) {
        CircularIndicator()
    }
    if (userProfileImageState.value.userData != null) {
        imageUri.value = userProfileImageState.value.userData.toString()
    } else if (userProfileImageState.value.errorMessage != null) {
        Toast.makeText(context, updateScreenState.value.errorMessage, Toast.LENGTH_SHORT).show()
    } else if (userProfileImageState.value.isLoading) {
        CircularIndicator()
    }
    if (profileScreenState.value.userData != null) {
        Scaffold() { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            )
            {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Start)
                ) {
                    SubcomposeAsyncImage(
                        model = if (isEditing.value) imageUri.value else imageUri.value,
                        contentDescription = "Profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, color = colorResource(R.color.purple_500))
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
                            is AsyncImagePainter.State.Error -> Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )

                            else -> SubcomposeAsyncImageContent()
                        }
                        if (isEditing.value) {
                            IconButton(
                                onClick = {
                                    pickMedia.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Change Picture",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row() {
                    OutlinedTextField(
                        value = firstname.value,
                        onValueChange = { firstname.value = it },
                        modifier = Modifier.weight(1f),
                        readOnly = if (isEditing.value) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(
                                id = R.color.purple_300
                            ), focusedBorderColor = colorResource(id = R.color.purple_300)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text("First Name") }
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    OutlinedTextField(
                        value = lastname.value,
                        onValueChange = { lastname.value = it },
                        modifier = Modifier.weight(1f),
                        readOnly = if (isEditing.value) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(
                                id = R.color.purple_300
                            ), focusedBorderColor = colorResource(id = R.color.purple_300)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text("Last Name") }
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(
                            id = R.color.purple_300
                        ), focusedBorderColor = colorResource(id = R.color.purple_300)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text("Email address") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(
                            id = R.color.purple_300
                        ), focusedBorderColor = colorResource(id = R.color.purple_300)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text("Phone Number") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = address.value,
                    onValueChange = { address.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(
                            id = R.color.purple_300
                        ), focusedBorderColor = colorResource(id = R.color.purple_300)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text("Address") }
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_300))
                ) {
                    Text(text = "Log out")
                }
                if (showDialog.value) {
                    LogoutAlertBox(onDismiss = { showDialog.value = false }, onConfirm = {
                        firebaseAuth.signOut()
                        navController.navigate(SubNavigation.LoginSignUpScreen)
                    })
                }
                Spacer(modifier = Modifier.size(16.dp))

                if (!isEditing.value) {
                    OutlinedButton(
                        onClick = { isEditing.value = !isEditing.value },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Edit Profile")
                    }
                } else {
                    OutlinedButton(onClick = {
                        val updatedUserData = UserData(
                            firstname.value,
                            lastName = lastname.value,
                            email = email.value,
                            address = address.value,
                            phoneNumber = phoneNumber.value,
                            image = imageUri.value
                        )
                        val userDataParent = UserDataParent(
                            nodeId = profileScreenState.value.userData!!.nodeId,
                            userData = updatedUserData
                        )
                        viewModel.upDateUserData(userDataParent)
                        isEditing.value = !isEditing.value
                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                        Text(text = "Save Profile")
                    }
                }
            }
        }
    } else if (profileScreenState.value.errorMessage != null) {
        Text(text = profileScreenState.value.errorMessage!!)
    } else if (profileScreenState.value.isLoading) {
        CircularIndicator()
    }

}