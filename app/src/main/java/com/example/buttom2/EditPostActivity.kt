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
import com.example.buttom2.databinding.ActivityEditPostBinding
import org.json.JSONObject

class EditPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPostBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var articleId: String
    private lateinit var slug: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the request queue
        requestQueue = Volley.newRequestQueue(this)

        // Obtener los datos del artículo
        articleId = intent.getStringExtra("ARTICLE_ID") ?: ""
        slug = intent.getStringExtra("ARTICLE_SLUG") ?: ""

        binding.updateTitleEditText.setText(intent.getStringExtra("ARTICLE_TITLE"))
        binding.contentEditText.setText(intent.getStringExtra("ARTICLE_CONTENT"))

        binding.editarButton.setOnClickListener{ clickBtnEditar() }



        //Flecha para volver
        val flechaImageView = findViewById<ImageView>(R.id.volver)

        // Agregar OnClickListener al ImageView de la flecha
        flechaImageView.setOnClickListener {
            // Cerrar SearchActivity y volver al MainActivity
            finish()
        }

    }

    private fun validateTitulo () : Boolean {
        val titulo = binding.updateTitleEditText.text.toString()
        return if (titulo.isEmpty()) {
            binding.updateTitleEditText.error = "Field can not be empty"
            false
        }else{
            binding.updateTitleEditText.error = null
            true
        }
    }

    private fun validateDescripcion () : Boolean {
        val titulo = binding.contentEditText.text.toString()
        return if (titulo.isEmpty()) {
            binding.contentEditText.error = "Field can not be empty"
            false
        }else{
            binding.contentEditText.error = null
            true
        }
    }

    private fun clickBtnEditar() {
        val result = arrayOf(validateTitulo(), validateDescripcion())

        if (false in result) {
            Toast.makeText(this, "Faltan llenar campos", Toast.LENGTH_SHORT).show()
            return
        }

        val title = binding.updateTitleEditText.text.toString()
        val content = binding.contentEditText.text.toString()

        updatePost(title, content)
    }

    private fun updatePost(title: String, content: String) {
        val url = "http://192.168.137.225:8000/api/v1/articles/$articleId"
        // Imprimir el slug para depuración
        Log.d("EditPostActivity", "Updating post with slug: $articleId")

        val sharedPreferences = getSharedPreferences("YourSharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null) ?: ""

        val jsonRequest = JSONObject().apply {
            val attributes = JSONObject().apply {
                put("title", title)
                put("content", content)
                put("slug", articleId) // Mantener el slug igual
            }
            val data = JSONObject().apply {
                put("type", "articles")
                put("id", articleId) // ID del artículo a actualizar
                put("attributes", attributes)
            }
            put("data", data)
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.PUT, url, jsonRequest,
            Response.Listener { response ->
                Toast.makeText(this, "Post actualizado exitosamente", Toast.LENGTH_SHORT).show()

                // Inicia MainActivity para refrescar
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al actualizar el post", Toast.LENGTH_SHORT).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/vnd.api+json"
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
}
