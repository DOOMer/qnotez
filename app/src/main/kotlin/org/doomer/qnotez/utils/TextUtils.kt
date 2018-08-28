package org.doomer.qnotez.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

import java.text.SimpleDateFormat
import java.util.Date

import org.doomer.qnotez.di.App
import org.doomer.qnotez.BuildConfig

object TextUtils {

    val versionString: String
        get() {
            var versionString: String

            val appContext = App.appContext

            try {
                val packageInfor = appContext!!.packageManager.getPackageInfo(
                        appContext.packageName, 0
                )
                versionString = packageInfor.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                versionString = "Unknown"
            }

            return versionString
        }

    val buildTime: String
        get() {
            val buildDate = Date(BuildConfig.TIMESTAMP)

            val sdf = SimpleDateFormat("dd MMM yyyy k:mm")

            return sdf.format(buildDate)
        }

    fun filenameForBackup() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd--k-mm-ss")
        return "qnotez-backup-" + sdf.format(Date()) + ".json"
    }

    fun prepareToLikeQuery(text: String): String {
        return "%$text%"
    }

    fun rowsToPreview(count: Int, text: String): String {
        var retStr = String()
        val lines = text.split("\\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        when (count) {
            2 -> retStr = arrayToString(2, lines)
            5 -> retStr = arrayToString(5, lines)
            10 -> retStr = arrayToString(10, lines)
            else -> {
            }
        }

        return retStr
    }

    private fun arrayToString(count: Int, array: Array<String>): String {
        val strBuild = StringBuilder()

        when (array.size) {
            0 -> strBuild.append("None")
            1 -> strBuild.append(array[0])
            else -> {

                var i = 0
                while (i < count) {
                    if (i < array.size) {
                        strBuild.append(array[i])

                        if (i < count - 1) {
                            strBuild.append("\n")
                        } else {
                            strBuild.append(" ... ")
                        }
                    }
                    i = i + 1
                }
            }
        }



        return strBuild.toString()
    }
}
