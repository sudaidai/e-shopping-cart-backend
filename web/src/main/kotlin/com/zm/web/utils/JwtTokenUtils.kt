package com.zm.web.utils

import com.zm.web.constant.TimeConstants
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
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
    private const val JWT_TOKEN_VALIDITY = TimeConstants.DAY_MILLIS;

    fun generateToken(subject: String, claims: Map<String, Any>, key: String): String {
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .addClaims(claims)
            .signWith(convertToSecretKey(key), SignatureAlgorithm.HS512)
            .compact()
    }

    fun validateToken(token: String, key: String): Boolean {
        return !isTokenExpired(token, key)
    }

    private fun <T> getClaimFromToken(token: String, key: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token, key)
        return claimsResolver.invoke(claims)
    }

    private fun getAllClaimsFromToken(token: String, key: String): Claims {
        val signingKey = convertToSecretKey(key)

        val parser: JwtParser = Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()

        return parser.parseClaimsJws(token).body
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
            throw RuntimeException(e)
        }
    }

    private fun getExpirationDateFromToken(token: String, key: String): Date {
        return getClaimFromToken(token, key) { it.expiration }
    }

    private fun isTokenExpired(token: String, key: String): Boolean {
        val expiration = getExpirationDateFromToken(token, key)
        return expiration.before(Date())
    }
}
