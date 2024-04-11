package kr.co.lion.farming_customer.fragment.tradeCrop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeDetailBinding
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeDetailViewModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeViewModel

class TradeDetailFragment : Fragment() {

    lateinit var fragmentTradeDetailBinding: FragmentTradeDetailBinding
    lateinit var mainActivity: MainActivity

    lateinit var tradeDetailViewModel: TradeDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        fragmentTradeDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trade_detail, container, false)
        tradeDetailViewModel = TradeDetailViewModel()
        fragmentTradeDetailBinding.tradeDetailViewModel = tradeDetailViewModel
        fragmentTradeDetailBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        setToolbar()

        return fragmentTradeDetailBinding.root
    }

    // 툴바 설정
    private fun setToolbar(){
        fragmentTradeDetailBinding.apply {
            toolbarTradeDetail.apply {
                // 제목
                title = "농산물 판매"

                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainFragmentName.TRADE_DETAIL_FRAGMENT)
                }
            }
        }
    }


}