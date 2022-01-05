package com.example.springbootkotlinexposed

import org.jetbrains.exposed.sql.Query

fun Query.applyComplexFilter(isFilterActivated: Boolean, complexFilter: Query.() -> Query): Query {
    return if (isFilterActivated) complexFilter() else this
}

