package net.callmeike.android.pawk.bandalorium

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class TimeSeries(val points: List<Point>, val attr: Attr)

enum class Tolerance { CRITICAL, IMPORTANT, REGULAR }

data class Attr(val name: String, val tolerance: Tolerance)

data class Point(
    val serial: String,
    val date: LocalDateTime,
    val value: Double
)

fun createCsv(timeSeries: List<TimeSeries>): String {
    val distinctAttrs = timeSeries
        .distinctBy { it.attr }
        .map { it.attr }
        .sortedBy { it.name }

    val csvHeader = "date;serial;" + distinctAttrs.joinToString(";") { it.name } + "\n"

    data class PointWithAttr(val point: Point, val attr: Attr)

    // original example
    val pointsWithAttrs1 = timeSeries.flatMap {
        it.points.map { point -> PointWithAttr(point, it.attr) }
    }

    // filtered to critical and important
    val importantPointsWithAttrs = timeSeries.filter {
        it.attr.tolerance == Tolerance.CRITICAL || it.attr.tolerance == Tolerance.IMPORTANT
    }.map { series ->
        series.points.map { point -> PointWithAttr(point, series.attr) }
    }.flatten()

    val rows = importantPointsWithAttrs.groupBy { it.point.date }  // <1>
        .toSortedMap()                                    // <2>
        .map { (date, ptsWithAttrs1) ->                   // <3>
            ptsWithAttrs1
                .groupBy { it.point.serial }
                .map { (serial, ptsWithAttrs2) ->         // <4>
                    listOf(date.format(DateTimeFormatter.ISO_LOCAL_DATE), serial) +
                            distinctAttrs.map { attr ->
                                val value = ptsWithAttrs2.firstOrNull { it.attr == attr }
                                value?.point?.value?.toString() ?: ""
                            }
                }.joinToString(separator = "") {          // <5>
                    it.joinToString(separator = ";", postfix = "\n")
                }
        }.joinToString(separator = "")

    return csvHeader + rows
}
