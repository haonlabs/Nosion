package id.haonlabs.nosion.repository

import android.util.Log
import androidx.lifecycle.LiveData
import id.haonlabs.nosion.data.local.Note
import id.haonlabs.nosion.data.local.NoteDao
import id.haonlabs.nosion.data.remote.NoteApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApiService: NoteApiService
) {
    suspend fun fetchNotesFromApi() {
        try {
            Log.d("Note Repository", "fetching...")
            val res = noteApiService.getNotes()
            Log.d("Note Repository", "fetchNotesFromApi: ${res.body()}")
            if (res.isSuccessful) {
                val data = res.body()?.data
                val notes = data?.map {
                    Note(
                        id = it?.id?.toInt() ?: 0,
                        title = it?.title ?: "",
                        content = it?.content ?: ""
                    )
                }
                noteDao.insertAllNote(notes ?: emptyList())
            }
        } catch (e: Exception) {
            Log.e("Note Repository", "fetchNotesFromApi: ${e.message}")
        }
    }

    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun addNote(note: Note) {
        try {
            noteDao.insertNote(note)
            val noteResponse = noteApiService.addNote(note)
            Log.d("Note Repository", "addNote: ${noteResponse.body()}")
        } catch (e: Exception) {
            Log.e("Note Repository", "addNote: ${e.message}")
        }

    }

    suspend fun deleteNote(note: Note) {
        try {
            noteDao.deleteNote(note)
            val noteResponse = noteApiService.deleteNote(note.id)
            Log.d("Note Repository", "deleteNote: ${noteResponse.body()}")
        } catch (e: Exception) {
            Log.e("Note Repository", "deleteNote: ${e.message}")
        }
    }
}