package org.doomer.qnotez.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import org.doomer.qnotez.R
import org.doomer.qnotez.ui.fragments.SettingsFragment
import org.doomer.qnotez.utils.ThemeChanger

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeChanger.setFromSettings(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction().replace(R.id.content, SettingsFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> if (themeIsChanged || displayedRowsCountCHanged) {
                themeIsChanged = false
                displayedRowsCountCHanged = false
                return false
            } else {
                finish()
                return true


            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        if (themeIsChanged || displayedRowsCountCHanged) {
            themeIsChanged = false
            displayedRowsCountCHanged = false

            val toMain = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(toMain)

            finish()
        }

        super.onBackPressed()
    }

    var themeIsChanged = false
    var displayedRowsCountCHanged = false
}
