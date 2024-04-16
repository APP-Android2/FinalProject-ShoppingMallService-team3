package kr.co.lion.farming_customer.fragment.tradeCrop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeTabDetailBinding
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeTabDetailViewModel

class TradeTabDetailFragment : Fragment() {

    lateinit var binding : FragmentTradeTabDetailBinding
    lateinit var mainActivity: MainActivity

    lateinit var tradeTabDetailViewModel: TradeTabDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trade_tab_detail, container, false)
        tradeTabDetailViewModel = TradeTabDetailViewModel()
        binding.tradeTabDetailViewModel = tradeTabDetailViewModel
        binding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        return binding.root
    }

}