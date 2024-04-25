package kr.co.lion.farming_customer.fragment.tradeCrop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.CartFragmentName
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.PaymentFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.activity.payment.PaymentActivity
import kr.co.lion.farming_customer.activity.tradeCrop.TradeDetailActivity
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.databinding.FragmentBottomSheetTradeCropBinding
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.BottomSheetTradeCropViewModel
import java.text.DecimalFormat

class BottomSheetTradeCrop : BottomSheetDialogFragment(), DialogYesNoInterface {

    lateinit var binding: FragmentBottomSheetTradeCropBinding
    private lateinit var tradeDetailActivity: TradeDetailActivity
    private lateinit var bottomSheetTradeCropViewModel: BottomSheetTradeCropViewModel

    // 농산품 데이터를 담을 프로퍼티
    var cropData: CropModel? = null

    // 드롭다운 이름 리스트
    var dropDownNameList = mutableListOf<String>()

    // 드롭다운 가격 리스트
    var dropDownPriceList = mutableListOf<String>()

    // 농산품 번호를 담을 프로퍼티
    var crop_idx = 0

    // 결제화면에 필요한 기본 가격 데이터로 인해 기본 가격 데이터만 담을 프로퍼티를 선언해준다.
    var optionPrice = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bottom_sheet_trade_crop, container, false)
        bottomSheetTradeCropViewModel = BottomSheetTradeCropViewModel()
        binding.bottomSheetTradeCropViewModel = bottomSheetTradeCropViewModel
        binding.lifecycleOwner = this
        tradeDetailActivity = activity as TradeDetailActivity

        gettingCropData()

        return binding.root
    }

    // 농산품 번호에 맞게 데이터를 가져온다.
    fun gettingCropData(){
        crop_idx = arguments?.getInt("crop_idx")!!
        CoroutineScope(Dispatchers.Main).launch {
            cropData = CropDao.selectCropData(crop_idx!!)
            settingDropDown()
            settingTextView()
            setButton()
        }
    }

    // textView 초기 설정
    fun settingTextView(){
        binding.apply {
            bottomSheetTradeCropViewModel!!.apply {
                // 배송비
                fee.value = "${cropData?.delivery_fee}원"
                // 총 가격
                totalPrice.value = "0원"
                // 기본 가격
                selectedCropPrice.value = "0원"

                // 기본가격은 옵션명에 따라 초기 설정을 다르게 해준다.
                textViewBottomTradeOptionName.setOnItemClickListener { parent, view, position, id ->
                    // 기본가격
                    selectedCropPrice.value = dropDownPriceList[position]
                    optionPrice = dropDownPriceList[position]

                    // 총 가격
                    // 총 가격은 기본가격 + 배송비이기 때문에 두개의 가격을 toInt로 변경하여 계산해야한다.
                    // 배송비에 ,와 원 을 지워준다.
                    val feeToString = fee.value!!.replace(",", "").replace("원", "")
                    val feeToInt = feeToString.toInt()

                    // 기본 가격에 ,와 원을 지워준다.
                    val selectedCropPriceToString = selectedCropPrice.value!!.replace(",", "").replace("원", "")
                    val selectedCropPriceToInt = selectedCropPriceToString.toInt()

                    // 계산한다.
                    val resultToInt = feeToInt + selectedCropPriceToInt
                    // 천 단위에 콤마를 넣어준다.
                    val dec = DecimalFormat("#,###")
                    var resultString = dec.format(resultToInt).toString()
                    // 끝에 원을 붙혀준다.
                    resultString += "원"
                    // 총 가격에 적용해준다.
                    totalPrice.value = resultString

                }
            }
        }
    }

    // 드롭다운 설정
    private fun settingDropDown() {
        binding.apply {
            // 드롭다운 설정
            cropData?.crop_option_detail?.forEach {
                dropDownNameList.add(it["crop_option_name"].toString())
                dropDownPriceList.add(it["crop_option_price"].toString())
            }
            val typeArrayAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_crop_option, dropDownNameList)
            textViewBottomTradeOptionName.setAdapter(typeArrayAdapter)


        }
    }
    private fun setButton(){
        binding.apply {
            // 옵션명이 비어있지 않을 때 클릭이 되도록 변경해야함

            // + 버튼
            toggleButtonPlus.setOnClickListener {
                // 옵션명이 비워져 있지 않을 때 실행이 되도록 조건을 걸어준다.
                if (textViewBottomTradeOptionName.text.toString() != "") {
                    bottomSheetTradeCropViewModel?.plusOptionCount()
                    // 현재 옵션명을 가져온다.
                    var currentOptionName = textViewBottomTradeOptionName.text.toString()
                    // 농산물 데이터를 반복해서 crop_option_name 필드와 현재 옵션명과 같으면 해당 옵션 기본가격을 가져온다.
                    var originalPrice = ""
                    cropData?.crop_option_detail?.forEach {
                        if (it["crop_option_name"].toString() == currentOptionName) {
                            originalPrice = it["crop_option_price"].toString()
                        }
                    }
                    // 기본가격을 계산하기 위해서 ","와 "원"글자를 공백으로 교체하고 toInt로 바꿔준다.
                    originalPrice = originalPrice.replace(",", "").replace("원", "")
                    val originalPriceInt = originalPrice.toInt()

                    // 현재 개수를 가져온다.
                    var currentCnt = bottomSheetTradeCropViewModel?.optionCounts?.value!!

                    // 변경된 가격을 적어준다.
                    var resultPrice = originalPriceInt * currentCnt
                    // 천 단위에 콤마를 넣어준다.
                    val dec = DecimalFormat("#,###")
                    var resultPriceString = dec.format(resultPrice).toString()
                    // 끝에 "원" 글자를 붙혀준다.
                    resultPriceString += "원"
                    // 변경된 값을 selectedCropPrice에 적용해준다.
                    bottomSheetTradeCropViewModel?.selectedCropPrice?.value = resultPriceString

                    // 총가격도 변경해주어야 한다.
                    // 배송비에 ,와 원 을 지워준다.
                    val feeToString = bottomSheetTradeCropViewModel?.fee?.value!!.replace(",", "").replace("원", "")
                    val feeToInt = feeToString.toInt()
                    // 총 가격을 계산해준다
                    val totalResultToInt = feeToInt + resultPrice
                    // 천단위 콤마
                    var totalResultToString = dec.format(totalResultToInt).toString()
                    // 원
                    totalResultToString += "원"
                    // 변경된 총가격
                    bottomSheetTradeCropViewModel?.totalPrice?.value = totalResultToString
                }
            }

            // = 버튼
            toggleButtonMinus.setOnClickListener {
                // 옵션명이 비워져 있지 않을 때 실행이 되도록 조건을 걸어준다.
                if (textViewBottomTradeOptionName.text.toString() != "") {
                    bottomSheetTradeCropViewModel?.minusOptionCount()
                    // 현재 옵션명을 가져온다.
                    var currentOptionName = textViewBottomTradeOptionName.text.toString()
                    // 농산물 데이터를 반복해서 crop_option_name 필드와 현재 옵션명과 같으면 해당 옵션 기본가격을 가져온다.
                    var originalPrice = ""
                    cropData?.crop_option_detail?.forEach {
                        if (it["crop_option_name"].toString() == currentOptionName) {
                            originalPrice = it["crop_option_price"].toString()
                        }
                    }
                    // 기본가격을 계산하기 위해서 ","와 "원"글자를 공백으로 교체하고 toInt로 바꿔준다.
                    originalPrice = originalPrice.replace(",", "").replace("원", "")
                    val originalPriceInt = originalPrice.toInt()

                    // 현재 개수를 가져온다.
                    var currentCnt = bottomSheetTradeCropViewModel?.optionCounts?.value!!

                    // 변경된 가격을 적어준다.
                    var resultPrice = originalPriceInt * currentCnt
                    // 천 단위에 콤마를 넣어준다.
                    val dec = DecimalFormat("#,###")
                    var resultPriceString = dec.format(resultPrice).toString()
                    // 끝에 "원" 글자를 붙혀준다.
                    resultPriceString += "원"
                    // 변경된 값을 selectedCropPrice에 적용해준다.
                    bottomSheetTradeCropViewModel?.selectedCropPrice?.value = resultPriceString

                    // 총가격도 변경해주어야 한다.
                    // 배송비에 ,와 원 을 지워준다.
                    val feeToString = bottomSheetTradeCropViewModel?.fee?.value!!.replace(",", "").replace("원", "")
                    val feeToInt = feeToString.toInt()
                    // 총 가격을 계산해준다
                    val totalResultToInt = feeToInt + resultPrice
                    // 천단위 콤마
                    var totalResultToString = dec.format(totalResultToInt).toString()
                    // 원
                    totalResultToString += "원"
                    // 변경된 총가격
                    bottomSheetTradeCropViewModel?.totalPrice?.value = totalResultToString

                }
            }

            // 장바구니 버튼
            buttonPaymentCropCart.setOnClickListener {
                // 옵션명이 비워져 있지 않을 때 실행이 되도록 조건을 걸어준다.
                if (textViewBottomTradeOptionName.text.toString() != "") {
                    // ("장바구니 연결")
                    val dialog = DialogYesNo(
                        this@BottomSheetTradeCrop,
                        "장바구니에 예약한 상품이 담겼습니다.",
                        "장바구니로 이동하시겠습니까?",
                        tradeDetailActivity
                    )
                    dialog.show(tradeDetailActivity.supportFragmentManager, "DialogYesNo")

                }
            }

            // 구매하기 버튼
            buttonPaymentCropReserv.setOnClickListener {
                // 옵션명이 비워져 있지 않을 때 실행이 되도록 조건을 걸어준다.
                if (textViewBottomTradeOptionName.text.toString() != "") {
                    // 구매("창 띄우기")
                    val intent = Intent(requireContext(), PaymentActivity::class.java)

                    intent.putExtra(
                        "PaymentFragmentName",
                        PaymentFragmentName.PAYMENT_CROP_FRAGMENT
                    )

                    // 현재 옵션 개수
                    var currentCnt = bottomSheetTradeCropViewModel?.optionCounts?.value!!
                    // 맵 생성
                    var mapList = ArrayList<HashMap<String,Any>>()
                    var map1 = HashMap<String,Any>()
                    // 농산품 번호
                    map1["product_idx"] = crop_idx
                    // 옵션
                    map1["option"] = mutableMapOf(
                        // 옵션명
                        "optionName" to binding.textViewBottomTradeOptionName.text.toString(),
                        // 현재 옵션 개수
                        "optionCnt" to currentCnt,
                        // 옵션 기본 가격
                        "optionPrice" to optionPrice
                    )
                    // 상품 타입
                    map1["productType"] = OrderProductType.ORDER_PRODUCT_TYPE_CROP.number
                    // 총 금액
                    map1["totalPrice"] = binding.bottomSheetTradeCropViewModel?.totalPrice?.value.toString()
                    mapList.add(map1)
                    val bundle = Bundle()
                    bundle.putSerializable("mapList",mapList)

                    intent.putExtra("paymentData",bundle)

                    startActivity(intent)
                }
            }
        }
    }
    override fun onYesButtonClick(activity: AppCompatActivity) {
        val intent = Intent(requireContext(), CartActivity::class.java)

        intent.putExtra("CartFragmentName",CartFragmentName.CART_TAB_CROP_FRAGMENT)
        // 농산품 번호
        intent.putExtra("crop_idx",crop_idx)
        // 옵션명
        intent.putExtra("optionName",binding.textViewBottomTradeOptionName.text.toString())
        // 현재 옵션 개수
        var currentCnt = bottomSheetTradeCropViewModel.optionCounts.value!!
        intent.putExtra("optionCnt",currentCnt)
        startActivity(intent)
    }
    override fun onYesButtonClick(id: Int) {

    }
}