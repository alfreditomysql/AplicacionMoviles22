package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.example.buttom2.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var db: NotesDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDataBaseHelper(this)

        val flechaImageView: ImageView = findViewById(R.id.volver)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            finish()
        }

        // Configurar el Spinner de categoría
        val categorias = arrayOf("Personal", "Trabajo", "Estudio", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoria.adapter = adapter

        binding.crearButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val selectedCategory = binding.categoria.selectedItem.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un título y contenido", Toast.LENGTH_SHORT).show()
            } else {
                val note = Note(8, title, content, selectedCategory) // Asegúrate de tener el constructor de Note adecuado
                db.insertNote(note)
                finish()
                Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
