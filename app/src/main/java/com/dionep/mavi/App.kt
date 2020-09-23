package com.dionep.mavi

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by Damir Rakhimulin on 23.09.2020.
 * damirpq1@gmail.com
 * tg: dima2828
 */
class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

    }

    override fun androidInjector() = dispatchingAndroidInjector

}