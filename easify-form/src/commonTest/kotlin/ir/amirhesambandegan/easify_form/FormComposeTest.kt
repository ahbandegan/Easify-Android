package ir.amirhesambandegan.easify_form

import androidx.compose.ui.text.AnnotatedString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FormComposeTest {

    @Test
    fun formStateValidatesAllWithoutShortCircuitAndTracksLiveValidity() {
        val email = FormField("email", rules = listOf(Validators.required(), Validators.email()))
        val name = FormField("name", rules = listOf(Validators.required()))
        val form = FormState(listOf(email, name))

        assertFalse(form.isValid.value)
        assertFalse(form.validateAll())
        assertNotNull(email.error)
        assertNotNull(name.error)

        email.value = "user@example.com"
        assertFalse(form.isValid.value)
        name.value = "Amir"
        assertTrue(form.isValid.value)
        assertTrue(form.isDirty)
        assertTrue(form.validateAll())

        form.resetAll()
        assertEquals("", email.value)
        assertFalse(form.isDirty)
    }

    @Test
    fun formArrayValidatesEveryItemAndExportsDraftJson() {
        val array = FormArrayState {
            field("title", rules = listOf(Validators.required()))
        }
        array.addItem()

        assertFalse(array.validateAll())
        assertNotNull(array.items[0].getField("title")?.error)
        assertNotNull(array.items[1].getField("title")?.error)

        val form = array.items[0]
        form.getField("title")?.value = "Lead Engineer"
        val json = form.exportDraftJson()

        val target = FormBuilder().apply { field("title") }.build()
        assertTrue(target.importDraftJson(json))
        assertEquals("Lead Engineer", target.getField("title")?.value)
    }

    @Test
    fun maskVisualTransformationKeepsOffsetsMonotonicAndBounded() {
        val mask = MaskVisualTransformation("+98 (###) ###-####")
        val inputs = listOf("", "9", "91234", "9123456789", "91234567890123")

        inputs.forEach { raw ->
            val result = mask.filter(AnnotatedString(raw))
            val outLen = result.text.text.length

            var prevTransformed = 0
            for (o in 0..raw.length) {
                val mapped = result.offsetMapping.originalToTransformed(o)
                assertTrue(mapped in 0..outLen, "orig($o) -> $mapped out of 0..$outLen for '$raw'")
                assertTrue(mapped >= prevTransformed, "non-monotonic orig($o)")
                prevTransformed = mapped
            }

            var prevOrig = 0
            for (t in 0..outLen) {
                val mapped = result.offsetMapping.transformedToOriginal(t)
                assertTrue(mapped in 0..raw.length, "trans($t) -> $mapped out of 0..${raw.length} for '$raw'")
                assertTrue(mapped >= prevOrig, "non-monotonic trans($t)")
                prevOrig = mapped
            }
        }
    }
}
