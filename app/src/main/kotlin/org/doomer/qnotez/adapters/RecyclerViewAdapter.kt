package org.doomer.qnotez.adapters

import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.doomer.qnotez.di.App
import org.doomer.qnotez.R
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.utils.TextUtils

import java.text.SimpleDateFormat

import kotlinx.android.synthetic.main.recycler_item.view.*


class RecyclerViewAdapter(private var noteModelList: List<NoteModel>,
                          private var sp: SharedPreferences,
                          private val clickListener: View.OnClickListener,
                          private val longClickListener: View.OnLongClickListener)
    : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item_title = itemView.item_title
        var item_text = itemView.item_text
        var item_updated = itemView.item_updated
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // get item from list by positin
        val note = noteModelList!![position]

        val rowCount = countOfDisplayedTextRows()
        when (rowCount) {
            0 -> holder.item_text.visibility = View.INVISIBLE
            else -> {
            }
        }

        // bind values to view in holder
        if (rowCount != 0) {
            val text = TextUtils.rowsToPreview(rowCount, note.text!!)
            holder.item_text.text = text
        }

        var title = note.title

        if (title!!.isEmpty()) {
            title = holder.item_text.context.getString(R.string.iten_no_title)
        }

        holder.item_title.text = title

        val sdf = SimpleDateFormat("dd MMM yyyy k:mm")
        val createdString = sdf.format(note.updated)

        holder.item_updated.text = createdString

        holder.itemView.tag = note
        holder.itemView.setOnClickListener(clickListener)
        holder.itemView.setOnLongClickListener(longClickListener)
    }

    private fun countOfDisplayedTextRows(): Int {

        // TODO - make get key from string resvia app context
        val key = App.appContext!!.getString(R.string.settings_key_count_rows)

        return Integer.parseInt(sp.getString(key, "-1"))
    }

    override fun getItemCount(): Int {
        return noteModelList!!.size
    }

    fun addItems(noteList: List<NoteModel>) {
        noteModelList = noteList
        notifyDataSetChanged()
    }


}
