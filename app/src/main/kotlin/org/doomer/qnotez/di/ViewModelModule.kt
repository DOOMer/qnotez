package org.doomer.qnotez.di

import android.arch.lifecycle.ViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.doomer.qnotez.viewmodel.*

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NoteListViewModel::class)
    abstract fun bindNoteListViewModel(viewModel : NoteListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteAddViewModel::class)
    abstract fun bindNoteAddViewModel(viewModel : NoteAddViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteDetailViewModel::class)
    abstract fun bindNoteDetailViewModel(viewModel : NoteDetailViewModel) : ViewModel
}