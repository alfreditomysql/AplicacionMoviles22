package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

class CambiarNombreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_nombre)

        val flechaImageView: ImageView = findViewById(R.id.volver)

        val botonGuardar: Button = findViewById(R.id.btnConfirmarNombre)



        flechaImageView.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
            finish()
        }

        botonGuardar.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
            finish()
        }

    }
}