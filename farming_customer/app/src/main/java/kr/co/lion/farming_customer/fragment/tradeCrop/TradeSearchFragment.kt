package kr.co.lion.farming_customer.fragment.tradeCrop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeSearchBinding
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeSearchViewModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeViewModel

class TradeSearchFragment : Fragment() {

    lateinit var fragmentTradeSearchBinding: FragmentTradeSearchBinding
    lateinit var mainActivity: MainActivity

    lateinit var tradeSearchViewModel: TradeSearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        fragmentTradeSearchBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trade_search, container, false)
        tradeSearchViewModel = TradeSearchViewModel()
        fragmentTradeSearchBinding.tradeSearchViewModel = tradeSearchViewModel
        fragmentTradeSearchBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        return inflater.inflate(R.layout.fragment_trade_search, container, false)
    }


}