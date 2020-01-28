package com.toolskart.android.model

import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.MyAddress

data class UpdateAddress(
        val result: ArrayList<MyAddress>?
) : CommonRes()