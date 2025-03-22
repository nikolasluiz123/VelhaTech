package br.com.velhatech.core.security

interface IPasswordHasher {
    fun hashPassword(password: String): String
}