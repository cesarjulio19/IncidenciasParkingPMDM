package com.example.incidenciasparkingpmdm.ui.incidencia.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentPreviewCameraBinding
import com.example.incidenciasparkingpmdm.ui.incidencia.IncidentViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class PreviewCameraFragment : Fragment() {

    private lateinit var binding: FragmentPreviewCameraBinding
    private val incidentViewModel: IncidentViewModel by activityViewModels()
    private var imageCapture: ImageCapture?=null
    private lateinit var outputDirectory: File


    private lateinit var cameraExecutor: ExecutorService

    companion object{
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO).apply {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewCameraBinding.inflate(inflater,
            container,
            false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.take_photo)
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.menu.close()

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        if(allPermisionsGranted()){
            Log.d("PERMISSIONS", "All permissions granted")
            startCamera()
        }else{
            Log.d("PERMISSIONS", "Requesting permissions")
            requestPermissions( REQUIRED_PERMISSIONS, 20)

        }

        binding.boton.setOnClickListener {
            takePhoto()


        }

        binding.image.setOnClickListener {
            val action = PreviewCameraFragmentDirections
                .actionPreviewCameraFragmentToCameraImageFragment()

            view.findNavController().navigate(action)
        }

            incidentViewModel.fileCapture.observe(viewLifecycleOwner) { data ->

              Glide.with(this)
                 .load(data)
                 .into(binding.image)

            }


    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {mFile->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }

        }

        return if(mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }



    private fun takePhoto(){

        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
                .format(System.currentTimeMillis()) +".jpg")


        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo Saved"

                    Toast.makeText(requireContext(),"$msg $savedUri", Toast.LENGTH_LONG).show()

                    loadPhotoIntoImageView(photoFile)

                    incidentViewModel.updateFileData(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("ERROR", "onError: ${exception.message}")
                }

            }
        )


    }

    private fun loadPhotoIntoImageView(photoFile: File) {

        Glide.with(this)
            .load(Uri.fromFile(photoFile))
            .into(binding.image)
    }

    private fun startCamera(){

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {mPreview->

                    mPreview.setSurfaceProvider(
                        binding.photoPreview.surfaceProvider
                    )

                }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            }catch (e: Exception){
                Log.d("CAMERA", "startCamera Fail: ", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("PERMISSIONS", "${requestCode}")
        if(requestCode == 20){

            if(allPermisionsGranted()){
                Log.d("PERMISSIONS", "All permissions granted after request")
                startCamera()
            }else{
                Log.d("PERMISSIONS", "Permissions not granted after request")
                Toast.makeText(requireActivity(), "Permissions not granted by the user",
                    Toast.LENGTH_SHORT).show()

                requireActivity().finish()
            }

        }
    }

    private fun allPermisionsGranted():Boolean = REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(requireContext(),it) == PackageManager.PERMISSION_GRANTED

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}