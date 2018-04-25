package org.doomer.qnotez.ui;

import android.app.SearchManager;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.content.Context;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.doomer.qnotez.R;
import org.doomer.qnotez.adapters.RecyclerViewAdapter;
import org.doomer.qnotez.db.AppDatabase;
import org.doomer.qnotez.db.NoteModel;
import org.doomer.qnotez.fragments.MainFragment;
import org.doomer.qnotez.fragments.TrashFragment;
import org.doomer.qnotez.viewmodel.NoteListViewModel;
import org.doomer.qnotez.utils.ActivityUtils;
import org.doomer.qnotez.utils.ThemeChanger;

import butterknife.BindView;
import butterknife.ButterKnife;

import org.doomer.qnotez.utils.ChangeLog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnQueryTextListener, OnCloseListener,
        LifecycleRegistryOwner {

    private LifecycleRegistry registryOwnder = new LifecycleRegistry(this);
    private Menu mainMenu = null;

    private NoteModel selectedItem;
    private NoteListViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private final String KEY_CURRENT_FRG = "saved_current_fragment";
    private String curFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChanger.setFromSettings(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent inAdd = new Intent(MainActivity.this, NoteAddActivity.class);
            startActivity(inAdd);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Associate searchable configuration with the SearchView
        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        if (savedInstanceState != null) {
            curFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRG);
        } else {
            curFragmentTag = MainFragment.FRAGMENT_TAG;
        }
        ActivityUtils.changeFragment(this, R.id.main_content, curFragmentTag);

        ChangeLog.showWhatsNew(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_CURRENT_FRG, curFragmentTag);

        super.onSaveInstanceState(outState);
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
        mainMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_qs) {
            searchView.setIconified(false);
            searchView.setVisibility(View.VISIBLE);
            item.setVisible(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Intent navIntent = null;

        switch (item.getItemId()) {
            case R.id.nav_main:
                curFragmentTag = MainFragment.FRAGMENT_TAG;
                ActivityUtils.changeFragment(this, R.id.main_content, MainFragment.FRAGMENT_TAG);
                showFab(true);
                break;

            case R.id.nav_trash:
                curFragmentTag = TrashFragment.FRAGMENT_TAG;
                ActivityUtils.changeFragment(this, R.id.main_content, TrashFragment.FRAGMENT_TAG);
                showFab(false);
                break;

            case R.id.nav_info:
                navIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(navIntent);
                break;

            case R.id.nav_settings:
                navIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(navIntent);
                break;

            default:
                navIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(navIntent);
        }

//        startActivity(navIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // TODO - create common base fragment class and inherit from it other fragments (main, trash, etc)
        if (curFragmentTag.equals(TrashFragment.FRAGMENT_TAG)) {
            TrashFragment frg = (TrashFragment) getSupportFragmentManager().findFragmentByTag(curFragmentTag);
            frg.quickSearch(newText);
        } else {
            MainFragment frg = (MainFragment) getSupportFragmentManager().findFragmentByTag(curFragmentTag);
            frg.quickSearch(newText);
        }
        return false;
    }

    /**
     * Call by searchview closing
     */
    @Override
    public boolean onClose() {
        MenuItem qs = mainMenu.findItem(R.id.action_qs);

        if (qs != null) {
            qs.setVisible(true);
            searchView.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    public void showFab(boolean show) {
        if (show) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }
}
