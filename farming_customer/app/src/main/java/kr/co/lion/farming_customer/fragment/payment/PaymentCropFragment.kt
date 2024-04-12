package kr.co.lion.farming_customer.fragment.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.PaymentFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.payment.PaymentActivity
import kr.co.lion.farming_customer.databinding.FragmentPaymentCropBinding
import kr.co.lion.farming_customer.databinding.RowPaymentProductBinding
import kr.co.lion.farming_customer.viewmodel.payment.PaymentCropViewModel

class PaymentCropFragment : Fragment() {

    lateinit var fragmentPaymentCropBinding: FragmentPaymentCropBinding
    lateinit var paymentActivity: PaymentActivity
    lateinit var paymentCropVIewModel:PaymentCropViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentCropBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_crop,container,false)
        paymentCropVIewModel = PaymentCropViewModel()
        fragmentPaymentCropBinding.paymentCropViewModel = paymentCropVIewModel
        fragmentPaymentCropBinding.lifecycleOwner = this
        paymentActivity = activity as PaymentActivity

        settingRecyclerViewPaymentFarmActivity()
        settingCheckBoxAllAgree()
        settingButtonPayDone()
        settingDetailButton()
        settingButtonDelivery()

        return fragmentPaymentCropBinding.root
    }

    // 동의 버튼 초기화
    fun settingCheckBoxAllAgree(){
        fragmentPaymentCropBinding.apply {
            // 결제버튼 비활성화
            paymentCropVIewModel?.buttonPaymentCropPayDone?.value = false

            // 체크박스 초기화
            paymentCropVIewModel?.checkBoxPaymentCropPrivacy1?.value = false
            paymentCropVIewModel?.checkBoxPaymentCropPrivacy2?.value = false

        }
    }

    // 결제버튼을 눌렀을 때
    fun settingButtonPayDone(){
        fragmentPaymentCropBinding.apply {
            buttonPaymentCropPayDone.setOnClickListener {
                paymentActivity.replaceFragment(PaymentFragmentName.PAYMENT_FAIL_FRAGMENT,false,true,null)
            }

        }
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

    // 필수 동의 내용 버튼
    fun settingDetailButton(){
        fragmentPaymentCropBinding.apply {
            textViewPaymentCropPrivacyDetail1.setOnClickListener {
                if(textViewPaymentCropPrivacyDetailContent.visibility == View.GONE){
                    textViewPaymentCropPrivacyDetailContent.visibility = View.VISIBLE
                }else{
                    textViewPaymentCropPrivacyDetailContent.visibility = View.GONE
                }
            }
            textViewPaymentCropPrivacyDetail2.setOnClickListener {
                if(textViewPaymentCropPrivacyDetailContent2.visibility == View.GONE){
                    textViewPaymentCropPrivacyDetailContent2.visibility = View.VISIBLE
                }else{
                    textViewPaymentCropPrivacyDetailContent2.visibility = View.GONE
                }
            }
        }
    }

    // 배송지 선택 버튼
    fun settingButtonDelivery(){
        fragmentPaymentCropBinding.apply {
            imageButtonPaymentDeliveryDetail.setOnClickListener {
                paymentActivity.replaceFragment(PaymentFragmentName.PAYMENT_DELIVERY_ADDRESS_FRAGMENT,true,true,null)
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