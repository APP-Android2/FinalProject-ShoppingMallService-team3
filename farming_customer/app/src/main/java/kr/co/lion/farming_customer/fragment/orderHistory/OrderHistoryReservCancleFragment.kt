package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryReservCancleBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryOrderReservCancelViewModel

class OrderHistoryReservCancleFragment : Fragment() {
    lateinit var fragmentOrderHistoryReservCancleBinding: FragmentOrderHistoryReservCancleBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryOrderReservCancelViewModel: OrderHistoryOrderReservCancelViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryReservCancleBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_reserv_cancle, container, false)
        orderHistoryOrderReservCancelViewModel = OrderHistoryOrderReservCancelViewModel()
        fragmentOrderHistoryReservCancleBinding.orderHistoryOrderReservCancelViewModel = orderHistoryOrderReservCancelViewModel
        fragmentOrderHistoryReservCancleBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingToolbar()
        settingData()


        return fragmentOrderHistoryReservCancleBinding.root
    }

    private fun settingData() {
        fragmentOrderHistoryReservCancleBinding.apply {
            // 드롭다운 설정
            val reseaonArray = resources.getStringArray(R.array.reserv_cancel_reason)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_reserv_cancle, reseaonArray)
            textViewReservCancelReason.setAdapter(arrayAdapter)
            // 주문 데이터 설정
            orderHistoryOrderReservCancelViewModel!!.apply {
                textViewReservCancel_productName.value = "파밍이네 주말농장"
                textViewReservCancle_option.value = "이용기간\n2024.03.01 ~ 2024.11.30"
                textViewReservCancle_price.value = "10,000원 / 1구획"
            }
        }
    }

    private fun settingToolbar() {
        fragmentOrderHistoryReservCancleBinding.apply {
            toolbarOrderHistoryReservCancle.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_CANCLE_FRAGMENT)
                }
            }
        }
    }
}