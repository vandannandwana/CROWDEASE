package com.minor.crowdease

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class PaymentActivity:ComponentActivity(),PaymentResultWithDataListener,ExternalWalletListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val amount = intent.getIntExtra("totalAmount",0)
        setContent {

            startPayment(amount)


        }

    }


    private fun startPayment(amount:Int) {
        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_test_rwpSKZoSE85lc9")
        try {
            val options = JSONObject()
            options.put("name","CROWDEASE")
            options.put("description","Hotel Booking Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://i.pinimg.com/736x/ec/4e/e0/ec4ee0841975985154a827be450a0cae.jpg")
            options.put("theme.color", "#8293D2");
            options.put("currency","INR");
            options.put("amount",amount*100)//pass amount in currency subunits
            val prefill = JSONObject()
            prefill.put("email","example@example.com")
            prefill.put("contact","123456789")

            options.put("prefill",prefill)

            co.open(this,options)
        } catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Log.d("VandanPayment","Success"+p0+"->"+ p1?.let { show(it) })
        val intent = Intent()
        intent.putExtra("paymentId",p0)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Log.d("VandanPayment","Error"+p0+"->"+p1)
        val intent = Intent()
        intent.putExtra("paymentId",p0)
        setResult(Activity.RESULT_CANCELED,intent)
        finish()
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        finish()
    }


    private fun show(paymentData: PaymentData):String{

        return ""+paymentData.paymentId +
                paymentData.data +
                paymentData.orderId +
                paymentData.userEmail +
                paymentData.userContact

    }


}

@Composable
fun PaymentScreen(onPayNowClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onPayNowClick) {
            Text("Pay Now")
        }
    }
}