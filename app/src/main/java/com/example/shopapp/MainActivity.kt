package com.example.shopapp

import android.app.Activity
import android.app.Notification
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.shopapp.presentation.Navigation.App
import com.example.shopapp.presentation.spalsScreen.SplashScreen
import com.example.shopapp.ui.theme.ShopAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopAppTheme {
                MainScreen(firebaseAuth, onPayTest = {startPayment()})
            }
        }
    }

@Composable
fun MainScreen(firebaseAuth: FirebaseAuth, onPayTest:() -> Unit){
    val showSplashScreen = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            showSplashScreen.value = false
        }, 3000)
    }
    if(showSplashScreen.value){
        SplashScreen()
    }else{
        App(firebaseAuth, onPayTest)
    }
}
    private fun startPayment() {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the Dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","<currency>");
            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount","50000")//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","<email>")
            prefill.put("contact","<phone>")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        TODO("Not yet implemented")
    }
}

