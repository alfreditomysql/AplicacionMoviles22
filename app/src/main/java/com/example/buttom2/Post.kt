package com.example.buttom2

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val category: Category,
    val author: Author,
    val comments: List<Comment>
)

data class Comment(
    val id: Int,
    val content: String,
    val author: Author
)

data class Author(
    val id: Int,
    val name: String
)

data class Category(
    val id: Int,
    val name: String
)

