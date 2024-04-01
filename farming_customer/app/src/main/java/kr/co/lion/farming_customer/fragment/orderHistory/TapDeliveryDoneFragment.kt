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
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentTapDeliveryDoneBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropLabeledBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryCropViewModel

class TapDeliveryDoneFragment : Fragment() {
    lateinit var fragmentTapDeliveryDoneBinding: FragmentTapDeliveryDoneBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapDeliveryDoneBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_delivery_done, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingRecyclerView()

        return fragmentTapDeliveryDoneBinding.root
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
            return 10
        }

        override fun onBindViewHolder(holder: DeliveryDoneViewHolder, position: Int) {
            holder.rowOrderHistoryCropLabeledBinding.apply {
                rowOrderHistoryCropViewModel!!.apply {
                    textViewRowOrderHistoryCrop_orderDate.value = "2024-03-30"
                    textViewRowOrderHistoryCrop_productName.value = "김파밍"
                    textViewRowOrderHistoryCrop_productOption.value = "못난이 감자 5kg ..외 3개"
                    textViewRowOrderHistoryCrop_price.value = "10,000원"
                    buttonRowOrderHistoryCrop_productInside.value = "환불 / 교환"
                    buttonRowOrderHistoryCrop_productOutside.value = "리뷰 작성 [D-3]"
                    buttonRowOrderHistoryCrop_label.value = "취소"
                }
            }
        }
    }
}