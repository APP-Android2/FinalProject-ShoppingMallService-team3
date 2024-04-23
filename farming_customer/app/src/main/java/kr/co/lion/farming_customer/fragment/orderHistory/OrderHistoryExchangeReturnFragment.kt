package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYes
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryExchangeReturnBinding
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryExchangeReturnViewModel

class OrderHistoryExchangeReturnFragment : Fragment() {
    lateinit var fragmentOrderHistoryExchangeReturnBinding: FragmentOrderHistoryExchangeReturnBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryExchangeReturnViewModel: OrderHistoryExchangeReturnViewModel

    var orderItem : OrderModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryExchangeReturnBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_exchange_return, container, false)
        orderHistoryExchangeReturnViewModel = OrderHistoryExchangeReturnViewModel()
        fragmentOrderHistoryExchangeReturnBinding.orderHistoryExchangeReturnViewModel = orderHistoryExchangeReturnViewModel
        fragmentOrderHistoryExchangeReturnBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingInitData()
        settingToolbar()
        settingEvent()

        return fragmentOrderHistoryExchangeReturnBinding.root
    }

    private fun settingInitData() {
        orderItem = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("orderItem", OrderModel::class.java)
        }else{
            arguments?.getParcelable<OrderModel>("orderItem")
        }
        settingData()
    }

    private fun settingEvent() {
        fragmentOrderHistoryExchangeReturnBinding.apply {
            buttonCancle.setOnClickListener {
                // 신청 처리
                // 유형 및 신청 사유 검사
                val type = orderHistoryExchangeReturnViewModel!!.textViewExchangeReturn_type.value
                val reason = orderHistoryExchangeReturnViewModel!!.textViewExchangeReturn_reason.value
                val reason_detail = orderHistoryExchangeReturnViewModel!!.textViewExchangeReturn_reasonDetail.value

                if(type.isNullOrBlank()){
                    // 유형 선택을 하지 않았을 때
                    val dialog = DialogYes("입력 오류", "예약 취소 유형을 선택해주세요.", null, orderHistoryActivity)
                    dialog.show(this@OrderHistoryExchangeReturnFragment.parentFragmentManager, "DialogYes")
                }else if(reason.isNullOrBlank()){
                    // 신청 사유 선택을 하지 않았을 때
                    val dialog = DialogYes("입력 오류", "신청사유를 선택해주세요.", null, orderHistoryActivity)
                    dialog.show(this@OrderHistoryExchangeReturnFragment.parentFragmentManager, "DialogYes")
                }else{
                    CoroutineScope(Dispatchers.Main).launch {
                        val map = mutableMapOf<String, Any>()
                        map["cancle_type"] = type
                        map["cancle_reason"] = reason
                        if(reason_detail != null){
                            map["cancle_reason_detail"] = reason_detail
                        }
                        OrderDao.applyExchangeReturn(orderItem!!.order_idx, map)
                        orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_EXCHANGE_RETURN_FRAGMENT)
                    }
                }
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
                textViewExchangeReturn_productName.value = "파밍이네 감자" // 상품 데이터 가져와야함
                textViewExchangeReturn_option.value = "${orderItem!!.order_option_detail[0]["option_name"]}...외 ${orderItem!!.order_option_detail.size-1}개"
                textViewExchangeReturn_price.value = orderItem!!.order_total_price
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