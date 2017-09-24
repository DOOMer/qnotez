package org.doomer.qnotez;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.viewmodel.NoteDetailViewModel;
import org.doomer.qnotez.utils.ThemeChanger;

import java.text.SimpleDateFormat;

public class NoteDetailActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    public static final String KEY_NOTE_ID = "qnotez_key_note_id";

    @BindView(R.id.edit_title)
    protected EditText editTitle;

    @BindView(R.id.edit_text)
    protected EditText editText;

    @BindView(R.id.txt_updated)
    protected TextView txtUpdated;

    private NoteDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.setFromSettings(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() == 0) {
                    Snackbar.make(view, getString(R.string.warning_emty_text), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    NoteModel editNote = viewModel.getNoteItem().getValue();
                    editNote.setTitle(editTitle.getText().toString());
                    editNote.setText(editText.getText().toString()    );

                    viewModel.updateItem(editNote);
                    Toast.makeText(NoteDetailActivity.this, "Saved", Toast.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent in = getIntent();
        int noteId = in.getIntExtra(KEY_NOTE_ID, 0);
        viewModel = ViewModelProviders.of(this).get(NoteDetailViewModel.class);

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
                    editTitle.setText(noteModel.getTitle());
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
}
