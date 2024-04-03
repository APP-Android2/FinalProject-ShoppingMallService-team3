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
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryCropBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryViewModel

class OrderHistoryCropFragment : Fragment() {
    lateinit var fragmentOrderHistoryCropBinding: FragmentOrderHistoryCropBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryViewModel: OrderHistoryViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryCropBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_crop, container, false)
        orderHistoryViewModel = OrderHistoryViewModel()
        fragmentOrderHistoryCropBinding.orderHistoryViewModel = orderHistoryViewModel
        fragmentOrderHistoryCropBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingToolbar()
        settingTapEvent()
        orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.TAP_PAYMENT_DONE_FRAGMENT, false, true, null, R.id.containerOrderHistoryCrop)

        return fragmentOrderHistoryCropBinding.root
    }

    private fun settingToolbar() {
        fragmentOrderHistoryCropBinding.apply {
            toolbarOrderHistoryCrop.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_CROP_FRAGMENT)
                }
            }
        }
    }

    private fun settingTapEvent() {
        fragmentOrderHistoryCropBinding.apply {
            tabCrop.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        // 결제완료 프래그먼트
                        0 -> {
                            orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.TAP_PAYMENT_DONE_FRAGMENT, false, true, null, R.id.containerOrderHistoryCrop)
                        }
                        // 배송중 프래그먼트
                        1 -> {
                            orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.TAP_DELIVERY_FRAGMENT, false, true, null, R.id.containerOrderHistoryCrop)
                        }
                        // 배송완료 프래그먼트
                        2 -> {
                            orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.TAP_DELIVERY_DONE_FRAGMENT, false, true, null, R.id.containerOrderHistoryCrop)
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