package kr.co.lion.farming_customer.fragment.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import kr.co.lion.farming_customer.CartFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var fragmentCartBinding: FragmentCartBinding
    lateinit var cartActivity: CartActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        fragmentCartBinding = FragmentCartBinding.inflate(inflater)
        cartActivity = activity as CartActivity

        settingToolbar()
        settingTabLayoutCart()

        return fragmentCartBinding.root
    }

    // 툴바설정
    fun settingToolbar() {
        fragmentCartBinding.apply {
            toolbarCart.apply {
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    cartActivity.finish()
                }
            }
        }
    }

    // 탭 설정
    private fun settingTabLayoutCart() {
        fragmentCartBinding.apply {
            tabLayoutCart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        // 농산물 탭
                        0 -> {
                            val cropIdx = arguments?.getInt("cropIdx")!!
                            val optionName = arguments?.getString("optionName")
                            val optionCnt = arguments?.getInt("optionCnt")!!

                            val bundle = Bundle()
                            bundle.apply {
                                putInt("cropIdx", cropIdx)
                                putString("optionName", optionName)
                                putInt("optionCnt", optionCnt)
                            }

                            cartActivity.replaceFragment(CartFragmentName.CART_TAB_CROP_FRAGMENT,
                                addToBackStack = false,
                                isAnimate = false,
                                data = bundle,
                                container = R.id.containerCartTab
                            )
                        }
                        // 주말농장 탭
                        1 -> {
                            cartActivity.replaceFragment(CartFragmentName.CART_TAB_FARM_FRAGMENT,
                                addToBackStack = false,
                                isAnimate = false,
                                data = null,
                                container = R.id.containerCartTab
                            )
                        }
                        // 체험활동 탭
                        2 -> {
                            cartActivity.replaceFragment(CartFragmentName.CART_TAB_ACTIVITY_FRAGMENT,
                                addToBackStack = false,
                                isAnimate = false,
                                data = null,
                                container = R.id.containerCartTab
                            )
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
        }
    }
}