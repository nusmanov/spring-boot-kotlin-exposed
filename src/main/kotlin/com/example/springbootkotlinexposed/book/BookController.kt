package com.example.springbootkotlinexposed.book

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import kotlin.random.Random

@RestController
class BookController(val service: BookService) {

    @GetMapping("/book/{id}")
    fun getBook(@PathVariable id: Int) = service.getBook(id)

    @GetMapping("/books", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBook(searchFilter: SearchFilterDto = SearchFilterDto()) = service.getBooks(searchFilter)

    @PostMapping("/book")
    fun createBook() = service.createBook("Book-" + Random(100).nextLong())

}
