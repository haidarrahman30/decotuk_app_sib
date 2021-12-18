package com.example.decotuk_app_capstone.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.decotuk_app_capstone.R
import com.example.decotuk_app_capstone.data.preferences.PreferenceRepository
import com.example.decotuk_app_capstone.data.preferences.UserPreference
import com.example.decotuk_app_capstone.data.source.local.entity.User
import com.example.decotuk_app_capstone.databinding.FragmentHomeBinding
import com.example.decotuk_app_capstone.databinding.FragmentProfileBinding
import com.example.decotuk_app_capstone.ui.auth.login.LoginActivity
import com.example.decotuk_app_capstone.ui.home.HomeViewModel
import com.example.decotuk_app_capstone.ui.profile.edit.EditProfileActivity
import com.example.decotuk_app_capstone.ui.profile.laporan.LaporanActivity
import com.example.decotuk_app_capstone.util.USER
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private lateinit var pref : PreferenceRepository

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mProfileUri : Uri
    private lateinit var uid : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = PreferenceRepository.getInstance(UserPreference(view.context))
        uid = pref.getUser("UID")!!

        showProfile()
        actionClick()

    }

    private fun actionClick() {
        binding.logout.setOnClickListener {
            pref.logoutUser()
            activity?.finishAffinity()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        binding.ivAddImage.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.editProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.kirimLaporan.setOnClickListener {
            startActivity(Intent(requireContext(), LaporanActivity::class.java))
        }
    }

    private fun showProfile() {
        val database = Firebase.database
        val id = pref.getUser("UID")

        database.getReference(USER).child(id.toString()).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()){
                    val user = snapshot.getValue(User::class.java)
                    if (user != null){
                        with(binding){
                            tvUsername.text = user.nama
                            textView8.text = user.email

                            Glide.with(requireContext())
                                .load(user.image)
                                .into(binding.ivProfile)
                        }
                    }
                }
            }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                mProfileUri = data?.data!!
                binding.ivProfile.setImageURI(mProfileUri)

                val storage = FirebaseStorage.getInstance().reference
                val progressDialog = Dialog(requireContext())
                progressDialog.setCancelable(false)
                progressDialog.show()

                val myRef = storage.child("profile/" + UUID.randomUUID().toString())
                myRef.putFile(mProfileUri)
                    .addOnSuccessListener {
                        progressDialog.dismiss()

                        myRef.downloadUrl.addOnSuccessListener {
                            val image = HashMap<String, Any>()
                            image["image"] = it.toString()

                            val database = Firebase.database
                            database.getReference(USER).child(uid).updateChildren(image)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Foto profile berhasil di ubah", Toast.LENGTH_SHORT).show()
                                }
                                .addOnCanceledListener {
                                    Toast.makeText(requireContext(), "Foto profile gagal di ubah", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}