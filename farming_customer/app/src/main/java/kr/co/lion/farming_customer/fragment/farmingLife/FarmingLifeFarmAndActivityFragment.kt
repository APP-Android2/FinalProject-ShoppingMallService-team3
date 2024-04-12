package kr.co.lion.farming_customer.fragment.farmingLife

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeFarmAndActivityBinding

class FarmingLifeFarmAndActivityFragment : Fragment() {
    lateinit var fragmentFarmingLifeFarmAndActivityBinding: FragmentFarmingLifeFarmAndActivityBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFarmingLifeFarmAndActivityBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_farming_life_farm_and_activity, container, false)
        mainActivity = activity as MainActivity

        settingToolbar()
        settingTabEvent()


        return fragmentFarmingLifeFarmAndActivityBinding.root
    }

    private fun settingTabEvent() {
        fragmentFarmingLifeFarmAndActivityBinding.apply {
            tabFarmAndActivity.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        // 주말농장 프래그먼트
                        0 -> {
                            mainActivity.replaceFragment(MainFragmentName.TAP_FARM_FRAGMENT, false, false, null, R.id.containerFarmingLife)
                        }
                        // 체험활동 프래그먼트
                        1 -> {
                            mainActivity.replaceFragment(MainFragmentName.TAP_ACTIVITY_FRAGMENT, false, false, null, R.id.containerFarmingLife)
                        }
                    }


                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // 탭이 선택되지 않았을 때
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // 탭이 다시 선택되었을 때
                }

            })
            mainActivity.replaceFragment(MainFragmentName.TAP_FARM_FRAGMENT, false, true, null, R.id.containerFarmingLife)
        }
    }

    private fun settingToolbar() {
        fragmentFarmingLifeFarmAndActivityBinding.apply {
            toolbarFarmingLife.apply {
                // 파밍생활 유형 변경
                buttonFarmingLifeChangeMode.setOnClickListener{


                }
                // 검색 버튼
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuItemFarmingLife_search -> {
                            val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                            intent.putExtra("fragmentName", FarmingLifeFragmnetName.FARMING_LIFE_SEARCH_FRAGMENT)
                            startActivity(intent)
                        }
                    }
                    true
                }
            }
        }
    }

}