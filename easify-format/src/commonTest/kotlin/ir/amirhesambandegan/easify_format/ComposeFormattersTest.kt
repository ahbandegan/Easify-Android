package ir.amirhesambandegan.easify_format

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeFormattersTest {

    @Test
    fun highlightMatchHighlightsAllOccurrences() {
        val annotated = "Kotlin Compose and kotlin Multiplatform".highlightMatch("kotlin", Color.Red)
        assertEquals("Kotlin Compose and kotlin Multiplatform", annotated.text)
        assertEquals(2, annotated.spanStyles.size)
        assertEquals(0, annotated.spanStyles[0].start)
        assertEquals(6, annotated.spanStyles[0].end)
        assertEquals(19, annotated.spanStyles[1].start)
        assertEquals(25, annotated.spanStyles[1].end)
    }

    @Test
    fun linkifiedAnnotatedStringHandlesUrlsEmailsAndMentionsWithoutOverlap() {
        val sample = "Visit https://easify.dev or mail dev@easify.dev and ping @easify"
        val annotated = sample.toLinkifiedAnnotatedString()
        val urls = annotated.getStringAnnotations("URL", 0, annotated.length).map { it.item }
        assertEquals(listOf("https://easify.dev", "dev@easify.dev", "@easify"), urls)
    }
}
