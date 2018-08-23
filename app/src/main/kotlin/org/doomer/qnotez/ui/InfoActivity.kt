package org.doomer.qnotez.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.content_info.*

import org.doomer.qnotez.R
import org.doomer.qnotez.utils.ThemeChanger
import org.doomer.qnotez.utils.TextUtils
import org.doomer.qnotez.utils.ChangeLog

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        ThemeChanger.setFromSettings(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { ChangeLog.showWFullChangelog(this@InfoActivity) }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fillVersion()
    }

    private fun fillVersion() {
        var version = getString(R.string.version) + ": " + TextUtils.versionString
        version += " - " + TextUtils.buildTime
        text_version.text = version
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
