package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.buttom2.databinding.ActivityIngresarCodigoBinding
import com.example.buttom2.databinding.ActivityMetodoDeRecuperarBinding

class IngresarCodigo : AppCompatActivity() {
    private lateinit var binding: ActivityIngresarCodigoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresar_codigo)
        binding = ActivityIngresarCodigoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flechaImageView: ImageView = findViewById(R.id.btn_back)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            //Cerrar SearchActivity y volver al MainActivity
            val intent = Intent(this, MetodoDeRecuperar::class.java)
            startActivity(intent)
        }

        val btnContinue : Button = findViewById(R.id.btnContinue)

        // Agregar OnClickListener al ImageView de la flecha
        btnContinue.setOnClickListener {
            if (validateCodigo()) {
                val intent = Intent(this, NewPassword::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateCodigo(): Boolean {
        val codigo = binding.ingresarCodigo.editText?.text.toString()
        return when {
            codigo.isEmpty() -> {
                binding.etCode.error = "Field cannot be empty"
                false
            }
            !codigo.matches(Regex("\\d+")) -> {
                binding.etCode.error = "Only numbers are allowed"
                false
            }
            codigo.length > 6 -> {
                binding.etCode.error = "Maximum length is 6 characters"
                false
            }
            else -> {
                binding.etCode.error = null
                true
            }
        }
    }
}