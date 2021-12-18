package com.example.decotuk_app_capstone.ui.profile.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.decotuk_app_capstone.data.preferences.PreferenceRepository
import com.example.decotuk_app_capstone.data.preferences.UserPreference
import com.example.decotuk_app_capstone.data.source.local.entity.User
import com.example.decotuk_app_capstone.databinding.ActivityEditProfileBinding
import com.example.decotuk_app_capstone.util.USER
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditProfileActivity : AppCompatActivity() {

    private var _binding : ActivityEditProfileBinding? = null
    private val binding get() = _binding!!

    private var database : FirebaseDatabase = Firebase.database
    private lateinit var pref : PreferenceRepository
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PreferenceRepository.getInstance(UserPreference(this))
        uid = pref.getUser("UID")!!

        showProfile()

        binding.button5.setOnClickListener {
            updateProfile()
        }

    }

    private fun updateProfile() {
        val nama = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isEmpty() -> {
                binding.edtEmail.error = "Email tidak boleh kosong"
                binding.edtEmail.requestFocus()
            }
            nama.isEmpty() -> {
                binding.edtName.error = "Nama tidak boleh kosong"
                binding.edtName.requestFocus()
            }
            password.isEmpty() -> {
                binding.edtPassword.error = "Password tidak boleh kosong"
                binding.edtPassword.requestFocus()
            }
            else -> {
                saveToFirebase(nama, email, password)
            }
        }

    }

    private fun saveToFirebase(nama: String, email: String, password: String) {
        val myRef = database.getReference(USER).child(uid)

        val user = HashMap<String, Any>()
        user["nama"] = nama
        user["email"] = email
        user["password"] = password

        myRef.updateChildren(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Ubah profile berhasil", Toast.LENGTH_SHORT).show()
            }
            .addOnCanceledListener {
                Toast.makeText(this, "Ubah profile gagal", Toast.LENGTH_SHORT).show()
            }

    }

    private fun showProfile() {
        val myyRef = database.getReference(USER).child(uid)
        myyRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()){
                val user = snapshot.getValue(User::class.java)

                with(binding){
                    if (user != null) {
                        edtEmail.setText(user.email)
                        edtName.setText(user.nama)
                        edtPassword.setText(user.password)
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}