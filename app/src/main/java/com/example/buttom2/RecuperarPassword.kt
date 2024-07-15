package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.buttom2.databinding.ActivityRecuperarPasswordBinding
import com.example.buttom2.databinding.ActivityRegisterBinding

class RecuperarPassword : AppCompatActivity() {
    private lateinit var binding:ActivityRecuperarPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_password)
        binding = ActivityRecuperarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnBuscar : Button = findViewById(R.id.btn_search_account)

        // Agregar OnClickListener al ImageView de la flecha
        btnBuscar.setOnClickListener {
            if (validateCorreo()) {
                val intent = Intent(this, MetodoDeRecuperar::class.java)
                startActivity(intent)
            }
        }

        val flechaImageView: ImageView = findViewById(R.id.btn_back)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
         //Cerrar SearchActivity y volver al MainActivity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun validateCorreo () : Boolean {
        val correo = binding.emailRecuperarInput.editText?.text.toString()
        return if (correo.isEmpty()) {
            binding.emailRecuperar.error = "Field can not be empty"
            false
        }else{
            binding.emailRecuperar.error = null
            true
        }
    }


}