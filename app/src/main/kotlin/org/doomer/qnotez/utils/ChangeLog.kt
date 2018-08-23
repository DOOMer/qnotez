package org.doomer.qnotez.utils

import android.content.Context
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.michaelflisar.changelog.ChangelogBuilder
import com.michaelflisar.changelog.ChangelogUtil

object ChangeLog {
    private val PREF_KEY = "installedVersion"

    fun showWFullChangelog(activity: AppCompatActivity) {
        val builder = ChangelogBuilder()
        builder.buildAndShowDialog(activity, false)
    }

    fun showWhatsNew(activity: AppCompatActivity) {
        val codeSaved = readSavedVersionCode(activity)

        if (isNewVersion(activity, codeSaved)) {
            val builder = ChangelogBuilder()
            builder.withMinVersionToShow(codeSaved + 1)
            builder.buildAndShowDialog(activity, false)
        }

    }

    private fun isNewVersion(ctx: Context, codeInstaller: Int): Boolean {
        var result = false

        //        int codeSaved = readSavedVersionCode(ctx);
        val codeCurrent = ChangelogUtil.getAppVersionCode(ctx)

        if (codeInstaller < codeCurrent) {
            writeCurrentVersionCode(ctx)
            result = true
        }

        return result
    }

    private fun readSavedVersionCode(ctx: Context): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
        val verCode = sp.getInt(PREF_KEY, 0)
        Log.d("XXXXX", verCode.toString())
        return verCode
    }

    private fun writeCurrentVersionCode(ctx: Context) {
        val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
        val currentVerCode = ChangelogUtil.getAppVersionCode(ctx)
        sp.edit().putInt(PREF_KEY, currentVerCode).apply()
    }
}
