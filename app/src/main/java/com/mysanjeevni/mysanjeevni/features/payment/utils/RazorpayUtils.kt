package com.mysanjeevni.mysanjeevni.features.payment.utils


import android.app.Activity
import android.util.Log
import com.mysanjeevni.mysanjeevni.BuildConfig
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.payment.data.dto.RazorpayOrder
import com.razorpay.Checkout
import org.json.JSONObject

fun startRazorpayCheckout(
    activity: Activity,
    order: RazorpayOrder,
    description:String,
    onSuccess: (String, String, String) -> Unit,
    onFailure: (String) -> Unit
) {

    Log.d("RZP_DEBUG", "========================")
    Log.d("RZP_DEBUG", "startRazorpayCheckout()")
    Log.d("RZP_DEBUG", "Order ID = ${order.id}")
    Log.d("RZP_DEBUG", "Amount = ${order.amount}")
    Log.d("RZP_DEBUG", "Currency = ${order.currency}")


    try {

        Log.d("RZP_DEBUG", "Creating Checkout")

        val checkout = Checkout()

        Log.d(
            "RZP_DEBUG",
            "Razorpay Key = ${BuildConfig.RAZORPAY_KEY}"
        )

        checkout.setKeyID(
            BuildConfig.RAZORPAY_KEY
        )


        Log.d("RZP_DEBUG", "Preparing Options")

        val options = JSONObject().apply {

            put("name", R.string.app_name)

            put(
                "description",
                description
            )

            put(
                "order_id",
                order.id
            )

            put(
                "amount",
                order.amount
            )

            put(
                "currency",
                order.currency
            )
            put(
                "retry",
                JSONObject().apply {
                    put("enabled", false)
                    put("max_count", 0)
                }
            )

            put(
                "prefill",
                JSONObject().apply {
                    put("contact", "")
                    put("email", "")
                }
            )
        }

        Log.d(
            "RZP_DEBUG",
            "Options = $options"
        )

        Log.d(
            "RZP_DEBUG",
            "Before checkout.open()"
        )

        checkout.open(
            activity,
            options
        )

        Log.d(
            "RZP_DEBUG",
            "After checkout.open()"
        )

    } catch (e: Exception) {

        Log.e(
            "RZP_DEBUG",
            "Checkout Exception",
            e
        )

        onFailure(
            e.message ?: "Checkout Failed"
        )
    }

    Log.d("RZP_DEBUG", "========================")
}


