package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.OrderLabelType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentTapPaymentDoneBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropBinding
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryCropViewModel

class TapPaymentDoneFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentTapPaymentDoneBinding: FragmentTapPaymentDoneBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    var orderList : MutableList<OrderModel>? = null

    var orderCancle_pos : Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapPaymentDoneBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_payment_done, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingInitData()


        return fragmentTapPaymentDoneBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            orderList = OrderDao.gettingOrderListCrop(OrderLabelType.ORDER_LABEL_TYPE_PAY_DONE)
            settingRecyclerView()
        }

    }

    private fun settingRecyclerView() {
        fragmentTapPaymentDoneBinding.apply {
            recyclerViewPaymentDone.apply {
                adapter = PaymentDoneRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(orderHistoryActivity)
            }
        }
    }

    inner class PaymentDoneRecyclerViewAdapter : RecyclerView.Adapter<PaymentDoneRecyclerViewAdapter.PaymentDoneViewHolder>() {
        inner class PaymentDoneViewHolder(rowOrderHistoryCropBinding : RowOrderHistoryCropBinding) : RecyclerView.ViewHolder(rowOrderHistoryCropBinding.root){
            val rowOrderHistoryCropBinding : RowOrderHistoryCropBinding

            init {
                this.rowOrderHistoryCropBinding = rowOrderHistoryCropBinding

                rowOrderHistoryCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentDoneViewHolder {
            val rowOrderHistoryCropBinding = DataBindingUtil.inflate<RowOrderHistoryCropBinding>(layoutInflater, R.layout.row_order_history_crop, parent, false)
            val rowOrderHistoryCropViewModel = RowOrderHistoryCropViewModel()
            rowOrderHistoryCropBinding.rowOrderHistoryCropViewModel = rowOrderHistoryCropViewModel
            rowOrderHistoryCropBinding.lifecycleOwner = this@TapPaymentDoneFragment

            val paymentDoneViewHolder = PaymentDoneViewHolder(rowOrderHistoryCropBinding)
            return paymentDoneViewHolder
        }

        override fun getItemCount(): Int {
            return orderList!!.size
        }

        override fun onBindViewHolder(holder: PaymentDoneViewHolder, position: Int) {
            holder.rowOrderHistoryCropBinding.apply {
                rowOrderHistoryCropViewModel!!.apply {
                    textViewRowOrderHistoryCrop_orderDate.value = orderList!![position].order_reg_date
                    textViewRowOrderHistoryCrop_productName.value = "파밍이네 감자" // 상품 데이터 가져와야함
                    textViewRowOrderHistoryCrop_productOption.value = "${orderList!![position].order_option_detail[0]["option_name"]}...외 ${orderList!![position].order_option_detail.size-1}개"
                    textViewRowOrderHistoryCrop_price.value = orderList!![position].order_total_price
                    buttonRowOrderHistoryCrop_productInside.value = "주문취소"
                }
            }
            // 아이템 클릭 리스터
            holder.rowOrderHistoryCropBinding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("orderNum", orderList!![position].order_num)
                orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_ORDER_DETAIL_FRAGMENT, true, true, bundle)
            }
            // 주문 취소
            holder.rowOrderHistoryCropBinding.buttonRowOrderHistoryCropProductInside.setOnClickListener {
                orderCancle_pos = position
                val dialog = DialogYesNo(
                    this@TapPaymentDoneFragment,
                    "주문 취소",
                    "주문 취소 신청을 하시겠습니까?",
                    orderHistoryActivity,
                    yes_text = "신청"
                )
                dialog.show(orderHistoryActivity.supportFragmentManager, "DialogYesNo")
            }
        }
    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity) {
        // 상태 라벨 취소 신청으로 바꾸기
        CoroutineScope(Dispatchers.Main).launch {
            OrderDao.updateOrderState(orderList!![orderCancle_pos].order_idx, OrderLabelType.ORDER_LABEL_TYPE_CANCLE_APPLY)
            orderList!!.removeAt(orderCancle_pos)
            fragmentTapPaymentDoneBinding.recyclerViewPaymentDone.adapter?.notifyItemRemoved(orderCancle_pos)
        }
    }
}