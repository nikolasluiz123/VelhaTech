package br.com.velha.tech.core.validation

class FieldValidationError<FIELD>(
    val field: Enum<FIELD>?,
    message: String,
): ValidationError(message) where FIELD : Enum<FIELD>

open class ValidationError(
    val message: String,
)