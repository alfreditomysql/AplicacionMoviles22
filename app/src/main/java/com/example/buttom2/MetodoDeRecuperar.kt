package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.buttom2.databinding.ActivityMetodoDeRecuperarBinding


class MetodoDeRecuperar : AppCompatActivity() {
    private lateinit var binding: ActivityMetodoDeRecuperarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodo_de_recuperar)
        binding = ActivityMetodoDeRecuperarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flechaImageView: ImageView = findViewById(R.id.btn_back)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            //Cerrar SearchActivity y volver al MainActivity
            val intent = Intent(this, RecuperarPassword::class.java)
            startActivity(intent)
        }

        val btnContinue : Button = findViewById(R.id.btnContinue)

        // Agregar OnClickListener al ImageView de la flecha
        btnContinue.setOnClickListener {
            val intent = Intent(this, IngresarCodigo::class.java)
            startActivity(intent)
        }
    }
}