package com.toolskart.android.ui.dialog

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.Gravity
import android.view.WindowManager
import com.toolskart.android.R
import com.toolskart.android.utils.getScreenWidth
import dagger.android.support.DaggerAppCompatDialogFragment

open class BaseDialog : DaggerAppCompatDialogFragment() {

    lateinit var mContext: Context
    lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mContext = requireContext()
            mActivity = requireActivity()
        } catch (exp: Exception) {
            exp as IllegalStateException
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.customDialogFragment)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        window.setLayout(mContext.getScreenWidth() - 80,
                WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(true)
    }


}