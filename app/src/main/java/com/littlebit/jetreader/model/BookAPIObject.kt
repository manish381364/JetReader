package com.littlebit.jetreader.model

data class BookAPIObject(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)
