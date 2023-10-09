package com.app.kerkly.pasarelaPagos


import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.kerkly.R
import com.stripe.android.paymentsheet.PaymentSheet
import okhttp3.*
import org.json.JSONException
import java.io.IOException


class CheckoutActivity : AppCompatActivity() {
    private val TAG = "CheckoutActivity"
    private val BACKEND_URL = "http://10.0.2.2:4242"
    private var paymentIntentClientSecret: String? = null
    private var paymentSheet: PaymentSheet? = null
    private var payButton: Button? = null
    private var addressLauncher: AddressLauncher? = null
    private var shippingDetails: AddressDetails? = null
    private var addressButton: Button? = null
    private val configuration: AddressLauncher.Configuration = Builder()
        .additionalFields(
            AdditionalFieldsConfiguration(
                AddressLauncher.AdditionalFieldsConfiguration.FieldConfiguration.REQUIRED
            )
        )
        .allowedCountries(HashSet<Any?>(Arrays.asList("US", "CA", "GB")))
        .title("Shipping Address")
        .googlePlacesApiKey("(optional) YOUR KEY HERE")
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Hook up the pay button
        // Hook up the pay button
        payButton = findViewById<Button>(com.app.kerkly.R.id.pay_button)
        payButton.setOnClickListener(this::onPayClicked)
        payButton.setEnabled(false)

        paymentSheet = PaymentSheet(this, this::onPaymentSheetResult)

        // Hook up the address button

        // Hook up the address button
        addressButton = findViewById<Button>(R.id.address_button)
        addressButton.setOnClickListener(this::onAddressClicked)
        addressLauncher = AddressLauncher(this, this::onAddressLauncherResult)

        fetchPaymentIntent()
    }


    private fun showAlert(title: String, @Nullable message: String) {
        runOnUiThread {
            val dialog: AlertDialog = Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
            dialog.show()
        }
    }

    private fun showToast(message: String) {
        runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
    }

    private fun fetchPaymentIntent() {
        val shoppingCartContent = "{\"items\": [ {\"id\":\"xl-tshirt\"}]}"
        val requestBody: RequestBody = create(
            shoppingCartContent,
            MediaType.get("application/json; charset=utf-8")
        )
        val request: Request = Builder()
            .url("$BACKEND_URL/create-payment-intent")
            .post(requestBody)
            .build()
        OkHttpClient()
            .newCall(request)
            .enqueue(object : Callback() {
                fun onFailure(@NonNull call: Call?, @NonNull e: IOException) {
                    showAlert("Failed to load data", "Error: $e")
                }

                @Throws(IOException::class)
                fun onResponse(
                    @NonNull call: Call?,
                    @NonNull response: Response
                ) {
                    if (!response.isSuccessful()) {
                        showAlert(
                            "Failed to load page",
                            "Error: $response"
                        )
                    } else {
                        val responseJson: JSONObject = parseResponse(response.body())
                        paymentIntentClientSecret = responseJson.optString("clientSecret")
                        runOnUiThread { payButton!!.isEnabled = true }
                        Log.i(TAG, "Retrieved PaymentIntent")
                    }
                }
            })
    }

    private fun parseResponse(responseBody: ResponseBody?): JSONObject {
        if (responseBody != null) {
            try {
                return JSONObject(responseBody.string())
            } catch (e: IOException) {
                Log.e(TAG, "Error parsing response", e)
            } catch (e: JSONException) {
                Log.e(TAG, "Error parsing response", e)
            }
        }
        return JSONObject()
    }

    private fun onPayClicked(view: View) {
        val configuration: PaymentSheet.Configuration = Configuration("Example, Inc.")

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }

    private fun onAddressClicked(view: View) {
        addressLauncher.present(
            publishableKey,
            addressConfiguration
        )
    }

    private fun onPaymentSheetResult(
        paymentSheetResult: PaymentSheetResult
    ) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            showToast("Payment complete!")
        } else if (paymentSheetResult is Canceled) {
            Log.i(TAG, "Payment canceled!")
        } else if (paymentSheetResult is PaymentSheetResult.Failed) {
            val error: Throwable = (paymentSheetResult as PaymentSheetResult.Failed).getError()
            showAlert("Payment failed", error.localizedMessage)
        }
    }

    private fun onAddressLauncherResult(result: AddressLauncherResult) {
        // TODO: Handle result and update your UI
        if (result is AddressLauncherResult.Succeeded) {
            shippingDetails = (result as AddressLauncherResult.Succeeded).getAddress()
        } else if (result is Canceled) {
            // TODO: Handle cancel
        }
    }

}