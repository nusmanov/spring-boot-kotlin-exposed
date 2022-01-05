package com.example.springbootkotlinexposed.book

import com.example.springbootkotlinexposed.applyComplexFilter
import com.example.springbootkotlinexposed.selectAllBooksForAuthorAndForItsChildrenFilter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class BookRepository {

    fun getAll(): List<BookDto> = transaction { BookTable.selectAll().map { BookDto.fromResultRow(it) } }

    fun create(bookName: String, bookAuthor: Int? = null): Int = transaction {
        BookTable.insertAndGetId {
            it[title] = bookName
            it[author] = bookAuthor
        }.value
    }

    fun getBook(id: Int): BookDto {
        return transaction {
            BookTable.select { BookTable.id eq id }.limit(1).single().let { BookDto.fromResultRow(it) }
        }
    }

    fun getAllWithSearchParameter(): List<BookDto> =

        transaction {
            BookTable.selectAll()
                .applyComplexFilter(true) { selectAllBooksForAuthorAndForItsChildrenFilter(this) }
                .map(BookDto.Companion::fromResultRow)
        }
}
