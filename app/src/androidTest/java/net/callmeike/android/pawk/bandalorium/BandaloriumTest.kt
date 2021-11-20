package net.callmeike.android.pawk.bandalorium

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
class BandaloriumTest {
    @Test
    fun testCSV() {
        val dates = listOf<LocalDateTime>(
            LocalDateTime.parse("2020-07-27T15:15:00"),
            LocalDateTime.parse("2020-07-27T15:25:00"),
            LocalDateTime.parse("2020-07-27T15:35:00"),
            LocalDateTime.parse("2020-07-27T15:45:00")
        )
        val seriesExample = listOf(
            TimeSeries(
                points = listOf(
                    Point("HC11", dates[3], 15.1),
                    Point("HC12", dates[2], 15.05),
                    Point("HC13", dates[1], 15.11),
                    Point("HC14", dates[0], 15.08)
                ),
                attr = Attr("AngleOfAttack", Tolerance.CRITICAL)
            ),
            TimeSeries(
                points = listOf(
                    Point("HC11", dates[3], 0.68),
                    Point("HC12", dates[2], 0.7),
                    Point("HC13", dates[1], 0.69),
                    Point("HC14", dates[0], 0.71)
                ),
                attr = Attr("ChordLength", Tolerance.IMPORTANT)
            ),
            TimeSeries(
                points = listOf(
                    Point("HC11", dates[3], 0x2196F3.toDouble()),
                    Point("HC14", dates[0], 0x795548.toDouble())
                ),
                attr = Attr("PaintColor", Tolerance.REGULAR)
            )
        )
        val csv = createCsv(seriesExample)
        println(csv)
    }
}
