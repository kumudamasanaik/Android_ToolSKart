package com.toolskart.android.utils

import com.toolskart.android.constants.Constants
import com.toolskart.android.model.CommonRes
import java.util.regex.Pattern

class Validation {

    companion object {
        private var EMAIL_PATTERN: String? = null

           fun isValidStatus(res: CommonRes?): Boolean {
               if (res!!.status!!.contentEquals(Constants.SUCCESS)) {
                   if (!res.key.isNullOrEmpty())
                       CommonUtils.setAuthorizationkey(res.key)
                   return true
               }
               return false
           }

        fun isValidObject(`object`: Any?): Boolean {
            return `object` != null
        }

        fun isValidString(string: String?): Boolean {
            return string != null && string.trim().isNotEmpty()
        }


        fun validateValue(value: String?): Boolean {
            return value != null && value.isNotEmpty()
        }

        fun isValidList(list: List<*>?): Boolean {
            return list != null && list.isNotEmpty()
        }
        fun isValidEmail(email: String): Boolean {
            EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }
        fun isValidMobileNumber(string: String?): Boolean {
            return string != null && string.trim().length == 10
        }
    }
}