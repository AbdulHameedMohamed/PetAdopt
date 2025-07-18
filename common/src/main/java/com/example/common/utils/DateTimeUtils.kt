package com.example.common.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateTimeUtils {
    fun parse(dateTimeString: String): LocalDateTime = try {
        LocalDateTime.parse(dateTimeString)
    } catch (e: Exception) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        LocalDateTime.parse(dateTimeString, dateFormatter)
    }
}