package org.doomer.qnotez.utils

import android.content.Context

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.ListCallback

import org.doomer.qnotez.R

object Dialogs {
    fun createListDialog(context: Context, strIdTitle: Int, strIdArrayList: Int,
                         callback: ListCallback): MaterialDialog {

        val builder = MaterialDialog.Builder(context)
        builder.title(strIdTitle)
        builder.items(strIdArrayList)
        builder.itemsCallback(callback)

        return builder.build()
    }

    fun createConfirmDialog(context: Context,
                            strIdTitle: Int, strIdContent: Int,
                            positive: MaterialDialog.SingleButtonCallback?,
                            negative: MaterialDialog.SingleButtonCallback?): MaterialDialog {
        val builder = MaterialDialog.Builder(context)

        builder.title(strIdTitle)
        builder.content(strIdContent)
        builder.positiveText(context.getString(R.string.msg_yes))
        builder.negativeText(context.getString(R.string.msg_no))

        positive?.let {
            builder.onPositive(positive)
        }

        negative?.let {
            builder.onNegative(negative)
        }

        return builder.build()
    }

    fun createCustomSingleDialog(context: Context, strIdTitle: Int,
                                 custom_view: Int): MaterialDialog {

        val builder = MaterialDialog.Builder(context)
        builder.title(strIdTitle)
        builder.customView(custom_view, false)

        return builder.build()
    }
}
