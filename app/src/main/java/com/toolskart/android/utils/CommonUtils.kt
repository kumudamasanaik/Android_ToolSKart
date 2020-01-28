package com.toolskart.android.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.gson.Gson
import com.toolskart.android.R
import com.toolskart.android.api.ApiConstants
import com.toolskart.android.constants.Constants
import com.toolskart.android.constants.Constants.Companion.CUSTOMER_ID
import com.toolskart.android.constants.Constants.Companion.IS_LOGGED_IN
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.CartSummary
import com.toolskart.android.model.Customer
import com.toolskart.android.ui.home.HomeActivity
import com.toolskart.android.ui.login.LoginActivity
import com.toolskart.android.utils.AppSignatureHelper.TAG
import kotlinx.android.synthetic.main.custome_toast.view.*
import kotlinx.android.synthetic.main.partial_popup_dialogue.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class CommonUtils {
    companion object {

        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private var pDialog: ProgressDialog? = null


        /*for circular image*/
        fun setCircularUserImage(mContext: Context, imageView: ImageView, photo: String?) {
            Glide.with(mContext).load(ApiConstants.IMAGE_BASE_URL + photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView)
        }

        fun setUserImage(mContext: Context, imageView: ImageView, photo: String?) {
            Glide.with(mContext).load(ApiConstants.IMAGE_BASE_URL + photo)
                    .into(imageView)
        }

        fun showLoading(mContext: Context, message: String, cancelable: Boolean) {
            try {
                CommonUtils.hideLoading()
                pDialog = ProgressDialog(mContext, R.style.AppTheme_Loading_Dialog)
                pDialog!!.setMessage(message)
                pDialog!!.setCancelable(cancelable)
                pDialog!!.setOnCancelListener(DialogInterface.OnCancelListener { dialog ->
                    //AppController.getInstance().getRequestQueue().cancelAll(tag);
                    dialog.dismiss()
                })
                pDialog!!.show()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        fun hideLoading() {
            if (pDialog != null && pDialog!!.isShowing())
                pDialog!!.dismiss()
            pDialog = null
        }

        fun checkValidEmail(email: String): Boolean {
            val check: Boolean
            val p: Pattern
            val m: Matcher
            val EMAIL_STRING = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
            p = Pattern.compile(EMAIL_STRING)
            m = p.matcher(email)
            check = m.matches()
            return check
        }

        fun getCurrentTimeMilliSecons(): Long {
            return System.currentTimeMillis()
        }

        fun goToLandingPage(context: Context) {
            try {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
                (context as AppCompatActivity).finish()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        fun convertToBase64(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val byteArrayImage = baos.toByteArray()
            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
        }

        fun goToDashBoard(context: Context) {
            try {
                val intent = Intent(context, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
                (context as AppCompatActivity).finish()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        /*LogOut Dialogue Box*/
        fun showDialog(context: Context, body: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partial_popup_dialogue, null)

            dialogview.dialog_body.text = body
            builder.setView(dialogview)
            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                AppSharedPreference.clearPreferences()
                val bundle = Bundle()
                startActivity(context, LoginActivity::class.java, bundle, true)
            }
            dialogview.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        fun showConfirmationDialogPopup(context: Context, listener: IAdapterClickListener, body: String, item: Any?, pos: Int) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partial_popup_dialogue, null)

            dialogview.dialog_body.text = body
            builder.setView(dialogview)

            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                listener.onclick(data = item!!, pos = pos, type = Constants.ORDER_CANCELLED_CONFIRMATION)
            }
            dialogview.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        fun startActivity(mContext: Context, activity: Class<*>, bundle: Bundle, actfinish: Boolean) {
            val move = Intent(mContext, activity)
            move.putExtras(bundle)
            mContext.startActivity(move)
            if (mContext is Activity) {
                if (actfinish)
                    mContext.finish()
            }
        }

        /*   fun goToCart(context: Context) {
               try {
                   val intent = Intent(context, CartActivity::class.java)
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                   context.startActivity(intent)
                   (context as AppCompatActivity).finish()
               } catch (ex: Exception) {
                   ex.printStackTrace()
               }
           }*/

        /*  fun goToWishList(context: Context) {
              try {
                  val intent = Intent(context, WishListActivity::class.java)
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                  context.startActivity(intent)
                  (context as AppCompatActivity).finish()
              } catch (ex: Exception) {
                  ex.printStackTrace()
              }
          }*/

        fun setImage(mContext: Context, imageView: ImageView, url: String) {
            val options = RequestOptions()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
            if (url.isNotEmpty())
            // GlideApp.with(mContext).load(ApiConstants.IMAGE_BASE_URL + url).apply(options).into(imageView)
                GlideApp.with(mContext).load(R.drawable.dummy).apply(options).into(imageView)
        }

        /*   fun setCircleImage(mContext: Context, imageView: ImageView, url: String) {
               val options = RequestOptions()
                       .circleCrop()
                       .placeholder(R.drawable.applogo)
                       .error(R.drawable.applogo)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                       .priority(Priority.HIGH)
               if (url.isNotEmpty())
                   GlideApp.with(mContext).load(ApiConstants.IMAGE_BASE_URL + url).apply(options).into(imageView)
           }*/

        fun isGooglePlayServicesAvailable(context: Context): Boolean {
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            if (ConnectionResult.SUCCESS == googleApiAvailability.isGooglePlayServicesAvailable(context)) {
                return true
            } else {
                try {
                    googleApiAvailability.getErrorDialog(context as AppCompatActivity, 0, CommonUtils.PLAY_SERVICES_RESOLUTION_REQUEST).show()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Toast.makeText(context, "Google Play Services Not Available.", Toast.LENGTH_SHORT).show()
                }

                return false
            }
        }

        fun setCustomerData(customerData: Customer) {
            try {
                setIsLoggedIn(true)
                setCurrentUser(customerData)
                if (Validation.validateValue(customerData._id)) {
                    AppSharedPreference.setPrefVal(Constants._ID, customerData._id
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.BIRTHDAY, customerData.birthday
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.CUSTOMER_TYPE, customerData.customer_type
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.EMAIL, customerData.email
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.EMAIL_VERIFIED, customerData.email_verified
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.FACEBOOK_ID, customerData.facebook_id
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.GENDER, customerData.gender
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.GOOGLE_ID, customerData.google_id
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.ID_DELIVERY_BOY, customerData.id_deliveryboy
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.LAST_NAME, customerData.last_name
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.MOBILE, customerData.mobile
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.NAME, customerData.name
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.NEW_SELECTER, customerData.newsletter
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.NOTE, customerData.note
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.OTP, customerData.otp
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.PIC, customerData.pic
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.REFERRAL_CODE, customerData.referral_code
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.REFERRAL_USED, customerData.referral_used
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.REFERRAL_AMOUNT, customerData.referred_amount
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.REFERRAL_BY, customerData.referred_by
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.REFERRED_CODE, customerData.referred_code
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.VERIFIED, customerData.verified
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.WALLET, customerData.wallet
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                    AppSharedPreference.setPrefVal(Constants.WEBSITE, customerData.website
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)

                    AppSharedPreference.setPrefVal(Constants.FIRST_NAME, customerData.first_name
                            ?: "", AppSharedPreference.VALUE_TYPE.STRING)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
        fun getCustomerID(): String {
            return AppSharedPreference.getPrefVal(Constants._ID, "", AppSharedPreference.VALUE_TYPE.STRING) as String
        }

        fun getEmailId(): String {
            return AppSharedPreference.getPrefVal(Constants.EMAIL, "", AppSharedPreference.VALUE_TYPE.STRING) as String
        }

        fun getUserName(): String {
            return AppSharedPreference.getPrefVal(Constants.FIRST_NAME, "", AppSharedPreference.VALUE_TYPE.STRING) as String
        }

        fun getMobileNum(): String {
            return AppSharedPreference.getPrefVal(Constants.MOBILE, "", AppSharedPreference.VALUE_TYPE.STRING) as String
        }

        private fun setCurrentUser(customer: Customer?) {
                AppSharedPreference.setPrefVal(Constants.CUR_CUSTOMER, Gson().toJson(customer), AppSharedPreference.VALUE_TYPE.STRING)
        }

        fun getCurrentUser(): Customer? {
            val json = AppSharedPreference.getPrefVal(Constants.CUR_CUSTOMER, "", AppSharedPreference.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<Customer>(json, Customer::class.java) as Customer
            } catch (exp: Exception) {
                null
            }
        }

        fun getProfilePic(): String {
            return AppSharedPreference.getPrefVal(Constants.PIC, "", AppSharedPreference.VALUE_TYPE.STRING) as String
        }

        fun isValidString(string: String?): Boolean {
            return string != null && string.trim { it <= ' ' }.length > 0
        }

        fun isValidList(list: List<*>?): Boolean {
            return list != null && list.size > 0
        }

        fun setAuthorizationkey(key: String?) {
            MyLogUtils.d(TAG, "Authorization key : $key")
            AppSharedPreference.setPrefVal(Constants.AUTHORIZATION_KEY, key!!, AppSharedPreference.VALUE_TYPE.STRING)
        }

        fun getAutharizationKey(): String {
            return AppSharedPreference.getPrefVal(Constants.AUTHORIZATION_KEY, "", AppSharedPreference.VALUE_TYPE.STRING) as String

        }
        fun getCustomerId(): String {
            return AppSharedPreference.getPrefVal(CUSTOMER_ID, "0", AppSharedPreference.VALUE_TYPE.STRING) as String
        }

        fun setIsLoggedIn(isLoggedIn: Boolean) {
            AppSharedPreference.setPrefVal(IS_LOGGED_IN, isLoggedIn, AppSharedPreference.VALUE_TYPE.BOOLEAN)
        }

        fun isUserLogin(): Boolean {
            return AppSharedPreference.getPrefVal(IS_LOGGED_IN, false, AppSharedPreference.VALUE_TYPE.BOOLEAN) as Boolean
        }

        fun getSession(): String {
            val session = AppSharedPreference.getPrefVal(AppSharedPreference.SESSION, "", AppSharedPreference.VALUE_TYPE.STRING) as String
            if (session.isEmpty()) {
                return generateSession()
            }
            return session
        }

        private fun generateSession(): String {
            return try {
                val chars = "abcdefghijklmnopqrstuvwxyz".toCharArray()
                val sb = StringBuilder()
                val random = Random()
                for (i in 0..15) {
                    val c = chars[random.nextInt(chars.size)]
                    sb.append(c)
                }
                val randomString = sb.toString() + "_" + SimpleDateFormat("ddMMyyyyhhmmssSSS").format(java.util.Date())
                setSession(randomString)
                return randomString
                // put(SESSION, randomString)
            } catch (ex: Exception) {
                ex.printStackTrace()
                return ""
            }
        }

        fun setSession(session: String) {
            AppSharedPreference.setPrefVal(AppSharedPreference.SESSION, session, AppSharedPreference.VALUE_TYPE.STRING)
            AppSharedPreference.setPrefVal(AppSharedPreference.SESSION_TIME, System.currentTimeMillis(), AppSharedPreference.VALUE_TYPE.LONG)
        }

        fun saveOtpVerificationHashKey(otpStaticKey: String) {
            AppSharedPreference.setPrefVal(AppSharedPreference.OTP_VERIFICATION_KEY, otpStaticKey, AppSharedPreference.VALUE_TYPE.STRING)

        }

        fun getOtpVerificationHashKey(): String? {
            return AppSharedPreference.getPrefVal(AppSharedPreference.OTP_VERIFICATION_KEY, "", AppSharedPreference.VALUE_TYPE.STRING) as String
        }

        fun saveCartSummary(cartsummary: CartSummary) {
            AppSharedPreference.setPrefVal(Constants.CART_SUMMARY, Gson().toJson(cartsummary), AppSharedPreference.VALUE_TYPE.STRING)
        }

        fun getRupeesSymbol(context: Context, price: String): String {
            return context.getString(R.string.Rs) + price
        }

        fun showCustomToast(context: Context, msg: String, length: Int) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(R.layout.custome_toast, null) as View
            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.duration = length
            toast.view = layout
            toast.show()
            if (msg != null)
                layout.tv_toast_msg.text = msg
            else
                layout.tv_toast_msg.text = context.getString(R.string.Please_login_to_the_application)
        }

        fun saveMobileNumber(mobile: String) {
            AppSharedPreference.setPrefVal(AppSharedPreference.MOBILE, mobile, AppSharedPreference.VALUE_TYPE.STRING)
        }

        fun getMobileNumber(): String {
            return AppSharedPreference.getPrefVal(AppSharedPreference.MOBILE, "", AppSharedPreference.VALUE_TYPE.STRING) as String
        }
    }
}

inline fun <E : Any, T : Collection<E>> T?.withNotNullNorEmpty(func: T.() -> Unit): Unit {
    if (this != null && this.isNotEmpty()) {
        with(this) { func() }
    }
}