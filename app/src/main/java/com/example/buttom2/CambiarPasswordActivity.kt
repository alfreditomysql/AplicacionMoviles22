package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class CambiarPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_password)

        val flechaImageView: ImageView = findViewById(R.id.volver)

        val botonGuardar: Button = findViewById(R.id.btnConfirmarPassword)



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