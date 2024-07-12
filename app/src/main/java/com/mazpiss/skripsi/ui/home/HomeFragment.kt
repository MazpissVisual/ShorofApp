package com.mazpiss.skripsi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.databinding.FragmentHomeBinding
import com.mazpiss.skripsi.ui.kosakata.KosakataAdapter

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var myAdapter: KosakataAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        myAdapter = KosakataAdapter(emptyList())

        binding.rvKosakata.layoutManager = LinearLayoutManager(context)
        binding.rvKosakata.setHasFixedSize(true)
        binding.rvKosakata.adapter = myAdapter

        viewModel.kosakataList.observe(viewLifecycleOwner, Observer { kosakataList ->
            myAdapter.updateData(kosakataList)
        })

        val btnAbout:ImageButton = view.findViewById(R.id.btnAbout)
        btnAbout.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aboutActivity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
