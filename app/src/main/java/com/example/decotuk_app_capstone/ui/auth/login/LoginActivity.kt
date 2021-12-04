package com.example.decotuk_app_capstone.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.decotuk_app_capstone.MainActivity
import com.example.decotuk_app_capstone.data.preferences.PreferenceRepository
import com.example.decotuk_app_capstone.data.preferences.UserPreference
import com.example.decotuk_app_capstone.data.source.local.entity.User
import com.example.decotuk_app_capstone.databinding.ActivityLoginBinding
import com.example.decotuk_app_capstone.ui.auth.signup.SignUpActivity
import com.example.decotuk_app_capstone.util.USER
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database

        actionClick()

    }

    private fun actionClick(){
        binding.textView13.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.button2.setOnClickListener {
            inputDataUser()
        }
    }

    private fun inputDataUser() {
        val email = binding.editTextTextEmailAddress.text.trim().toString()
        val password = binding.editTextTextPassword.text.trim().toString()

        when {
            email.isEmpty() -> {
                binding.editTextTextEmailAddress.error = "Email tidak boleh kosong"
                binding.editTextTextEmailAddress.requestFocus()
            }
            password.isEmpty() -> {
                binding.editTextTextPassword.error = "Password tidak boleh kosong"
                binding.editTextTextPassword.requestFocus()
            }
            else -> {
                login(email, password)

            }
        }
    }

    private fun login(email: String, password: String) {
        val database = Firebase.database

        database.getReference(USER).get().addOnSuccessListener {
            for (data in it.children){
                val user = data.getValue(User::class.java)

                if (user != null){
                    if (email == user.email) {
                        if(password == user.password){
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                            finishAffinity()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, "Email atau Password yang anda masukan salah", Toast.LENGTH_SHORT).show()
                        }
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