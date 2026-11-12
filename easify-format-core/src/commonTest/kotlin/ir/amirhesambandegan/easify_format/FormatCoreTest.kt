package ir.amirhesambandegan.easify_format

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatCoreTest {
    @Test
    fun compactFormat_handlesPositiveNegativeAndMin() {
        assertEquals("999", 999L.toCompactFormat())
        assertEquals("1.2K", 1200L.toCompactFormat())
        assertEquals("-1.5M", (-1_500_000L).toCompactFormat())
        assertEquals("1K", 1000L.toCompactFormat())
    }

    @Test
    fun masking_handlesEdgeCasesSafely() {
        assertEquals("@domain.com", "@domain.com".maskEmail())
        assertEquals("joh***@example.com", "john.doe@example.com".maskEmail())
        assertEquals("0912***6789", "۰۹۱۲۳۴۵۶۷۸۹".maskPhone())
        assertEquals("6037 **** **** 1234", "6037991122331234".maskCard())
        assertEquals("IR****01", "IR123401".maskMiddle(2, 2))
    }

    @Test
    fun htmlCasingExtractorsAndTime_workPureKmp() {
        assertEquals("Hello & World", "<p>Hello &amp; <b>World</b></p>".stripHtmlTags())
        assertEquals("JD", "john   doe".toInitials())
        assertEquals("01:02:03", 3723L.toTimerFormat())
        assertEquals("00:00", (-500L).toTimeString())
        assertEquals("1h 2m 3s", 3723L.toReadableDuration())
        assertEquals("1.50 MB", (1024L * 1024L * 3L / 2L).toReadableFileSize())
        assertEquals(listOf("https://easify.ir/docs"), "See https://easify.ir/docs now".extractUrls())
        assertEquals(listOf("dev@easify.ir"), "Contact dev@easify.ir please".extractEmails())
        assertEquals(4, "  one   two three\nfour ".wordCount())
    }
}
