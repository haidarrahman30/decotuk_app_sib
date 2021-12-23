package com.example.decotuk_app_capstone.ui.record

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.decotuk_app_capstone.R
import com.example.decotuk_app_capstone.data.preferences.PreferenceRepository
import com.example.decotuk_app_capstone.data.preferences.UserPreference
import com.example.decotuk_app_capstone.data.source.local.entity.User
import com.example.decotuk_app_capstone.databinding.FragmentRecordBinding
import com.example.decotuk_app_capstone.ml.SoundclassifierWithMetadata
import com.example.decotuk_app_capstone.util.DateFormat
import com.example.decotuk_app_capstone.util.Timer
import com.example.decotuk_app_capstone.util.USER
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import org.tensorflow.lite.support.audio.TensorAudio
import java.io.File
import kotlin.collections.HashMap
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.audio.classifier.AudioClassifier.AudioClassifierOptions
import org.tensorflow.lite.task.audio.classifier.Classifications
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment(), Timer.OnTimerTickListener {

    companion object {
        const val REQUEST_CODE = 200
        private const val MODEL_FILE = "soundclassifier_with_metadata.tflite"
    }

    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    private lateinit var model : SoundclassifierWithMetadata
    private lateinit var audioTensor : TensorAudio
    private lateinit var classifier : AudioClassifier

    private var recorder : MediaRecorder? = null

    private var dirPath = ""
    private var fileName = ""
    private var isRecord = false
    private var fileUri : Uri? = null
    private var uid : String? = null

    private lateinit var timer : Timer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordViewModel =
            ViewModelProvider(this).get(RecordViewModel::class.java)

        permissionGranted = ActivityCompat.checkSelfPermission(requireContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted)
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE)

        timer = Timer(this)


        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classifier = AudioClassifier.createFromFile(context, MODEL_FILE)
        audioTensor = classifier.createInputTensorAudio()

        binding.btRecord.setOnClickListener {
            when {
                isRecord -> stopRecording()
                else -> startRecording()
            }
        }

        val pref = PreferenceRepository.getInstance(UserPreference(requireContext()))
        uid = pref.getUser("UID")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE){
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startRecording(){
        if (!permissionGranted){
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE)
            return
        }
        // startRecording
        recorder = MediaRecorder()
        dirPath = "${view?.context?.externalCacheDir?.absolutePath}/"

        dirPath = activity?.getExternalFilesDir("/")?.absolutePath!!
        val simpletDate = SimpleDateFormat("yyyy.MM.DD_hh:mm:ss")
        val date = simpletDate.format(Date())
        fileName = "audiorRecord_$date"

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath/$fileName.mp3")

            Log.d("RecordFragment", "$dirPath/$fileName.mp3")

            try {
                prepare()
            } catch (e : Exception) {}

            start()
        }

        val record = classifier.createAudioRecord()
        record.startRecording()

        audioTensor.load(record)

        binding.btRecord.setImageResource(R.drawable.ic_baseline_stop_circle_24)
        binding.btnSave.visibility = View.GONE
        isRecord = true

        timer.start()

        val file = "$dirPath/$fileName.mp3"
        fileUri = Uri.fromFile(File(file))
    }

    private fun saveToFirebase(fileUri: Uri?) {
        val storage = FirebaseStorage.getInstance().reference

        val results: List<Classifications> = classifier.classify(audioTensor)
        Log.d("RecordFragment", "result : ${results}")

        results[0].categories.forEach {
            println("nama : ${it.label}, score : ${it.score}")
        }

        val metaData = StorageMetadata.Builder()
            .setContentType("audio/mp3")
            .build()

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Memeriksa...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val ref = storage.child("$uid/rekaman/" + uid + DateFormat.setDate())
        if (results[0].categories[0].score == 0.0.toFloat()){
            Toast.makeText(requireContext(), results[0].categories[0].label, Toast.LENGTH_SHORT).show()
        } else {
            ref.putFile(fileUri!!, metaData)
                .addOnSuccessListener {
                    progressDialog.dismiss()

                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val suara = HashMap<String, Any>()
                        suara["suara"] = uri.toString()
                        suara["uid"] = uid.toString()
                        suara["createdAt"] = DateFormat.setDate()

                        if (results[0].categories[0].score == 0.0.toFloat()) {
                            activity?.finish()
                            startActivity(Intent(requireContext(), CovidPositifActivity::class.java))
                            suara["result"] = "Positif"
                        } else {
                            activity?.finish()
                            startActivity(Intent(requireContext(), NonCovidActivity::class.java))
                            suara["result"] = "Negatif"
                        }

                        val database = Firebase.database
                        database.getReference("Rekaman").push().setValue(suara).addOnSuccessListener {
                            database.getReference(USER).child("$uid/rekaman/${suara["createdAt"]}").updateChildren(suara)
                        }



                    }
                }
                .addOnProgressListener {
                    progressDialog.setMessage("Mendeteksi..")
                }
        }

    }

    private fun stopRecording(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            timer.stop()
            recorder?.stop()
            recorder?.release()
            isRecord = false
            recorder = null
            binding.btRecord.setImageResource(R.drawable.ic_mic_24)
            binding.btnSave.visibility = View.VISIBLE
            binding.textView25.visibility = View.INVISIBLE

            binding.btnSave.setOnClickListener {
                saveToFirebase(fileUri)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimerTick(duration: String) {
        binding.elapsedTime.text = duration
    }

}