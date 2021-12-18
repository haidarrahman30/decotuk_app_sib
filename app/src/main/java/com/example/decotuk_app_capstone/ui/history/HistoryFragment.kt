package com.example.decotuk_app_capstone.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decotuk_app_capstone.data.preferences.PreferenceRepository
import com.example.decotuk_app_capstone.data.preferences.Record
import com.example.decotuk_app_capstone.data.preferences.UserPreference
import com.example.decotuk_app_capstone.databinding.FragmentHistoryBinding
import com.example.decotuk_app_capstone.util.USER
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var pref : PreferenceRepository
    private var uid : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = PreferenceRepository.getInstance(UserPreference(requireContext()))
        uid = pref.getUser("UID")
        lifecycleScope.launch {
            showRecords()
        }

    }

    private suspend fun showRecords() {
        val database = Firebase.database
        val listRecords = ArrayList<Record>()

        val myRef = database.getReference(USER).child("$uid/rekaman")
        coroutineScope {
            this.launch(Dispatchers.IO) {
                myRef.get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val records = data.getValue(Record::class.java)
                            if (records != null) {
                                listRecords.add(records)
                            }
                        }

                        if (listRecords.isNotEmpty()) {
                            binding.rvRecords.visibility = View.VISIBLE
                            binding.tvDataEmpty.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE

                            val adapter = HistoryAdapter()
                            listRecords.reverse()
                            adapter.setData(listRecords)

                            binding.rvRecords.layoutManager = LinearLayoutManager(activity)
                            binding.rvRecords.setHasFixedSize(true)
                            binding.rvRecords.adapter = adapter
                        } else {
                            binding.rvRecords.visibility = View.GONE
                            binding.tvDataEmpty.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}