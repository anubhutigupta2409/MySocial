package com.example.mysocial.models

data class Post (
    val text : String = "",
    val createBy : User = User(),
    val createdAt : Long = 0L,
    val likedBy : ArrayList<String> = ArrayList()

        )