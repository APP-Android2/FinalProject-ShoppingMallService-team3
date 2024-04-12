package kr.co.lion.farming_customer.fragment.famingLifeTools

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeToolsListBinding
import kr.co.lion.farming_customer.fragment.famingLifeTools.adapter.FarmingLifeListRVAdapter

class FarmingLifeToolsListFragment : Fragment() {
    lateinit var binding: FragmentFarmingLifeToolsListBinding
    lateinit var listRVAdapter: FarmingLifeListRVAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFarmingLifeToolsListBinding.inflate(layoutInflater)

        listRVAdapter = FarmingLifeListRVAdapter(requireContext())
        binding.farmingLifeToolsListRv.adapter = listRVAdapter
        binding.farmingLifeToolsListRv.layoutManager = LinearLayoutManager(context)

        return binding.root
    }
}