package com.example.buttom2


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvNombre: TextView
    private lateinit var tvCorreo: TextView

    private lateinit var requestQueue: RequestQueue
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnEditName: ImageButton
    private lateinit var btnSaveName: Button
    private lateinit var btnEditEmail: ImageButton
    private lateinit var btnSaveEmail: Button

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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inicializar las vistas
        tvNombre = view.findViewById(R.id.tvNombre)
        tvCorreo = view.findViewById(R.id.tvCorreo)

        etName = view.findViewById(R.id.etNombre)
        etEmail = view.findViewById(R.id.etCorreo)
        tvName = view.findViewById(R.id.tvNombre)
        tvEmail = view.findViewById(R.id.tvCorreo)
        btnEditName = view.findViewById(R.id.btnEditarNombre)
        btnSaveName = view.findViewById(R.id.btnGuardarNombre)
        btnEditEmail = view.findViewById(R.id.btnEditarCorreo)
        btnSaveEmail = view.findViewById(R.id.btnGuardarCorreo)

        // Inicializar SharedPreferences y RequestQueue
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", 0)
        requestQueue = Volley.newRequestQueue(context)

        // Obtener y mostrar la información del usuario
        fetchUserData()

        // Configurar listeners
        btnEditName.setOnClickListener {
            showEditName()
        }

        btnSaveName.setOnClickListener {
            val newName = etName.text.toString().trim()
            if (newName.isNotEmpty()) {
                updateUser(newName)
            } else {
                Toast.makeText(context, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        btnEditEmail.setOnClickListener {
            showEditEmail()
        }

        btnSaveEmail.setOnClickListener {
            val newEmail = etEmail.text.toString().trim()
            if (newEmail.isNotEmpty()) {
                updateEmail(newEmail)
            } else {
                Toast.makeText(context, "El correo no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun fetchUserData() {
        val sharedPreferences =
            activity?.getSharedPreferences("YourSharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("auth_token", null)

        Log.d("ProfileFragment", "Token: $token")  // Agrega esta línea para verificar el token
        val url = "http://192.168.0.10:8000/api/v1/user"

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val name = response.getString("name")
                    val email = response.getString("email")

                    tvNombre.text = name
                    tvCorreo.text = email

                    hideEditFields()

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error en el JSON: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }


    private fun updateEmail(newEmail: String) {
        val sharedPreferences = activity?.getSharedPreferences("YourSharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("auth_token", null)
        val url = "http://192.168.0.10:8000/api/v1/user"
        val requestBody = JSONObject().apply {
            put("name", tvName.text.toString())
            put("email", newEmail)
        }

        Log.d("ProfileFragment", "Request Body: $requestBody")

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.PUT, url, requestBody,
            Response.Listener { response ->
                try {
                    Toast.makeText(requireContext(), "Email updated successfully", Toast.LENGTH_SHORT).show()
                    fetchUserData() // Update the view with new data
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "JSON Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/vnd.api+json"
                headers["Content-Type"] = "application/vnd.api+json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun updateUser(newUser: String) {
        val sharedPreferences = activity?.getSharedPreferences("YourSharedPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("auth_token", null)
        val url = "http://192.168.0.10:8000/api/v1/user"
        val requestBody = JSONObject().apply {
            put("name", newUser)
            put("email", tvCorreo.text.toString())
        }

        Log.d("ProfileFragment", "Request Body: $requestBody")

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.PUT, url, requestBody,
            Response.Listener { response ->
                try {
                    Toast.makeText(requireContext(), "User updated successfully", Toast.LENGTH_SHORT).show()
                    fetchUserData() // Update the view with new data
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "JSON Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/vnd.api+json"
                headers["Content-Type"] = "application/vnd.api+json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun hideEditFields() {
        // Ocultar campos de edición y botones
        etName.visibility = View.GONE
        btnSaveName.visibility = View.GONE
        etEmail.visibility = View.GONE
        btnSaveEmail.visibility = View.GONE
        tvNombre.visibility = View.VISIBLE
        tvCorreo.visibility = View.VISIBLE
    }

    private fun showEditName() {
        tvNombre.visibility = View.GONE
        etName.visibility = View.VISIBLE
        btnSaveName.visibility = View.VISIBLE
        etName.setText(tvNombre.text) // Pre-llenar el campo de texto
    }

    private fun showEditEmail() {
        tvCorreo.visibility = View.GONE
        etEmail.visibility = View.VISIBLE
        btnSaveEmail.visibility = View.VISIBLE
        etEmail.setText(tvCorreo.text) // Pre-llenar el campo de texto
    }
}