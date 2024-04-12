package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryActivityBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryViewModel

class OrderHistoryActivityFragment : Fragment() {
    lateinit var fragmentOrderHistoryActivityBinding: FragmentOrderHistoryActivityBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryViewModel: OrderHistoryViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryActivityBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_activity, container, false)
        orderHistoryViewModel = OrderHistoryViewModel()
        fragmentOrderHistoryActivityBinding.orderHistoryViewModel = orderHistoryViewModel
        fragmentOrderHistoryActivityBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingToolbar()
        settingTapEvent()
        orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.TAP_RESERV_DONE_FRAGMENT, false, true, null, R.id.containerOrderHistoryActivity)

        return fragmentOrderHistoryActivityBinding.root
    }

    private fun settingToolbar() {
        fragmentOrderHistoryActivityBinding.apply {
            toolbarOrderHistoryActivity.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.finish()
                }
            }
        }
    }

    private fun settingTapEvent() {
        fragmentOrderHistoryActivityBinding.apply {
            tabActivity.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        // 예약 완료 프래그먼트
                        0 -> {
                            orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.TAP_RESERV_DONE_FRAGMENT, false, true, null, R.id.containerOrderHistoryActivity)
                        }
                        // 예약 취소 프래그먼트
                        1 -> {
                            orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.TAP_RESERV_CANCLE_FRAGMENT, false, true, null, R.id.containerOrderHistoryActivity)
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