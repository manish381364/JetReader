package com.littlebit.jetreader.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.littlebit.jetreader.model.JetUser
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
//    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth
    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState
    fun signUp(email: String, password: String, takeHome: () -> Unit) = viewModelScope.launch{
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        val displayName = it.result.user?.email?.split("@")?.get(0)
                        _loadingState.value = false
                        createUser(displayName)
                        takeHome()
                        Log.d("LOGIN", "signUp: Success")
                    }
                    else{
                        Log.d("LOGIN_ERROR", "signUp: ${it.exception}")
                    }
                }
        }
        catch (Ex: Exception) {
            Log.d("LOGIN_ERROR", "signUp: ${Ex.message}")
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = JetUser(userId = userId!!, displayName = displayName!!, email = auth.currentUser?.email!!, quote = "", profession = "", avatarUrl = "").toMap()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }

    fun signIn(email: String, password: String, takeHome: () -> Unit) =  viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        _loadingState.value = false
                        Log.d("LOGIN", "signUp: Success")
                        takeHome()
                    }
                    else{
                        Log.d("LOGIN_ERROR", "signUp: ${it.exception}")
                    }
                }
        }
        catch (Ex: Exception) {
            Log.d("LOGIN_ERROR", "signUp: ${Ex.message}")
        }
    }
}