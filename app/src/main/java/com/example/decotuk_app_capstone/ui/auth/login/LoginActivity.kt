package com.example.decotuk_app_capstone.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
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
    private lateinit var pref : PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database
        pref = PreferenceRepository.getInstance(UserPreference(this))

        actionClick()

        if (pref.isUserLogin()){
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

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
                showProgressBar()
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
                            pref.loginUser("UID", user.id.toString())
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                            finish()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, "Email atau Password yang anda masukan salah", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            hideProgressBar()
        }
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
        binding.textView9.isInvisible = true
        binding.editTextTextEmailAddress.isInvisible = true
        binding.textView10.isInvisible = true
        binding.textView11.isInvisible = true
        binding.editTextTextPassword.isInvisible = true
        binding.button2.isInvisible = true
        binding.textView13.isInvisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isInvisible = true
        binding.textView9.isVisible = true
        binding.editTextTextEmailAddress.isVisible = true
        binding.textView10.isVisible = true
        binding.textView11.isVisible = true
        binding.editTextTextPassword.isVisible = true
        binding.button2.isVisible = true
        binding.textView13.isVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}