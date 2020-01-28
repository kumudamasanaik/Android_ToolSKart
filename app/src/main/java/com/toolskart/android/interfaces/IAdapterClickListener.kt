

package com.toolskart.android.interfaces

interface IAdapterClickListener {
    fun onclick(data: Any, pos: Int = 0, type: String = "none", op: String = "none")
}