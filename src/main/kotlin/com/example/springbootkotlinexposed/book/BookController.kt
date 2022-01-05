package com.example.springbootkotlinexposed.book

import com.example.springbootkotlinexposed.book.BookService
import org.springframework.web.bind.annotation.*
import kotlin.random.Random

@RestController
class BookController(val service: BookService) {

    @GetMapping("/book/{id}")
    fun getBook(@PathVariable id: Int) = service.getBook(id)

    @GetMapping("/books")
    fun getBook() = service.getBooks()

    @PostMapping("/book")
    fun createBook() = service.createBook("Book-" + Random(100).nextLong())

}
