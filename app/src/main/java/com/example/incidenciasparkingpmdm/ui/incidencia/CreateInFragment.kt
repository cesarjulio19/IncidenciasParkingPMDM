package com.example.incidenciasparkingpmdm.ui.incidencia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentCreateInBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class CreateInFragment : Fragment() {
    private lateinit var binding: FragmentCreateInBinding
    private val incidentViewModel: IncidentViewModel by activityViewModels()
    private val args : CreateInFragmentArgs by navArgs()
    private lateinit var uri: Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.add_inc_title)
        topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.textButtonArchivo.setOnClickListener {
             val action = CreateInFragmentDirections
                 .actionCreateInFragmentToPreviewCameraFragment()

            view.findNavController().navigate(action)

        }

        incidentViewModel.uri.observe(viewLifecycleOwner) { uri2 ->
            if (uri2 != null) {
                Glide.with(this)
                    .load(uri2)
                    .into(binding.image)

                uri = uri2

            }
        }

        incidentViewModel.fileCapture.observe(viewLifecycleOwner) { data ->

            Glide.with(this)
                .load(data)
                .into(binding.image)
            incidentViewModel.updateUriData(data.toUri())
        }

        binding.textButtonGaleria.setOnClickListener {
            pickPhotoFromGallery()
        }

        binding.filledButtonCreate.setOnClickListener {
            val incidentDto = IncidentDto(binding.titleInputTitulo.text.toString(),
                binding.titleInputDesc.text.toString(),
                false,
                args.id)
            if(uri != null) {
                // paso la uri a file
                val file: File? = uriToFile(requireContext(), uri)

                // creo el requestBody del file
                val fileRequestBody = RequestBody.create(
                    MediaType
                        .parse("image/jpeg"), file
                )
                // creo el filePart
                val filePart = MultipartBody.Part
                    .createFormData("file", file?.name, fileRequestBody)

                lifecycleScope.launch {
                    try {
                        val call = incidentViewModel.createIncident(incidentDto, filePart)
                        val response = call.execute()
                    } catch (e: Exception) {

                        Log.e("Error", "Error en la llamada: ${e.message}", e)
                    }
                    incidentViewModel.updateUriData(Uri.EMPTY)
                    incidentViewModel.updateFileData(File(""))
                    findNavController().popBackStack()
                }
            } else {
                Log.e("Error", "La Uri es nula")
            }
        }
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->

        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data

            Glide.with(this)
                .load(data)
                .into(binding.image)
            if (data != null) {
                incidentViewModel.updateUriData(data)
            }

        }

    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    // Uri to File

    fun uriToFile(context: Context, uri: Uri): File? {
        var inputStream: InputStream? = null
        var file : File? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                file = createTempFile(context)
                copyInputStreamToFile(inputStream, file)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }

    @Throws(IOException::class)
    private fun createTempFile(context: Context): File {
        val tempFileName = "temp_file"
        val outputDir = context.cacheDir
        return File.createTempFile(tempFileName, null, outputDir)
    }

    @Throws(IOException::class)
    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.flush()
        outputStream.close()
    }
}