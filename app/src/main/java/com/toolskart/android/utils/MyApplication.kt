package com.toolskart.android.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.StrictMode
import android.support.multidex.MultiDex
import android.view.WindowManager
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.toolskart.android.BuildConfig
import com.toolskart.android.api.ApiConstants
import com.toolskart.android.api.ApiService
import com.toolskart.android.di.AppComponent
import com.toolskart.android.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MyApplication : DaggerApplication(), Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var apiService: ApiService

    var currAct: String = ""

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var myApplication: MyApplication
        var mActivity: Activity? = null
        lateinit var service: ApiService
        lateinit var context: Context
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {

        super.onCreate()
        myApplication = this
        context = this

        val appSignature = AppSignatureHelper(this)
        appSignature.appSignatures
        createRetroFitObject()

        registerActivityLifecycleCallbacks(this)
        Stetho.initializeWithDefaults(this)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build())

    }

    private fun createRetroFitObject() {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(getRequestHeader())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(ApiService::class.java)

    }

    private fun getRequestHeader(): OkHttpClient {
        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.readTimeout(60, TimeUnit.SECONDS)
        okhttpClient.writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okhttpClient.addNetworkInterceptor(StethoInterceptor())
            okhttpClient.addInterceptor(logging)
        }
        return okhttpClient.build()


    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mActivity = activity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onActivityStarted(activity: Activity) {
        mActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        mActivity = activity
        currAct = activity!!::class.java.simpleName
    }

    override fun onActivityPaused(activity: Activity) {
        mActivity = null
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}