package com.toolskart.android.ui.base

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.internal.ScrimInsetsFrameLayout
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.ui.navigationdrawer.NavigationDrawerFragment
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.MyApplication
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_header.*
import javax.inject.Inject

open class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var myApp: MyApplication

    @Inject
    lateinit var appShredPref: AppSharedPreference

    var toolbar: Toolbar? = null
    var drawerLayout: DrawerLayout? = null
    var fragmentLayout: LinearLayout? = null
    var tvTitle: AppCompatTextView? = null
    var appbar: AppBarLayout? = null
    var scrimInsetsFrameLayout: ScrimInsetsFrameLayout? = null
    var leftMenuFrag: FrameLayout? = null
    var iv_cart: AppCompatImageView? = null
    private var sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        initScreen()
        setSupportActionBar(toolbar)
        showMenu()
        setUpNavigationDrawer()
    }


    private fun initScreen() {
        fragmentLayout = findViewById<View>(R.id.fragment_layout) as LinearLayout
        tvTitle = findViewById<View>(R.id.tv_title) as AppCompatTextView
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        iv_cart = findViewById<View>(R.id.iv_cart) as AppCompatImageView
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        scrimInsetsFrameLayout = findViewById<View>(R.id.scrim_insets_frameLayout) as ScrimInsetsFrameLayout
        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        leftMenuFrag = findViewById<View>(R.id.navigation_drawer) as FrameLayout
        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun setUpNavigationDrawer() {
        lateinit var newFragment: Fragment
        val ft = supportFragmentManager.beginTransaction()
        newFragment = NavigationDrawerFragment.newInstance()
        ft.replace(R.id.navigation_drawer, newFragment).commit()
        newFragment.setDrawer(drawerLayout!!)
    }

    internal fun showMenu() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun showBack() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun setProfiletoolbar() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar?.setBackgroundColor(resources.getColor(R.color.colorWhite))
    }

    fun setToolbarScrollFlags(setFlags: Boolean = false) {
        val params: AppBarLayout.LayoutParams = toolbar!!.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (setFlags) AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS else 0
    }

    override fun onResume() {
        super.onResume()
        registerSharedPreferenceChangeListener()
        updateCartCount()
    }

    private fun updateCartCount() {
        AppSharedPreference.getCartData()?.apply {
            if (!cart_count.isNullOrEmpty() && cart_count!!.toInt() > 0) {
                cart_badge.text = cart_count
                cart_badge.visibility = View.VISIBLE
            } else
                cart_badge.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterSharedPreferenceChangeListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun registerSharedPreferenceChangeListener() {
       // appShredPref.mSharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            key?.let {
                if (it.contentEquals(AppSharedPreference.PREF_CART_DATA)) {
                    updateCartCount()
                }
            }
        }
        AppSharedPreference.getPrefs().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    private fun unregisterSharedPreferenceChangeListener() {
        appShredPref.mSharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    fun hideCartView() {
        cart_layout!!.visibility = View.GONE
    }
}