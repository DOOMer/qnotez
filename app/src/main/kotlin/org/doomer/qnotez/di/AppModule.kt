package org.doomer.qnotez.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {
    @Provides
    @Singleton
    fun provideSharedPreference(app: Application): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(app)

}

