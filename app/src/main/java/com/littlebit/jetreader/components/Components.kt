package com.littlebit.jetreader.components

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Facebook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.littlebit.jetreader.R
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.utils.isValidPassWord

// Icons
@Composable
fun AppleIcon(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = color,
        shadowElevation = 10.dp
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
                .clickable { onClick() },
            painter = painterResource(id = R.drawable.ic_apple_icon),
            contentDescription = "Apple",
            tint = Color.White
        )
    }

}

@Composable
fun FaceBookIcon(
    modifier: Modifier = Modifier,
    color: Color,
    borderStroke: BorderStroke = BorderStroke(1.dp, Color.LightGray),
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(50),
        color = color,
        shadowElevation = 5.dp,
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Rounded.Facebook,
                contentDescription = "Facebook Icon",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Facebook")
        }
    }
}

@Composable
fun GoogleIcon(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = color,
        shadowElevation = 5.dp
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .clickable { onClick() },
            painter = painterResource(id = R.drawable.ic_google_icon),
            contentDescription = "Google",
            tint = Color.White
        )
    }
}


// Login / Sign_in Components


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Fields(
    modifier: Modifier = Modifier,
    loading: MutableState<Boolean>,
    isCreateAccount: MutableState<Boolean>,
    onDone: (String, String) -> Unit = { _, _ -> },
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val confirmPassword = rememberSaveable {
        mutableStateOf("")
    }
    val confirmPasswordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val keyBoardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        mutableStateOf(
            (email.value.isNotEmpty() && password.value.isNotEmpty() && isValidPassWord(password.value) && Patterns.EMAIL_ADDRESS.matcher(
                email.value
            ).matches())
        )
    }
    val validPassword = remember(password.value, confirmPassword.value) {
        mutableStateOf(
            (valid.value && password.value == confirmPassword.value)
        )
    }
    Column(
        modifier = modifier
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            inputText = email,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions.Default,
            enabled = !loading.value,
        )
        if(isCreateAccount.value){
            PasswordField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                inputText = password,
                imeAction = ImeAction.Next,
                oncAction = KeyboardActions.Default,
                enabled = !loading.value,
                passWordVisibility = passwordVisibility,
            )
            PasswordField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                inputText = confirmPassword,
                isConfirmPassword = true,
                imeAction = ImeAction.Done,
                enabled = !loading.value,
                passWordVisibility = confirmPasswordVisibility,
                oncAction = KeyboardActions {
                    if (validPassword.value) {
                        onDone(email.value.trim(), password.value.trim())
                        keyBoardController?.hide()
                    } else {
                        keyBoardController?.hide()
                        return@KeyboardActions
                    }
                },
            )
        }
        else{
            PasswordField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                inputText = password,
                imeAction = ImeAction.Done,
                enabled = !loading.value,
                passWordVisibility = passwordVisibility,
                oncAction = KeyboardActions {
                    if (valid.value) {
                        onDone(email.value.trim(), password.value.trim())
                        keyBoardController?.hide()
                    } else {
                        keyBoardController?.hide()
                        return@KeyboardActions
                    }
                },
            )
        }
        SubmitButton(
            isCreateAccount = isCreateAccount,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 5.dp),
            loading = loading,
            valid = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyBoardController?.hide()
        }
    }
}


@Composable
fun SubmitButton(
    isCreateAccount: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    loading: MutableState<Boolean>,
    valid: MutableState<Boolean>,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = valid.value && !loading.value,
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        if (loading.value) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        } else {
            Text(text = if (isCreateAccount.value) "SignUp" else "Login")
        }
    }
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    inputText: MutableState<String>,
    passWordVisibility: MutableState<Boolean>,
    oncAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction,
    isConfirmPassword: Boolean = false,
    enabled: Boolean = true,
) {
    val visualTransformation = remember(passWordVisibility.value) {
        if (passWordVisibility.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    }
    InputField(
        modifier = modifier,
        inputText = inputText,
        label = "Password",
        placeholder = if (isConfirmPassword) "Confirm password" else "Enter your password",
        keyBoardType = KeyboardType.Password,
        imeAction = imeAction,
        oncAction = oncAction,
        enabled = enabled,
        isSingleLine = true,
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(
                onClick = {
                    passWordVisibility.value = !passWordVisibility.value
                }
            ) {
                Icon(
                    imageVector = if (passWordVisibility.value) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.VisibilityOff
                    },
                    contentDescription = "Password Visibility"
                )
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = "Password Visibility"
            )
        }
    )
}


@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    inputText: MutableState<String>,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
) {
    InputField(
        modifier = modifier,
        inputText = inputText,
        label = "Email",
        placeholder = "Enter your email",
        keyBoardType = KeyboardType.Email,
        imeAction = imeAction,
        enabled = enabled,
        oncAction = onAction,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Password Visibility"
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    inputText: MutableState<String>,
    label: String,
    placeholder: String,
    keyBoardType: KeyboardType,
    imeAction: ImeAction,
    oncAction: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val error = remember(inputText.value) {
        mutableStateOf(false)
    }
    OutlinedTextField(
        modifier = modifier,
        value = inputText.value,
        onValueChange = {
            inputText.value = it
            error.value = it.isEmpty()
        },
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBoardType,
            imeAction = imeAction
        ),
        isError = error.value,
        keyboardActions = oncAction,
        enabled = enabled,
        singleLine = isSingleLine,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
    )
}

@Composable
fun SocialMediaButtons(
    googleSignInClient: GoogleSignInClient,
    navController: NavHostController
) {
    val context = LocalContext.current
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Signed in successfully, handle the user's details
                Log.d("SUCCESS", "SocialMediaButtons: ${account?.displayName}")
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signInWithCredential(
                    GoogleAuthProvider.getCredential(account?.idToken, null)
                ).addOnCompleteListener { signInTask ->
                    if (signInTask.isSuccessful) {
                        Log.d("FIRE SUCCESS", "SocialMediaButtons: ${signInTask.result}")
                        makeToast(context, "Login Successful")
                        navController.navigate(JetScreens.HomeScreen.name){
                            popUpTo(JetScreens.LoginSignUpScreen.name){
                                inclusive = true
                            }
                        }
                    } else {
                        Log.d("FIRE ERROR", "SocialMediaButtons: ${signInTask.exception}")
                    }
                }
            } catch (e: ApiException) {
                // Handle sign-in failure
                makeToast(context, "Login Failed")
                Log.d("ERROR", "SocialMediaButtons: ${e.message}")
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialMediaButton(
            modifier = Modifier
                .padding(5.dp)
                .size(50.dp),
            color = Color(0xFF3B5998),
            icon = Icons.Rounded.Facebook
        ) {
            Log.d("FacebookButton", "Facebook Button Clicked: Facebook")
            makeToast(context, "Coming Soon")
        }
        GoogleIcon(
            modifier = Modifier
                .padding(5.dp)
                .size(50.dp),
            color = MaterialTheme.colorScheme.inverseSurface,
        ) {
            Log.d("GoogleButton", "GoogleButton Clicked: Google")
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        AppleIcon(
            modifier = Modifier
                .padding(5.dp)
                .size(50.dp),
            color = MaterialTheme.colorScheme.onSurface
        ) {
            Log.d("AppleButton", "AppleButton Clicked: Apple")
            makeToast(context, "Coming Soon")
        }
    }
}



fun makeToast(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}

@Composable
fun SocialMediaButton(modifier: Modifier, color: Color, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(50),
        color = color,
        shadowElevation = 5.dp
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            imageVector = icon,
            contentDescription = "Social Media Button",
            tint = Color.White
        )
    }
}

@Composable
fun TextBetweenDivider(text: String = "Or continue with") {
    Row(
        modifier = Modifier
            .padding(top = 15.dp, bottom = 15.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .width(70.dp)
                .padding(5.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
        Text(
            modifier = Modifier.padding(5.dp),
            text = text,
            color = Color.Gray
        )
        Divider(
            modifier = Modifier
                .width(70.dp)
                .padding(5.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}

@Composable
fun SignUpOrLogin(
    modifier: Modifier = Modifier,
    signUp: MutableState<Boolean>,
    onClick: () -> Unit = {}
) {
    val text = if (!signUp.value) "Don't have an account? Sign Up" else "Already have an account? Login"
    Row(
        modifier = modifier.padding(top = 5.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text.split("?")[0] + "? "
        )
        Text(
            modifier = Modifier.clickable(onClick = onClick),
            text = text.split("?")[1],
            color = Color.Blue.copy(alpha = 0.7f),
        )
    }
}







