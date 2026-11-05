
package za.co.unimarket

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import za.co.unimarket.utils.ListingValidator

class ListingValidatorTest {

    @Test
    fun validListingPasses() {
        assertTrue(ListingValidator.isValid("Laptop", 2500.0))
    }

    @Test
    fun emptyTitleFails() {
        assertFalse(ListingValidator.isValid("", 500.0))
    }

    @Test
    fun negativePriceFails() {
        assertFalse(ListingValidator.isValid("Book", -50.0))
    }

    @Test
    fun zeroPriceAllowed() {
        assertTrue(ListingValidator.isValid("Free notes", 0.0))
    }
}
