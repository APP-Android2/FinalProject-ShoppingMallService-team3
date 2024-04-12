package kr.co.lion.farming_customer.fragment.famingLifeTools

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.MaterialSharedAxis
import kr.co.lion.farming_customer.FarmingLifeToolsFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeToolsBinding

class FarmingLifeToolsFragment : Fragment() {
    lateinit var binding: FragmentFarmingLifeToolsBinding

    // 프래그먼트 객체를 담을 변수
    var oldFragment: Fragment? = null
    var newFragment: Fragment? = null

    // 맵 프래그먼트의 주소값을 담을 변수
    var farmingLifeToolsMapFragment: FarmingLifeToolsMapFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFarmingLifeToolsBinding.inflate(layoutInflater)

        settingFarmingLifeToolbar()
        replaceFragment(FarmingLifeToolsFragmentName.FARMING_LIFE_TOOLS_MAP_FRAGMENT, false, false, null)

        return binding.root
    }

    private fun settingFarmingLifeToolbar() {
        binding.apply {
            farmingLifeToolsToolbar.apply {
                // 메뉴
                inflateMenu(R.menu.menu_farming_life_tools)
                menu.findItem(R.id.menu_item_tools_map).isVisible = false

                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.menu_item_tools_list -> {
                            replaceFragment(FarmingLifeToolsFragmentName.FARMING_LIFE_TOOLS_LIST_FRAGMENT, false, false, null)
                            it.isVisible = false
                            farmingLifeToolsToolbar.menu.findItem(R.id.menu_item_tools_map).isVisible = true
                            farmingLifeToolsToolbar.menu.findItem(R.id.menu_item_tools_map_location).isVisible = false

                        }
                        R.id.menu_item_tools_map -> {
                            replaceFragment(FarmingLifeToolsFragmentName.FARMING_LIFE_TOOLS_MAP_FRAGMENT, false, false, null)
                            it.isVisible = false
                            farmingLifeToolsToolbar.menu.findItem(R.id.menu_item_tools_map_location).isVisible = true
                            farmingLifeToolsToolbar.menu.findItem(R.id.menu_item_tools_list).isVisible = true
                        }

                        R.id.menu_item_tools_map_location -> {
                            if (farmingLifeToolsMapFragment != null) {
                                farmingLifeToolsMapFragment?.getMyLocation()
                            }
                        }
                    }
                    true
                }
            }
        }
    }

    fun replaceFragment(name: FarmingLifeToolsFragmentName, addToBackStack:Boolean, isAnimate:Boolean, data:Bundle?){
        SystemClock.sleep(200)
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        if(newFragment != null){
            oldFragment = newFragment
        }

        when(name){
            FarmingLifeToolsFragmentName.FARMING_LIFE_TOOLS_MAP_FRAGMENT -> {
                farmingLifeToolsMapFragment = FarmingLifeToolsMapFragment()
                newFragment = farmingLifeToolsMapFragment
            }
            FarmingLifeToolsFragmentName.FARMING_LIFE_TOOLS_LIST_FRAGMENT -> {
                newFragment = FarmingLifeToolsListFragment()
            }
        }

        if(data != null){
            newFragment?.arguments = data
        }

        if(newFragment != null){
            // 애니메이션 설정
            if(isAnimate){
                if(oldFragment != null){
                    oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                    oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

                    oldFragment?.enterTransition = null
                    oldFragment?.returnTransition = null
                }

                newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

                newFragment?.exitTransition = null
                newFragment?.reenterTransition = null
            }
            fragmentTransaction?.replace(R.id.farming_life_tools_container, newFragment!!)

            if(addToBackStack){
                fragmentTransaction?.addToBackStack(name.str)
            }
            fragmentTransaction?.commit()
        }
    }
}