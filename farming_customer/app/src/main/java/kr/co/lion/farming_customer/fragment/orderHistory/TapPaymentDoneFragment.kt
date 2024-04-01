package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentTapPaymentDoneBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryCropViewModel

class TapPaymentDoneFragment : Fragment() {
    lateinit var fragmentTapPaymentDoneBinding: FragmentTapPaymentDoneBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapPaymentDoneBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_payment_done, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingRecyclerView()

        return fragmentTapPaymentDoneBinding.root
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
            return 10
        }

        override fun onBindViewHolder(holder: PaymentDoneViewHolder, position: Int) {
            holder.rowOrderHistoryCropBinding.apply {
                rowOrderHistoryCropViewModel!!.apply {
                    textViewRowOrderHistoryCrop_orderDate.value = "2024-03-30"
                    textViewRowOrderHistoryCrop_productName.value = "김파밍"
                    textViewRowOrderHistoryCrop_productOption.value = "못난이 감자 5kg ..외 3개"
                    textViewRowOrderHistoryCrop_price.value = "10,000원"
                    buttonRowOrderHistoryCrop_productInside.value = "결제취소"
                }
            }
        }
    }
}