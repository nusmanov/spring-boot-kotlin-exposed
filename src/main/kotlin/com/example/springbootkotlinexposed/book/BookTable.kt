package com.example.springbootkotlinexposed.book

import org.jetbrains.exposed.dao.id.IntIdTable

object BookTable : IntIdTable(name = "book", columnName = "id") {
    val title = varchar("title", length = 200)
    val author = integer("author").nullable()
}