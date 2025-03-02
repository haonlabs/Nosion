package id.haonlabs.nosion.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import id.haonlabs.nosion.R
import id.haonlabs.nosion.data.local.Note
import id.haonlabs.nosion.ui.adapter.NoteAdapter
import id.haonlabs.nosion.ui.viewmodel.NoteViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewNotes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NoteAdapter { note ->
            viewModel.deleteNote(note)
        }
        recyclerView.adapter = adapter

        viewModel.allNotes.observe(this) { notes ->
            adapter.setNotes(notes)
        }

        val fabAddNote = findViewById<FloatingActionButton>(R.id.fabAddNote)
        fabAddNote.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun showAddNoteDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Note")
            .create()

        // Find views inside dialog
        val editTextTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val editTextContent = dialogView.findViewById<EditText>(R.id.etContent)
        val btnSaveNote = dialogView.findViewById<Button>(R.id.btnSaveNote)

        // Handle Save button click
        btnSaveNote.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val content = editTextContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newNote = Note(title = title, content = content)
                viewModel.addNote(newNote)
                dialog.dismiss() // Close dialog
            } else {
                Toast.makeText(this, "Please enter title and content", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}