package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.buttom2.databinding.ActivityIngresarCodigoBinding
import com.example.buttom2.databinding.ActivityNewPasswordBinding

class NewPassword : AppCompatActivity() {
    private lateinit var binding: ActivityNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flechaImageView: ImageView = findViewById(R.id.btn_back)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            //Cerrar SearchActivity y volver al MainActivity
            val intent = Intent(this, IngresarCodigo::class.java)
            startActivity(intent)
        }

        val btnContinuar : Button = findViewById(R.id.btnConfirmarNuevaContra)

        // Agregar OnClickListener al ImageView de la flecha
        btnContinuar.setOnClickListener {
            if (validatePassword()) {

            }
            if (validatePassword2()) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
    }
    private fun validatePassword () : Boolean {
        val password = binding.password.editText?.text.toString()
        return if (password.isEmpty()) {
            binding.etPassword.error = "Field can not be empty"
            false
        }else{
            binding.etPassword.error = null
            true
        }
    }

    private fun validatePassword2 () : Boolean {
        val password = binding.password.editText?.text.toString()
        val password2 = binding.confirmarPassword.editText?.text.toString()
        return if (password != password2){
            binding.etPassword2.error = "Las contraseñas no son iguales"
            false
        } else if (password2.isEmpty()) {
            binding.etPassword2.error = "Field can not be empty"
            false
        }else{
            binding.etPassword2.error = null
            true
        }
    }
}