package com.zm.web.utils

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeUtils {
    fun formatInstant(instant: Instant?): String {
        if (instant == null) return ""

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneOffset.UTC)

        return formatter.format(instant)
    }
}
