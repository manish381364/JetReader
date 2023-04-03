package com.littlebit.jetreader.screens.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.littlebit.jetreader.components.Fields
import com.littlebit.jetreader.components.SignUpOrLogin
import com.littlebit.jetreader.components.SocialMediaButtons
import com.littlebit.jetreader.components.TextBetweenDivider
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.screens.JetReaderLogo
import com.littlebit.jetreader.utils.isValidEmail
import com.littlebit.jetreader.utils.isValidPassWord


@Composable
fun LoginSignUpScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LoginScreenViewModel = LoginScreenViewModel()) {
    val showLoginScreen = rememberSaveable {
        mutableStateOf(false)
    }
    LoginScreen(
        navController = navController,
        isCreateAccount = showLoginScreen,
        viewModel = viewModel
    )
}
@Composable
fun LoginScreen(
    navController: NavHostController,
    isCreateAccount: MutableState<Boolean>,
    viewModel: LoginScreenViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val loading = remember {
                mutableStateOf(false)
            }

            JetReaderLogo()

            Fields(
                modifier = Modifier.padding(top = 5.dp, bottom = 15.dp),
                navController,
                loading = loading,
                isCreateAccount = isCreateAccount,
            ) { email, password ->
                if(isCreateAccount.value){
                    if(!loading.value){
                        loading.value = true
                        Log.d("Done", "LoginScreen: $email, $password")
                        if (isValidEmail(email) && isValidPassWord(password)) {
                            viewModel.signUp(email, password){
                                navController.navigate(JetScreens.HomeScreen.name)
                            }
                            Log.d("Valid", "LoginScreen: $email, $password")
                        } else {
                            Log.d("Not Valid", "LoginScreen: $email, $password")
                        }
                        loading.value = false
                    }
                }
                else{
                    if(!loading.value) {
                        loading.value = true
                        viewModel.signIn(email, password){
                            navController.navigate(JetScreens.HomeScreen.name)
                        }
                        Log.d("Done", "SignUpScreen: $email, $password")
                        if (isValidEmail(email) && isValidPassWord(password)) {
                            Log.d("Valid", "SignUpScreen: $email, $password")
                        } else {
                            Log.d("Not Valid", "SignUpScreen: $email, $password")
                        }
                        loading.value = false
                    }
                }
            }
            SignUpOrLogin(signUp = isCreateAccount, modifier = Modifier.fillMaxWidth(), onClick = {
                isCreateAccount.value = !isCreateAccount.value
            })
            TextBetweenDivider(text = "Or continue with")
            SocialMediaButtons()
        }
    }
}

