package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.example.buttom2.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editarButton.setOnClickListener{ clickBtnCrear() }

        // Configurar el Spinner
        val categorias = arrayOf("Categoría 1", "Categoría 2", "Categoría 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoria.adapter = adapter

        //Flecha para volver
        val flechaImageView = findViewById<ImageView>(R.id.volver)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
            finish()
        }

    }

    private fun validateTitulo () : Boolean {
        val titulo = binding.updateTitleEditText.text.toString()
        return if (titulo.isEmpty()) {
            binding.updateTitleEditText.error = "Field can not be empty"
            false
        }else{
            binding.updateTitleEditText.error = null
            true
        }
    }

    private fun validateDescripcion () : Boolean {
        val titulo = binding.contentEditText.text.toString()
        return if (titulo.isEmpty()) {
            binding.contentEditText.error = "Field can not be empty"
            false
        }else{
            binding.contentEditText.error = null
            true
        }
    }

    fun clickBtnCrear(){
        val result = arrayOf(validateTitulo(), validateDescripcion())

        if (false in result){
            Toast.makeText(this, "Faltan llenar campos", Toast.LENGTH_SHORT).show()
            return
        }
    }
}
