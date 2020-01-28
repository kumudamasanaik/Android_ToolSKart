package com.toolskart.android.model

data class Customer(
        val _id: String?="",
        val birthday: Any?="",
        val customer_type: String?="",
        val email: String?="",
        val email_verified: String?="",
        val facebook_id: String?="",
        val gender: String?="",
        val google_id: String?="",
        val id_deliveryboy: String?="",
        val last_name: String?="",
        val mobile: String?="",
        val name: String?="",
        val newsletter: String?="",
        val note: Any?="",
        val otp: String?="",
        val pic: String?="",
        val referral_code: String?="",
        val referral_used: String?="",
        val referred_amount: String?="",
        val referred_by: String?="",
        val referred_code: String?="",
        val verified: String?="",
        val wallet: String?="",
        val website: Any?="",

        /*verify otp */
        val first_name: String?=null,
        val updated: String?="",

        val address: String?="",

       /*get all address*/
        val block: String?,
        val password: String?

): CommonRes()