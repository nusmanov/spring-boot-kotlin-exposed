package com.example.springbootkotlinexposed.archive

import com.example.springbootkotlinexposed.book.BookTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.andWhere

abstract class FilterStrategy {
    abstract fun execute(query: Query): Query
}

data class SearchFilterObjectTypeStrategy(val valueToCompare: String) : FilterStrategy() {
    override fun execute(query: Query): Query = query.andWhere { BookTable.title eq valueToCompare }
}


// generic way
interface FilterAbstractStrategy<T> {
    fun <T> execute(query: Query): Query
}

abstract class SearchFilterStrategy<T>(val columnName: Column<T>, val colValue: T) : FilterAbstractStrategy<T> {
    override fun <T> execute(query: Query): Query = query.andWhere { columnName eq colValue }

}