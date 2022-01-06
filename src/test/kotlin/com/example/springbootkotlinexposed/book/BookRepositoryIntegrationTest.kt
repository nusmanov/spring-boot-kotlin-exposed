package com.example.springbootkotlinexposed.book

import com.example.springbootkotlinexposed.person.PersonRepository
import com.example.springbootkotlinexposed.person.PersonTable
import org.assertj.core.api.Assertions
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRepositoryIntegrationTest {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var personRepository: PersonRepository

    companion object {

        @Container
        val container = MySQLContainer<Nothing>("mysql:5").apply {
            withDatabaseName("test")
            withUsername("root")
            withPassword("123")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }
    }


    @BeforeEach
    fun beforeEach() {
        transaction {
            SchemaUtils.create(BookTable, PersonTable)
        }
    }

    @Test
    @Transactional
    fun test_search_parameters_left_join() {

        // prepare
        val personId1 = personRepository.create("Tom")
        val personChild = personRepository.create("Tom", personId1)

        val correlationId = 123
        bookRepository.create("Java Book", personId1, correlationId)
        bookRepository.create("Kotlin Book", personId1, correlationId)

        bookRepository.create("This Book", personChild, correlationId)

        bookRepository.create("Boring Book", 888, 999)
        // act

        val books =
            bookRepository.getAll(SearchFilterDto(correlationId = correlationId, booksFromAuthorAndHerChildren = true))

        // verify
        Assertions.assertThat(books).hasSize(3)
    }

    @Test
    @Transactional
    fun test_create_books() {
        // prepare
        bookRepository.create("Java Book2", 1)
        bookRepository.create("Kotlin Book3", 1)

        val contactId1 = personRepository.create("Tom")
        personRepository.create("Tom", contactId1)
        // act

        // verify
        Assertions.assertThat(bookRepository.getAll(SearchFilterDto())).hasSize(2)
    }

    @Test
    @Transactional
    fun test_create_person() {

        // prepare
        val contactId1 = personRepository.create("Tom")
        personRepository.create("Tom", contactId1)
        // act

        // verify
        Assertions.assertThat(personRepository.getAll()).hasSize(2)
    }

}