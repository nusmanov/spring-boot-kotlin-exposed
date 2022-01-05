package com.example.springbootkotlinexposed.person

import org.jetbrains.exposed.sql.ResultRow

data class PersonDto(val id: Int, val firstname: String, val parentId: Int? = null) {

    companion object {
        fun fromResultRow(resultRow: ResultRow) =
            PersonDto(resultRow[PersonTable.id].value, resultRow[PersonTable.firstname], resultRow[PersonTable.parentId])
    }
}
