package org.doomer.qnotez.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

public class RecyclerViewEx extends RecyclerView {

    private TextView textForEmpty = null;

   private final AdapterDataObserver observer = new AdapterDataObserver() {
       @Override
       public void onChanged() {
           checkOnEmpty();
       }

       @Override
       public void onItemRangeInserted(int positionStart, int itemCount) {
           checkOnEmpty();
       }

       @Override
       public void onItemRangeRemoved(int positionStart, int itemCount) {
           checkOnEmpty();
       }
   };

    public RecyclerViewEx(Context ctx) {
        super(ctx);
    }

    public RecyclerViewEx(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public RecyclerViewEx(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
    }

    public void checkOnEmpty() {
        if (textForEmpty != null && getAdapter() != null) {
            final boolean emptyTextVisible = getAdapter().getItemCount() == 0;
            textForEmpty.setVisibility(emptyTextVisible ? VISIBLE : INVISIBLE);
            setVisibility(emptyTextVisible ? INVISIBLE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();

        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkOnEmpty();
    }

    public void setEmptyTextView(TextView txtView) {
        textForEmpty = txtView;
        checkOnEmpty();
    }
}
