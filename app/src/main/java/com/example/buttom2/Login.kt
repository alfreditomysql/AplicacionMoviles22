package com.example.buttom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.buttom2.databinding.ActivityLoginBinding
import com.example.buttom2.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txtEmail = findViewById(R.id.emailLogin)
        txtPassword = findViewById(R.id.passwordLogin)

        //binding.btnIniciarSesion.setOnClickListener { validate() }
        binding.crearCuenta.setOnClickListener { goRegisterActivity() }
        binding.btnIniciarSesion.setOnClickListener{ clickBtnLogin() }
        binding.recuperarPassword.setOnClickListener { goRecuperar() }
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

    private fun goRecuperar(){
        startActivity(Intent(this, RecuperarPassword::class.java))
        finish()
    }

    private fun goRegisterActivity(){
        startActivity(Intent(this, Register::class.java))
        finish()
    }

    private fun validateCorreo () : Boolean {
        val nombre = binding.correo1.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.emailLogin.error = "Field can not be empty"
            false
        }else{
            binding.emailLogin.error = null
            true
        }
    }

    private fun validatePassword () : Boolean {
        val nombre = binding.password1.editText?.text.toString()
        return if (nombre.isEmpty()) {
            binding.passwordLogin.error = "Field can not be empty"
            false
        }else{
            binding.passwordLogin.error = null
            true
        }
    }

    fun clickBtnLogin (){
        val email = txtEmail?.text.toString().trim()
        val pass = txtPassword?.text.toString().trim()
        val device = "device_name"
        val result = arrayOf(validateCorreo(), validatePassword())

        if (false in result){
            Toast.makeText(this, "Faltan llenar campos", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.0.11:8000/api/v1/login"
        val body = JSONObject().apply{
            put("email",email)
            put("password",pass)
            put("device_name",device)
        }

        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            Response.Listener { response ->
                try {
                    // Extrae el token desde "plain-text-token"
                    if (response.has("plain-text-token")) {
                        val token = response.getString("plain-text-token")
                        Log.d("Login", "Token obtenido: $token")

                        // Guarda el token en SharedPreferences
                        val sharedPreferences = getSharedPreferences("YourSharedPreferences", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("auth_token", token)
                        editor.apply()

                        // Continúa a la actividad principal
                        goMainActivity()
                    } else {
                        Log.e("Login", "No se encontró el token en la respuesta.")
                        Toast.makeText(this, "No se encontró el token en la respuesta.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Log.e("Login", "Error al parsear la respuesta JSON: ${e.message}")
                    Toast.makeText(this, "Error al parsear la respuesta JSON.", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Maneja los errores
                Toast.makeText(this, "El correo o la contraseña son incorrectos", Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                //Log.e("TAG", "Error: ${error.message}")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}