package com.example.springbootkotlinexposed.book

import org.jetbrains.exposed.sql.ResultRow

data class BookDto(val id: Int, val title: String, val author: Int?) {

    companion object {
        fun fromResultRow(resultRow: ResultRow) =
            BookDto(resultRow[BookTable.id].value, resultRow[BookTable.title], resultRow[BookTable.author])
    }
}
