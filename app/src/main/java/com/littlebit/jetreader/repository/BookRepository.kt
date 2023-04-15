package com.littlebit.jetreader.repository

import android.util.Log
import com.littlebit.jetreader.data.Resource
import com.littlebit.jetreader.model.Item
import com.littlebit.jetreader.network.BooksApi
import javax.inject.Inject

//class BookRepository @Inject constructor(private val api: BooksApi) {
//
//    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
//    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()
//    suspend fun getBooks(query: String): DataOrException<List<Item>, Boolean, Exception>{
//         try{
//            dataOrException.loading = true
//            dataOrException.data = api.getAllBooks(query).items
//            if(dataOrException.data!!.isNotEmpty()){
//                Log.d("GETBOOKS", "getBooks:  ${dataOrException.data.toString()}")
//                dataOrException.loading = false
//            }else{
//                dataOrException.exception = Exception("No books found")
//            }
//        } catch (e: Exception){
//            dataOrException.exception = e
//        }
//        return dataOrException
//    }
//
//    suspend fun getBook(bookId: String): DataOrException<Item, Boolean, Exception>{
//        val response = try{
//            bookInfoDataOrException.loading = true
//            bookInfoDataOrException.data = api.getBookById(bookId)
//            if(bookInfoDataOrException.data.toString().isNotEmpty()){
//                bookInfoDataOrException.loading = false
//            }
//            bookInfoDataOrException.loading = false
//            bookInfoDataOrException
//        } catch (e: Exception){
//            bookInfoDataOrException.exception = e
//            bookInfoDataOrException
//        }
//        return response
//    }
//}
class BookRepository @Inject constructor(private val api: BooksApi) {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
        return try {
            Resource.Loading(data = true)

            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)

        } catch (exception: Exception) {
            Log.d("SEARCH_ERROR", "getBooks: ${exception.message.toString()}")
            Resource.Error(message = exception.message.toString())
        }

    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookById(bookId)
        }catch (exception: Exception){
            Resource.Loading(data = false)
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }


}