package edu.pablogj0397.add_1evaluacion.core.serialization

interface JsonSerializer<T> {
    fun toJson(obj: T): String
    fun fromJson(json: String): T
}
