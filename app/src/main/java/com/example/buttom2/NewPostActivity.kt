package com.example.buttom2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.buttom2.databinding.ActivityNewPostBinding
import org.json.JSONObject

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar requestQueue aquí
        requestQueue = Volley.newRequestQueue(this)

        binding.crearButton.setOnClickListener{ clickBtnCrear() }

        val flechaImageView: ImageView = findViewById(R.id.volver)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            finish()
        }

        // Configurar el Spinner de categoría
        val categorias = arrayOf("Personal", "Trabajo", "Estudio", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriaNew.adapter = adapter


    }

    private fun validateTitulo () : Boolean {
        val titulo = binding.titleEditText.text.toString()
        return if (titulo.isEmpty()) {
            binding.titleEditText.error = "Field can not be empty"
            false
        }else{
            binding.titleEditText.error = null
            true
        }
    }

    private fun validateDescripcion () : Boolean {
        val descripcion = binding.contentEditText.text.toString()
        return if (descripcion.isEmpty()) {
            binding.contentEditText.error = "Field can not be empty"
            false
        }else{
            binding.contentEditText.error = null
            true
        }
    }

    private fun clickBtnCrear() {
        val result = arrayOf(validateTitulo(), validateDescripcion())

        if (false in result) {
            Toast.makeText(this, "Faltan llenar campos", Toast.LENGTH_SHORT).show()
            return
        }

        val title = binding.titleEditText.text.toString()
        val content = binding.contentEditText.text.toString()
        val category = binding.categoriaNew.selectedItem.toString()

        createPost(title, content)
    }

    private fun createPost(title: String, content: String) {
        val url = "http://192.168.0.11:8000/api/v1/articles"

        val sharedPreferences = getSharedPreferences("YourSharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null) ?: ""

        val jsonRequest = JSONObject().apply {
            val attributes = JSONObject().apply {
                put("title", title)
                put("slug", title.replace(" ", "-").lowercase())
                put("content", content)
            }
            val relationships = JSONObject().apply {
                val categoryData = JSONObject().apply {
                    put("id", "dolorum-reprehenderit-ut-sequi-quo-assumenda-autem") // Ajusta esto según la categoría seleccionada
                }
                put("category", JSONObject().put("data", categoryData))
                val authorData = JSONObject().apply {
                    put("id", "03add82e-95c3-4344-b073-01ae25bb357e") // Ajusta esto según el autor
                }
                put("author", JSONObject().put("data", authorData))
            }
            val data = JSONObject().apply {
                put("type", "articles")
                put("attributes", attributes)
                put("relationships", relationships)
            }
            put("data", data)
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonRequest,
            Response.Listener { response ->
                // Maneja la respuesta
                Toast.makeText(this, "Post creado exitosamente", Toast.LENGTH_SHORT).show()

                // Inicia MainActivity para refrescar
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                // Maneja el error
                error.printStackTrace()
                Toast.makeText(this, "Error al crear el post", Toast.LENGTH_SHORT).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/vnd.api+json"
                headers["Content-Type"] = "application/vnd.api+json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
}