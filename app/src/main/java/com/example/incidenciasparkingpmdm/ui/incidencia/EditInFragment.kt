package com.example.incidenciasparkingpmdm.ui.incidencia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentEditInBinding
import com.google.android.material.appbar.MaterialToolbar


class EditInFragment : Fragment() {
    private lateinit var binding: FragmentEditInBinding
    private val args: EditInFragmentArgs by navArgs()
    private val incidentViewModel: IncidentViewModel by activityViewModels()
    private val observer = Observer<Incident> {
        binding.titleInputTitulo.setText(it.title)
        binding.titleInputDesc.setText(it.description)
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
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.edit_inc_title)
        topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.textButtonArchivo.setOnClickListener {
            val action = EditInFragmentDirections.actionEditInFragmentToPreviewCameraFragment()

            view.findNavController().navigate(action)
        }

        incidentViewModel.fileCapture.observe(viewLifecycleOwner) { data ->

            Glide.with(this)
                .load(data)
                .into(binding.image)

        }
        incidentViewModel.fetchIncident(args.id)
        incidentViewModel.incident.observe(viewLifecycleOwner,observer)
    }



}