package com.example.buttom2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.imageview.ShapeableImageView
import java.io.FileNotFoundException
import java.io.InputStream

class EditProfile : AppCompatActivity() {

    private lateinit var profileImageView: ShapeableImageView
    private lateinit var editTextView: TextView
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_CODE_PERMISSIONS = 1001
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImageView = findViewById(R.id.flProfileCircle)
        editTextView = findViewById(R.id.tv_edit)

        val flechaImageView: ImageView = findViewById(R.id.btn_back)
        val cambiarNombre: LinearLayout = findViewById(R.id.cambiarNombre)
        val cambiarContrasena: LinearLayout = findViewById(R.id.cambiarContraseña)
        val botonGuardar: Button = findViewById(R.id.btnGuardarPerfil)

        // Solicita permisos si no se han concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSIONS)
        }

        editTextView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } else {
                // Permiso denegado, solicita permisos
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSIONS)
            }
        }

        cambiarNombre.setOnClickListener {
            val intent = Intent(this, CambiarNombreActivity::class.java)
            startActivity(intent)
        }

        cambiarContrasena.setOnClickListener {
            val intent = Intent(this, CambiarPasswordActivity::class.java)
            startActivity(intent)
        }

        flechaImageView.setOnClickListener {
            finish()
        }

        botonGuardar.setOnClickListener {
            // Guarda los cambios en SharedPreferences si es necesario
            if (selectedImageUri != null) {
                saveImageToLocalStorage(selectedImageUri!!)
            }
            finish()
        }

        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                // Usuario deslizó hacia abajo
            } else {
                // Usuario deslizó hacia arriba
            }
        })

        loadImageFromLocalStorage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                this.selectedImageUri = selectedImageUri // Guarda el URI seleccionado
                var inputStream: InputStream? = null
                try {
                    inputStream = contentResolver.openInputStream(selectedImageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    profileImageView.setImageBitmap(bitmap)
                    saveImageToLocalStorage(selectedImageUri) // Guarda la imagen en SharedPreferences
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } finally {
                    inputStream?.close()
                }
            }
        }
    }

    private fun saveImageToLocalStorage(imageUri: Uri) {
        val sharedPref = getSharedPreferences("profile", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profile_image_uri", imageUri.toString())
            apply()
        }
    }

    private fun loadImageFromLocalStorage() {
        val sharedPref = getSharedPreferences("profile", MODE_PRIVATE)
        val imageUriString = sharedPref.getString("profile_image_uri", null)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            try {
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                profileImageView.setImageBitmap(bitmap)
                inputStream?.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                // Manejar el error adecuadamente
            } catch (e: SecurityException) {
                e.printStackTrace()
                // Manejar el caso en que el acceso es denegado
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes continuar
            } else {
                // Permiso denegado, maneja la situación
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}