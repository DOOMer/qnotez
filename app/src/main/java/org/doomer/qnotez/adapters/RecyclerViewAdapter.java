package org.doomer.qnotez.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.doomer.qnotez.R;
import org.doomer.qnotez.db.NoteModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<NoteModel> noteModelList;
    private View.OnLongClickListener longClickListener;


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtText;
        private TextView txtCreated;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            // TODO replace to butter knife
            txtText = (TextView) itemView.findViewById(R.id.itemText);
            txtTitle = (TextView) itemView.findViewById(R.id.titleView);
            txtCreated = (TextView) itemView.findViewById(R.id.createdView);
        }
    }


    public RecyclerViewAdapter(List<NoteModel> list, View.OnLongClickListener listener) {
        noteModelList = list;
        longClickListener = listener;
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

        // bind values to view in holder
        holder.txtText.setText(note.getText());
        holder.txtTitle.setText(note.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy k:mm");
        String createdString = sdf.format(note.getCreated());

        holder.txtCreated.setText(createdString);

        holder.itemView.setTag(note);
        holder.itemView.setOnLongClickListener(longClickListener);
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
