package kr.co.lion.farming_customer.fragment.orderHistory

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.OrderLabelType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentTapDeliveryDoneBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropLabeledBinding
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryCropViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TapDeliveryDoneFragment : Fragment() {
    lateinit var fragmentTapDeliveryDoneBinding: FragmentTapDeliveryDoneBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    var orderList : MutableList<OrderModel>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapDeliveryDoneBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_delivery_done, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingInitData()

        return fragmentTapDeliveryDoneBinding.root
    }

    private fun settingInitData() {
        fragmentTapDeliveryDoneBinding.apply {
            // 전체 버튼을 기본 값으로
            toggleButtonAll.isChecked = true

            CoroutineScope(Dispatchers.Main).launch {
                val orderLabelTypes = listOf(
                    OrderLabelType.ORDER_LABEL_TYPE_DELIVERY_DONE,
                    OrderLabelType.ORDER_LABEL_TYPE_CANCLE_APPLY,
                    OrderLabelType.ORDER_LABEL_TYPE_CANCLE_DONE,
                    OrderLabelType.ORDER_LABEL_TYPE_RETURN_APPLY,
                    OrderLabelType.ORDER_LABEL_TYPE_RETURN_DONE,
                    OrderLabelType.ORDER_LABEL_TYPE_EXCHANGE_APPLY,
                    OrderLabelType.ORDER_LABEL_TYPE_EXCHANGE_DONE
                )
                orderList = OrderDao.gettingOrderListCrop(orderLabelTypes)
                settingRecyclerView()
            }
        }
    }

    private fun settingRecyclerView() {
        fragmentTapDeliveryDoneBinding.apply {
            recyclerViewDeliveryDone.apply {
                adapter = DeliveryDoneRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(orderHistoryActivity)
            }
        }
    }

    inner class DeliveryDoneRecyclerViewAdapter : RecyclerView.Adapter<DeliveryDoneRecyclerViewAdapter.DeliveryDoneViewHolder>() {
        inner class DeliveryDoneViewHolder(rowOrderHistoryCropLabeledBinding : RowOrderHistoryCropLabeledBinding) : RecyclerView.ViewHolder(rowOrderHistoryCropLabeledBinding.root){
            val rowOrderHistoryCropLabeledBinding : RowOrderHistoryCropLabeledBinding

            init {
                this.rowOrderHistoryCropLabeledBinding = rowOrderHistoryCropLabeledBinding

                rowOrderHistoryCropLabeledBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryDoneViewHolder {
            val rowOrderHistoryCropLabeledBinding = DataBindingUtil.inflate<RowOrderHistoryCropLabeledBinding>(layoutInflater, R.layout.row_order_history_crop_labeled, parent, false)
            val rowOrderHistoryCropViewModel = RowOrderHistoryCropViewModel()
            rowOrderHistoryCropLabeledBinding.rowOrderHistoryCropViewModel = rowOrderHistoryCropViewModel
            rowOrderHistoryCropLabeledBinding.lifecycleOwner = this@TapDeliveryDoneFragment

            val deliveryDoneViewHolder = DeliveryDoneViewHolder(rowOrderHistoryCropLabeledBinding)
            return deliveryDoneViewHolder
        }

        override fun getItemCount(): Int {
            return orderList!!.size
        }

        override fun onBindViewHolder(holder: DeliveryDoneViewHolder, position: Int) {
            holder.rowOrderHistoryCropLabeledBinding.apply {
                rowOrderHistoryCropViewModel!!.apply {
                    textViewRowOrderHistoryCrop_orderDate.value = orderList!![position].order_reg_date
                    textViewRowOrderHistoryCrop_productName.value = "파밍이네 감자" // 상품 데이터 가져와야함
                    textViewRowOrderHistoryCrop_productOption.value = "${orderList!![position].order_option_detail[0]["option_name"]}...외 ${orderList!![position].order_option_detail.size-1}개"
                    textViewRowOrderHistoryCrop_price.value = orderList!![position].order_total_price
                    buttonRowOrderHistoryCrop_productInside.value = "환불 / 교환"
                    // 주문한지 3일이 지나면 환불 / 교환 버튼 비활성화, 리뷰 작성 버튼 사라짐
                    val calc_date = calculateDaysUntil(orderList!![position].order_reg_date)
                    if(calc_date > 3){
                        // 주문 3일 이상 지남. 환불 교환 비활성화, 리뷰 작성 버튼 사라짐
                        buttonRowOrderHistoryCropProductInside.isEnabled = false
                        divider2.visibility = View.GONE
                        buttonRowOrderHistoryCropProductOutSide.visibility = View.GONE
                    }else if(calc_date == 0.toLong()){
                        buttonRowOrderHistoryCrop_productOutside.value = "리뷰 작성 가능 [D-DAY]"
                    }else{
                        buttonRowOrderHistoryCrop_productOutside.value = "리뷰 작성 [D-${calc_date}]"
                    }
                    buttonRowOrderHistoryCrop_label.value = OrderLabelType.fromNumber(orderList!![position].order_label)
                }
                // 환불 / 교환
                buttonRowOrderHistoryCropProductInside.setOnClickListener {
                    orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_EXCHANGE_RETURN_FRAGMENT, true, true, null)
                }
                // 리뷰 작성
                buttonRowOrderHistoryCropProductOutSide.setOnClickListener {
                    orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_WRITE_REVIEW_FRAGMENT, true, true, null)
                }
            }
            // 아이템 클릭 리스터
            holder.rowOrderHistoryCropLabeledBinding.root.setOnClickListener {
                orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_ORDER_DETAIL_FRAGMENT, true, true, null)
            }
        }
    }

    // 입력된 문자열을 현재 날짜와 비교해서 차이를 반환한다.
    fun calculateDaysUntil(targetDateStr: String): Long {
        // 입력된 문자열을 LocalDate로 파싱합니다.
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val targetDate = LocalDate.parse(targetDateStr, formatter)

        // 현재 날짜를 가져옵니다.
        val currentDate = LocalDate.now()

        // 날짜 차이를 계산하여 반환합니다.
        return java.time.temporal.ChronoUnit.DAYS.between(currentDate, targetDate)
    }
}