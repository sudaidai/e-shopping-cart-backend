package com.zm.web.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object HashUtils {

    private val HEX_ARRAY = "0123456789ABCDEF".toByteArray(StandardCharsets.US_ASCII)

    fun md5(plainText: String): String = hash(plainText, "MD5")

    fun sha1(plainText: String): String = hash(plainText, "SHA-1")

    fun sha256(plainText: String): String = hash(plainText, "SHA-256")

    fun sha384(plainText: String): String = hash(plainText, "SHA-384")

    fun sha512(plainText: String): String = hash(plainText, "SHA-512")

    private fun hash(plainText: String, algorithm: String): String {
        val messageDigest = MessageDigest.getInstance(algorithm)
        val hashBytes = messageDigest.digest(plainText.toByteArray(StandardCharsets.UTF_8))
        return bytesToHex(hashBytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = ByteArray(bytes.size * 2)
        for (i in bytes.indices) {
            val byteValue = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = HEX_ARRAY[byteValue ushr 4]
            hexChars[i * 2 + 1] = HEX_ARRAY[byteValue and 0x0F]
        }
        return String(hexChars, StandardCharsets.UTF_8)
    }
}
