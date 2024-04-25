package kr.co.lion.farming_customer.fragment.payment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.OrderLabelType
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.OrderStatus
import kr.co.lion.farming_customer.PaymentFragmentName
import kr.co.lion.farming_customer.PaymentStatus
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.payment.PaymentActivity
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.dao.payment.PaymentDao
import kr.co.lion.farming_customer.databinding.FragmentPaymentCropBinding
import kr.co.lion.farming_customer.databinding.ItemProductBinding
import kr.co.lion.farming_customer.databinding.RowPaymentProductBinding
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.model.payment.PaymentModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.payment.PaymentCropViewModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeViewModel
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PaymentCropFragment : Fragment() {

    lateinit var fragmentPaymentCropBinding: FragmentPaymentCropBinding
    lateinit var paymentActivity: PaymentActivity
    lateinit var paymentCropVIewModel:PaymentCropViewModel

    // 결제할 농산품 데이터를 담을 리스트
    var paymentCropDataList = mutableListOf<CropModel>()

    // 농산품 번호를 담을 리스트
    var cropIdxList = mutableListOf<Any>()

    // 각 농산품의 총 가격을 담을 리스트
    var cropTotalPriceList = mutableListOf<Any>()

    // 인텐트로부터 데이터를 받을 mapList
    var mapList = ArrayList<HashMap<String,Any>>()

    // 로그인한 사용자 데이터를 받을 List
    var userData:UserModel? = null

    // 라디오 버튼에 따라 결제 타입을 바꿀 프로퍼티
    var paymentType = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentCropBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_crop,container,false)
        paymentCropVIewModel = PaymentCropViewModel()
        fragmentPaymentCropBinding.paymentCropViewModel = paymentCropVIewModel
        fragmentPaymentCropBinding.lifecycleOwner = this
        paymentActivity = activity as PaymentActivity

        gettingData()
        settingCheckBoxAllAgree()
        settingButtonPayDone()
        settingDetailButton()
        settingButtonDelivery()
        settingRecyclerViewPaymentFarmActivity()
        settingPaymentType()
        settingButtonUseAllPoint()
        settingButtonTermText()

        return fragmentPaymentCropBinding.root
    }

    // 인텐트로부터 데이터를 받아오고 필요한 데이터를 받아온다.
    fun gettingData(){
        mapList = (arguments?.getSerializable("mapList") as? ArrayList<HashMap<String, Any>>)!!
        Log.d("test12","$mapList")
        // 맵 리스트가 null이 아니고 비어있지 않은 경우에만 처리
        if (!mapList.isNullOrEmpty()) {
            // cropIdxList를 비워준다.
            cropIdxList.clear()
            // cropTotalPriceList를 비워준다.
            cropTotalPriceList.clear()

            // mapList의 크기만큼 반복한다.
            for (map in mapList) {
                // product_idx만 리스트에 담아준다.
                cropIdxList.add(map["product_idx"]!!)
                cropTotalPriceList.add(map["totalPrice"]!!)
            }
        }
        // paymentCropDataList를 비워준다.
        paymentCropDataList.clear()
        // 농산품 번호를 이용해 농산품 데이터를 가져온다.
        cropIdxList.forEach {
            CoroutineScope(Dispatchers.Main).launch {
                // 농산품 번호가 여러개 일 수 있기 때문에 반복문을 돌려 전보 paymentCropDataList에 농산품 데이터를 담아준다.
                paymentCropDataList.add(CropDao.selectCropData(it as Int)!!)
                // 로그인한 사용자번호를 가져와 번호를 통해 데이터를 받아온다.
                val sharedPreferences = context?.getSharedPreferences("AutoLogin", MODE_PRIVATE)
                val savedIndex = sharedPreferences?.getInt("loginUserIdx", -1)
                // 로그인한 사용자 번호를 통해 데이터를 받아온다.
                userData = UserDao.gettingUserInfoByUserIdx(savedIndex!!)

                fragmentPaymentCropBinding.recyclerViewPaymentProductCrop.adapter?.notifyDataSetChanged()
                settingTextView()
                changedUsePoint()
            }
        }
    }

    // 인텐트로부터 받아온 데이터를 TextView에 적용하는 메서드
    fun settingTextView(){
        fragmentPaymentCropBinding.apply {
            // 모든 상품의 가격을 ","와 "원"을 공백으로 대체하고 가격을 더해준다.
            var totalPriceAll = 0
            var resultPriceString = ""
            cropTotalPriceList.forEachIndexed { index, totalPrice ->
                var totalPriceString = totalPrice.toString()
                totalPriceString = totalPriceString.replace(",","").replace("원","")
                var totalPriceInt = totalPriceString.toInt()
                totalPriceAll += totalPriceInt
                // 천 단위에 콤마를 넣어준다.
                val dec = DecimalFormat("#,###")
                resultPriceString = dec.format(totalPriceAll).toString()
                // 끝에 "원" 글자를 붙혀준다.
                resultPriceString += "원"
                paymentCropVIewModel?.textViewPaymentCropProductPrice2?.value = resultPriceString
            }
            // 사용 가능 포인트
            paymentCropViewModel?.textViewPaymentCropAvailablePoint?.value = "사용가능 : ${userData?.user_point}P"
            // 사용 할 포인트
            paymentCropViewModel?.textFieldPaymentCropUsePoint?.value = "0"
            // 할인 받은 포인트
            paymentCropViewModel?.textViewPaymentCropUsePoint2?.value = "-0P"
            // 총 결제 금액
            paymentCropViewModel?.textViewPaymentCropTotalPayPrice2?.value = resultPriceString
            // 수령인
            paymentCropViewModel?.textViewPaymentReceiver2?.value = "${userData?.user_name}"
            // 수령인 주소
            paymentCropViewModel?.textViewPaymentReceiverAddress?.value = "파밍시 파밍동 파밍아파트 101동 101호"
            // 연락처
            paymentCropViewModel?.textViewPaymentReceiverPhoneNumber?.value = "${userData?.user_phone}"
            // 요청사항
            paymentCropViewModel?.textViewPaymentReceiverRequest?.value = "문 앞에 두고 벨 눌러주세요"
        }
    }

    // 사용할 포인트를 사용자가 임의로 수정했을 때
    fun changedUsePoint(){
        fragmentPaymentCropBinding.apply {
            textFieldPaymentCropUsePoint.addTextChangedListener {
                val pointString = it.toString()
                // 칸이 비워져 있을 때
                if(it!!.isEmpty()){
                    textInputLayoutPaymentCropUsePoint.error = "사용할 포인트를 입력하세요"
                    paymentCropViewModel?.textViewPaymentCropUsePoint2?.value = "-0P"
                    paymentCropViewModel?.textViewPaymentCropTotalPayPrice2?.value = paymentCropViewModel?.textViewPaymentCropProductPrice2?.value
                }
                else{
                    textInputLayoutPaymentCropUsePoint.error = null
                    textInputLayoutPaymentCropUsePoint.isErrorEnabled = false
                    // 사용 가능한 포인트보다 큰 값을 넣었을 때
                    val pointInt = pointString.toInt()
                    val availablePoint = userData?.user_point
                    if(pointInt>availablePoint!!){
                        textInputLayoutPaymentCropUsePoint.error = "사용 가능한 최대 포인트는 ${userData?.user_point}P 입니다"
                    }
                    else{
                        textInputLayoutPaymentCropUsePoint.error = null
                        textInputLayoutPaymentCropUsePoint.isErrorEnabled = false
                        // 총 할인 받은 포인트 text를 변경한다.
                        paymentCropViewModel?.textViewPaymentCropUsePoint2?.value = "-${pointInt}P"
                        // 총 결제 금액 text를 변경한다.
                        // 총 결제 금액을 담을 프로퍼티
                        var resultTotalPrice = paymentCropViewModel?.textViewPaymentCropProductPrice2?.value
                        resultTotalPrice = resultTotalPrice?.replace(",","")?.replace("원","")
                        var totalPriceInt = resultTotalPrice?.toInt()
                        var totalPriceInt2 = totalPriceInt?.minus(pointInt)
                        // 천 단위에 콤마를 넣어준다.
                        val dec = DecimalFormat("#,###")
                        var resultPriceString = dec.format(totalPriceInt2).toString()
                        // 끝에 "원" 글자를 붙혀준다.
                        resultPriceString += "원"
                        paymentCropViewModel?.textViewPaymentCropTotalPayPrice2?.value = resultPriceString
                    }
                }
            }
            // endIcon을 눌렀을 때
            textInputLayoutPaymentCropUsePoint.setEndIconOnClickListener {
                // 텍스트 필드 내용이 빈칸이 아닌 0으로 초기화 시켜준다.
                paymentCropViewModel?.textFieldPaymentCropUsePoint?.value = "0"
            }
        }
    }

    // 포인트 전체 사용 버튼 눌렀을 때
    fun settingButtonUseAllPoint(){
        fragmentPaymentCropBinding.apply {
            buttonPaymentCropUseAllPoint.setOnClickListener {
                // 사용 할 포인트
                paymentCropViewModel?.textFieldPaymentCropUsePoint?.value = "${userData?.user_point}"
            }
        }
    }

    // 동의 버튼 초기화
    fun settingCheckBoxAllAgree(){
        fragmentPaymentCropBinding.apply {
            // 결제버튼 비활성화
            paymentCropVIewModel?.buttonPaymentCropPayDone?.value = false

            // 체크박스 초기화
            paymentCropVIewModel?.checkBoxPaymentCropPrivacy1?.value = false
            paymentCropVIewModel?.checkBoxPaymentCropPrivacy2?.value = false

        }
    }

    // 결제 수단 메서드
    fun settingPaymentType(){
        // 초기화 설정
        fragmentPaymentCropBinding.radioButtonPaymentCropCreditOrCheckCard.isChecked = true
        fragmentPaymentCropBinding.radioGroupPaymentType.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioButtonPaymentCropCreditOrCheckCard -> {
                    // 체크카드
                    paymentType = 1
                }
                R.id.radioButtonPaymentCropKakaoPay -> {
                    // 카카오페이
                    paymentType = 2
                }
                R.id.radioButtonPaymentCropNaverPay -> {
                    // 네이버페이
                    paymentType = 3
                }
                R.id.radioButtonPaymentCropTossPay -> {
                    // 토스페이
                    paymentType = 4
                }
            }
        }
    }

    // 결제버튼을 눌렀을 때
    fun settingButtonPayDone(){
        fragmentPaymentCropBinding.apply {
            buttonPaymentCropPayDone.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        val currentDate = LocalDate.now()
                        // OrderData를 저장한다.
                        // 상품의 개수만큼 반복한다.
                        mapList.forEach {
                            val sequence = OrderDao.getOrderSequence()
                            OrderDao.updateOrderSequence(sequence + 1)
                            var order_idx = sequence + 1
                            val format = SimpleDateFormat("yyMMDD")
                            var order_product_type = OrderProductType.ORDER_PRODUCT_TYPE_CROP.number
                            var order_num =
                                format.format(System.currentTimeMillis()) + order_product_type.toString() + (SecureRandom().nextInt(
                                    1000
                                )).toString()
                            var order_user_idx = userData?.user_idx!!
                            var order_seller_idx = sequence + 1
                            var order_product_idx = it["product_idx"] as Int
                            var order_label = OrderLabelType.ORDER_LABEL_TYPE_PAY_DONE.number
                            var order_invoice_number = ""
                            var order_delivery_address = mutableMapOf(
                                "receiver" to "${paymentCropViewModel?.textViewPaymentReceiver2?.value}",
                                "address" to "${paymentCropViewModel?.textViewPaymentReceiverAddress?.value}",
                                "phone" to "${paymentCropViewModel?.textViewPaymentReceiverPhoneNumber?.value}",
                                "request" to "${paymentCropViewModel?.textViewPaymentReceiverRequest?.value}"
                            )
                            val order_reg_date = currentDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                            val order_mod_date = order_reg_date
                            var order_delivery_start_date = ""
                            var order_delivery_done_date = ""
                            var order_is_reviewed = false
                            var order_reserv_date = ""
                            var order_option_detail = mutableListOf<MutableMap<String, String>>(
                                mutableMapOf(
                                    "option_name" to "${(it["option"] as HashMap<String,Any>)["optionName"]}",
                                    "option_cnt" to "${(it["option"] as HashMap<String,Any>)["optionCnt"]}",
                                    "option_price" to "${(it["option"] as HashMap<String,Any>)["optionPrice"]}",
                                    "option_total_price" to "${it["totalPrice"]}"
                                )
                            )
                            var order_total_price = "${ paymentCropViewModel?.textViewPaymentCropTotalPayPrice2?.value}"
                            var order_cancel = mutableMapOf<String, String>()
                            var order_status = OrderStatus.ORDER_STATUS_NORMAL.number

                            val model = OrderModel(
                                order_idx,
                                order_num,
                                order_user_idx,
                                order_seller_idx,
                                order_product_type,
                                order_product_idx,
                                order_label,
                                order_invoice_number,
                                order_delivery_address,
                                order_reg_date,
                                order_mod_date,
                                order_delivery_start_date,
                                order_delivery_done_date,
                                order_is_reviewed,
                                order_reserv_date,
                                order_option_detail,
                                order_total_price,
                                order_cancel,
                                order_status
                            )
                            OrderDao.insertOrderData(model)
                        }
                        // PaymentData를 저장한다.
                        val sequence = PaymentDao.getPaymentSequence()
                        PaymentDao.updatePaymentSequence(sequence + 1)
                        var payment_idx = sequence + 1
                        val format = SimpleDateFormat("yyMMDD")
                        var payment_order_num =
                            format.format(System.currentTimeMillis()) + OrderProductType.ORDER_PRODUCT_TYPE_CROP.number.toString() + (SecureRandom().nextInt(
                                1000
                            )).toString()
                        var payment_total_price = paymentCropViewModel?.textViewPaymentCropProductPrice2?.value
                        var payment_total_discount = paymentCropViewModel?.textViewPaymentCropUsePoint2?.value
                        var payment_final_price = paymentCropViewModel?.textViewPaymentCropTotalPayPrice2?.value
                        var payment_type = paymentType
                        var payment_status = PaymentStatus.PAYMENT_STATUS_NORMAL.num

                        val model = PaymentModel(
                            payment_idx,
                            payment_order_num,
                            payment_total_price!!,
                            payment_total_discount!!,
                            payment_final_price!!,
                            payment_type,payment_status
                        )
                        PaymentDao.insertPaymentData(model)
                        paymentActivity.replaceFragment(PaymentFragmentName.PAYMENT_FAIL_FRAGMENT,false,true,null)

                    }
            }

        }
    }

    // 농산품 결제상품 recyclerView 설정
    fun settingRecyclerViewPaymentFarmActivity(){
        fragmentPaymentCropBinding.apply {
            recyclerViewPaymentProductCrop.apply {
                adapter = PaymentCropRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(paymentActivity)
            }
        }
    }

    // 필수 동의 내용 버튼
    fun settingDetailButton(){
        fragmentPaymentCropBinding.apply {
            textViewPaymentCropPrivacyDetail1.setOnClickListener {
                if(textViewPaymentCropPrivacyDetailContent.visibility == View.GONE){
                    textViewPaymentCropPrivacyDetailContent.visibility = View.VISIBLE
                }else{
                    textViewPaymentCropPrivacyDetailContent.visibility = View.GONE
                }
            }
            textViewPaymentCropPrivacyDetail2.setOnClickListener {
                if(textViewPaymentCropPrivacyDetailContent2.visibility == View.GONE){
                    textViewPaymentCropPrivacyDetailContent2.visibility = View.VISIBLE
                }else{
                    textViewPaymentCropPrivacyDetailContent2.visibility = View.GONE
                }
            }
        }
    }

    // 배송지 선택 버튼
    fun settingButtonDelivery(){
        fragmentPaymentCropBinding.apply {
            imageButtonPaymentDeliveryDetail.setOnClickListener {
                paymentActivity.replaceFragment(PaymentFragmentName.PAYMENT_DELIVERY_ADDRESS_FRAGMENT,true,true,null)
            }
        }
    }

    // 이용규칙, 취소 및 환불 규칙 text를 눌렀을 때
    fun settingButtonTermText(){

    }

    // 농산품 결제상품 RecyclerView Adapter
    inner class PaymentCropRecyclerViewAdapter: RecyclerView.Adapter<PaymentCropRecyclerViewAdapter.PaymentCropViewHolder>(){
        inner class PaymentCropViewHolder(rowPaymentProductBinding: RowPaymentProductBinding): RecyclerView.ViewHolder(rowPaymentProductBinding.root){
            val rowPaymentProductBinding: RowPaymentProductBinding

            init {
                this.rowPaymentProductBinding = rowPaymentProductBinding
                this.rowPaymentProductBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentCropViewHolder {
            val rowPaymentProductBinding = DataBindingUtil.inflate<RowPaymentProductBinding>(layoutInflater,R.layout.row_payment_product, parent,false)
            val paymentCropViewModel = PaymentCropViewModel()
            rowPaymentProductBinding.paymentCropViewModel = paymentCropViewModel
            rowPaymentProductBinding.lifecycleOwner = this@PaymentCropFragment
            return PaymentCropViewHolder(rowPaymentProductBinding)
        }

        override fun getItemCount(): Int {
            return paymentCropDataList.size
        }

        override fun onBindViewHolder(holder: PaymentCropViewHolder, position: Int) {
            // 상품명
            holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductName?.value = paymentCropDataList[position].crop_title
            // 옵션명
            holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductOption?.value = (mapList[position]["option"] as HashMap<String,Any>)["optionName"].toString()
            // 옵션 개수
            holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductCnt?.value = "${(mapList[position]["option"] as HashMap<String,Any>)["optionCnt"].toString()}개"
            // 총 가격
            holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductPrice?.value = mapList[position]["totalPrice"].toString()
            // 이미지
            CoroutineScope(Dispatchers.Main).launch {
                CropDao.gettingCropImage(requireContext(),paymentCropDataList[position].crop_images[0], holder.rowPaymentProductBinding.imageViewPaymentProduct2)
            }
        }
    }
}