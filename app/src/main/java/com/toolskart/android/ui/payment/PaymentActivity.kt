package com.toolskart.android.ui.payment

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : BaseActivity() {
    private val TAG = "PaymentActivity"
    private lateinit var source: String
    private lateinit var snackbbar: View
    private var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_payment, fragmentLayout)
        mContext = this
        init()
    }

    fun init() {
        snackbbar = payment_snackbar
        tvTitle?.text = getString(R.string.payment)
        hideCartView()
        showBack()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
