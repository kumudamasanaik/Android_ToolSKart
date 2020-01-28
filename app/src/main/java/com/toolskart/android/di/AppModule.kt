

package com.toolskart.android.di

import android.content.Context
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.schedulers.BaseScheduler
import com.toolskart.android.utils.schedulers.Scheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: MyApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun baseScheduler(): BaseScheduler {
        return Scheduler()
    }

    @Singleton
    @Provides
    fun sharedPrefHelper(context: Context): AppSharedPreference = AppSharedPreference(context.getSharedPreferences("app_pref", Context.MODE_PRIVATE))

}