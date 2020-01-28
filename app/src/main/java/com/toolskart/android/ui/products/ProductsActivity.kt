package com.toolskart.android.ui.products

import android.os.Bundle
import android.view.MenuItem
import com.toolskart.android.R
import com.toolskart.android.ui.base.BaseActivity

class ProductsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_products, fragmentLayout)
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
