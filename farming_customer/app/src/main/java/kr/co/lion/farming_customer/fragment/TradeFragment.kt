package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeBinding
import kr.co.lion.farming_customer.viewmodel.TradeViewModel

class TradeFragment : Fragment() {
    lateinit var fragmentTradeBinding: FragmentTradeBinding
    lateinit var mainActivity: MainActivity

    lateinit var tradeViewModel: TradeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentTradeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trade, container, false)
        tradeViewModel = TradeViewModel()
        fragmentTradeBinding.tradeViewModel = tradeViewModel
        fragmentTradeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        return fragmentTradeBinding.root
    }
}