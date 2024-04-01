package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryActivityBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryViewModel

class OrderHistoryActivityFragment : Fragment() {
    lateinit var fragmentOrderHistoryActivityBinding: FragmentOrderHistoryActivityBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryViewModel: OrderHistoryViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryActivityBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_activity, container, false)
        orderHistoryViewModel = OrderHistoryViewModel()
        fragmentOrderHistoryActivityBinding.orderHistoryViewModel = orderHistoryViewModel
        fragmentOrderHistoryActivityBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        return fragmentOrderHistoryActivityBinding.root
    }

}