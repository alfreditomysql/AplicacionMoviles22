package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.buttom2.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { validate() }
        binding.iniciarSesion.setOnClickListener { goLoginActivity() }
    }

    private fun validate (){
        val result = arrayOf(validateNombre(), validateCorreo(), validatePassword(), validatePassword2())

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

    private fun goLoginActivity(){
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun validateNombre () : Boolean {
        val nombre = binding.nombre1.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.nombre1.error = "Field can not be empty"
            false
        }else{
            binding.nombre1.error = null
            true
        }
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

    private fun validatePassword2 () : Boolean {
        val nombre = binding.password2.editText?.text.toString()
        val nombre2 = binding.password1.editText?.text.toString()
        return if (nombre != nombre2){
            binding.password2.error = "Las contraseñas no son iguales"
            false
        } else if (nombre.isEmpty()) {
            binding.password2.error = "Field can not be empty"
            false
        }else{
            binding.password2.error = null
            true
        }
    }
}