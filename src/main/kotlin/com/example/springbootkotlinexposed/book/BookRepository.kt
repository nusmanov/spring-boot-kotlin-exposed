package com.example.springbootkotlinexposed.book

import com.example.springbootkotlinexposed.applyComplexFilter
import com.example.springbootkotlinexposed.selectAllBooksForAuthorAndForItsChildrenFilter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class BookRepository {


    fun create(bookName: String, bookAuthor: Int? = null, bookCorrelationId: Int? = null): Int = transaction {
        BookTable.insertAndGetId {
            it[title] = bookName
            it[author] = bookAuthor
            it[correlationId] = bookCorrelationId
        }.value
    }

    fun getBook(id: Int): BookDto {
        return transaction {
            BookTable.select { BookTable.id eq id }.single().let { BookDto.fromResultRow(it) }
        }
    }

    fun getAll(searchFilter: SearchFilterDto = SearchFilterDto()): List<BookDto> =

        transaction {
            BookTable.selectAll()
                // .andWhere { BookTable.correlationId eq searchFilter.correlationId }
                .applyComplexFilter(searchFilter.booksFromAuthorAndHerChildren) {
                    selectAllBooksForAuthorAndForItsChildrenFilter(this)
                }
                .map(BookDto::fromResultRow)
        }
}
