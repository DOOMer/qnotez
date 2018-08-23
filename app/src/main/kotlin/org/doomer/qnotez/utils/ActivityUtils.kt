package org.doomer.qnotez.utils

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import org.doomer.qnotez.R
import org.doomer.qnotez.ui.fragments.MainFragment
import org.doomer.qnotez.ui.fragments.TrashFragment


object ActivityUtils {
    val BACK_SAVE_NO = 0
    val BACK_SAVE_CONFIRM = 1
    val BACK_SAVE_AUTO = 2

    fun noteEditBackBehavior(ctx: Context): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(ctx)
        val key = ctx.getString(R.string.settings_key_back_action_note_edit)

        return Integer.parseInt(sp.getString(key, BACK_SAVE_CONFIRM.toString()))
    }

    // DEPRECATED on 3.0
    // DELETED on 3.x
    // TODO - change it on androidx.arch.navigation
    fun changeFragment(act: AppCompatActivity, layoutId: Int, tag: String) {

        var frg: Fragment? = null

        if (tag == TrashFragment.FRAGMENT_TAG) {
            frg = TrashFragment()
        } else {
            frg = MainFragment()
        }

        if (frg != null) {
            act.supportFragmentManager.beginTransaction()
                    .replace(layoutId, frg, tag)
                    .commit()
        }
    }
}

fun Activity.showMessageSnack(view : View, msg : String) {
    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
}

fun Activity.showMessageToast(msg : String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}


