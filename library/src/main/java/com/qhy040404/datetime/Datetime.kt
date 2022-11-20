package com.qhy040404.datetime

import java.time.LocalDate
import java.time.LocalDateTime as jvmDateTime

/**
 * Datetime Class
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class Datetime {
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

    constructor(jvmDatetime: jvmDateTime) {
        this.year = jvmDatetime.year
        this.month = jvmDatetime.monthValue
        this.day = jvmDatetime.dayOfMonth
        this.hour = jvmDatetime.hour
        this.minute = jvmDatetime.minute
        this.second = jvmDatetime.second
        this.nanosecond = jvmDatetime.nano
    }

    /**
     * Check if a datetime is after another one
     * @param datetime the "another" datetime
     * @return Boolean
     */
    fun isAfter(datetime: Datetime): Boolean {
        return isBiggerThan(year, datetime.year) {
            isBiggerThan(month, datetime.month) {
                isBiggerThan(day, datetime.day) {
                    isBiggerThan(hour, datetime.hour) {
                        isBiggerThan(minute, datetime.minute) {
                            isBiggerThan(second, datetime.second) {
                                isBiggerThan(nanosecond, datetime.nanosecond) {
                                    false
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if a datetime is before another one
     * @param datetime the "another" datetime
     * @return Boolean
     */
    fun isBefore(datetime: Datetime): Boolean {
        return !isAfter(datetime)
    }

    private fun isBiggerThan(a: Int, b: Int, foo: () -> Boolean): Boolean {
        return if (a > b) {
            true
        } else if (a < b) {
            false
        } else {
            foo()
        }
    }

    companion object {
        /**
         * Parse string to datetime
         *
         * Accept:
         *
         * "2022-11-20T08:12:13Z"
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
                return Datetime(date[0].toInt(),
                    date[1].toInt(),
                    date[2].toInt(),
                    time[0].toInt(),
                    time[1].toInt(),
                    time[2].toInt(),
                    0)
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
                    2 -> Datetime(dateList[0].toInt(),
                        dateList[1].toInt(),
                        dateList[2].toInt(),
                        timeList[0].toInt(),
                        timeList[1].toInt(),
                        0,
                        0)
                    3 -> Datetime(dateList[0].toInt(),
                        dateList[1].toInt(),
                        dateList[2].toInt(),
                        timeList[0].toInt(),
                        timeList[1].toInt(),
                        timeList[2].toInt(),
                        0)
                    else -> throw IllegalArgumentException("Illegal delimiter in time")
                }
            }
        }

        /**
         * Convert a string into datetime
         * @return Datetime
         */
        fun String.toDateTime() = parse(this)

        fun jvmDateTime.toDateTime() = Datetime(this)
    }
}