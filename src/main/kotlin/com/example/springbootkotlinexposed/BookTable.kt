package com.example.springbootkotlinexposed

import org.jetbrains.exposed.dao.id.IntIdTable

object BookTable : IntIdTable() {
    val name = varchar("name", length = 200)

    val author = varchar("author", length = 200).default("authorRandom")
    val objectType = varchar("object_type", length = 200).default("Task")
}