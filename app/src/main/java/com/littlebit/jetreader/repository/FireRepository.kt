package com.littlebit.jetreader.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.littlebit.jetreader.data.DataOrException
import com.littlebit.jetreader.model.JetBook
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryBook: Query
) {
    suspend fun getAllBooksFromDatabase(): DataOrException<List<JetBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<JetBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(JetBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        } catch (exception: FirebaseFirestoreException) {
            dataOrException.exception = exception
        }
        return dataOrException

    }
}