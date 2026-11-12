package ir.amirhesambandegan.easify_fintech

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FintechComposeTest {

    private fun assertValidOffsetMapping(transformation: VisualTransformation, inputs: List<String>) {
        for (raw in inputs) {
            val result = transformation.filter(AnnotatedString(raw))
            val outLen = result.text.length
            val mapping = result.offsetMapping

            var prevTrans = -1
            for (orig in 0..raw.length) {
                val mapped = mapping.originalToTransformed(orig)
                assertTrue(mapped in 0..outLen, "orig=$orig -> $mapped out of 0..$outLen for raw='$raw'")
                assertTrue(mapped >= prevTrans, "Non-monotonic origToTrans at $orig for raw='$raw'")
                prevTrans = mapped
            }

            var prevOrig = -1
            for (trans in 0..outLen) {
                val mapped = mapping.transformedToOriginal(trans)
                assertTrue(mapped in 0..raw.length, "trans=$trans -> $mapped out of 0..${raw.length} for raw='$raw'")
                assertTrue(mapped >= prevOrig, "Non-monotonic transToOrig at $trans for raw='$raw'")
                prevOrig = mapped
            }
        }
    }

    @Test
    fun cardNumberTransformationFormatsAndMapsOffsetsSafely() {
        val tf = CardNumberVisualTransformation()
        assertEquals("6037-9918-1234-5678", tf.filter(AnnotatedString("603799181234567899")).text.text)
        assertValidOffsetMapping(tf, listOf("", "6", "6037", "60379", "6037-99ab", "603799181234567890"))
    }

    @Test
    fun priceAndCurrencyTransformationsNeverOverflowBounds() {
        val price = PriceVisualTransformation()
        assertEquals("1,500,000", price.filter(AnnotatedString("1500000")).text.text)
        assertEquals("12,345", price.filter(AnnotatedString("a1b2c3d4e5f")).text.text)
        assertValidOffsetMapping(price, listOf("", "1", "123", "1234", "1500000", "1,23a4"))

        val currency = CurrencyVisualTransformation(" تومان")
        val currencyOut = currency.filter(AnnotatedString("250000"))
        assertEquals("250,000 تومان", currencyOut.text.text)
        assertEquals(7, currencyOut.offsetMapping.originalToTransformed(6))
        assertValidOffsetMapping(currency, listOf("", "5", "2500", "250000"))
    }

    @Test
    fun expirationAndShebaTransformationsWorkCleanly() {
        val exp = ExpirationDateVisualTransformation()
        assertEquals("12/08", exp.filter(AnnotatedString("12089")).text.text)
        assertValidOffsetMapping(exp, listOf("", "1", "12", "120", "1208", "120899"))

        val sheba = ShebaVisualTransformation()
        val shebaOut = sheba.filter(AnnotatedString("062960000000100324200001"))
        assertEquals("IR06 2960 0000 0010 0324 2000 01", shebaOut.text.text)
        assertValidOffsetMapping(sheba, listOf("", "06", "062960", "IR062960000000100324200001"))
    }
}
