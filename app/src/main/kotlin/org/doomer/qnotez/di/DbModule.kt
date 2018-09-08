package org.doomer.qnotez.di

import android.app.Application
import android.arch.persistence.room.Room

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

import org.doomer.qnotez.db.DB_NAME
import org.doomer.qnotez.db.AppDatabase
import org.doomer.qnotez.db.NoteDao


@Module
class DbModule {
    @Provides
    @Singleton
    fun provideDb(app: Application) : AppDatabase =
            Room.databaseBuilder(app,
                    AppDatabase::class.java, DB_NAME).build()

    @Provides
    @Singleton
    fun provideUsers(db : AppDatabase) : NoteDao = db.noteModel()

}