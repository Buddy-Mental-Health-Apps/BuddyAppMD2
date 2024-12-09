package com.example.buddyapp.ui.medicine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buddyapp.databinding.FragmentMedicineBinding
import com.example.buddyapp.ui.MedicineViewModelFactory

class MedicineFragment : Fragment() {

    private var _binding: FragmentMedicineBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MedicineViewModel
    private lateinit var medicineAdapter: MedicineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvMedicine.layoutManager = layoutManager

        val factory = MedicineViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[MedicineViewModel::class.java]

        medicineAdapter = MedicineAdapter(arrayListOf())
        binding.rvMedicine.adapter = medicineAdapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val query = searchView.text.toString()
                        if (query.isNotEmpty()) {
                            viewModel.searchMedicine(query)
                            searchBar.setText(searchView.text)
                        } else {
                            viewModel.retryGetMedicine()
                            searchBar.setText(null)
                        }
                        searchView.hide()
                        true
                    } else {
                        false
                    }
                }
        }

        viewModel.listMedicine.observe(viewLifecycleOwner) { medicines ->
            medicineAdapter.updateMedicineList(medicines)
            if (medicines.isNullOrEmpty()) {
                binding.tvEventNotFound.visibility = View.VISIBLE
            } else {
                binding.tvEventNotFound.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}