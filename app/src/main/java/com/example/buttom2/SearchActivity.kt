package com.example.buttom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //val flechaImageView: ImageView = findViewById(R.id.flecha)

        // Agregar OnClickListener al ImageView de la flecha
        //flechaImageView.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
        //    finish()
        //}
    }
}
