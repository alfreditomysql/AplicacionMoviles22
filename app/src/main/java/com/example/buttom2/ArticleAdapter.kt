package com.example.buttom2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ArticleAdapter(private val articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.titleArticle)
        private val content: TextView = itemView.findViewById(R.id.contentArticle)
        private val created_at: TextView = itemView.findViewById(R.id.created_at)
        private val updated_at: TextView = itemView.findViewById(R.id.updated_at)
        private val author: TextView = itemView.findViewById(R.id.author)
        private val category: TextView = itemView.findViewById(R.id.category)


        fun bind(article: Article) {
            title.text = article.title
            content.text = article.content
            created_at.text = article.created_at
            updated_at.text = article.updated_at
            author.text = article.author.name
            category.text = article.category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)

        holder.itemView.findViewById<View>(R.id.optionsButtonA).setOnClickListener {

            val popupMenu = PopupMenu(holder.itemView.context, it)
            popupMenu.inflate(R.menu.note_item_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.editOption -> {
                        // Navega a EditArticleActivity con los datos del artículo
                        val intent = Intent(holder.itemView.context , EditPostActivity::class.java).apply {
                            putExtra("ARTICLE_ID", article.id)
                            putExtra("ARTICLE_TITLE", article.title)
                            putExtra("ARTICLE_CONTENT", article.content)
                            putExtra("ARTICLE_CATEGORY", article.category.name)
                        }
                        holder.itemView.context.startActivity(intent)
                        true
                    }
                    R.id.deleteOption -> {
                        // Borrar artículo
                        val sharedPreferences = holder.itemView.context.getSharedPreferences("YourSharedPreferences", Context.MODE_PRIVATE)
                        val token = sharedPreferences.getString("auth_token", null) ?: ""
                        deleteArticleFromApi(holder.itemView.context, article.id, token) {
                            if (it) {
                                removeItem(holder.adapterPosition)
                                Toast.makeText(holder.itemView.context, "Artículo eliminado", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(holder.itemView.context, "Error al eliminar el artículo", Toast.LENGTH_SHORT).show()
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()

            popupMenu.setOnDismissListener {
                popupMenu.dismiss()
            }
        }
    }

    override fun getItemCount(): Int = articles.size
    private fun removeItem(position: Int) {
        (articles as? MutableList)?.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun deleteArticleFromApi(context: Context, articleId: String, token: String, callback: (Boolean) -> Unit) {
        val url = "http://192.168.137.225:8000/api/v1/articles/$articleId" // Asegúrate de que esta URL es correcta

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(Request.Method.DELETE, url,
            Response.Listener<String> {
                callback(true)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                callback(false)
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/vnd.api+json"
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        requestQueue.add(stringRequest)
    }
}
