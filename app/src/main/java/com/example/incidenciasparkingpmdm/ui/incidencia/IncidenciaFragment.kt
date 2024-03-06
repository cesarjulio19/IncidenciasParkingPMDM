package com.example.incidenciasparkingpmdm.ui.incidencia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentIncidenciaBinding
import com.example.incidenciasparkingpmdm.ui.user.User
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class IncidenciaFragment: Fragment() {
    private lateinit var binding: FragmentIncidenciaBinding
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
        val adapter = IncidentAdapter(::onShowEdit, ::onDeleteIncident)
        val rv = binding.incidentList
        val observer = Observer<List<Incident>> {
            list.clear()
            list.addAll(it.filter { i -> i.user.id == user?.id })
            adapter.submitList(list)
            rv.adapter = adapter
        }
        incidentViewModel.fetch()
        incidentViewModel.incidentList.observe(viewLifecycleOwner, observer)

        binding.addButton.setOnClickListener {
            val action = IncidenciaFragmentDirections.actionIncidenciaFragmentToCreateInFragment(user?.id!!)
            view.findNavController().navigate(action)
        }
    }

    private fun onShowEdit(incident: Incident) {
        val action = IncidenciaFragmentDirections
            .actionIncidenciaFragmentToEditInFragment(incident.idInc!!)
        findNavController().navigate(action)
    }

    private fun onDeleteIncident(incident: Incident, position: Int, adapter: IncidentAdapter) {
        val call = incidentViewModel.deleteIncident(incident.idInc!!)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("RESPONSE", response.body().toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }

        })
        adapter.notifyItemRemoved(position)
        incidentViewModel.fetch()
    }
}