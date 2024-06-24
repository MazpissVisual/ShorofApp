package com.mazpiss.skripsi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.databinding.FragmentHomeBinding
import com.mazpiss.skripsi.ui.kosakata.Kosakata
import com.mazpiss.skripsi.ui.kosakata.KosakataAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val list = ArrayList<Kosakata>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.rvKosakata.setHasFixedSize(true)
        list.addAll(getKosakata())
        showRecyclerList()

        return binding.root
    }

    private fun showRecyclerList() {
        binding.rvKosakata.layoutManager = LinearLayoutManager(context)
        // Ambil hanya 4 item pertama dari list dan konversikan ke ArrayList
        val limitedList = if (list.size > 4) ArrayList(list.subList(0, 4)) else list
        val listKosakataAdapter = KosakataAdapter(limitedList)
        binding.rvKosakata.adapter = listKosakataAdapter
    }

    private fun getKosakata(): ArrayList<Kosakata> {
        val kosakata1 = resources.getStringArray(R.array.data_kosakata_harian1)
        val kosakata2 = resources.getStringArray(R.array.data_kosakata_harian2)
        val terjemahan1 = resources.getStringArray(R.array.data_terjemahan1)
        val terjemahan2 = resources.getStringArray(R.array.data_terjemahan2)

        val listKosakata = ArrayList<Kosakata>()
        for (i in kosakata1.indices) {
            val kosakata = Kosakata(kosakata1[i], kosakata2[i], terjemahan1[i], terjemahan2[i])
            listKosakata.add(kosakata)
        }
        return listKosakata
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
