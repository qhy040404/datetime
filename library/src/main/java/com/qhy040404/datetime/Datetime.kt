package com.qhy040404.datetime

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Datetime Class
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class Datetime : Comparable<Datetime> {
    val year: Int
    val month: Int
    val day: Int
    val hour: Int
    val minute: Int
    val second: Int
    val nanosecond: Int

    constructor(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
        nanosecond: Int,
    ) {
        this.year = year
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
        this.second = second
        this.nanosecond = nanosecond
    }

    constructor(hour: Int, minute: Int, second: Int, nanosecond: Int) {
        val now = LocalDate.now()
        this.year = now.year
        this.month = now.monthValue
        this.day = now.dayOfMonth
        this.hour = hour
        this.minute = minute
        this.second = second
        this.nanosecond = nanosecond
    }

    constructor(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
        this.hour = 0
        this.minute = 0
        this.second = 0
        this.nanosecond = 0
    }

    constructor(localDateTime: LocalDateTime) {
        this.year = localDateTime.year
        this.month = localDateTime.monthValue
        this.day = localDateTime.dayOfMonth
        this.hour = localDateTime.hour
        this.minute = localDateTime.minute
        this.second = localDateTime.second
        this.nanosecond = localDateTime.nano
    }

    /**
     * Get Instant class of a datetime
     * @return Instant
     */
    fun toInstant(): Instant {
        return LocalDateTime.of(year, month, day, hour, minute, second, nanosecond)
            .atZone(ZoneId.systemDefault()).toInstant()
    }

    /**
     * Get timestamp of a datetime
     * @return Long
     */
    fun toTimestamp(): Long {
        return toInstant().toEpochMilli()
    }

    private fun toLocalDateTime(): LocalDateTime {
        return LocalDateTime.of(year, month, day, hour, minute, second, nanosecond)
    }

    /**
     * Plus a part of Datetime and return a copy modified
     * @param n the number to be plus
     * @param part The part defined in DatetimePart
     * @return Datetime
     */
    fun plus(n: Int, part: DatetimePart): Datetime {
        return when (part) {
            DatetimePart.YEAR -> toLocalDateTime().plusYears(n.toLong()).toDatetime()
            DatetimePart.MONTH -> toLocalDateTime().plusMonths(n.toLong()).toDatetime()
            DatetimePart.DAY -> toLocalDateTime().plusDays(n.toLong()).toDatetime()
            DatetimePart.HOUR -> toLocalDateTime().plusHours(n.toLong()).toDatetime()
            DatetimePart.MINUTE -> toLocalDateTime().plusMinutes(n.toLong()).toDatetime()
            DatetimePart.SECOND -> toLocalDateTime().plusSeconds(n.toLong()).toDatetime()
            DatetimePart.NANOSECOND -> toLocalDateTime().plusNanos(n.toLong()).toDatetime()
        }
    }

    /**
     * Minus a part of Datetime and return a copy modified
     * @param n the number to be minus
     * @param part The part defined in DatetimePart
     * @return Datetime
     */
    fun minus(n: Int, part: DatetimePart): Datetime {
        return when (part) {
            DatetimePart.YEAR -> toLocalDateTime().minusYears(n.toLong()).toDatetime()
            DatetimePart.MONTH -> toLocalDateTime().minusMonths(n.toLong()).toDatetime()
            DatetimePart.DAY -> toLocalDateTime().minusDays(n.toLong()).toDatetime()
            DatetimePart.HOUR -> toLocalDateTime().minusHours(n.toLong()).toDatetime()
            DatetimePart.MINUTE -> toLocalDateTime().minusMinutes(n.toLong()).toDatetime()
            DatetimePart.SECOND -> toLocalDateTime().minusSeconds(n.toLong()).toDatetime()
            DatetimePart.NANOSECOND -> toLocalDateTime().minusNanos(n.toLong()).toDatetime()
        }
    }

    override fun equals(other: Any?): Boolean =
        (this === other) || (other is Datetime && this.toTimestamp() == other.toTimestamp())

    override fun compareTo(other: Datetime) = this.toTimestamp().compareTo(other.toTimestamp())

    /**
     * Return a time string
     * @return yyyy-MM-ddTHH:mm:ss
     */
    override fun toString(): String {
        return "${year.one2two()}-${month.one2two()}-${day.one2two()}T${hour.one2two()}:${minute.one2two()}:${second.one2two()}"
    }

    private fun Int.one2two(): String {
        return if (this >= 10) {
            "$this"
        } else {
            "0$this"
        }
    }

    override fun hashCode(): Int =
        LocalDateTime.of(year, month, day, hour, minute, second, nanosecond).hashCode()

    companion object {
        /**
         * Parse string to datetime
         *
         * Accept:
         *
         * "2022-11-20T08:12:13Z"
         * "2022-11-20T08:12:13"
         *
         * "2022-11-20 08:12:13"
         * "2022.11.20 08:12:13"
         *
         * "2022-11-20 08:12"
         * "2022.11.20 08:12"
         * @param origStr
         * @return Datetime
         */
        fun parse(origStr: String): Datetime {
            if (origStr.split("T").size == 2) {
                val date = origStr.split("T").first().split("-")
                val time = origStr.split("T").last().trimEnd('Z').split(":")
                return Datetime(
                    date[0].toInt(),
                    date[1].toInt(),
                    date[2].toInt(),
                    time[0].toInt(),
                    time[1].toInt(),
                    time[2].toInt(),
                    0
                )
            } else {
                val date = origStr.split(" ").first()
                val time = origStr.split(" ").last()

                val dateList = if (date.split("-").size == 3) {
                    date.split("-")
                } else if (date.split(".").size == 3) {
                    date.split(".")
                } else {
                    throw IllegalArgumentException("Illegal delimiter or size in date")
                }

                val timeList = time.split(":")

                return when (timeList.size) {
                    2 -> Datetime(
                        dateList[0].toInt(),
                        dateList[1].toInt(),
                        dateList[2].toInt(),
                        timeList[0].toInt(),
                        timeList[1].toInt(),
                        0,
                        0
                    )

                    3 -> Datetime(
                        dateList[0].toInt(),
                        dateList[1].toInt(),
                        dateList[2].toInt(),
                        timeList[0].toInt(),
                        timeList[1].toInt(),
                        timeList[2].toInt(),
                        0
                    )

                    else -> throw IllegalArgumentException("Illegal delimiter in time")
                }
            }
        }

        /**
         * Parse a Instant class to datetime
         * @return Datetime
         */
        fun fromInstant(instant: Instant): Datetime {
            return LocalDateTime.ofInstant(
                instant,
                ZoneId.systemDefault()
            ).toDatetime()
        }

        /**
         * Parse a timestamp to datetime
         * @return Datetime
         */
        fun fromTimestamp(timestamp: Long): Datetime {
            return fromInstant(Instant.ofEpochMilli(timestamp))
        }

        /**
         * Get a datetime of now
         * @return Datetime
         */
        fun now() = Datetime(LocalDateTime.now())

        /**
         * Convert a string to datetime
         * @return Datetime
         */
        fun String.toDatetime() = parse(this)

        /**
         * Convert a Instant to datetime
         * @return Datetime
         */
        fun Instant.toDatetime() = fromInstant(this)

        /**
         * Convert a LocalDateTime to datetime
         * @return Datetime
         */
        fun LocalDateTime.toDatetime() = Datetime(this)
    }
}