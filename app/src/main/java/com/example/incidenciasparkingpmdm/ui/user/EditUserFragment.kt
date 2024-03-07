package com.example.incidenciasparkingpmdm.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.incidenciasparkingpmdm.R
import com.example.incidenciasparkingpmdm.databinding.FragmentEditUserBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class EditUserFragment() : Fragment() {
    private lateinit var binding: FragmentEditUserBinding
    private val viewModel: UserViewModel by viewModels()
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
        topAppBar.title = getString(R.string.edit_profile_title)
        topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        lifecycleScope.launch {
            val user = requireActivity().intent.getSerializableExtra("user") as? User
            val list = listOf<String>("DAW", "DAM", "SMR")
            val mutableList: MutableList<String> = mutableListOf()
            list.forEach {
                mutableList.add(it)
            }
            val autoCompleteTextView: AutoCompleteTextView = binding.autoComplete
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                mutableList
            )
            autoCompleteTextView.setAdapter(adapter)
            binding.name.text = "${user?.name} ${user?.surname}"
            binding.titleInputDirec.setText(user?.address)
            autoCompleteTextView.setText(user?.schoolYear)
            binding.saveButton.setOnClickListener {
                lifecycleScope.launch {
                    if (hasBeenChanged(
                            binding.titleInputDirec.text.toString(),
                            autoCompleteTextView.text.toString()
                        )
                    ) {
                        val updatedUser = User(
                            user?.id!!,
                            user.name,
                            user.surname,
                            user.nif,
                            user.email,
                            user.password,
                            user.postalCode,
                            binding.titleInputDirec.text.toString(),
                            user.rol,
                            autoCompleteTextView.text.toString(),
                            user.parkingAccess
                        )
                        val call = viewModel.updateUser(updatedUser.id!!, updatedUser)
                        call.enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                            }
                        })
                        findNavController().popBackStack()
                    }

                }
            }
        }
    }
    private fun hasBeenChanged(address: String, schoolYear: String): Boolean {
        return address != binding.titleInputDirec.text.toString() ||
                schoolYear != binding.autoComplete.text.toString()
    }
}
/*private val startForActivityGallery = registerForActivityResult(
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
    }*/