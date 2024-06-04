package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.buttom2.databinding.ActivityLoginBinding
import com.example.buttom2.databinding.ActivityRegisterBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIniciarSesion.setOnClickListener { validate() }
        binding.crearCuenta.setOnClickListener { goRegisterActivity() }
    }

    private fun validate (){
        val result = arrayOf(validateCorreo(), validatePassword())

        if (false in result){
            Toast.makeText(this, "Faltan llenar campos", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Cmpos verificados", Toast.LENGTH_SHORT).show()
        goMainActivity()
    }

    private fun goMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goRegisterActivity(){
        startActivity(Intent(this, Register::class.java))
        finish()
    }

    private fun validateCorreo () : Boolean {
        val nombre = binding.correo1.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.correo1.error = "Field can not be empty"
            false
        }else{
            binding.correo1.error = null
            true
        }
    }

    private fun validatePassword () : Boolean {
        val nombre = binding.password1.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.password1.error = "Field can not be empty"
            false
        }else{
            binding.password1.error = null
            true
        }
    }
}