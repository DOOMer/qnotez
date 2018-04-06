package org.doomer.qnotez;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.utils.Dialogs;
import org.doomer.qnotez.viewmodel.NoteAddViewModel;

import org.doomer.qnotez.utils.ActivityUtils;
import org.doomer.qnotez.utils.ThemeChanger;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Date;

public class NoteAddActivity extends AppCompatActivity {

    @BindView(R.id.edit_title)
    protected EditText editTitle;

    @BindView(R.id.edit_text)
    protected EditText editText;

    @BindView(R.id.txt_updated)
    TextView txtUpdated;

    private NoteAddViewModel addViewModel;

    private int backAction = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.setFromSettings(this);
        backAction = ActivityUtils.noteEditBackBehavior(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        ButterKnife.bind(this);

        setTitle(getString(R.string.title_activity_note_add));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtUpdated.setText("");

        Intent inSended = getIntent();
        String actiion = inSended.getAction();
        String type = inSended.getType();

        if (Intent.ACTION_SEND.equals(actiion) && type != null) {
            if (("text/plain".equals(type))) {
                dataFromIntent(inSended);
            }
        }

        addViewModel = ViewModelProviders.of(this).get(NoteAddViewModel.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() == 0) {
                    emptyTextWarning();
                } else {
                    saveNote();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void dataFromIntent(Intent in) {
        String sharedTitle = in.getStringExtra(Intent.EXTRA_SUBJECT);
        String sharedText = in.getStringExtra(Intent.EXTRA_TEXT);

        if (sharedTitle != null) {
            editTitle.setText(sharedTitle);
        }

        if (sharedText != null) {
            editText.setText(sharedText);
        }

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
        switch (backAction) {
            case ActivityUtils.BACK_SAVE_AUTO:
                if (editText.getText().toString().isEmpty()) {
                    emptyTextWarning();
                } else {
                    saveNote();
                }
                break;
            case ActivityUtils.BACK_SAVE_CONFIRM:
                int strIdTitle = R.string.msg_warning;
                int strIdContent = R.string.msg_note_delete_text;

                MaterialDialog saveConfirm = Dialogs.createConfirmDialog(this,
                        strIdTitle, strIdContent, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (editText.getText().toString().isEmpty()) {
                                    emptyTextWarning();
                                } else {
                                    saveNote();
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
    }

    private void saveNote() {
        NoteModel newNote = new NoteModel(editTitle.getText().toString(),
                editText.getText().toString(), new Date());
        addViewModel.addNote(newNote);
        finish();
    }

    private void emptyTextWarning() {
        Snackbar.make(editText, getString(R.string.warning_emty_text), Snackbar.LENGTH_LONG)
                .show();
    }
}
