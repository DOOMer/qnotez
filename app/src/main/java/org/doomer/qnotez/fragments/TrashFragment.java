package org.doomer.qnotez.fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.doomer.qnotez.NoteDetailActivity;
import org.doomer.qnotez.R;
import org.doomer.qnotez.adapters.RecyclerViewAdapter;
import org.doomer.qnotez.consts.NoteActions;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.Dialogs;
import org.doomer.qnotez.utils.NoteUtils;
import org.doomer.qnotez.viewmodel.NoteListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrashFragment extends Fragment implements OnClickListener, OnLongClickListener {

    public static final String FRAGMENT_TAG = "TrashFragment";

    private NoteModel selectedItem;
    private NoteListViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<NoteModel>(), this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(getActivity()).get(NoteListViewModel.class);
        viewModel.setShowTrash(true);

        // load items and make observer
        viewModel.getNoteItems().observe((LifecycleOwner) getActivity(), new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(@Nullable List<NoteModel> noteItems) {
                recyclerViewAdapter.addItems(noteItems);
            }
        });

        getActivity().setTitle(getString(R.string.nav_list_trash));

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

                        MaterialDialog itemDelete = Dialogs.createConfirmDialog(TrashFragment.this.getActivity(),
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
            viewModel.deleteItem(selectedItem);

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
        viewModel.quickSearch(query).observe((LifecycleOwner) getActivity(), new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(@Nullable List<NoteModel> noteItems) {
                recyclerViewAdapter.addItems(noteItems);
            }
        });
    }
}
