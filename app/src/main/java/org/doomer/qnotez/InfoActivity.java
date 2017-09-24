package org.doomer.qnotez;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import org.doomer.qnotez.utils.ThemeChanger;
import org.doomer.qnotez.utils.TextUtils;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.text_version)
    protected TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.setFromSettings(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillVersion();
    }

    private void fillVersion() {
        String version = getString(R.string.version) + ": " + TextUtils.getVersionString();
        version += " - " + TextUtils.getBuildTime();
        txtVersion.setText(version);
    }

}
