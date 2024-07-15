package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
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

        // Configurar el Spinner
        val categorias = arrayOf("Categoría 1", "Categoría 2", "Categoría 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoria.adapter = adapter

        val flechaImageView = findViewById<ImageView>(R.id.volver)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
            finish()
        }

        binding.editarButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.contentEditText.text.toString()

            // Obtener la categoría seleccionada del Spinner
            val selectedCategory = binding.categoria.selectedItem.toString()

            if (newTitle.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un título y contenido", Toast.LENGTH_SHORT).show()
            } else {
                // Actualizar el constructor de Note para incluir category
                val updateNote = Note(noteId, newTitle, newContent, selectedCategory, )
                db.UpdateNote(updateNote)
                finish()
                Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
