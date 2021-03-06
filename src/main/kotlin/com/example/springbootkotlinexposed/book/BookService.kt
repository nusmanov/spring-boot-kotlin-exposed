package com.example.springbootkotlinexposed.book

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(val repository: BookRepository) {

    fun getBook(id: Int): BookDto = repository.getBook(id)
    fun getBooks(searchFilter: SearchFilterDto = SearchFilterDto()): List<BookDto> = repository.getAll(searchFilter)

    @Transactional
    fun createBook(title: String, author: Int? = null): Int = repository.create(title, author)

}