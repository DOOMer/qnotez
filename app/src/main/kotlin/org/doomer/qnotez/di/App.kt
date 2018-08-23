package org.doomer.qnotez.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        // initialize Dagger
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    // this is required to setup Dagger2 for Activity
    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    companion object {
        var appContext: Context? = null
            private set
    }
}