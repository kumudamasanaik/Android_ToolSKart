

package com.toolskart.android.di

import com.toolskart.android.utils.MyApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [AndroidSupportInjectionModule::class,
            AndroidInjectionModule::class,
            NetworkModule::class,
            AppModule::class,
            MyActivityBuilder::class])
interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyApplication>()

}