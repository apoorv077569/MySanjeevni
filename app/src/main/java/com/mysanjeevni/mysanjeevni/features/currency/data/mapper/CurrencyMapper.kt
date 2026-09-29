package com.mysanjeevni.mysanjeevni.features.currency.data.mapper


object CurrencyMapper {

    // Maps ISO country code -> Pair(currencyCode, currencySymbol)
    private val countryToCurrency: Map<String, Pair<String, String>> = mapOf(
        // North America
        "US" to ("USD" to "$"),
        "CA" to ("CAD" to "$"),
        "MX" to ("MXN" to "$"),

        // Europe
        "GB" to ("GBP" to "£"),
        "DE" to ("EUR" to "€"),
        "FR" to ("EUR" to "€"),
        "IT" to ("EUR" to "€"),
        "ES" to ("EUR" to "€"),
        "NL" to ("EUR" to "€"),
        "PT" to ("EUR" to "€"),
        "IE" to ("EUR" to "€"),
        "BE" to ("EUR" to "€"),
        "AT" to ("EUR" to "€"),
        "CH" to ("CHF" to "Fr"),
        "SE" to ("SEK" to "kr"),
        "NO" to ("NOK" to "kr"),
        "DK" to ("DKK" to "kr"),
        "PL" to ("PLN" to "zł"),
        "RU" to ("RUB" to "₽"),
        "TR" to ("TRY" to "₺"),
        "UA" to ("UAH" to "₴"),

        // Asia
        "JP" to ("JPY" to "¥"),
        "CN" to ("CNY" to "¥"),
        "KR" to ("KRW" to "₩"),
        "IN" to ("INR" to "₹"),
        "SG" to ("SGD" to "$"),
        "HK" to ("HKD" to "$"),
        "MY" to ("MYR" to "RM"),
        "TH" to ("THB" to "฿"),
        "ID" to ("IDR" to "Rp"),
        "PH" to ("PHP" to "₱"),
        "VN" to ("VND" to "₫"),
        "PK" to ("PKR" to "₨"),
        "BD" to ("BDT" to "৳"),
        "NP" to ("NPR" to "₨"),
        "LK" to ("LKR" to "₨"),
        "AE" to ("AED" to "د.إ"),
        "SA" to ("SAR" to "﷼"),
        "IL" to ("ILS" to "₪"),
        "QA" to ("QAR" to "﷼"),
        "KW" to ("KWD" to "د.ك"),

        // Oceania
        "AU" to ("AUD" to "$"),
        "NZ" to ("NZD" to "$"),

        // South America
        "BR" to ("BRL" to "R$"),
        "AR" to ("ARS" to "$"),
        "CL" to ("CLP" to "$"),
        "CO" to ("COP" to "$"),
        "PE" to ("PEN" to "S/"),

        // Africa
        "ZA" to ("ZAR" to "R"),
        "NG" to ("NGN" to "₦"),
        "EG" to ("EGP" to "£"),
        "KE" to ("KES" to "KSh")
    )

    /**
     * Returns Pair(currencyCode, currencySymbol) for a given ISO country code.
     * Falls back to USD if country isn't in the map.
     */
    fun getCurrencyForCountry(countryCode: String): Pair<String, String> {
        return countryToCurrency[countryCode.uppercase()] ?: ("USD" to "$")
    }
}