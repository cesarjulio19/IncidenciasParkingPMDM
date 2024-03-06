package com.example.incidenciasparkingpmdm.ui.incidencia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentEditInBinding
import com.example.incidenciasparkingpmdm.ui.user.User
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
class EditInFragment : Fragment() {
    private lateinit var binding: FragmentEditInBinding
    private val args: EditInFragmentArgs by navArgs()
    private val incidentViewModel: IncidentViewModel by activityViewModels()
    private var uri: Uri = Uri.EMPTY
    private val observer = Observer<Incident> {
        binding.titleInputTitulo.setText(it.title)
        binding.titleInputDesc.setText(it.description)
        val base64String = it.file
        val uriEdit = stringBytesToUri(requireContext(),base64String)
        incidentViewModel.updateUriEditData(uriEdit)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = requireActivity().intent.getSerializableExtra("user") as? User
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.edit_inc_title)
        topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        incidentViewModel.uriEdit.observe(viewLifecycleOwner){
            if(it != null){
                Glide.with(this)
                    .load(it)
                    .into(binding.image)

                uri = it
            }
        }

        binding.textButtonArchivo.setOnClickListener {
            val action = EditInFragmentDirections.actionEditInFragmentToPreviewCameraFragment()

            view.findNavController().navigate(action)
        }

        binding.textButtonGaleria.setOnClickListener {
            pickPhotoFromGallery()
        }

        incidentViewModel.fileCapture.observe(viewLifecycleOwner) { data ->

            incidentViewModel.updateUriEditData(data.toUri())
        }
        incidentViewModel.fetchIncident(args.id)
        incidentViewModel.incident.observe(viewLifecycleOwner,observer)

        binding.filledButtonEdit.setOnClickListener {
            val incidentDto = IncidentDto(binding.titleInputTitulo.text.toString(),
                binding.titleInputDesc.text.toString(),
                false,
                args.id)

            if(uri != null){
                val file: File? = uriToFile(requireContext(), uri)
                val fileRequestBody = RequestBody.create(
                    MediaType
                        .parse("image/jpeg"), file
                )
                val filePart = MultipartBody.Part
                    .createFormData("file", file?.name, fileRequestBody)

                lifecycleScope.launch {
                    try {
                        val call = incidentViewModel.updateIncident(args.id,incidentDto,filePart)
                        val response = call.execute()
                    } catch (e: Exception) {

                        Log.e("Error", "Error en la llamada: ${e.message}", e)
                    }
                    findNavController().popBackStack()
                }


            }else{
                Log.e("Error", "La Uri es nula")
            }
        }




    }

    fun stringBytesToUri(context: Context, base64String: String): Uri {
        // Decodificar la cadena a un array de bytes
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        // Guardar los bytes en un archivo temporal
        val tempFile = File.createTempFile("tempfile", ".jpg", context.cacheDir)
        val fileOutputStream = FileOutputStream(tempFile)
        fileOutputStream.write(decodedBytes)
        fileOutputStream.close()



        return Uri.fromFile(tempFile)
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->

        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data

            if (data != null) {
                incidentViewModel.updateUriEditData(data)
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

