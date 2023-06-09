package com.littlebit.jetreader.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class JetBook(
    @Exclude
    var id: String? = null,
    @get:PropertyName("book_id")
    @set:PropertyName("book_id")
    var bookId: String? = null,
    var title: String? = null,
    var author: String? = null,
    var description: String? = null,
    var image: String? = null,
    var previewLink: String? = null,
    var publishedDate: String? = null,
    var publisher: String? = null,
    var pageCount: String? = null,
    var categories: String? = null,
    var language: String? = null,
    var isbn10: String? = null,
    var isbn13: String? = null,
    var rating: Int? = null,
    var price: String? = null,
    var currency: String? = null,
    var buyLink: String? = null,
    @get:PropertyName("favorite")
    @set:PropertyName("favorite")
    var isFavorite: Boolean = false,
    var notes: String? = null,
    val isRead: Boolean? = null,
    val userId: String? = null,
    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var startedReading: String? = null,
    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finishedReading: String? = null,
)
