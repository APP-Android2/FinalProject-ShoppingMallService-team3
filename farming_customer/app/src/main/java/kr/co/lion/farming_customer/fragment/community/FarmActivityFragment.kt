package kr.co.lion.farming_customer.fragment.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityBinding
import kr.co.lion.farming_customer.databinding.FragmentFarmActivityBinding
import kr.co.lion.farming_customer.fragment.FarmingLifeBottomSheetFragment

class FarmActivityFragment : Fragment() {
    lateinit var fragmentFarmActivityBinding: FragmentFarmActivityBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentFarmActivityBinding = FragmentFarmActivityBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        settingToolbar()
        return fragmentFarmActivityBinding.root
    }

    // 툴바
    fun settingToolbar() {
        fragmentFarmActivityBinding.apply {
            toolbarFarmActivity.apply {
                imageViewFarmActivityBottom.setOnClickListener {
                    val farmingLifeBottomSheetFragment = FarmingLifeBottomSheetFragment(MainFragmentName.FARM_ACTIVITY_FRAGMENT)
                    farmingLifeBottomSheetFragment.show(mainActivity.supportFragmentManager, "FarmingLifeBottomSheet")
                }
            }
        }
    }
}