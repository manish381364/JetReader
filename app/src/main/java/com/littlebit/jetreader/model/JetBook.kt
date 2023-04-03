package com.littlebit.jetreader.model

data class JetBook(
    var id: String? = null,
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
    var rating: String? = null,
    var price: String? = null,
    var currency: String? = null,
    var buyLink: String? = null,
    var isFavourite: Boolean = false,
    var notes: String? = null,
) {

}
