package com.example.buttom2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.buttom2.databinding.FragmentHomeBinding
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeIndex.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeIndex : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestQueue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize RecyclerView
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        requestQueue = Volley.newRequestQueue(context)

        fetchArticles()

        binding.insertarPost.setOnClickListener {
            val intent = Intent(requireActivity(), NewPostActivity::class.java)
            startActivity(intent)
        }

        binding.addButton.setOnClickListener {
            val intent = Intent(requireActivity(), NewPostActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeIndex.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeIndex().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun fetchArticles() {
        val url = "http://192.168.0.10:8000/api/v1/articles"

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val articles = mutableListOf<Article>()
                    val dataArray = response.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val articleJson = dataArray.getJSONObject(i)
                        val categoryLink = articleJson.getJSONObject("relationships")
                            .getJSONObject("category")
                            .getJSONObject("links")
                            .getString("related")  // Cambiado a getString
                        val authorLink = articleJson.getJSONObject("relationships")
                            .getJSONObject("author")
                            .getJSONObject("links")
                            .getString("related")  // Cambiado a getString

                        val comments = mutableListOf<Comment>()
                        if (articleJson.has("comments")) { // Verificar si 'comments' existe
                            val commentsJsonArray = articleJson.getJSONArray("comments")
                            for (j in 0 until commentsJsonArray.length()) {
                                val commentJson = commentsJsonArray.getJSONObject(j)
                                val commentAuthorJson = commentJson.getJSONObject("author")
                                comments.add(
                                    Comment(
                                        id = commentJson.getInt("id"),
                                        content = commentJson.getString("content"),
                                        author = Author(
                                            id = commentAuthorJson.getInt("id"),
                                            name = commentAuthorJson.getString("name")
                                        )
                                    )
                                )
                            }
                        }

                        articles.add(
                            Article(
                                id = articleJson.getString("id"),
                                title = articleJson.getJSONObject("attributes").getString("title"),
                                content = articleJson.getJSONObject("attributes")
                                    .getString("content"),
                                created_at = articleJson.getJSONObject("attributes")
                                    .getString("created-at"),
                                updated_at = articleJson.getJSONObject("attributes")
                                    .getString("updated-at"),
                                category = Category(
                                    id = 0,
                                    name = articleJson.getJSONObject("attributes")
                                        .getString("category")
                                ),
                                author = Author(
                                    id = 0,
                                    name = articleJson.getJSONObject("attributes")
                                        .getString("author")
                                ),
                                comments = comments
                            )
                        )
                    }

                    // Ordenar la lista de artículos por fecha de creación en orden descendente
                    articles.sortWith { a, b ->
                        val aDate = parseDate(a.created_at)
                        val bDate = parseDate(b.created_at)
                        bDate.compareTo(aDate) // Ordenar de más reciente a más antiguo
                    }

                    // Actualizar el RecyclerView con la lista de artículos
                    binding.notesRecyclerView.adapter = ArticleAdapter(articles)

                    // Agregar logs para verificar el orden
                    Log.d(
                        "HomeIndex",
                        "Articles ordered by date: ${articles.map { it.created_at }}"
                    )

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "JSON Parsing error: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                Log.e("TAG", "Error: ${error.message}")
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/vnd.api+json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        return try {
            format.parse(dateString) ?: Date()
        } catch (e: ParseException) {
            e.printStackTrace()
            Date()
        }
    }

}