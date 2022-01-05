package com.example.springbootkotlinexposed

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(val repository: BookRepository) {

    fun getBook(id: Int): BookDto = repository.getBook(id)
    fun getBooks(): List<BookDto> = repository.getAll()

    @Transactional
    fun createBook(name: String): Int = repository.create(name)

}