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
                "SELECT ID.ID, ID.TITLE, ID.AUTHOR, xyz.ID, xyz.\"NAME\", xyz.AUTHOR, xyz.PARENT_ID, xyz.ID, xyz.\"NAME\", xyz.AUTHOR, xyz.PARENT_ID FROM ID LEFT JOIN (SELECT PERSON.ID, PERSON.\"NAME\", PERSON.AUTHOR, PERSON.PARENT_ID, contactChildren.ID, contactChildren.\"NAME\", contactChildren.AUTHOR, contactChildren.PARENT_ID FROM PERSON LEFT JOIN PERSON contactChildren ON PERSON.ID = contactChildren.PARENT_ID) xyz ON ID.AUTHOR = xyz.ID",
                query.prepareSQL(QueryBuilder(false))
            )
        }
    }
}