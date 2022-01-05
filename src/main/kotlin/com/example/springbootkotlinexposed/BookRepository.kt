package com.example.springbootkotlinexposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class BookRepository {

    fun getAll(): List<BookDto> = transaction { BookTable.selectAll().map { BookDto.fromResultRow(it) } }

    fun create(bookName: String): Int = transaction { BookTable.insertAndGetId { it[name] = bookName }.value }

    fun getBook(id: Int): BookDto {
        return transaction {
            BookTable.select { BookTable.id eq id }.limit(1).single().let { BookDto.fromResultRow(it) }
        }
    }

    fun fireToDb(filterStrategies: HashSet<FilterStrategy>): List<BookDto> =

            transaction {

                // search filter + pagination
                var baseQuery: Query = BookTable.selectAll()

                for (strategy in filterStrategies) {
                    baseQuery = strategy.execute(baseQuery)
                }
                // map to BO
                baseQuery.map { BookDto.fromResultRow(it) }
            }

    fun simulateParsedAndConvertedFilters(): List<BookDto> {
        val filterStrategies = hashSetOf(
                SearchFilterBookNameStrategy("tom"),
                SearchFilterObjectTypeStrategy("Task"),
                PaginationStrategy(0, 50)
        )

        return fireToDb(filterStrategies)
    }
}


abstract class FilterStrategy {
    abstract fun execute(query: Query): Query
}

data class SearchFilterObjectTypeStrategy(val valueToCompare: String) : FilterStrategy() {
    override fun execute(query: Query): Query = query.andWhere { BookTable.objectType eq valueToCompare }
}

data class SearchFilterBookNameStrategy(val valueToCompare: String) : FilterStrategy() {
    override fun execute(query: Query): Query = query.andWhere { BookTable.name eq valueToCompare }
}

data class PaginationStrategy(val offset: Long, val limit: Int) : FilterStrategy() {
    override fun execute(query: Query): Query = query.limit(n = limit, offset = offset)
}


// generic way - elegant but not readable
interface FilterAbstractStrategy<T> {
    fun <T> execute(query: Query): Query
}

abstract class SearchFilterStrategy<T>(val columnName: Column<T>, val colValue: T) : FilterAbstractStrategy<T> {
    override fun <T> execute(query: Query): Query = query.andWhere { columnName eq colValue }

}