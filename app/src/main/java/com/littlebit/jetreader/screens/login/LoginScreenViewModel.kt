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
    private val auth: FirebaseAuth = Firebase.auth
    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState
    fun signUp(email: String, password: String, takeHome: () -> Unit) = viewModelScope.launch{
        _loadingState.value = true
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
                        _loadingState.value = false
                        Log.d("LOGIN_ERROR", "signUp: ${it.exception}")
                    }
                }
        }
        catch (Ex: Exception) {
            _loadingState.value = false
            Log.d("LOGIN_ERROR", "signUp: ${Ex.message}")
        }
    }

    private fun createUser(displayName: String?) {
        _loadingState.value = true
        val userId = auth.currentUser?.uid
        val user = JetUser(userId = userId!!, displayName = displayName!!, email = auth.currentUser?.email!!, quote = "", profession = "", avatarUrl = "").toMap()
        FirebaseFirestore.getInstance().collection("users")
            .add(user).addOnSuccessListener {
                _loadingState.value = false
                Log.d("LOGIN", "signUp: Success")
            }.addOnFailureListener {
                _loadingState.value = false
                Log.d("LOGIN_ERROR", "signUp: ${it.message}")
            }
    }

    fun signIn(email: String, password: String, takeHome: () -> Unit) =  viewModelScope.launch{
        _loadingState.value = true
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        _loadingState.value = false
                        Log.d("LOGIN", "signUp: Success")
                        takeHome()
                    }
                    else{
                        _loadingState.value = false
                        Log.d("LOGIN_ERROR", "signUp: ${it.exception}")
                    }
                }
        }
        catch (Ex: Exception) {
            _loadingState.value = false
            Log.d("LOGIN_ERROR", "signUp: ${Ex.message}")
        }
    }
}