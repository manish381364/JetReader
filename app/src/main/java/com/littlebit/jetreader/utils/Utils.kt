package com.littlebit.jetreader.utils

import android.icu.text.DateFormat
import android.util.Patterns
import com.google.firebase.Timestamp

fun isValidPassWord(password: String): Boolean {
    return password.length >= 8
            && password.contains(Regex("[a-zA-Z]"))
            && password.contains(Regex("[0-9]") )
            && password.contains(Regex("[!@#$%^&*(),.?\":{}|<>]"))
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun formatDate(timestamp: String): String {
    val stamp = Timestamp(timestamp.split(",")[0].removePrefix("Timestamp(seconds=").toLong(), timestamp.split(",")[1].removePrefix(" nanoseconds=").removeSuffix(")").toInt())
    return DateFormat.getDateInstance().format(stamp.toDate()).toString()
}