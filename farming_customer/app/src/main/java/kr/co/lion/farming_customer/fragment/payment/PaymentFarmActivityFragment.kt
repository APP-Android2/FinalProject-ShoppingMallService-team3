package kr.co.lion.farming_customer.fragment.payment

import android.os.Bundle
import android.util.Log
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
import kr.co.lion.farming_customer.databinding.FragmentPaymentFarmActivityBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.databinding.RowPaymentProductBinding
import kr.co.lion.farming_customer.viewmodel.payment.PaymentFarmActivityViewModel

class PaymentFarmActivityFragment : Fragment() {

    lateinit var fragmentPaymentFarmActivityBinding: FragmentPaymentFarmActivityBinding
    lateinit var paymentActivity: PaymentActivity
    lateinit var paymentFarmActivityViewModel: PaymentFarmActivityViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentFarmActivityBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_farm_activity,container,false)
        paymentFarmActivityViewModel = PaymentFarmActivityViewModel()
        fragmentPaymentFarmActivityBinding.paymentFarmActivityViewModel = paymentFarmActivityViewModel
        fragmentPaymentFarmActivityBinding.lifecycleOwner = this
        paymentActivity = activity as PaymentActivity

        settingRecyclerViewPaymentFarmActivity()
        settingCheckBoxAllAgree()
        settingButtonPayDone()
        settingDetailButton()

        return fragmentPaymentFarmActivityBinding.root
    }

    // 주말농장 및 체험활동 결제상품 recyclerView 설정
    fun settingRecyclerViewPaymentFarmActivity(){
        fragmentPaymentFarmActivityBinding.apply {
            recyclerViewPaymentProduct.apply {
                adapter = PaymentFarmActivityRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(paymentActivity)
            }
        }
    }

    // 동의 버튼 초기화
    fun settingCheckBoxAllAgree(){
        fragmentPaymentFarmActivityBinding.apply {
            // 결제버튼 비활성화
            paymentFarmActivityViewModel?.buttonPaymentFarmActivityPayDone?.value = false

            // 체크박스 초기화
            paymentFarmActivityViewModel?.checkBoxPaymentFarmActivityPrivacy1?.value = false
            paymentFarmActivityViewModel?.checkBoxPaymentFarmActivityPrivacy2?.value = false

        }
    }

    // 결제버튼을 눌렀을 때
    fun settingButtonPayDone(){
        fragmentPaymentFarmActivityBinding.apply {
            buttonPaymentFarmActivityPayDone.setOnClickListener {
                paymentActivity.replaceFragment(PaymentFragmentName.PAYMENT_SUCCESS_FRAGMENT,false,true,null)
            }

        }
    }

    // 필수 동의 내용 버튼
    fun settingDetailButton(){
        fragmentPaymentFarmActivityBinding.apply {
            textViewPaymentFarmActivityPrivacyDetail1.setOnClickListener {
                if(textViewPaymentFarmActivityPrivacyDetailContent1.visibility == View.GONE){
                    textViewPaymentFarmActivityPrivacyDetailContent1.visibility = View.VISIBLE
                }else{
                    textViewPaymentFarmActivityPrivacyDetailContent1.visibility = View.GONE
                }
            }
            textViewPaymentFarmActivityPrivacyDetail2.setOnClickListener {
                if(textViewPaymentFarmActivityPrivacyDetailContent2.visibility == View.GONE){
                    textViewPaymentFarmActivityPrivacyDetailContent2.visibility = View.VISIBLE
                }else{
                    textViewPaymentFarmActivityPrivacyDetailContent2.visibility = View.GONE
                }
            }
        }
    }

    // 주말농장 및 체험활동 결제상품 RecyclerView Adapter
    inner class PaymentFarmActivityRecyclerViewAdapter: RecyclerView.Adapter<PaymentFarmActivityRecyclerViewAdapter.PaymentFarmActivityViewHolder>(){
        inner class PaymentFarmActivityViewHolder(rowPaymentProductBinding: RowPaymentProductBinding): RecyclerView.ViewHolder(rowPaymentProductBinding.root){
            val rowPaymentProductBinding: RowPaymentProductBinding

            init {
                this.rowPaymentProductBinding = rowPaymentProductBinding
                this.rowPaymentProductBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentFarmActivityViewHolder {
            val rowPaymentProductBinding = RowPaymentProductBinding.inflate(layoutInflater)
            val paymentFarmActivityViewHolder = PaymentFarmActivityViewHolder(rowPaymentProductBinding)
            return paymentFarmActivityViewHolder
        }

        override fun getItemCount(): Int {
            return 15
        }

        override fun onBindViewHolder(holder: PaymentFarmActivityViewHolder, position: Int) {
            holder.rowPaymentProductBinding.textViewPaymentProductName.text = "상품입니다 $position"
            holder.rowPaymentProductBinding.textViewPaymentProductOption.text = "옵션입니다아아 $position"
            holder.rowPaymentProductBinding.textViewPaymentProductCnt.text = "${position+1}개"
            holder.rowPaymentProductBinding.textViewPaymentProductPrice.text = "${position+1}0,000원"
        }
    }
}