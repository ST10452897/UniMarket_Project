
package za.co.unimarket.utils

object ListingValidator {

    fun isValid(title: String, price: Double): Boolean {
        return title.isNotBlank() && price >= 0
    }
}
