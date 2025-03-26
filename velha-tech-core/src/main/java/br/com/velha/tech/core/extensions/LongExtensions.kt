package br.com.velha.tech.core.extensions

import br.com.velha.tech.core.enums.EnumDateTimePatterns
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun Long.toLocalDateFormattedOnlyNumbers(enumDateTimePatterns: EnumDateTimePatterns): String {
    return toLocalDate().format(enumDateTimePatterns).replace("/", "")
}