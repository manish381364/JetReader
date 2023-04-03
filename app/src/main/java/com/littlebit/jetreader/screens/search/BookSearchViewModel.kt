package com.littlebit.jetreader.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlebit.jetreader.model.Item
import com.littlebit.jetreader.repository.BookRepository
import com.littlebit.jetreader.data.DataOrException
import com.littlebit.jetreader.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class BookSearchViewModel @Inject constructor(private val repository: BookRepository) :
//    ViewModel() {
//    var listOfBooks: MutableState<DataOrException<List<Item>, Boolean, Exception>> =
//        mutableStateOf(DataOrException(null, loading = true, exception = Exception("")))
//
//    init {
//        Log.d("INIT", "Searching Books: Loading...")
//        searchBooks("android")
//        searchBooks("kotlin")
//        Log.d("INIT", "Searching Books: Loaded")
//
//    }
//
//    fun searchBooks(query: String) {
//        viewModelScope.launch {
//            if (query.isEmpty()) return@launch
//            listOfBooks.value.loading = true
//            listOfBooks.value = repository.getBooks(query)
//            Log.d("SEARCH $query", "searchBooks: ${listOfBooks.value.data.toString()}")
//            if (listOfBooks.value.data.toString().isNotEmpty()) {
//                listOfBooks.value.loading = false
//                Log.d("SEARCH $query", "searchBooks: ${listOfBooks.value.data.toString()}")
//            }
//        }
//    }
//}
@HiltViewModel
class BooksSearchViewModel @Inject constructor(private val repository: BookRepository)
    : ViewModel() {
    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {

            if (query.isEmpty()){
                return@launch
            }
            try {
                when(val response = repository.getBooks(query)) {
                    is Resource.Success -> {
                        list = response.data!!
                        if (list.isNotEmpty()){
                            isLoading = false
                            Log.d("Response", "searchBooks:  ${list.toString()}")
                        }
                    }
                    is Resource.Error -> {
                        isLoading = false
                        Log.e("Network", "searchBooks: Failed getting books ", )
                    }
                    else -> {isLoading = false}
                }

            }catch (exception: Exception){
                isLoading = false
                Log.d("Network", "searchBooks: ${exception.message.toString()}")
            }

        }


    }


}