package com.littlebit.jetreader.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlebit.jetreader.data.DataOrException
import com.littlebit.jetreader.model.JetBook
import com.littlebit.jetreader.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository) :
    ViewModel() {
    val data: MutableState<DataOrException<List<JetBook>, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, loading = true, exception = Exception("")))

    init {
        getAllBooksFromDataBase()
    }

    private fun getAllBooksFromDataBase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooksFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
        Log.d("DATA FROM FIRE_STORE", "getAllBooksFromDataBase:  ${data.value.data.toString()}")
    }
}