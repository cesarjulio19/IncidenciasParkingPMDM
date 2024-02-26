package com.example.incidenciasparkingpmdm.ui.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentEditUserBinding
import com.google.android.material.appbar.MaterialToolbar


class EditUserFragment : Fragment() {
    private lateinit var binding: FragmentEditUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAppBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        val user = this.activity?.intent?.getSerializableExtra("user") as? User
        binding.name.text = "${user?.name} ${user?.surname}"
        binding.titleInputCpostal.text = user?.postalCode.toString() as Editable
        binding.titleInputDirec.text = user?.address.toString() as Editable
        binding.autoComplete.text = user?.schoolYear.toString() as Editable
        topAppBar.title = getString(R.string.add_inc_title)
        topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.textButtonEditPhoto.setOnClickListener {
            pickPhotoFromGallery()
        }
    }




    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->

        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data

            Glide.with(this)
                .load(data)
                .into(binding.profileImage)
        }

    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }


}