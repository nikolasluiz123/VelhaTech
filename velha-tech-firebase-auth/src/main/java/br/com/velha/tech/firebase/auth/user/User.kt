package br.com.velha.tech.firebase.auth.user

data class User(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
)
