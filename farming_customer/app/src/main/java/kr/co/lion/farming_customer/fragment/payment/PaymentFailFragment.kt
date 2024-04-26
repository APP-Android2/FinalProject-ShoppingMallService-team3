package kr.co.lion.farming_customer.fragment.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.payment.PaymentActivity
import kr.co.lion.farming_customer.databinding.FragmentPaymentFailBinding

class PaymentFailFragment : Fragment() {

    lateinit var fragmentPaymentFailBinding: FragmentPaymentFailBinding
    lateinit var paymentActivity: PaymentActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentFailBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_fail,container,false)
        paymentActivity = activity as PaymentActivity

        settingHomeButton()

        return fragmentPaymentFailBinding.root
    }

    // Home Button 설정
    fun settingHomeButton(){
        fragmentPaymentFailBinding.apply {
            buttonPaymentFailGoHome.setOnClickListener {
                paymentActivity.finish()
            }
        }
    }
}