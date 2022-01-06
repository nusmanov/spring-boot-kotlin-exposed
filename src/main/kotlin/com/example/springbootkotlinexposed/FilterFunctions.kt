package com.example.springbootkotlinexposed

import com.example.springbootkotlinexposed.book.BookTable
import com.example.springbootkotlinexposed.person.PersonTable
import org.jetbrains.exposed.sql.*

fun selectAllBooksForAuthorAndForItsChildrenFilter(query: Query): Query {

    val personTable: Alias<PersonTable> = PersonTable.alias("personTable")

    val personWithChildren: QueryAlias = PersonTable
        .leftJoin(
            personTable, { PersonTable.id },
            { personTable[PersonTable.parentId] })
        .slice(PersonTable.id, PersonTable.parentId)
        .selectAll()
        .alias("xyz")

    return query

        .adjustColumnSet {
            leftJoin(
                personWithChildren,
                { BookTable.author },
                { personWithChildren[PersonTable.id] }

            )
        }
        .adjustSlice { slice(fields) }

}
