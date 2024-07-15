package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.example.buttom2.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crearButton.setOnClickListener{ clickBtnCrear() }

        val flechaImageView: ImageView = findViewById(R.id.volver)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            finish()
        }

        // Configurar el Spinner de categoría
        val categorias = arrayOf("Personal", "Trabajo", "Estudio", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriaNew.adapter = adapter


    }

    private fun validateTitulo () : Boolean {
        val titulo = binding.titleEditText.text.toString()
        return if (titulo.isEmpty()) {
            binding.titleEditText.error = "Field can not be empty"
            false
        }else{
            binding.titleEditText.error = null
            true
        }
    }

    private fun validateDescripcion () : Boolean {
        val descripcion = binding.contentEditText.text.toString()
        return if (descripcion.isEmpty()) {
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
