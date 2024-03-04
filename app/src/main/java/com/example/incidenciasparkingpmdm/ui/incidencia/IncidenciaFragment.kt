package com.example.incidenciasparkingpmdm.ui.incidencia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.api.IncidentService
import com.example.incidenciasparkingpmdm.databinding.FragmentIncidenciaBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncidenciaFragment : Fragment() {
    private lateinit var binding: FragmentIncidenciaBinding
    @Inject
    lateinit var incidentService: IncidentService
    private val incidentViewModel: IncidentViewModel by activityViewModels()
    val list: MutableList<Incident> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncidenciaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val user: User? = requireActivity().intent.getSerializableExtra("user") as? User
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.incidents_title)
        incidentViewModel.fetch(user?.email.toString(), user?.password.toString())
        incidentViewModel.incidentList.observe(viewLifecycleOwner){
            it.forEach(){
                if(it.userId == user?.id){
                    list.add(it)
                }
            }
        }

        binding.addButton.setOnClickListener {
            val action = IncidenciaFragmentDirections.actionIncidenciaFragmentToCreateInFragment(user?.id!!)
            view.findNavController().navigate(action)
        }

        val adapter = IncidentAdapter( ::onShowEdit)
        val rv = binding.incidentList
        adapter.submitList(list)
        rv.adapter = adapter




    }

    private fun onShowEdit(incident: Incident) {
        val action = IncidenciaFragmentDirections
            .actionIncidenciaFragmentToEditInFragment(incident.idInc!!)
        findNavController().navigate(action)
    }

}