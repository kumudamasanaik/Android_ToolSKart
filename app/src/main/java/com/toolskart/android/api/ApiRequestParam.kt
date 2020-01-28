package com.toolskart.android.api

import com.google.gson.JsonObject
import com.toolskart.android.constants.Constants
import com.toolskart.android.utils.AppSignatureHelper.TAG
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.MyLogUtils

object ApiRequestParam {
    var respParamObj = JsonObject()

    private fun addCustomerID(jsonObject: JsonObject) {
        if (CommonUtils.isUserLogin() && CommonUtils.getCustomerId().isNotEmpty()) {
           // jsonObject.addProperty(Constants._ID, CommonUtil.getCustomerId())
        }
    }

    fun getCommonParameter(jsonObject: JsonObject) {
        jsonObject.addProperty(Constants._ID, CommonUtils.getCustomerID())
    }

    fun getSession(jsonObject: JsonObject) {
        jsonObject.addProperty(Constants._SESSION, CommonUtils.getSession())
    }

    fun login(emailormobile: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.MOBILE, emailormobile)
        }
        return respParamObj
    }

    fun verifyOtp(otp: String, mobile:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.OTP, otp)
            addProperty(Constants.MOBILE, mobile)
        }
        return respParamObj
    }

    fun resendOtp(mobile:String): JsonObject {
        try {
            respParamObj = JsonObject()
            respParamObj.addProperty(Constants.MOBILE,mobile)
            //respParamObj.addProperty(Constants.SMS_KEY, CommonUtils.getOtpVerificationHashKey())
        } catch (exp: Exception) {
            MyLogUtils.e(TAG, exp.message!!)
        }
        return respParamObj
    }

    fun getHomeParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            if (CommonUtils.isUserLogin())
                getCommonParameter(respParamObj)
            getSession(respParamObj)
        }
        return respParamObj
    }

    fun getProductListParameters(lowCatId: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.ID_CATEGORY, lowCatId)
            getSession(respParamObj)
        }
        return respParamObj
    }

    fun getProductDetailParameters(product_id: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.LANG, "en")
            getSession(respParamObj)
            respParamObj.addProperty(Constants.ID_PRODUCT, product_id)
        }
        return respParamObj
    }

    fun myProfileRequest(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            if (CommonUtils.isUserLogin())
            getCommonParameter(respParamObj)
        }
        return respParamObj
    }

    fun updatedProfile(address:String,pic:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
                getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.ADDRESS,address)
            respParamObj.addProperty(Constants.PICDATA,pic)
        }
        return respParamObj
    }

    fun getNotificationData(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.CUSTOMER_ID, CommonUtils.getCustomerID())
        }
        return respParamObj
    }

    fun getCartParameters(op: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            when (op) {
                Constants.CART_GET -> {
                    getSession(respParamObj)
                    getCommonParameter(respParamObj)
                    respParamObj.addProperty(Constants.OP, Constants.CART_GET)
                }
            }
        }
        return respParamObj
    }

        fun getWishListParameters(op: String, productId: String? = ""): JsonObject {
            respParamObj = JsonObject()
            getCommonParameter(respParamObj)
            getSession(respParamObj)
            respParamObj.apply {
                when (op) {
                    Constants.GET ->
                        respParamObj.addProperty(Constants.OP, op)

                    Constants.CREATE -> {
                        respParamObj.addProperty(Constants.ID_PRODUCT, productId)
                        respParamObj.addProperty(Constants.OP, op)
                    }
                    Constants.DELETE -> {
                        respParamObj.addProperty(Constants.ID_PRODUCT, productId)
                        respParamObj.addProperty(Constants.OP, op)
                    }
                }
            }
            return respParamObj
        }

    fun getMyOrderParameters(status: String): JsonObject {
        respParamObj = JsonObject()
        getCommonParameter(respParamObj)

        respParamObj.apply {
            when (status) {
                Constants.PLACED ->
                    respParamObj.addProperty(Constants.status, status)
                Constants.DELIVERED ->
                    respParamObj.addProperty(Constants.status, status)
                Constants.CANCELLED ->
                    respParamObj.addProperty(Constants.status, status)
            }
        }
        return respParamObj
    }

    fun geCancelOrderParameters(status: String,order_no: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.ORDERED_STATUS, Constants.CANCELLED)
            respParamObj.addProperty(Constants.ORDER_NO, order_no)
        }
        return respParamObj
    }

    fun getMyOrderDetailsParameters(order_no: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.ORDER_NO, order_no)
        }
        return respParamObj
    }

    fun getReviewParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
        }
        return respParamObj
    }

    fun getSelectAddressRequestParameters(op:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.OP, Constants.GET)
        }
        return respParamObj
    }
}