package kr.co.lion.farming_customer.fragment.tradeCrop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.lion.farming_customer.R

class BottomSheetTradeCrop : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_bottom_sheet_trade_crop, container, false)
    }
}