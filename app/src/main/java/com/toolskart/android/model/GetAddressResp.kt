package com.toolskart.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class GetAddressResp(
        val result: ArrayList<MyAddress>?
) : CommonRes(), Parcelable

@Parcelize

data class MyAddress(
        val _id: String?,
        val address1: String?,
        val address2: String?,
        val alias: String?,
        val city: String?,
        val company: String?,
        val country: String?,
        val district: String?,
        val id_customer: String?,
        val landmark: String?,
        val lat: String?,
        val lon: String?,
        val name: String?,
        val person_prefix: String?,
        val phone: String?,
        val pincode: String?,
        val selected: String?,
        val state: String?,
        val taluk: String?,
        val type: String?
) : Parcelable {

    fun getAddressString(): String {
        return StringBuilder().append("#").append(address1).append("," + " ").append(address2).append(pincode).toString()
    }
}