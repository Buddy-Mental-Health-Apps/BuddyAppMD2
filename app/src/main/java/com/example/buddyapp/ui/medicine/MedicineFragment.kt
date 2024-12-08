package com.example.buddyapp.ui.medicine

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buddyapp.R
import com.example.buddyapp.databinding.FragmentMedicineBinding

class MedicineFragment : Fragment() {

    private var _binding: FragmentMedicineBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedicineViewModel by viewModels()
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

//        TODO("Implement Adapter")

//        with(binding) {
//            searchView.setupWithSearchBar(searchBar)
//            searchView
//                .editText
//                .setOnEditorActionListener { _, actionId, _ ->
//                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                        val query = searchView.text.toString()
//                        if (query.isNotEmpty()) {
//                            viewModel.searchMedicine(query)
//                            searchBar.setText(searchView.text)
//                        } else {
//                            viewModel.retryGetMedicine()
//                            searchBar.setText(null)
//                        }
//                        searchView.hide()
//                        true
//                    } else {
//                        false
//                    }
//                }
//        }

    }
}