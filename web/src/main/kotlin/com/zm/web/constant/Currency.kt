package com.zm.web.constant;

/**
 * Enumeration representing various currencies with their details.
 * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a>
 */
enum class Currency(
    private val countryName: String,
    private val num: Int,
    private val symbol: String,
    private val decimalPrecision: Int
) {
    USD("United States", 840, "$", 2),
    EUR("Eurozone", 978, "€", 2),
    GBP("United Kingdom", 826, "£", 2),
    JPY("Japan", 392, "¥", 0),
    AUD("Australia", 36, "$", 2),
    CAD("Canada", 124, "$", 2),
    CHF("Switzerland", 756, "Fr", 2),
    CNY("China", 156, "¥", 2),
    INR("India", 356, "₹", 2),
    SGD("Singapore", 702, "$", 2),
    NZD("New Zealand", 554, "$", 2);

    override fun toString(): String {
        return "$countryName uses $name($symbol) as currency, D: $decimalPrecision, Num: $num"
    }

    fun getSymbol(): String = symbol

    fun getNum(): Int = num

    fun getCountryName(): String = countryName

    fun getDecimalPrecision(): Int = decimalPrecision

    companion object {
        fun fromCode(code: String): Currency? {
            return try {
                enumValueOf<Currency>(code)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}
