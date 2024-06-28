package com.example.buttom2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        // Encuentra el NestedScrollView por su ID

        // Encuentra el NestedScrollView por su ID
        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)

        val flechaImageView: ImageView = findViewById(R.id.btn_back)
        val cambiarNombre: LinearLayout = findViewById(R.id.cambiarNombre)
        val cambiarContrasena: LinearLayout = findViewById(R.id.cambiarContraseña)
        val botonGuardar: Button = findViewById(R.id.btnGuardarPerfil)


        cambiarNombre.setOnClickListener {
            // Abrir SearchActivity cuando se haga clic en la lupa
            val intent = Intent(this, CambiarNombreActivity::class.java)
            startActivity(intent)
        }

        cambiarContrasena.setOnClickListener {
            // Crear un Intent para iniciar CambiarNombreActivity
            val intent = Intent(this, CambiarPasswordActivity::class.java)
            startActivity(intent)
        }

        flechaImageView.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
            finish()
        }

        botonGuardar.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
            finish()
        }



        // Configura el OnScrollChangeListener

        // Configura el OnScrollChangeListener
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // Implementa el comportamiento de desplazamiento aquí
            if (scrollY > oldScrollY) {
                // Usuario deslizó hacia abajo
            } else {
                // Usuario deslizó hacia arriba
            }
        })
    }
}