package kr.co.lion.farming_customer.fragment.famingLifeTools

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.dao.farmingLifeTools.RentalDao
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeToolsListBinding
import kr.co.lion.farming_customer.fragment.famingLifeTools.adapter.FarmingLifeListRVAdapter
import kr.co.lion.farming_customer.model.farminLifeTools.RentalModel

class FarmingLifeToolsListFragment : Fragment() {
    lateinit var binding: FragmentFarmingLifeToolsListBinding
    lateinit var listRVAdapter: FarmingLifeListRVAdapter

    var rentalList = mutableListOf<RentalModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFarmingLifeToolsListBinding.inflate(layoutInflater)

        settingInitData()



        return binding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            rentalList = RentalDao.gettingRentalList()

            listRVAdapter = FarmingLifeListRVAdapter(requireContext(), rentalList)
            binding.farmingLifeToolsListRv.adapter = listRVAdapter
            binding.farmingLifeToolsListRv.layoutManager = LinearLayoutManager(context)
        }
    }
}