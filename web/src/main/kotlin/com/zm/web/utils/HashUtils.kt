package com.zm.web.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object HashUtils {

    private val HEX_ARRAY = "0123456789ABCDEF".toByteArray(StandardCharsets.US_ASCII)

    fun md5(plainText: String): String {
        return digest(plainText, "MD5")
    }

    fun sha1(plainText: String): String {
        return digest(plainText, "SHA-1")
    }

    fun sha256(plainText: String): String {
        return digest(plainText, "SHA-256")
    }

    fun sha384(plainText: String): String {
        return digest(plainText, "SHA-384")
    }

    fun sha512(plainText: String): String {
        return digest(plainText, "SHA-512")
    }

    private fun digest(plainText: String, ALGO: String): String {
        return try {
            val md = MessageDigest.getInstance(ALGO)
            val hash = md.digest(plainText.toByteArray(StandardCharsets.UTF_8))
            bytesToHex(hash)
        } catch (e: Exception) {
            throw RuntimeException("digest err >>> $plainText error: ", e)
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = ByteArray(bytes.size * 2)
        for (i in bytes.indices) {
            val x = bytes[i].toInt() and 0xFF // 0xFF in the binary is 11111111
            hexChars[i * 2] = HEX_ARRAY[x ushr 4]
            hexChars[i * 2 + 1] = HEX_ARRAY[x and 0x0F]
        }
        return String(hexChars, StandardCharsets.UTF_8)
    }
}
