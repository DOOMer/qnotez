package org.doomer.qnotez;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import org.doomer.qnotez.consts.NoteActions;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.ActivityUtils;
import org.doomer.qnotez.utils.Dialogs;
import org.doomer.qnotez.viewmodel.NoteDetailViewModel;
import org.doomer.qnotez.utils.ThemeChanger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    public static final String KEY_NOTE_ID = "qnotez_key_note_id";

    @BindView(R.id.edit_title)
    protected EditText editTitle;

    @BindView(R.id.edit_text)
    protected EditText editText;

    @BindView(R.id.txt_updated)
    protected TextView txtUpdated;

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    private NoteDetailViewModel viewModel;
    private int backAction = -1;
    private boolean readOnlyNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.setFromSettings(this);
        backAction = ActivityUtils.noteEditBackBehavior(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() == 0) {
                    Snackbar.make(view, getString(R.string.warning_emty_text), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    saveNote();
//                    NoteModel editNote = viewModel.getNoteItem().getValue();
//                    editNote.setTitle(editTitle.getText().toString());
//                    editNote.setText(editText.getText().toString()    );
//
//                    viewModel.updateItem(editNote);
                    Toast.makeText(NoteDetailActivity.this, "Saved", Toast.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent in = getIntent();
        int noteId = in.getIntExtra(KEY_NOTE_ID, 0);
        viewModel = ViewModelProviders.of(this).get(NoteDetailViewModel.class);

        readOnlyNote = in.getBooleanExtra(NoteActions.NOTE_READ_ONLY, false);

        if (readOnlyNote) {
            fab.setVisibility(View.INVISIBLE);
            editTitle.setFocusable(false);
            editText.setFocusable(false);
            setTitle(getString(R.string.title_activity_note_detail_ro));
        }

        subscribe();

        viewModel.getData(noteId);


    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }

    public void subscribe() {
        final Observer<NoteModel> noteObserver = new Observer<NoteModel>() {
            @Override
            public void onChanged(@Nullable NoteModel noteModel) {

                if (noteModel != null) {

                    String title = noteModel.getTitle();

                    if (readOnlyNote) {
                        if (title.isEmpty()) {
                            title = getString(R.string.iten_no_title);
                        }
                    }

                    editTitle.setText(title);
                    editText.setText(noteModel.getText());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy k:mm");
                    String createdString = sdf.format(noteModel.getCreated());
                    String updatedString = sdf.format(noteModel.getUpdated());

                    String dateString = getString(R.string.txt_created) + " " + createdString;

                    if ( !noteModel.getCreated().equals(noteModel.getUpdated())  ) {
                        dateString += "\n" + getString(R.string.txt_updated) + " " + updatedString;
                    }

                    txtUpdated.setText(dateString);
                }
            }
        };

        viewModel.getNoteItem().observe(this, noteObserver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                checkForSave();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        checkForSave();
    }

    private void checkForSave() {
        if (!readOnlyNote) {
            switch (backAction) {
                case ActivityUtils.BACK_SAVE_AUTO:
                    if (editText.getText().toString().isEmpty()) {
                        emptyTextWarning();
                    } else {
                        saveNote();
                        finish();
                    }
                    break;
                case ActivityUtils.BACK_SAVE_CONFIRM:
                    int strIdTitle = R.string.msg_warning;
                    int strIdContent = R.string.msg_note_save_text;

                    MaterialDialog saveConfirm = Dialogs.createConfirmDialog(this,
                            strIdTitle, strIdContent, new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if (editText.getText().toString().isEmpty()) {
                                        emptyTextWarning();
                                    } else {
                                        saveNote();
                                        finish();
                                    }
                                }
                            }, new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            });
                    saveConfirm.show();
                    break;
                case ActivityUtils.BACK_SAVE_NO:
                    finish();
                    break;
            }
        } else {
            finish();
        }
    }

    private void saveNote() {
        NoteModel editNote = viewModel.getNoteItem().getValue();
        editNote.setTitle(editTitle.getText().toString());
        editNote.setText(editText.getText().toString()    );

        viewModel.updateItem(editNote);
    }

    private void emptyTextWarning() {
        Snackbar.make(editText, getString(R.string.warning_emty_text), Snackbar.LENGTH_LONG)
                .show();
    }
}
