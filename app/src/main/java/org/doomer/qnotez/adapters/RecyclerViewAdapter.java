package org.doomer.qnotez.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.doomer.qnotez.App;
import org.doomer.qnotez.R;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.TextUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<NoteModel> noteModelList;
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title)
        protected TextView txtTitle;

        @BindView(R.id.item_text)
        protected TextView txtText;

        @BindView(R.id.item_updated)
        protected TextView txtUpdated;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public RecyclerViewAdapter(List<NoteModel> list,
                               View.OnClickListener listener,
                               View.OnLongClickListener longListener) {
        noteModelList = list;
        clickListener = listener;
        longClickListener = longListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        // get item from list by positin
        NoteModel note = noteModelList.get(position);

        int rowCount = countOfDisplayedTextRows();
        switch (rowCount) {
            case 0:
                holder.txtText.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        // bind values to view in holder
        if (rowCount != 0) {
            String text = TextUtils.rowsToPreview(rowCount, note.getText());
            holder.txtText.setText(text);
        }

        String title = note.getTitle();

        if (title.isEmpty()) {
            title = holder.txtTitle.getContext().getString(R.string.iten_no_title);
        }

        holder.txtTitle.setText(title);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy k:mm");
        String createdString = sdf.format(note.getUpdated());

        holder.txtUpdated.setText(createdString);

        holder.itemView.setTag(note);
        holder.itemView.setOnClickListener(clickListener);
        holder.itemView.setOnLongClickListener(longClickListener);
    }

    private int countOfDisplayedTextRows() {
        Context ctx = App.getAppContext();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String key = ctx.getString(R.string.settings_key_count_rows);

        return Integer.parseInt(sp.getString(key, "-1"));
    }

    @Override
    public int getItemCount() {
        return noteModelList.size();
    }

    public void addItems(List<NoteModel> noteList) {
        noteModelList = noteList;
        notifyDataSetChanged();
    }


}
