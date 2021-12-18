package com.example.decotuk_app_capstone.ui.profile.laporan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.decotuk_app_capstone.data.preferences.PreferenceRepository
import com.example.decotuk_app_capstone.data.preferences.UserPreference
import com.example.decotuk_app_capstone.data.source.local.entity.User
import com.example.decotuk_app_capstone.databinding.ActivityLaporanBinding
import com.example.decotuk_app_capstone.util.DateFormat
import com.example.decotuk_app_capstone.util.USER
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LaporanActivity : AppCompatActivity() {

    private var _binding : ActivityLaporanBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref : PreferenceRepository
    private lateinit var uid : String

    private var database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PreferenceRepository.getInstance(UserPreference(this))
        uid = pref.getUser("UID")!!

        showEmail()

        binding.btnKirim.setOnClickListener {
            kirimSaran()
        }
    }

    private fun kirimSaran() {
        val email = binding.edtEmail.text.toString()
        val saran = binding.edtSaran.text.toString()

        when {
            email.isEmpty() -> {
                binding.edtEmail.error = "Email tidak boleh kosong"
            }
            saran.isEmpty() -> {
                binding.edtSaran.error = "Saran tidak boleh kosong"
            }
            else -> {
                saveToFirebase(email, saran)
            }
        }
    }

    private fun saveToFirebase(email : String, saran : String) {
        val myRef = database.getReference("Laporan")
        val laporan = HashMap<String, Any>()
        laporan["email"] = email
        laporan["saran"] = saran
        laporan["createdAt"] = DateFormat.setDate()

        myRef.push().setValue(laporan)
            .addOnSuccessListener {
                Toast.makeText(this, "Kritik dan saran berhasil dikirim", Toast.LENGTH_SHORT).show()
                binding.edtSaran.setText("")
            }
            .addOnFailureListener {
                Toast.makeText(this, "Kritik dan saran gagal dikirim", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEmail() {
       val myRef = database.getReference(USER).child(uid)
        myRef.get().addOnSuccessListener {
            if (it.exists()) {
                val user = it.getValue(User::class.java)

                if (user != null){
                    binding.edtEmail.setText(user.email)
                }
            }
        }
    }
}