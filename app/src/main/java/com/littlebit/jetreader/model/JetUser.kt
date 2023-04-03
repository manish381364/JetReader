package com.littlebit.jetreader.model

data class JetUser(
    val userId: String,
    val displayName: String,
    val email: String,
    val quote: String,
    val profession: String,
    val avatarUrl: String){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "userId" to this.userId,
            "displayName" to this.displayName,
            "email" to this.email,
            "quote" to this.quote,
            "profession" to this.profession,
            "avatarUrl" to this.avatarUrl
        )
    }
}
