package org.doomer.qnotez.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import android.content.Intent
import android.content.pm.ActivityInfo
import android.util.Log

import org.doomer.qnotez.R

object ThemeChanger {
    val USE_DARK_DEFAULT = false

    fun setFromSettings(ctx: Context) {
        val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
        val ket = ctx.getString(R.string.settings_key_darkskin)

        val useDark = sp.getBoolean(ket, USE_DARK_DEFAULT)

        Log.d("USE DARK", useDark.toString())

        if (useDark) {
            ctx.setTheme(R.style.AppThemeDark)
        } else {
            ctx.setTheme(R.style.AppThemeLight)
        }
    }
}
