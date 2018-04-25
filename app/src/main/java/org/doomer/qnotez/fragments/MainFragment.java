package org.doomer.qnotez.fragments;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.doomer.qnotez.ui.NoteDetailActivity;
import org.doomer.qnotez.R;
import org.doomer.qnotez.adapters.RecyclerViewAdapter;
import org.doomer.qnotez.consts.NoteActions;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.Dialogs;
import org.doomer.qnotez.utils.NoteUtils;
import org.doomer.qnotez.viewmodel.NoteListViewModel;
import org.doomer.qnotez.views.RecyclerViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements OnClickListener, OnLongClickListener {

    public static final String FRAGMENT_TAG = "MainFragment";

    private NoteModel selectedItem;
    private NoteListViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;

    @BindView(R.id.recycler_view)
    RecyclerViewEx recyclerView;

    @BindView(R.id.empty_text_view)
    TextView textEmptyView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        textEmptyView.setText(getString(R.string.txt_empty_notes_list));
        textEmptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_for_empty_list));

        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<NoteModel>(), this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setEmptyTextView(textEmptyView);

        noteTouchHelper.attachToRecyclerView(recyclerView);

        viewModel = ViewModelProviders.of(getActivity()).get(NoteListViewModel.class);
        viewModel.setShowTrash(false);

        // load items and make observer
        viewModel.getNoteItems().observe((LifecycleOwner) getActivity(), new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(@Nullable List<NoteModel> noteItems) {
                recyclerViewAdapter.addItems(noteItems);
            }
        });

        getActivity().setTitle(getString(R.string.app_name));

        return rootView;
    }

    @Override
    public boolean onLongClick(View view) {
        selectedItem = (NoteModel) view.getTag();

        MaterialDialog itemMenu = Dialogs.createListDialog(getActivity(), R.string.title_item_actions,
                R.array.item_action_names, itemSelectCallback);
        itemMenu.show();
        return true;
    }

    private MaterialDialog.ListCallback itemSelectCallback = new MaterialDialog.ListCallback() {
        @Override
        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

            switch (position) {
                case NoteActions.ACTION_VIEW:
                    if (selectedItem != null) {
                        editItem(selectedItem.id);
                    }
                    break;
                case NoteActions.ACTION_SHARE:
                    if (selectedItem != null) {
                        NoteUtils.shareNote(selectedItem, getActivity());
                    }
                    break;
                case NoteActions.ACTION_DELETE:
                    if (selectedItem != null) {
                        int strIdTitle = R.string.msg_warning;
                        int strIdContent = R.string.msg_note_delete_text;

                        // TODO add chek settings for move note to trashbox or direct delete
                        MaterialDialog itemDelete = Dialogs.createConfirmDialog(MainFragment.this.getActivity(),
                                strIdTitle, strIdContent,itemDeleteCallback, null);
                        itemDelete.show();
                    }
                    break;
            }
        }
    };

    private MaterialDialog.SingleButtonCallback itemDeleteCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            // TODO add chek settings for move note to trashbox or direct delete
            viewModel.moveToTrash(selectedItem);

        }
    };

    @Override
    public void onClick(View view) {
        NoteModel note = (NoteModel) view.getTag();
        editItem(note.id);
    }

    private void editItem(int id) {
        Intent di = new Intent(getActivity(), NoteDetailActivity.class);
        di.putExtra(NoteDetailActivity.KEY_NOTE_ID, id);
        startActivity(di);
    }

    public void quickSearch(String query) {
        viewModel.quickSearch(query, false).observe((LifecycleOwner) getActivity(), new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(@Nullable List<NoteModel> noteItems) {
                recyclerViewAdapter.addItems(noteItems);
            }
        });
    }

    private ItemTouchHelper.SimpleCallback noteTouchCallback = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
    ) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            NoteModel item = (NoteModel) viewHolder.itemView.getTag();
            viewModel.moveToTrash(item);
            recyclerViewAdapter.notifyDataSetChanged();
            Snackbar.make(recyclerView, getString(R.string.snack_msg_move_to_trash), Snackbar.LENGTH_LONG)
                    .show();
        }
    };

    private ItemTouchHelper noteTouchHelper = new ItemTouchHelper(noteTouchCallback);

}
