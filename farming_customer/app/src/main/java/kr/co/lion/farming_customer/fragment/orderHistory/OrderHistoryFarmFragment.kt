package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryFarmBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryViewModel

class OrderHistoryFarmFragment : Fragment() {
    lateinit var fragmentOrderHistoryFarmBinding: FragmentOrderHistoryFarmBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryViewModel: OrderHistoryViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryFarmBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_farm, container, false)
        orderHistoryViewModel = OrderHistoryViewModel()
        fragmentOrderHistoryFarmBinding.orderHistoryViewModel = orderHistoryViewModel
        fragmentOrderHistoryFarmBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        return fragmentOrderHistoryFarmBinding.root
    }

}