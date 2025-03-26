package br.com.velha.tech.firebase.models

import br.com.velha.tech.core.extensions.defaultGSon
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

abstract class FirestoreDocument {

    fun toMap(): Map<String, Any?> {
        val gson = GsonBuilder().defaultGSon()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any?>>() {}.type)
    }
}