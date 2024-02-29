package com.example.incidenciasparkingpmdm.ui.incidencia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.FragmentCreateInBinding
import com.example.incidenciasparkingpmdm.ui.incidencia.camera.PreviewCameraFragmentDirections
import com.example.incidenciasparkingpmdm.ui.register.RegisterDirections
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class CreateInFragment : Fragment() {
    private lateinit var binding: FragmentCreateInBinding
    private val incidentViewModel: IncidentViewModel by activityViewModels()
    private val args : CreateInFragmentArgs by navArgs()
    private lateinit var uri: Uri
    @Inject
    lateinit var incidentService: IncidentService
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

        incidentViewModel.fileCapture.observe(viewLifecycleOwner) { data ->

            Glide.with(this)
                .load(data)
                .into(binding.image)
            uri = data.toUri()
        }

        binding.textButtonGaleria.setOnClickListener {
            pickPhotoFromGallery()
        }

        binding.filledButtonCreate.setOnClickListener {
            val incidentDto = IncidentDto(binding.titleInputTitulo.text.toString(),
                binding.titleInputDesc.text.toString(),
                false,
                args.id)
            // paso la uri a file
            val file: File = uriToFile(requireContext(), uri)

            // creo el requestBody del file
            val fileRequestBody = RequestBody.create(
                MediaType
                    .parse("multipart/form-data"), file
            )
            // creo el filePart
            val filePart = MultipartBody.Part
                .createFormData("file", file.name, fileRequestBody)

            lifecycleScope.launch {
                var call = incidentService.api.addIncident(incidentDto,filePart)
                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(!response.isSuccessful) {
                            Log.e("No esito","no tuvo esito")
                        }
                        response.body()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("Fallo","Fallo")
                    }
                })
                val action = RegisterDirections.actionRegisterToLogin()
                findNavController().navigate(action)
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
                uri = data
            }

        }

    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    // Uri to File

    fun uriToFile(context: Context, uri: Uri): File {
        var inputStream: InputStream? = null
        var file = File("")
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