package com.example.incidenciasparkingpmdm.ui.incidencia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.api.IncidentApi
import com.example.incidenciasparkingpmdm.databinding.FragmentIncidenciaBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView


class IncidenciaFragment : Fragment() {
    private lateinit var binding: FragmentIncidenciaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncidenciaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawerLayout)
        val navigationView: NavigationView = requireActivity().findViewById(R.id.navigation_view)
        topAppBar.title = getString(R.string.incidents_title)
        topAppBar.setNavigationIcon(R.drawable.ic_launcher_foreground)
        topAppBar.setNavigationOnClickListener {
            drawerLayout.open()
        }
        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            drawerLayout.close()
            true
        }

        binding.addButton.setOnClickListener {
            val action = IncidenciaFragmentDirections.actionIncidenciaFragmentToCreateInFragment()
            view.findNavController().navigate(action)
        }

    }

    private fun onShowEdit(id: Int,view:View) {
        val action = IncidenciaFragmentDirections
            .actionIncidenciaFragmentToEditInFragment(id)
        view.findNavController().navigate(action)
    }
}