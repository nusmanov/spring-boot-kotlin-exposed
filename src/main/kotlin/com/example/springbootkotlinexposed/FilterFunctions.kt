package com.example.springbootkotlinexposed

import com.example.springbootkotlinexposed.book.BookTable
import com.example.springbootkotlinexposed.person.PersonTable
import org.jetbrains.exposed.sql.*

fun selectAllBooksForAuthorAndForItsChildrenFilter(query: Query): Query {

    val contactChildren: Alias<PersonTable> = PersonTable.alias("contactChildren")


    val contactsWithKidssss: QueryAlias = PersonTable
        .leftJoin(
            contactChildren, { PersonTable.id },
            { contactChildren[PersonTable.parentId] })
        // .slice(PersonTable.id, PersonTable.parentId)
        .selectAll()
        .alias("xyz")

    return query

        .adjustColumnSet {
            leftJoin(
                contactsWithKidssss,
                { BookTable.author },
                { contactsWithKidssss[PersonTable.id] }

            )
        }

        .adjustSlice { slice(fields) }

}
