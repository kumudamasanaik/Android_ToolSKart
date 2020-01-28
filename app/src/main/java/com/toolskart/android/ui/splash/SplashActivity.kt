package com.toolskart.android.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.toolskart.android.R
import com.toolskart.android.ui.home.HomeActivity
import com.toolskart.android.ui.login.LoginActivity
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.CommonUtils
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var appShredPref: AppSharedPreference
    internal lateinit var mContext: Context
    var TAG: String = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this
    }

    override fun onResume() {
        super.onResume()
        navigateScreen()
        /* Handler().postDelayed(Runnable {
            *//* if (appShredPref.isLogIn()) {
                CommonUtils.goToDashBoard(this)
            } else {
                CommonUtils.goToLandingPage(this)
            }*//*
            CommonUtils.goToLandingPage(this)
            finish()
        }, 1000)*/
    }

    private fun navigateScreen() {
        Thread(Runnable {
            Thread.sleep(3000)
            this@SplashActivity.runOnUiThread {
                if (CommonUtils.isUserLogin())
                    navigateToHomeScreen()
                else
                    navigateToLoginScreen()
            }
        }).start()
    }

    fun navigateToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun navigateToHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
