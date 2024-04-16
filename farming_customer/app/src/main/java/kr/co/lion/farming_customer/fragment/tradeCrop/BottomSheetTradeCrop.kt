package kr.co.lion.farming_customer.fragment.tradeCrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }

    private fun setButton(){
        binding.apply {
            // 농산물 표시 버튼
            imageButtonDropDown.setOnClickListener {
                // visibility 변경
                if(constraintLayoutCrop.visibility == View.GONE) {
                    imageButtonDropDown.setImageResource(R.drawable.polygon_up)
                    constraintLayoutCrop.visibility = View.VISIBLE
                } else {
                    imageButtonDropDown.setImageResource(R.drawable.polygon_down)
                    constraintLayoutCrop.visibility = View.GONE
                }
            }

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