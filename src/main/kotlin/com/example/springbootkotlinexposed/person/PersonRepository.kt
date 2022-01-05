package com.example.springbootkotlinexposed.person

import com.example.springbootkotlinexposed.book.BookTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
internal class PersonRepository {

    fun getAll(): List<PersonDto> = transaction { PersonTable.selectAll().map { PersonDto.fromResultRow(it) } }

    fun create(personFirstname: String, personParentId: Int? = null): Int = transaction {
        PersonTable.insertAndGetId {
            it[firstname] = personFirstname
            it[parentId] = personParentId
        }.value
    }
}