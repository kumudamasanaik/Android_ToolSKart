package com.toolskart.android.model

data class PrivacyPolicyRes(
        val result: PrivacyResult?
):CommonRes()
    data class PrivacyResult(
            val privacy_policy: ArrayList<PrivacyPolicy?>?
    )
        data class PrivacyPolicy(
                val _id: String?,
                val name: String?,
                val type: String?
        )