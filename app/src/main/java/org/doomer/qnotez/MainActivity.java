package org.doomer.qnotez;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.support.design.widget.NavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.doomer.qnotez.adapters.RecyclerViewAdapter;
import org.doomer.qnotez.consts.NoteActions;
import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.viewmodel.NoteListViewModel;
import org.doomer.qnotez.utils.Dialogs;
import org.doomer.qnotez.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnClickListener, OnLongClickListener,
        LifecycleRegistryOwner {

    private LifecycleRegistry registryOwnder = new LifecycleRegistry(this);

    private NoteModel selectedItem;
    private NoteListViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent inAdd = new Intent(MainActivity.this, NoteAddActivity.class);
            startActivity(inAdd);
            }
        });

        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<NoteModel>(), this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);

        // load items and make observer
        viewModel.getNoteItems().observe(MainActivity.this, new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(@Nullable List<NoteModel> noteItems) {
                recyclerViewAdapter.addItems(noteItems);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return registryOwnder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onLongClick(View view) {
        selectedItem = (NoteModel) view.getTag();

        MaterialDialog itemMenu = Dialogs.createListDialog(this, R.id.item_title,
                R.array.item_action_names, itemSelectCallback);
        itemMenu.show();
        return true;
    }

    private MaterialDialog.ListCallback itemSelectCallback = new ListCallback() {
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
                        NoteUtils.shareNote(selectedItem, MainActivity.this);
                    }
                    break;
                case NoteActions.ACTION_DELETE:
                    if (selectedItem != null) {
                        viewModel.deleteItem(selectedItem);
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        NoteModel note = (NoteModel) view.getTag();
        editItem(note.id);
    }

    private void editItem(int id) {
        Intent di = new Intent(this, NoteDetailActivity.class);
        di.putExtra(NoteDetailActivity.KEY_NOTE_ID, id);
        startActivity(di);
    }
}
