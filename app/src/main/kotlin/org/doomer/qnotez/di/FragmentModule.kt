package org.doomer.qnotez.di

import dagger.Module
import dagger.android.ContributesAndroidInjector

import org.doomer.qnotez.ui.fragments.BackupFragment
import org.doomer.qnotez.ui.fragments.MainFragment
import org.doomer.qnotez.ui.fragments.TrashFragment
import org.doomer.qnotez.ui.fragments.SettingsFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeBackupFragment() : BackupFragment

    @ContributesAndroidInjector
    abstract fun contributeMainFragment() : MainFragment

    @ContributesAndroidInjector
    abstract fun contributeTrashFragment() : TrashFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment() : SettingsFragment
}