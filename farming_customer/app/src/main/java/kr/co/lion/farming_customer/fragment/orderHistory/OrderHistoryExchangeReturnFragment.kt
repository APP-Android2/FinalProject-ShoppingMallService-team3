package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryExchangeReturnBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryExchangeReturnViewModel

class OrderHistoryExchangeReturnFragment : Fragment() {
    lateinit var fragmentOrderHistoryExchangeReturnBinding: FragmentOrderHistoryExchangeReturnBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryExchangeReturnViewModel: OrderHistoryExchangeReturnViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryExchangeReturnBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_exchange_return, container, false)
        orderHistoryExchangeReturnViewModel = OrderHistoryExchangeReturnViewModel()
        fragmentOrderHistoryExchangeReturnBinding.orderHistoryExchangeReturnViewModel = orderHistoryExchangeReturnViewModel
        fragmentOrderHistoryExchangeReturnBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingToolbar()
        settingData()
        settingEvent()

        return fragmentOrderHistoryExchangeReturnBinding.root
    }

    private fun settingEvent() {
        fragmentOrderHistoryExchangeReturnBinding.apply {
            buttonCancle.setOnClickListener {
                // 신청 처리
                orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_EXCHANGE_RETURN_FRAGMENT)
            }
        }
    }

    private fun settingData() {
        fragmentOrderHistoryExchangeReturnBinding.apply {
            // 드롭다운 설정
            val typeArray = resources.getStringArray(R.array.exchange_return_type)
            val typeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_reserv_cancle, typeArray)
            textViewExchangeReturnType.setAdapter(typeArrayAdapter)

            val reasonArray = resources.getStringArray(R.array.exchange_return_reason)
            val reasonArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_reserv_cancle, reasonArray)
            textViewExchangeReturnReason.setAdapter(reasonArrayAdapter)

            // 주문 데이터 설정
            orderHistoryExchangeReturnViewModel!!.apply {
                textViewExchangeReturn_productName.value = "파밍이네 감자"
                textViewExchangeReturn_option.value = "못난이 감자 5kg ...외 3개"
                textViewExchangeReturn_price.value = "10,000원 1개"
            }
        }


    }

    private fun settingToolbar() {
        fragmentOrderHistoryExchangeReturnBinding.apply {
            toolbarOrderHistoryExchangeReturn.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_EXCHANGE_RETURN_FRAGMENT)
                }
            }
        }
    }

}