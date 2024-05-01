package com.zm.web.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonObjectSerializer
import java.util.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class CustomSerializer : JsonObjectSerializer<Any>() {
    override fun serializeObject(value: Any, gen: JsonGenerator, serializers: SerializerProvider) {
        value.javaClass.kotlin.declaredMemberProperties.forEach { prop ->
            prop.isAccessible = true
            val field = prop.javaField
            field?.isAccessible = true
            val fieldValue = field?.get(value)
            val fieldName = prop.name.toSnakeCase()
            if (fieldValue != null) {
                gen.writeObjectField(fieldName, fieldValue)
            }
        }
        gen.writeEndObject()
    }

    private fun String.toSnakeCase(): String {
        return this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase(Locale.getDefault())
    }
}