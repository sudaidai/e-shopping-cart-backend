package com.zm.web.utils

import com.zm.web.constant.TimeConstants
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object JwtTokenUtils {
    private const val KEY_ALGORITHM = "PBKDF2WithHmacSHA512"
    private const val JWT_TOKEN_VALIDITY = TimeConstants.DAY_MILLIS

    fun generateToken(subject: String, claims: Map<String, Any>, key: String): String =
        Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .addClaims(claims)
            .signWith(convertToSecretKey(key), SignatureAlgorithm.HS512)
            .compact()

    fun validateToken(token: String, key: String): Boolean = !isTokenExpired(token, key)

    fun getAllClaimsFromToken(token: String, key: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(convertToSecretKey(key))
            .build()
            .parseClaimsJws(token)
            .body

    private fun <T> getClaimFromToken(token: String, key: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token, key)
        return claimsResolver(claims)
    }

    private fun convertToSecretKey(key: String): SecretKey {
        val hash = HashUtils.sha512(key)
        val keyCharArray = hash.toCharArray()

        return try {
            val keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM)
            val keySpec = PBEKeySpec(keyCharArray, ByteArray(16), 65536, 512)
            val secretKey = keyFactory.generateSecret(keySpec)
            SecretKeySpec(secretKey.encoded, Keys.hmacShaKeyFor(secretKey.encoded).algorithm)
        } catch (e: Exception) {
            throw RuntimeException("Error generating secret key from the given key: $key", e)
        }
    }

    private fun getExpirationDateFromToken(token: String, key: String): Date =
        getClaimFromToken(token, key, Claims::getExpiration)

    private fun isTokenExpired(token: String, key: String): Boolean =
        getExpirationDateFromToken(token, key).before(Date())
}
