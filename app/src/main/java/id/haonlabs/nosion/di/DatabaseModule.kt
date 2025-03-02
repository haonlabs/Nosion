package id.haonlabs.nosion.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.haonlabs.nosion.data.local.NoteDao
import id.haonlabs.nosion.data.local.NoteDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, "note_db").build()
    }

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()
}