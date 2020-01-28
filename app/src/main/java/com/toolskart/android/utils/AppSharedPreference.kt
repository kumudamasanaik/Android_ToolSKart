package com.toolskart.android.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.toolskart.android.model.CartSummary
import com.toolskart.android.model.Category
import java.util.*

class AppSharedPreference(var mSharedPreferences: SharedPreferences) {

    enum class VALUE_TYPE {
        BOOLEAN, INTEGER, STRING, FLOAT, LONG
    }

    companion object {

        private val PREFERENCE_NAME = "app_pref"
        val IS_LOGED_IN = "IS_LOGED_IN"
        val USER_DATA = "USER_DATA"
        val TOKEN = "TOKEN"
        val SELECTED_AREA = "SELECTED_AREA"
        val SELECTED_CITY = "SELECTED_CITY"
        val SESSION = "SESSION"
        val CART = "CART"
        val OTP_VERIFICATION_KEY = "otp key"
        val MAIN_CAT_LIST_DATA = "MAIN_CAT_LIST_DATA"
        val MOBILE = "MOBILE"
        val SESSION_TIME = "SESSION_TIME"
        var PREF_CART_DATA = "pref_cart_data"


        fun clearPreferences() {
            getPrefs().edit().clear().apply()
        }

        fun getPrefs(): SharedPreferences {
            return MyApplication.myApplication.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        fun setPrefVal(key: String, value: Any, vType: VALUE_TYPE) {
            when (vType) {
                VALUE_TYPE.BOOLEAN -> getPrefs().edit().putBoolean(key, value as Boolean).apply()
                VALUE_TYPE.INTEGER -> getPrefs().edit().putInt(key, value as Int).apply()
                VALUE_TYPE.STRING -> getPrefs().edit().putString(key, value as String).apply()
                VALUE_TYPE.FLOAT -> getPrefs().edit().putFloat(key, value as Float).apply()
                VALUE_TYPE.LONG -> getPrefs().edit().putLong(key, value as Long).apply()
                else -> {
                }
            }
        }

        fun getPrefVal(key: String, defValue: Any, vType: VALUE_TYPE): Any? {
            val `object`: Any?
            when (vType) {
                VALUE_TYPE.BOOLEAN -> `object` = getPrefs().getBoolean(key, defValue as Boolean)
                VALUE_TYPE.INTEGER -> `object` = getPrefs().getInt(key, defValue as Int)
                VALUE_TYPE.STRING -> `object` = getPrefs().getString(key, defValue as String)
                VALUE_TYPE.FLOAT -> `object` = getPrefs().getFloat(key, defValue as Float)
                VALUE_TYPE.LONG -> `object` = getPrefs().getLong(key, defValue as Long)
            }
            return `object`
        }

        fun put(key: String, value: String?) {
            getPrefs().edit().putString(key, value).apply()
        }

        fun put(key: String, value: Int) {
            getPrefs().edit().putInt(key, value).apply()
        }

        fun put(key: String, value: Float) {
            getPrefs().edit().putFloat(key, value).apply()
        }

        fun put(key: String, value: Boolean) {
            getPrefs().edit().putBoolean(key, value).apply()
        }

        fun get(key: String, defaultValue: String = ""): String {
            return getPrefs().getString(key, defaultValue)
        }

        fun get(key: String, defaultValue: Int = -1): Int {
            return getPrefs().getInt(key, defaultValue)
        }

        fun get(key: String, defaultValue: Float = -1F): Float {
            return getPrefs().getFloat(key, defaultValue)
        }

        fun get(key: String, defaultValue: Boolean = false): Boolean {
            return getPrefs().getBoolean(key, defaultValue)
        }

        fun isLogIn(): Boolean {
            return get(IS_LOGED_IN, false)
        }

        fun getSelectedArea(): String {
            return get(SELECTED_AREA, "")
        }

        fun getSelectedCity(): String {
            return get(SELECTED_CITY, "")
        }

        fun getSelectedDeliveryLocation(): String {
            return getSelectedArea() + "," + getSelectedCity()
        }

        /* fun getSession(): String {
             val session = get(SESSION, "")
             if (session.isEmpty()) {
                 return generateSession()
             }
             return session
         }*/

        /* private fun generateSession(): String {
             return try {
                 val chars = "abcdefghijklmnopqrstuvwxyz".toCharArray()
                 val sb = StringBuilder()
                 val random = Random()
                 for (i in 0..15) {
                     val c = chars[random.nextInt(chars.size)]
                     sb.append(c)
                 }
                 val randomString = sb.toString() + "_" + SimpleDateFormat("ddMMyyyyhhmmssSSS").format(java.util.Date())
                 put(SESSION, randomString)
                 randomString
             } catch (ex: Exception) {
                 ex.printStackTrace()
                 return ""
             }
         }*/



        /*   fun getCartData(): CartSummary? {
        val gson = get(CART, "")
        try {
            return Gson().fromJson<CartSummary?>(gson, CartSummary::class.java)
        } catch (exp: Exception) {
        }
        return null
    }*/

/*    private fun getUserData(): Any {
        var gson: Gson = Gson()
        var customerData: UserData
        var data: String = get(USER_DATA, "") as String
        customerData = gson.fromJson(data, UserData::class.java)
        return customerData
    }*/

        /*  fun getUserId(): String {
        return if (isLogIn()) {
            (getUserData() as UserData).id!!
        } else {
            "0"
        }

    }*/
        fun saveCategoryListData(categoryList: ArrayList<Category>?) = setPrefVal(MAIN_CAT_LIST_DATA, Gson().toJson(categoryList), VALUE_TYPE.STRING)

        /*fun saveCartData(summary: CartSummary?) {
            put(CART, Gson().toJson(summary))
        }*/
        fun saveCartData(cartSummery: CartSummary?) = setPrefVal(PREF_CART_DATA, Gson().toJson(cartSummery), VALUE_TYPE.STRING)

        fun getCartData(): CartSummary? {
            val gson = getPrefVal(PREF_CART_DATA, "", VALUE_TYPE.STRING) as String
            try {
                return Gson().fromJson<CartSummary>(gson, CartSummary::class.java) as CartSummary
            } catch (exp: Exception) {
            }
            return null
        }
    }
}