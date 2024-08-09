package com.example.buttom2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private val categoriesMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)

        binding.crearButton.setOnClickListener { clickBtnCrear() }

        val flechaImageView: ImageView = findViewById(R.id.volver)
        flechaImageView.setOnClickListener {
            finish()
        }

        // Cargar las categorías desde la API
        loadCategories()
    }

    private fun loadCategories() {
        val url = "http://192.168.0.10:8000/api/v1/categories"

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val categories = mutableListOf<String>()
                val dataArray = response.getJSONArray("data")
                for (i in 0 until dataArray.length()) {
                    val categoryObject = dataArray.getJSONObject(i)
                    val attributes = categoryObject.getJSONObject("attributes")
                    val categoryName = attributes.getString("name")
                    val categorySlug = attributes.getString("slug") // Obtener el slug

                    categories.add(categoryName)
                    categoriesMap[categoryName] = categorySlug // Asociar nombre con slug
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categoriaNew.adapter = adapter
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar categorías", Toast.LENGTH_SHORT).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/vnd.api+json"
                headers["Authorization"] = "Bearer ${getAuthToken()}"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun getAuthToken(): String {
        val sharedPreferences = getSharedPreferences("YourSharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null) ?: ""
    }

    private fun validateTitulo(): Boolean {
        val titulo = binding.titleEditText.text.toString()
        return if (titulo.isEmpty()) {
            binding.titleEditText.error = "Field can not be empty"
            false
        } else {
            binding.titleEditText.error = null
            true
        }
    }

    private fun validateDescripcion(): Boolean {
        val descripcion = binding.contentEditText.text.toString()
        return if (descripcion.isEmpty()) {
            binding.contentEditText.error = "Field can not be empty"
            false
        } else {
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

        // Obtener el ID del usuario y luego crear el post
        getUserData { userId ->
            createPost(title, content, category, userId)
        }
    }

    private fun getUserData(onResponse: (userId: String) -> Unit) {
        val url = "http://192.168.0.10:8000/api/v1/user"
        val token = getAuthToken()

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val userId = response.getString("id")
                onResponse(userId)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT)
                    .show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/vnd.api+json"
                headers["Authorization"] = "Bearer ${getAuthToken()}"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun createPost(title: String, content: String, category: String, userId: String?) {
        val url = "http://192.168.0.10:8000/api/v1/articles"

        val token = getAuthToken()
        val categorySlug = categoriesMap[category] ?: "" // Obtener el slug basado en el nombre

        val jsonRequest = JSONObject().apply {
            val attributes = JSONObject().apply {
                put("title", title)
                put("slug", title.replace(" ", "-").lowercase())
                put("content", content)
            }
            val relationships = JSONObject().apply {
                val categoryData = JSONObject().apply {
                    put("id", categorySlug) // Usar el slug de la categoría
                }
                put("category", JSONObject().put("data", categoryData))
                val authorData = JSONObject().apply {
                    put("id", userId ?: "") // Usar el ID del usuario obtenido
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
                error.printStackTrace()
                val responseBody = error.networkResponse?.data?.let { String(it) }
                Toast.makeText(this, "Error al crear el post: $responseBody", Toast.LENGTH_LONG)
                    .show()
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