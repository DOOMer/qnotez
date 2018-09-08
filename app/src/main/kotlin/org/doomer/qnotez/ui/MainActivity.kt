package org.doomer.qnotez.ui

import android.app.SearchManager
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Intent
import android.content.Context

import android.os.Bundle
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.support.v7.widget.SearchView.OnCloseListener
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.app_bar_main.*

import org.doomer.qnotez.R
import org.doomer.qnotez.adapters.RecyclerViewAdapter
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.ui.fragments.BackupFragment
import org.doomer.qnotez.ui.fragments.MainFragment
import org.doomer.qnotez.ui.fragments.TrashFragment
import org.doomer.qnotez.viewmodel.NoteListViewModel
import org.doomer.qnotez.utils.ActivityUtils
import org.doomer.qnotez.utils.ThemeChanger

import org.doomer.qnotez.utils.ChangeLog
import org.doomer.qnotez.utils.hideKeyboard


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnQueryTextListener, OnCloseListener, LifecycleRegistryOwner {

    private val registryOwnder = LifecycleRegistry(this)
    private var mainMenu: Menu? = null

    private val selectedItem: NoteModel? = null
    private val viewModel: NoteListViewModel? = null
    private val recyclerViewAdapter: RecyclerViewAdapter? = null

    private val KEY_CURRENT_FRG = "saved_current_fragment"
    private var curFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        ThemeChanger.setFromSettings(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val inAdd = Intent(this@MainActivity, NoteAddActivity::class.java)
            startActivity(inAdd)
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        // Associate searchable configuration with the SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search_view.setOnQueryTextListener(this)
        search_view.setOnCloseListener(this)

        if (savedInstanceState != null) {
            curFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRG)
        } else {
            curFragmentTag = MainFragment.FRAGMENT_TAG
        }
        ActivityUtils.changeFragment(this, R.id.main_content, curFragmentTag!!)
        
        if (curFragmentTag != MainFragment.FRAGMENT_TAG) showFab(false)

        ChangeLog.showWhatsNew(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_CURRENT_FRG, curFragmentTag)

        super.onSaveInstanceState(outState)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return registryOwnder
    }

    override fun onDestroy() {
        super.onDestroy()
//        AppDatabase.destroyInstance()
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        mainMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_qs) {
            search_view.isIconified = false
            search_view.visibility = View.VISIBLE
            item.isVisible = false
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        var navIntent: Intent? = null

        when (item.itemId) {
            R.id.nav_main -> {
                curFragmentTag = MainFragment.FRAGMENT_TAG
                ActivityUtils.changeFragment(this, R.id.main_content, MainFragment.FRAGMENT_TAG)
                showFab(true)
            }

            R.id.nav_trash -> {
                curFragmentTag = TrashFragment.FRAGMENT_TAG
                ActivityUtils.changeFragment(this, R.id.main_content, TrashFragment.FRAGMENT_TAG)
                showFab(false)
            }

            R.id.nav_info -> {
                navIntent = Intent(this@MainActivity, InfoActivity::class.java)
                startActivity(navIntent)
            }

            R.id.nav_settings -> {
                navIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(navIntent)
            }

            R.id.nav_backup -> {
                curFragmentTag = BackupFragment.FRAGMENT_TAG
                search_view.visibility = View.GONE
                search_view.hideKeyboard()
                ActivityUtils.changeFragment(this, R.id.main_content, BackupFragment.FRAGMENT_TAG)
                showFab(false)
            }

            else -> {
                navIntent = Intent(this@MainActivity, InfoActivity::class.java)
                startActivity(navIntent)
            }
        }

        //        startActivity(navIntent);

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        // TODO - create common base fragment class and inherit from it other fragments (main, trash, etc)
        if (curFragmentTag == TrashFragment.FRAGMENT_TAG) {
            val frg = supportFragmentManager.findFragmentByTag(curFragmentTag) as TrashFragment
            frg.quickSearch(newText)
        } else if (curFragmentTag == MainFragment.FRAGMENT_TAG) {
            val frg = supportFragmentManager.findFragmentByTag(curFragmentTag) as MainFragment
            frg.quickSearch(newText)
        }
        return false
    }

    /**
     * Call by searchview closing
     */
    override fun onClose(): Boolean {
        val qs = mainMenu!!.findItem(R.id.action_qs)

        if (qs != null) {
            qs.isVisible = true
            search_view.visibility = View.INVISIBLE
        }
        return false
    }

    fun showFab(show: Boolean) {
        if (show) {
            fab!!.visibility = View.VISIBLE
        } else {
            fab!!.visibility = View.INVISIBLE
        }
    }
}
