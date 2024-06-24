package com.mazpiss.skripsi.ui.tentang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.mazpiss.skripsi.databinding.FragmentTentangBinding

class TentangFragment : Fragment() {

    private var _binding: FragmentTentangBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val tentangViewModel =
            ViewModelProvider(this).get(TentangViewModel::class.java)

        _binding = FragmentTentangBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val appDescriptionTextView:TextView = binding.textDeskirpsiAplikasi
        val developerDescriptionTextView:TextView = binding.textSubDeskripsi1

        tentangViewModel.appDescription.observe(viewLifecycleOwner){
            appDescriptionTextView.text = it
        }
        tentangViewModel.developerDescription.observe(viewLifecycleOwner){
            developerDescriptionTextView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}