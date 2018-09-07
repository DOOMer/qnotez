package org.doomer.qnotez.utils

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment

fun Fragment.isPermissionGranted(permission: String) : Boolean =
        ActivityCompat.checkSelfPermission(activity!!, permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.shouldShowPermissionRationale(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)
