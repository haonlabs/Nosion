package id.haonlabs.nosion.data.remote

import id.haonlabs.nosion.data.local.Note
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApiService {
    @GET("notes")
    suspend fun getNotes(): Response<NoteResponse>

    @POST("notes")
    suspend fun addNote(@Body note: Note): Response<NoteResponse>

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: Int): Response<NoteResponse>

}