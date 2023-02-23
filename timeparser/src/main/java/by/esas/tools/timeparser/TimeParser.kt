/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.timeparser

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeParser {

    const val serverRegex: String = "^[\\d]{4}-[\\d]{2}-[\\d]{2}[ T][\\d]{2}:[\\d]{2}:[\\d]{2}.[\\d]+[Z]?\$"
    const val serverDatePattern: String = "yyyy-MM-dd HH:mm:ss.ss"

    const val serverShortRegex: String = "^[\\d]{4}-[\\d]{2}-[\\d]{2}[ T][\\d]{2}:[\\d]{2}:[\\d]{2}\$"
    const val serverShortDatePattern: String = "yyyy-MM-dd HH:mm:ss"

    const val localDateRegex: String = "^[\\d]{2}.[\\d]{2}.[\\d]{4}\$"
    const val simpleDatePattern: String = "dd.MM.yyyy"

    const val localDateTimeRegex: String = "^[\\d]{2}.[\\d]{2}.[\\d]{4}[ T][\\d]{2}:[\\d]{2}\$"
    const val simpleDateTimePattern: String = "dd.MM.yyyy HH:mm"

    const val DATE_IN_UNEXPECTED_FORMAT = "DATE_IN_UNEXPECTED_FORMAT"

    fun getDefaultPatterns(): MutableMap<String, String> = mutableMapOf(
        serverRegex to serverDatePattern,
        serverShortRegex to serverShortDatePattern,
        localDateRegex to simpleDatePattern,
        localDateTimeRegex to simpleDateTimePattern
    )

    private fun getPattern(parsable: String, patterns: Map<String, String>): String {
        val key: String = patterns.keys.find { regString ->
            parsable.contains(regString.toRegex())
        } ?: throw Exception(DATE_IN_UNEXPECTED_FORMAT)

        return patterns.getValue(key)
    }

    fun getDateFromUTC(
        dateInUtc: String?,
        utcPattern: String? = null,
        patterns: Map<String, String> = getDefaultPatterns()
    ): Date? {
        return dateInUtc?.let { parsable ->
            if (parsable.isNotBlank()) {
                val pattern: String = utcPattern ?: getPattern(parsable, patterns)
                if (pattern.isBlank()) return@let null
                else {
                    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")

                    val dateFormatted = parsable.replace("T", " ")
                    val tail = if (dateFormatted.contains(".")) dateFormatted.substringAfterLast(".") else ""

                    val formatString = if (tail.length > 2) {
                        dateFormatted.substring(0, dateFormatted.length - (tail.length - 2))
                    } else dateFormatted

                    try {
                        sdf.parse(formatString)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        null
                    } ?: throw Exception(DATE_IN_UNEXPECTED_FORMAT)
                }
            } else null
        }
    }

    fun getDateFromLocal(
        localDate: String?,
        localPattern: String? = null,
        patterns: Map<String, String> = getDefaultPatterns()
    ): Date? {
        return localDate?.let { parsable ->
            if (parsable.isNotBlank()) {
                val pattern: String = localPattern ?: getPattern(parsable, patterns)
                if (pattern.isBlank()) return@let null
                else {
                    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                    sdf.timeZone = TimeZone.getDefault()

                    try {
                        sdf.parse(parsable)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        null
                    } ?: throw Exception(DATE_IN_UNEXPECTED_FORMAT)
                }
            } else null
        }
    }

    fun getUTCDate(date: Date?): Date {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.time = date ?: Date(System.currentTimeMillis())
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        cal.timeZone = TimeZone.getTimeZone("UTC")
        cal.set(year, month, day, 0, 0, 0)
        return cal.time
    }

    fun Date.getLocal(outPattern: String = simpleDatePattern, locale: Locale = Locale.getDefault()): String {
        val sdf = SimpleDateFormat(outPattern, locale)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(this)
    }

    fun Date.getUTC(outPattern: String = serverShortDatePattern, locale: Locale = Locale.getDefault()): String {
        val sdf = SimpleDateFormat(outPattern, locale)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.format(this)
        return date.replaceFirst(" ", "T")
    }
}