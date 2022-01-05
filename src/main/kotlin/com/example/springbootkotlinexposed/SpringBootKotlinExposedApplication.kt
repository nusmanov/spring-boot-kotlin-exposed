package com.example.springbootkotlinexposed

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootKotlinExposedApplication

fun main(args: Array<String>) {
	runApplication<SpringBootKotlinExposedApplication>(*args)
}
