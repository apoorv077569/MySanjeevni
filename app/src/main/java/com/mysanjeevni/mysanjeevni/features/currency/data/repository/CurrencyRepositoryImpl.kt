package com.mysanjeevni.mysanjeevni.features.currency.data.repository


import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.currency.data.mapper.CurrencyMapper
import com.mysanjeevni.mysanjeevni.features.currency.domain.model.CurrencyInfo
import com.mysanjeevni.mysanjeevni.features.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CurrencyRepository {

    override suspend fun getCurrencyInfo(): Result<CurrencyInfo> {
        val TAG = "CurrencyInfo"

        return try {
            Log.d(TAG, "Starting getCurrencyInfo()")

            // 1. Detect user's country
            Log.d(TAG, "Fetching user country...")
            val countryResponse = apiService.getUserCountry()

            if (!countryResponse.isSuccessful) {
                val requestUrl = countryResponse.raw().request.url
                val errorBody = try { countryResponse.errorBody()?.string() } catch (e: Exception) { null }
                Log.e(TAG, "Country API call failed: url=$requestUrl, code=${countryResponse.code()}, message=${countryResponse.message()}, errorBody=$errorBody")
                return Result.failure(Exception("Unable to detect user country"))
            }

            val body = countryResponse.body()

            if (body?.success == false) {
                Log.e(TAG, "Country API returned success=false: ${body.message}")
                return Result.failure(Exception("Unable to detect user country: ${body.message}"))
            }

            val countryCode = body?.country_code?.uppercase()

            Log.d(TAG, "Detected country code: $countryCode")

            if (countryCode.isNullOrBlank()) {
                Log.e(TAG, "Country code is null or blank")
                return Result.failure(Exception("Country information unavailable"))
            }

            // 2. India → INR
            if (countryCode == "IN") {
                Log.d(TAG, "Country is India, returning INR currency info")
                return Result.success(
                    CurrencyInfo(
                        currencyCode = "INR",
                        currencySymbol = "₹",
                        exchangeRate = 1.0
                    )
                )
            }

            // 3. Outside India → resolve local currency + live rate
            val (currencyCode, currencySymbol) = CurrencyMapper.getCurrencyForCountry(countryCode)
            Log.d(TAG, "Mapped country $countryCode to currency $currencyCode ($currencySymbol)")

            Log.d(TAG, "Fetching exchange rate...")
            val exchangeResponse = apiService.getExchangeRate()

            if (!exchangeResponse.isSuccessful) {
                val requestUrl = exchangeResponse.raw().request.url
                val errorBody = try { exchangeResponse.errorBody()?.string() } catch (e: Exception) { null }
                Log.e(TAG, "Exchange rate API call failed: url=$requestUrl, code=${exchangeResponse.code()}, message=${exchangeResponse.message()}, errorBody=$errorBody")
                return Result.failure(Exception("Unable to fetch exchange rate"))
            }

            val rate = exchangeResponse.body()?.rates?.get(currencyCode)

            Log.d(TAG, "Fetched $currencyCode rate: $rate")

            if (rate == null || rate <= 0.0) {
                Log.e(TAG, "$currencyCode rate is null or invalid: $rate — falling back to USD")
                // Fallback to USD if the mapped currency isn't in the rates response
                val usdRate = exchangeResponse.body()?.rates?.get("USD")
                if (usdRate == null || usdRate <= 0.0) {
                    return Result.failure(Exception("Exchange rate unavailable"))
                }
                return Result.success(
                    CurrencyInfo(
                        currencyCode = "USD",
                        currencySymbol = "$",
                        exchangeRate = usdRate
                    )
                )
            }

            Log.d(TAG, "Returning $currencyCode currency info with rate: $rate")
            Result.success(
                CurrencyInfo(
                    currencyCode = currencyCode,
                    currencySymbol = currencySymbol,
                    exchangeRate = rate
                )
            )

        } catch (e: Exception) {
            Log.e(TAG, "Exception in getCurrencyInfo(): ${e.message}", e)
            Result.failure(e)
        }
    }
}