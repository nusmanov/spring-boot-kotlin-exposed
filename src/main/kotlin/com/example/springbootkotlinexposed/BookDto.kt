package com.example.springbootkotlinexposed

import org.jetbrains.exposed.sql.ResultRow

data class BookDto(val id: Int, val name: String) {

    companion object {
        fun fromResultRow(resultRow: ResultRow) = BookDto(resultRow[BookTable.id].value, resultRow[BookTable.name])
    }
}
