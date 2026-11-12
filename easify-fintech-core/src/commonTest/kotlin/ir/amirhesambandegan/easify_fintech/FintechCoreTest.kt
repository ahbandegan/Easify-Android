package ir.amirhesambandegan.easify_fintech

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FintechCoreTest {

    @Test
    fun shebaValidationAndFormattingWorkWithoutCrash() {
        assertNull("IR123".getShebaBankName())
        assertTrue("IR062960000000100324200001".isValidSheba())
        assertFalse("IR062960000000100324200002".isValidSheba())
        assertEquals("IR06 2960 0000 0010 0324 2000 01", "ir062960000000100324200001".formatSheba())
    }

    @Test
    fun billValidatorEnforcesBase11AndDecodesInfo() {
        // Construct a valid billId & paymentId pair via Base-11 check digit
        fun appendBase11(prefix: String): String {
            var sum = 0
            var mul = 2
            for (i in prefix.length - 1 downTo 0) {
                sum += (prefix[i] - '0') * mul
                mul = if (mul == 7) 2 else mul + 1
            }
            val rem = sum % 11
            val check = if (rem < 2) 0 else 11 - rem
            return prefix + check
        }

        val billId = appendBase11("9876542") // type 2 = Electricity
        val payBody = "15000401"
        val payInner = appendBase11(payBody)
        val outerCheck = appendBase11(billId + payInner).last()
        val paymentId = payInner + outerCheck

        assertTrue(BillValidator.isValidBill(billId, paymentId))
        val info = BillValidator.extractBillInfo(billId, paymentId)
        assertNotNull(info)
        assertEquals(BillType.ELECTRICITY, info.billType)
        assertEquals(15_000_000L, info.amountRials)
        assertEquals("401", info.periodCode)

        // Flipping payment outer digit must now FAIL (unlike old buggy return true!)
        val brokenPay = payInner + ((outerCheck - '0' + 1) % 10)
        assertFalse(BillValidator.isValidBill(billId, brokenPay))
    }

    @Test
    fun pricesAndWordsHandleEdgeCasesSafely() {
        assertEquals("1,500,000 تومان", 1_500_000L.toPriceFormat())
        assertEquals("-250 تومان", (-250L).toPriceFormat())
        assertTrue(Long.MIN_VALUE.toPersianWords().startsWith("منفی نه کوینتیلیون"))
        assertTrue(1_000_000_000_000_000L.toPersianWords().contains("کوآدریلیون"))
    }
}
