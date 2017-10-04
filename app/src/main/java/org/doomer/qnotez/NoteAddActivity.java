package org.doomer.qnotez;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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

import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.viewmodel.NoteAddViewModel;
import org.doomer.qnotez.utils.ThemeChanger;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.Date;

public class NoteAddActivity extends AppCompatActivity {

    @BindView(R.id.edit_title)
    protected EditText editTitle;

    @BindView(R.id.edit_text)
    protected EditText editText;

    @BindView(R.id.txt_updated)
    TextView txtUpdated;

    private NoteAddViewModel addViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.setFromSettings(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtUpdated.setText("");

        addViewModel = ViewModelProviders.of(this).get(NoteAddViewModel.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() == 0) {
                    Snackbar.make(view, getString(R.string.warning_emty_text), Snackbar.LENGTH_LONG)
                        .show();
                } else {
                    NoteModel newNote = new NoteModel(editTitle.getText().toString(),
                            editText.getText().toString(), new Date());
                    addViewModel.addNote(newNote);
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
