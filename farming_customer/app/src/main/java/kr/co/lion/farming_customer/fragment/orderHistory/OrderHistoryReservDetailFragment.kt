package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryReservDetailBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryReservDetailViewModel

class OrderHistoryReservDetailFragment : Fragment() {
    lateinit var fragmentOrderHistoryReservDetailBinding: FragmentOrderHistoryReservDetailBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryReservDetailViewModel: OrderHistoryReservDetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryReservDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_reserv_detail, container, false)
        orderHistoryReservDetailViewModel = OrderHistoryReservDetailViewModel()
        fragmentOrderHistoryReservDetailBinding.orderHistoryReservDetailViewModel = orderHistoryReservDetailViewModel
        fragmentOrderHistoryReservDetailBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingToolbar()
        settingEvent()
        settingData()

        return fragmentOrderHistoryReservDetailBinding.root
    }

    private fun settingData() {
        fragmentOrderHistoryReservDetailBinding.apply {
            orderHistoryReservDetailViewModel!!.apply {
                textviewReservDetail_orderDate.value = "2024.04.04"
                textviewReservDetail_orderNum.value = "주문번호 : 12341234"

                textViewReservDetail_productName.value = "파밍이네 주말농장"
                textViewReservDetail_option.value = "이용기간\n2024.03.03 - 2024.04.27"
                textViewReservDetail_price.value = "10,000원 / 1구획"

                textViewReservDetail_reservName.value = "김파밍"
                textViewReservDetail_phoneNum.value = "010-1111-1111"

                textViewReservDetail_productPrice.value = "10,000원"
                textViewReservDetail_productNumber.value = "1개"
                textViewReservDetail_discountPrice.value = "-0원"
                textViewReservDetail_totalPrice.value = "10,000원"
            }
        }
    }

    private fun settingEvent() {
        fragmentOrderHistoryReservDetailBinding.apply {
            buttonReservDetailCheck.setOnClickListener {
                orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_DETAIL_FRAGMENT)
            }
        }
    }

    private fun settingToolbar() {
        fragmentOrderHistoryReservDetailBinding.apply {
            toolbarReservDetail.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_DETAIL_FRAGMENT)
                }
            }

        }
    }
}