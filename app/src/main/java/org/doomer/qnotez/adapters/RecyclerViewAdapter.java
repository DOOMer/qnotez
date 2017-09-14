package org.doomer.qnotez.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtText;
        private TextView txtCreated;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);

            // TODO replace to butter knife
            txtText = (TextView) itemView.findViewById(R.id.itemText);
            txtTitle = (TextView) itemView.findViewById(R.id.titleView);
            txtCreated = (TextView) itemView.findViewById(R.id.createdView);
        }

//        @Override
//        public void onClick(View view) {
//            int position = getAdapterPosition();
//
//            if (position != RecyclerView.NO_POSITION) {
//                Log.d("ZZZZ", String.valueOf(position));
//
//                NoteModel note = (NoteModel) view.getTag();
//                Log.d("ZZZZ", String.valueOf(note.id));
//                Log.d("ZZZZ", note.getTitle());
//
//                // Intent i =
//
//            }
//        }
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

        // bind values to view in holder
        holder.txtText.setText(note.getText());
        holder.txtTitle.setText(note.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy k:mm");
        String createdString = sdf.format(note.getCreated());

        holder.txtCreated.setText(createdString);

        holder.itemView.setTag(note);
        holder.itemView.setOnClickListener(clickListener);
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
