package kr.co.lion.farming_customer.fragment.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.payment.PaymentActivity
import kr.co.lion.farming_customer.databinding.FragmentPaymentSuccessBinding
import kr.co.lion.farming_customer.databinding.RowPaymentProductBinding

class PaymentSuccessFragment : Fragment() {

    lateinit var fragmentPaymentSuccessBinding: FragmentPaymentSuccessBinding
    lateinit var paymentActivity: PaymentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentSuccessBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_success,container,false)
        paymentActivity = activity as PaymentActivity

        settingRecyclerViewPaymentFarmActivity()
        settingHomeButton()

        return fragmentPaymentSuccessBinding.root
    }

    // HOME 버튼 설정
    fun settingHomeButton(){
        fragmentPaymentSuccessBinding.apply {
            buttonPaymentSuccessGoHome.setOnClickListener {
                paymentActivity.finish()
            }
        }
    }

    // 결제 성공 상품 recyclerView 설정
    fun settingRecyclerViewPaymentFarmActivity(){
        fragmentPaymentSuccessBinding.apply {
            recyclerViewPaymentSuccessProduct.apply {
                adapter = PaymentSuccessRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(paymentActivity)
            }
        }
    }

    // 결제 성공 상품 RecyclerView Adapter
    inner class PaymentSuccessRecyclerViewAdapter: RecyclerView.Adapter<PaymentSuccessRecyclerViewAdapter.PaymentSuccessViewHolder>(){
        inner class PaymentSuccessViewHolder(rowPaymentProductBinding: RowPaymentProductBinding): RecyclerView.ViewHolder(rowPaymentProductBinding.root){
            val rowPaymentProductBinding: RowPaymentProductBinding

            init {
                this.rowPaymentProductBinding = rowPaymentProductBinding
                this.rowPaymentProductBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentSuccessViewHolder {
            val rowPaymentProductBinding = RowPaymentProductBinding.inflate(layoutInflater)
            val paymentSuccessViewHolder = PaymentSuccessViewHolder(rowPaymentProductBinding)
            return paymentSuccessViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: PaymentSuccessViewHolder, position: Int) {
            holder.rowPaymentProductBinding.textViewPaymentProductName.text = "상품입니다 $position"
            holder.rowPaymentProductBinding.textViewPaymentProductOption.text = "옵션입니다아아 $position"
            holder.rowPaymentProductBinding.textViewPaymentProductCnt.text = "${position+1}개"
            holder.rowPaymentProductBinding.textViewPaymentProductPrice.text = "${position+1}0,000원"
        }
    }
}