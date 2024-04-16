package kr.co.lion.farming_customer.fragment.tradeCrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.tradeCrop.TradeDetailActivity
import kr.co.lion.farming_customer.databinding.FragmentBottomSheetTradeCropBinding
import kr.co.lion.farming_customer.viewmodel.tradeCrop.BottomSheetTradeCropViewModel

class BottomSheetTradeCrop : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetTradeCropBinding
    private lateinit var tradeDetailActivity: TradeDetailActivity
    private lateinit var bottomSheetTradeCropViewModel: BottomSheetTradeCropViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bottom_sheet_trade_crop, container, false)
        bottomSheetTradeCropViewModel = BottomSheetTradeCropViewModel()
        binding.bottomSheetTradeCropViewModel = bottomSheetTradeCropViewModel
        binding.lifecycleOwner = this
        tradeDetailActivity = activity as TradeDetailActivity

        setButton()
        settingDropDown()

        return binding.root
    }


    // 드롭다운 설정
    private fun settingDropDown() {
        binding.apply {
            // 드롭다운 설정
            val typeList = listOf("감자 10kg")
            val typeArrayAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_crop_option, typeList)
            textViewBottomTradeOptionName.setAdapter(typeArrayAdapter)
        }
    }
    private fun setButton(){
        binding.apply {

            // + 버튼
            toggleButtonPlus.setOnClickListener {
                bottomSheetTradeCropViewModel?.plusOptionCount()
            }

            // = 버튼
            toggleButtonMinus.setOnClickListener {
                bottomSheetTradeCropViewModel?.minusOptionCount()
            }

            // 장바구니 버튼
            buttonPaymentCropCart.setOnClickListener {
                //TODO ("장바구니 연결")
            }

            // 구매하기 버튼
            buttonPaymentCropReserv.setOnClickListener {
                //TODO 구매("창 띄우기")
            }
        }
    }

}