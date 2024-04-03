package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentTapDeliveryBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryCropViewModel

class TapDeliveryFragment : Fragment() {
    lateinit var fragmentTapDeliveryBinding: FragmentTapDeliveryBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapDeliveryBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_delivery, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingRecyclerView()

        return fragmentTapDeliveryBinding.root
    }

    private fun settingRecyclerView() {
        fragmentTapDeliveryBinding.apply {
            recyclerViewDelivery.apply {
                adapter = DeliveryRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(orderHistoryActivity)
            }
        }
    }

    inner class DeliveryRecyclerViewAdapter : RecyclerView.Adapter<DeliveryRecyclerViewAdapter.DeliveryViewHolder>(){
        inner class DeliveryViewHolder(rowOrderHistoryCropBinding : RowOrderHistoryCropBinding) : RecyclerView.ViewHolder(rowOrderHistoryCropBinding.root) {
            val rowOrderHistoryCropBinding: RowOrderHistoryCropBinding

            init {
                this.rowOrderHistoryCropBinding = rowOrderHistoryCropBinding

                rowOrderHistoryCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
            val rowOrderHistoryCropBinding = DataBindingUtil.inflate<RowOrderHistoryCropBinding>(layoutInflater, R.layout.row_order_history_crop, parent, false)
            val rowOrderHistoryCropViewModel = RowOrderHistoryCropViewModel()
            rowOrderHistoryCropBinding.rowOrderHistoryCropViewModel = rowOrderHistoryCropViewModel
            rowOrderHistoryCropBinding.lifecycleOwner = this@TapDeliveryFragment

            val deliveryViewHolder = DeliveryViewHolder(rowOrderHistoryCropBinding)
            return deliveryViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
            holder.rowOrderHistoryCropBinding.apply {
                rowOrderHistoryCropViewModel!!.apply {
                    textViewRowOrderHistoryCrop_orderDate.value = "2024-03-30"
                    textViewRowOrderHistoryCrop_productName.value = "김파밍"
                    textViewRowOrderHistoryCrop_productOption.value = "못난이 감자 5kg ..외 3개"
                    textViewRowOrderHistoryCrop_price.value = "10,000원"
                    buttonRowOrderHistoryCrop_productInside.value = "수취확인"
                }
            }
            // 아이템 클릭 리스터
            holder.rowOrderHistoryCropBinding.root.setOnClickListener {
                orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_ORDER_DETAIL_FRAGMENT, true, true, null)
            }
        }
    }
}