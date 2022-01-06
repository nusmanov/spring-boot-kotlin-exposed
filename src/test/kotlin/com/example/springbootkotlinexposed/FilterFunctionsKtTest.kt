package com.example.springbootkotlinexposed

import com.example.springbootkotlinexposed.book.BookTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll

internal class FilterFunctionsKtTest {
    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Test
    fun test_selectAllBooksForAuthorAndForItsChildrenFilter_OK() {
        transaction {
            // given
            val query = BookTable.selectAll().apply { selectAllBooksForAuthorAndForItsChildrenFilter(this) }
            // when
            assertEquals(
                "SELECT BOOK.ID, BOOK.TITLE, BOOK.AUTHOR, xyz.ID, xyz.FIRSTNAME, xyz.PARENT_ID, xyz.ID, xyz.FIRSTNAME, xyz.PARENT_ID FROM BOOK LEFT JOIN (SELECT PERSON.ID, PERSON.FIRSTNAME, PERSON.PARENT_ID, contactChildren.ID, contactChildren.FIRSTNAME, contactChildren.PARENT_ID FROM PERSON LEFT JOIN PERSON contactChildren ON PERSON.ID = contactChildren.PARENT_ID) xyz ON BOOK.AUTHOR = xyz.ID",
                query.prepareSQL(QueryBuilder(false))
            )
        }
    }
}