package com.mazpiss.skripsi.ui.materi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazpiss.skripsi.databinding.FragmentMateriBinding

class MateriFragment : Fragment() {

    private lateinit var viewModel: MateriViewModel
    private lateinit var myAdapter: MateriAdapter
    private var _binding : FragmentMateriBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMateriBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MateriViewModel::class.java)

        binding.rvMateri.layoutManager = LinearLayoutManager(context)
        binding.rvMateri.setHasFixedSize(true)
        myAdapter = MateriAdapter(arrayListOf())
        binding.rvMateri.adapter = myAdapter

        viewModel.materiList.observe(viewLifecycleOwner, Observer { materialList->
            myAdapter.updateData(materialList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}