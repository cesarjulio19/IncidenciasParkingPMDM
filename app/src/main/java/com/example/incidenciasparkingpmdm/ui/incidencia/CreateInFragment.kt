package com.example.incidenciasparkingpmdm.ui.incidencia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentCreateInBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class CreateInFragment : Fragment() {
    private lateinit var binding: FragmentCreateInBinding
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
    }
}