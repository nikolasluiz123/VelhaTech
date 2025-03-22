package br.com.velhatech.core.extensions

import br.com.velhatech.core.adapters.LocalDateTimeTypeAdapter
import br.com.velhatech.core.adapters.LocalDateTypeAdapter
import br.com.velhatech.core.adapters.LocalTimeTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun GsonBuilder.defaultGSon(): Gson {
    return this.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .create()
}