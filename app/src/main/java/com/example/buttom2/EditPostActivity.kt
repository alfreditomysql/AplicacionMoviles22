package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.buttom2.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPostBinding
    private lateinit var db: NotesDataBaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDataBaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1){
            finish()
            return
        }
        val note = db.getNoteById(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.contentEditText.setText(note.content)

        binding.saveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.contentEditText.text.toString()

            if (newTitle.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un título y contenido", Toast.LENGTH_SHORT).show()
            } else {
                val updateNote = Note(noteId, newTitle, newContent)
                db.UpdateNote(updateNote)
                finish()
                Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
            }
        }

    }
}