package br.com.velhatech.core.exceptions

open class NoLoggingException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable?) : super(message, cause)
}