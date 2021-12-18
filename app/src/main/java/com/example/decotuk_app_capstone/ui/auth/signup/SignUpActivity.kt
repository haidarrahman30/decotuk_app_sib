package com.example.decotuk_app_capstone.ui.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.decotuk_app_capstone.data.source.local.entity.User
import com.example.decotuk_app_capstone.databinding.ActivitySignUpBinding
import com.example.decotuk_app_capstone.ui.auth.login.LoginActivity
import com.example.decotuk_app_capstone.util.USER
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private var _binding : ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionClick()
    }

    private fun actionClick() {
        binding.textView13.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.button5.setOnClickListener {
            submitData()
        }
    }

    private fun submitData() {
        val nama = binding.editTextTextPersonName.text.toString().trim()
        val email = binding.editTextTextEmailAddress2.text.toString().trim()
        val password = binding.editTextTextPassword2.text.toString().trim()
        val repassword = binding.editTextTextPassword3.text.toString().trim()

        when{
            nama.isEmpty() -> {
                binding.editTextTextPersonName.error = "Nama tidak boleh kosong"
                binding.editTextTextPersonName.requestFocus()
            }
            email.isEmpty() -> {
                binding.editTextTextEmailAddress2.error = "Email tidak boleh kosong"
                binding.editTextTextEmailAddress2.requestFocus()
            }
            password.isEmpty() -> {
                binding.editTextTextPassword2.error = "Password tidak boleh kosong"
                binding.editTextTextPassword2.requestFocus()
            }
            password.length < 6 -> {
                binding.editTextTextPassword2.error = "Password minimal terdiri dari 6 karakter"
                binding.editTextTextPassword2.requestFocus()
            }
            repassword != password -> {
                binding.editTextTextPassword3.error = "Password tidak sama"
                binding.editTextTextPassword3.requestFocus()
            }
            else -> saveDataToFirebase(nama, email, repassword)
        }
    }

    private fun saveDataToFirebase(nama: String, email: String, repassword: String) {
        val database = Firebase.database
        val firebaseAuth = Firebase.auth

        firebaseAuth.createUserWithEmailAndPassword(email, repassword).addOnCompleteListener {
            if (it.isSuccessful){
                val newUser = User(
                    id = it.result?.user?.uid,
                    nama = nama,
                    email = email,
                    password = repassword,
                    image = "",
                )

                database.getReference(USER).child("${it.result?.user?.uid}").setValue(newUser)
                    .addOnSuccessListener {
                        Toast.makeText(this@SignUpActivity, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    }
                    .addOnFailureListener{
                        Toast.makeText(this@SignUpActivity, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}