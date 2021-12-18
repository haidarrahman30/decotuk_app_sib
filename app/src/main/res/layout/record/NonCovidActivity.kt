package com.example.decotuk_app_capstone.ui.record

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.decotuk_app_capstone.MainActivity
import com.example.decotuk_app_capstone.R
import com.example.decotuk_app_capstone.databinding.ActivityNonCovidBinding

class NonCovidActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNonCovidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNonCovidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button4.setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}