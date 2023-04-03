package com.littlebit.jetreader.screens.details

import androidx.compose.runtime.mutableStateOf
import com.littlebit.jetreader.data.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlebit.jetreader.model.Item
import com.littlebit.jetreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {
    val bookId = mutableStateOf("")
    val bookResource = mutableStateOf<Resource<Item>>(Resource.Loading())
    fun getBookById(bookId: String){
        viewModelScope.launch{
            bookResource.value = Resource.Loading()
            bookResource.value = repository.getBookInfo(bookId)
        }
    }
}