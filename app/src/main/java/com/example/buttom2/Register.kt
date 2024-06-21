package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.buttom2.databinding.ActivityRegisterBinding
import org.json.JSONObject

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var txtNombre: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtConfirmPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txtNombre = findViewById(R.id.nombre1)
        txtEmail = findViewById(R.id.email)

        txtPassword = findViewById(R.id.password1)
        txtConfirmPassword = findViewById(R.id.passwordConfirm)
        
        //binding.btnRegister.setOnClickListener { validate() }
        binding.iniciarSesion.setOnClickListener { goLoginActivity() }
        binding.btnRegister.setOnClickListener{ clickBtnRegister() }
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
        val nombre = binding.nombreinput1.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.nombre1.error = "Field can not be empty"
            false
        }else{
            binding.nombre1.error = null
            true
        }
    }

    private fun validateCorreo () : Boolean {
        val nombre = binding.emailinput.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.email.error = "Field can not be empty"
            false
        }else{
            binding.email.error = null
            true
        }
    }

    private fun validatePassword () : Boolean {
        val nombre = binding.passwordinput1.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.password1.error = "Field can not be empty"
            false
        }else{
            binding.password1.error = null
            true
        }
    }

    private fun validatePassword2 () : Boolean {
        val nombre = binding.passwordinput2.editText?.text.toString()
        val nombre2 = binding.passwordinput1.editText?.text.toString()
        return if (nombre != nombre2){
            binding.passwordinput2.error = "Las contraseñas no son iguales"
            false
        } else if (nombre.isEmpty()) {
            binding.passwordinput2.error = "Field can not be empty"
            false
        }else{
            binding.passwordinput2.error = null
            true
        }
    }

    fun clickBtnRegister (){
        val name = txtNombre?.text.toString().trim()
        val email = txtEmail?.text.toString().trim()
        val pass = txtPassword?.text.toString().trim()
        val confPass = txtConfirmPassword?.text.toString().trim()
        val device = "device_name"
        val result = arrayOf(validateNombre(), validateCorreo(), validatePassword(), validatePassword2())

        if (false in result){
            Toast.makeText(this, "Faltan llenar campos", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.141.92:8000/api/v1/register"
        val body = JSONObject().apply{
            put("name",name)
            put("email",email)
            put("password",pass)
            put("password_confirmation",confPass)
            put("device_name",device)
        }

        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            Response.Listener { response ->
                // Maneja la respuesta de la API
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Maneja los errores
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
        goMainActivity()
    }
}