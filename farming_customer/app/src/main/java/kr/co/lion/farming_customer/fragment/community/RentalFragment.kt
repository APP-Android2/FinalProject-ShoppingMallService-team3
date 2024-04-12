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
import kr.co.lion.farming_customer.databinding.FragmentFarmActivityBinding
import kr.co.lion.farming_customer.databinding.FragmentRentalBinding
import kr.co.lion.farming_customer.fragment.FarmingLifeBottomSheetFragment

class RentalFragment : Fragment() {
    lateinit var fragmentRentalBinding: FragmentRentalBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentRentalBinding = FragmentRentalBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        settingToolbar()
        return fragmentRentalBinding.root
    }

    // 툴바
    fun settingToolbar() {
        fragmentRentalBinding.apply {
            toolbarRental.apply {
                imageViewRentalBottom.setOnClickListener {
                    val farmingLifeBottomSheetFragment = FarmingLifeBottomSheetFragment(MainFragmentName.RENTAL_FRAGMENT)
                    farmingLifeBottomSheetFragment.show(mainActivity.supportFragmentManager, "FarmingLifeBottomSheet")
                }
            }
        }
    }

}