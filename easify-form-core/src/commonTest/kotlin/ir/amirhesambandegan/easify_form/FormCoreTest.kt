package ir.amirhesambandegan.easify_form

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FormCoreTest {

    @Test
    fun formValidatesAllFieldsWithoutShortCircuiting() {
        val form = buildForm {
            field("email", rules = listOf(Validators.required(), Validators.email()))
            field("mobile", rules = listOf(Validators.phoneIr()), inputFilter = InputFilters.onlyNumbers())
            field("nationalId", rules = listOf(Validators.nationalCodeIr()))
        }

        assertFalse(form.validateAll())
        // Ensure ALL invalid fields get their errors simultaneously
        assertEquals("این فیلد الزامی است", form.getField("email")?.error)
        assertEquals("شماره موبایل نامعتبر است", form.getField("mobile")?.error)
        assertEquals("کد ملی نامعتبر است", form.getField("nationalId")?.error)

        form.getField("email")?.value = "amir@easify.ir"
        form.getField("mobile")?.value = "۰۹۱۲-۳۴۵-۶۷۸۹"
        form.getField("nationalId")?.value = "0084575948"

        assertEquals("09123456789", form.getField("mobile")?.value)
        assertTrue(form.isValid.value)
        assertTrue(form.validateAll())
    }

    @Test
    fun resetAndClearErrorsWorkAndAsyncValidatorResetsFlag() = runTest {
        val form = buildForm {
            field(
                key = "username",
                initialValue = "guest",
                rules = listOf(Validators.minLength(3)),
                asyncValidator = { if (it == "admin") "Reserved" else null }
            )
        }
        val field = form.getField("username")!!
        field.value = "admin"
        assertFalse(form.validateAllAsync())
        assertEquals("Reserved", field.error)
        assertFalse(field.isValidating)

        form.clearErrors()
        assertNull(field.error)

        form.resetAll()
        assertEquals("guest", field.value)
        assertFalse(field.isDirty)
    }
}
