package kr.co.lion.farming_customer.fragment.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.PaymentFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.payment.PaymentActivity
import kr.co.lion.farming_customer.databinding.FragmentPaymentDeliveryAddressBinding
import kr.co.lion.farming_customer.databinding.RowPaymentDeliveryAddressBinding
import kr.co.lion.farming_customer.databinding.RowPaymentProductBinding

class PaymentDeliveryAddressFragment : Fragment() {

    lateinit var fragmentPaymentDeliveryAddressBinding: FragmentPaymentDeliveryAddressBinding
    lateinit var paymentActivity: PaymentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentDeliveryAddressBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_delivery_address,container,false)
        paymentActivity = activity as PaymentActivity

        settingPaymentDeliveryAddressRecyclerView()

        return fragmentPaymentDeliveryAddressBinding.root
    }

    // 농산품 배송지 선택 RecyclerView 설정
    fun settingPaymentDeliveryAddressRecyclerView(){
        fragmentPaymentDeliveryAddressBinding.apply {
            recyclerViewPaymentDeliveryAddress.apply {
                adapter = PaymentDeliveryAddressRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(paymentActivity)
                val deco = MaterialDividerItemDecoration(paymentActivity,MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 농산품 배송지 선택 RecyclerView Adapter
    inner class PaymentDeliveryAddressRecyclerViewAdapter: RecyclerView.Adapter<PaymentDeliveryAddressRecyclerViewAdapter.PaymentDeliveryAddressViewHolder>(){
        inner class PaymentDeliveryAddressViewHolder(rowPaymentDeliveryAddressBinding: RowPaymentDeliveryAddressBinding): RecyclerView.ViewHolder(rowPaymentDeliveryAddressBinding.root){
            val rowPaymentDeliveryAddressBinding: RowPaymentDeliveryAddressBinding

            init {
                this.rowPaymentDeliveryAddressBinding = rowPaymentDeliveryAddressBinding
                this.rowPaymentDeliveryAddressBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentDeliveryAddressViewHolder {
            val rowPaymentDeliveryAddressBinding = RowPaymentDeliveryAddressBinding.inflate(layoutInflater)
            val paymentDeliveryAddressViewHolder = PaymentDeliveryAddressViewHolder(rowPaymentDeliveryAddressBinding)
            return paymentDeliveryAddressViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: PaymentDeliveryAddressViewHolder, position: Int) {
            holder.rowPaymentDeliveryAddressBinding.textViewPaymentDeliveryAddressName.text = "홍길동 $position"
            holder.rowPaymentDeliveryAddressBinding.textViewPaymentDeliveryAddressPhoneNumber.text = "010-1111-111$position"
            holder.rowPaymentDeliveryAddressBinding.textViewPaymentDeliveryAddressDetail.text = "00구 00면 00동 $position"
            if(position==0){
                holder.rowPaymentDeliveryAddressBinding.buttonPaymentDeliveryAddressDefault.isVisible = true
            }else{
                holder.rowPaymentDeliveryAddressBinding.buttonPaymentDeliveryAddressDefault.isVisible = false
            }
            holder.rowPaymentDeliveryAddressBinding.root.setOnClickListener {
                paymentActivity.removeFragment(PaymentFragmentName.PAYMENT_DELIVERY_ADDRESS_FRAGMENT )
            }
        }
    }
}