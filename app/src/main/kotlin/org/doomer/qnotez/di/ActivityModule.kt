package org.doomer.qnotez.di

import dagger.Module
import dagger.android.ContributesAndroidInjector

import org.doomer.qnotez.ui.InfoActivity
import org.doomer.qnotez.ui.MainActivity
import org.doomer.qnotez.ui.NoteAddActivity
import org.doomer.qnotez.ui.NoteDetailActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contrubuteInfoActivity() : InfoActivity

    @ContributesAndroidInjector
    internal abstract fun contrubuteMainActivity() : MainActivity

    @ContributesAndroidInjector
    internal abstract fun contrubuteNoteAddActivity() : NoteAddActivity

    @ContributesAndroidInjector
    internal abstract fun contrubuteNoteDetailActivity() : NoteDetailActivity
}