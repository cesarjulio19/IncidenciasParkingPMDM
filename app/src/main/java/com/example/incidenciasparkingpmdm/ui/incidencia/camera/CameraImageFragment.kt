package com.example.incidenciasparkingpmdm.ui.incidencia.camera

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.databinding.FragmentCameraImageBinding
import com.example.incidenciasparkingpmdm.ui.incidencia.IncidentViewModel


class CameraImageFragment : Fragment() {

    private lateinit var binding: FragmentCameraImageBinding
    private val incidentViewModel: IncidentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraImageBinding.inflate(inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incidentViewModel.fileCapture.observe(viewLifecycleOwner) { data ->
            Log.e("IMAGE", "${data}")
            Glide.with(this)
                .load(data)
                .into(binding.image1)

        }
    }

}