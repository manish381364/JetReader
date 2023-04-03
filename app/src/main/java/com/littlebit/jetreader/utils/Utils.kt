package com.littlebit.jetreader.utils

import android.util.Patterns

fun isValidPassWord(password: String): Boolean {
    return password.length >= 8
            && password.contains(Regex("[a-zA-Z]"))
            && password.contains(Regex("[0-9]") )
            && password.contains(Regex("[!@#$%^&*(),.?\":{}|<>]"))
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}