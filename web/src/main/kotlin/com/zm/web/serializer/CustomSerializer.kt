package com.zm.web.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonObjectSerializer
import java.util.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

class CustomSerializer : JsonObjectSerializer<Any>() {
    override fun serializeObject(value: Any, gen: JsonGenerator, serializers: SerializerProvider) {
        value.javaClass.kotlin.declaredMemberProperties.forEach { prop ->
            val fieldName = prop.name.toSnakeCase()
            val fieldValue = prop.javaField?.get(value)
            gen.writeObjectField(fieldName, fieldValue)
        }
    }

    private fun String.toSnakeCase(): String {
        return this.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase(Locale.getDefault())
    }
}