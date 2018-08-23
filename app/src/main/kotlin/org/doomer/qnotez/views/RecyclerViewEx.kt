package org.doomer.qnotez.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

class RecyclerViewEx : RecyclerView {

    private var textForEmpty: TextView? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkOnEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkOnEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkOnEmpty()
        }
    }

    constructor(ctx: Context) : super(ctx) {}

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {}

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int) : super(ctx, attrs, defStyle) {}

    fun checkOnEmpty() {
        if (textForEmpty != null && adapter != null) {
            val emptyTextVisible = adapter.itemCount == 0
            textForEmpty!!.visibility = if (emptyTextVisible) View.VISIBLE else View.INVISIBLE
            visibility = if (emptyTextVisible) View.INVISIBLE else View.VISIBLE
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()

        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)

        adapter?.registerAdapterDataObserver(observer)

        checkOnEmpty()
    }

    fun setEmptyTextView(txtView: TextView) {
        textForEmpty = txtView
        checkOnEmpty()
    }
}
