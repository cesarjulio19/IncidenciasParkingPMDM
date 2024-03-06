package com.example.incidenciasparkingpmdm.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.LoginActivity
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentUserBinding
import com.google.android.material.appbar.MaterialToolbar

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = this.requireActivity().intent.getSerializableExtra("user") as? User
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        topAppBar.title = getString(R.string.profile)
        topAppBar.navigationIcon = null
        if(user != null) {
            binding.userName.text = "${user.name} ${user.surname}"
            binding.userSchoolYear.text = user.schoolYear
            binding.editProfile.setOnClickListener {
                val action = UserFragmentDirections.actionUserFragmentToEditUserFragment()
                findNavController().navigate(action)
            }
            binding.logout.setOnClickListener {
                val intent = Intent(this.activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }
}