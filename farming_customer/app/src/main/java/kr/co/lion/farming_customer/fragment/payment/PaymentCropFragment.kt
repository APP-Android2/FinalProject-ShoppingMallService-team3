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
import kr.co.lion.farming_customer.databinding.FragmentPaymentCropBinding
import kr.co.lion.farming_customer.databinding.RowPaymentProductBinding

class PaymentCropFragment : Fragment() {

    lateinit var fragmentPaymentCropBinding: FragmentPaymentCropBinding
    lateinit var paymentActivity: PaymentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentCropBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_crop,container,false)
        paymentActivity = activity as PaymentActivity

        settingRecyclerViewPaymentFarmActivity()

        return fragmentPaymentCropBinding.root
    }


    // 농산품 결제상품 recyclerView 설정
    fun settingRecyclerViewPaymentFarmActivity(){
        fragmentPaymentCropBinding.apply {
            recyclerViewPaymentProductCrop.apply {
                adapter = PaymentCropRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(paymentActivity)
            }
        }
    }

    // 농산품 결제상품 RecyclerView Adapter
    inner class PaymentCropRecyclerViewAdapter: RecyclerView.Adapter<PaymentCropRecyclerViewAdapter.PaymentCropViewHolder>(){
        inner class PaymentCropViewHolder(rowPaymentProductBinding: RowPaymentProductBinding): RecyclerView.ViewHolder(rowPaymentProductBinding.root){
            val rowPaymentProductBinding: RowPaymentProductBinding

            init {
                this.rowPaymentProductBinding = rowPaymentProductBinding
                this.rowPaymentProductBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentCropViewHolder {
            val rowPaymentProductBinding = RowPaymentProductBinding.inflate(layoutInflater)
            val paymentCropViewHolder = PaymentCropViewHolder(rowPaymentProductBinding)
            return paymentCropViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: PaymentCropViewHolder, position: Int) {
            holder.rowPaymentProductBinding.textViewPaymentProductName.text = "상품입니다 $position"
            holder.rowPaymentProductBinding.textViewPaymentProductOption.text = "옵션입니다아아 $position"
            holder.rowPaymentProductBinding.textViewPaymentProductCnt.text = "${position+1}개"
            holder.rowPaymentProductBinding.textViewPaymentProductPrice.text = "${position+1}0,000원"
        }
    }
}