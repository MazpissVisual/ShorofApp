package com.mazpiss.skripsi.ui.kosakata_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazpiss.skripsi.databinding.FragmentKosakataBinding
import com.mazpiss.skripsi.ui.kosakata.KosakataAdapter

class KosakataFragment : Fragment() {

    private lateinit var kosakataViewModel: KosakataViewModel
    private lateinit var kosakataAdapter: KosakataAdapter
    private var _binding: FragmentKosakataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentKosakataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kosakataViewModel = ViewModelProvider(this).get(KosakataViewModel::class.java)
        kosakataAdapter = KosakataAdapter(emptyList())

        binding.rvKosakata.layoutManager = LinearLayoutManager(context)
        binding.rvKosakata.setHasFixedSize(true)
        binding.rvKosakata.adapter = kosakataAdapter

        kosakataViewModel.kosakataList.observe(viewLifecycleOwner, Observer { kosakatalist ->
            kosakataAdapter.updateData(kosakatalist)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}