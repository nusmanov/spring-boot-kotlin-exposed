package com.example.springbootkotlinexposed.person

import org.jetbrains.exposed.dao.id.IntIdTable

object PersonTable : IntIdTable(name = "person", columnName = "id") {
    val firstname = varchar("firstname", length = 200)
    val parentId = integer("parent_id").nullable()
}