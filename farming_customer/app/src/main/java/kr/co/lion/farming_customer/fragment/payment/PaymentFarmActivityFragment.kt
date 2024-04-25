package kr.co.lion.farming_customer.fragment.payment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.dao.payment.PaymentDao
import kr.co.lion.farming_customer.databinding.FragmentPaymentFarmActivityBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.databinding.RowPaymentProductBinding
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.model.payment.PaymentModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.payment.PaymentCropViewModel
import kr.co.lion.farming_customer.viewmodel.payment.PaymentFarmActivityViewModel
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PaymentFarmActivityFragment : Fragment() {

    lateinit var fragmentPaymentFarmActivityBinding: FragmentPaymentFarmActivityBinding
    lateinit var paymentActivity: PaymentActivity
    lateinit var paymentFarmActivityViewModel: PaymentFarmActivityViewModel

    // 결제할 농산품 데이터를 담을 리스트
    var paymentFarmDataList = mutableListOf<FarmModel>()

    // 결제할 농산품 데이터를 담을 리스트
    var paymentActivityDataList = mutableListOf<ActivityModel>()

    // 인텐트로부터 데이터를 받을 mapList
    var mapList = ArrayList<HashMap<String,Any>>()

    // 농산품 번호를 담을 리스트
    var farmActivityIdxList = mutableListOf<Any>()

    // 각 농산품의 총 가격을 담을 리스트
    var farmActivityTotalPriceList = mutableListOf<Any>()

    // 로그인한 사용자 데이터를 받을 List
    var userData: UserModel? = null

    // 라디오 버튼에 따라 결제 타입을 바꿀 프로퍼티
    var paymentType = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentPaymentFarmActivityBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_farm_activity,container,false)
        paymentFarmActivityViewModel = PaymentFarmActivityViewModel()
        fragmentPaymentFarmActivityBinding.paymentFarmActivityViewModel = paymentFarmActivityViewModel
        fragmentPaymentFarmActivityBinding.lifecycleOwner = this
        paymentActivity = activity as PaymentActivity

        gettingData()
        settingCheckBoxAllAgree()
        settingButtonPayDone()
        settingDetailButton()
        settingButtonUseAllPoint()
        settingButtonTermText()
        settingPaymentType()
        settingRecyclerViewPaymentFarmActivity()

        return fragmentPaymentFarmActivityBinding.root
    }

    // intent로부터 데이터를 가져오는 메서드
    fun gettingData(){
        mapList = (arguments?.getSerializable("mapList") as? ArrayList<HashMap<String, Any>>)!!
        // 맵 리스트가 null이 아니고 비어있지 않고 productType이 주말농장일 경우
        if (!mapList.isNullOrEmpty()) {
            // cropIdxList를 비워준다.
            farmActivityIdxList.clear()
            // cropTotalPriceList를 비워준다.
            farmActivityTotalPriceList.clear()
            // mapList의 크기만큼 반복한다.
            for (map in mapList) {
                // product_idx만 리스트에 담아준다.
                farmActivityIdxList.add(map["product_idx"]!!)
                farmActivityTotalPriceList.add(map["totalPrice"]!!)
            }
        }
        // paymentCropDataList를 비워준다.
        paymentFarmDataList.clear()
        paymentActivityDataList.clear()
        // 인덱스 번호를 이용해 농산품 데이터를 가져온다.
        farmActivityIdxList.forEach {
            CoroutineScope(Dispatchers.Main).launch {
                // 여기서 Idx가 주말농장인지 체험활동인지 구분을 해야한다.
                // 주말농장이라면..
                if(mapList[0]["productType"]==2){
                    // 주말농장 번호가 여러개 일 수 있기 때문에 반복문을 돌려 전부 paymentFarmActivityDataList에 주말농장 데이터를 담아준다.
                    paymentFarmDataList.add(FarmDao.selectFarmData(it as Int)!!)
                    // 로그인한 사용자번호를 가져와 번호를 통해 데이터를 받아온다.
                    val sharedPreferences = context?.getSharedPreferences("AutoLogin",
                        Context.MODE_PRIVATE
                    )
                    val savedIndex = sharedPreferences?.getInt("loginUserIdx", -1)
                    // 로그인한 사용자 번호를 통해 데이터를 받아온다.
                    userData = UserDao.gettingUserInfoByUserIdx(savedIndex!!)

                    fragmentPaymentFarmActivityBinding.recyclerViewPaymentProduct.adapter?.notifyDataSetChanged()

                }
                // 체험활동이라면..
                else{
                    // 체험활동 번호가 여러개 일 수 있기 때문에 반복문을 돌려 전부 paymentFarmActivityDataList에 주말농장 데이터를 담아준다.
                    paymentActivityDataList.add(ActivityDao.selectActivityData(it as Int)!!)
                    // 로그인한 사용자번호를 가져와 번호를 통해 데이터를 받아온다.
                    val sharedPreferences = context?.getSharedPreferences("AutoLogin",
                        Context.MODE_PRIVATE
                    )
                    val savedIndex = sharedPreferences?.getInt("loginUserIdx", -1)
                    // 로그인한 사용자 번호를 통해 데이터를 받아온다.
                    userData = UserDao.gettingUserInfoByUserIdx(savedIndex!!)

                    fragmentPaymentFarmActivityBinding.recyclerViewPaymentProduct.adapter?.notifyDataSetChanged()
                }
                settingTextView()
                changedUsePoint()
            }
        }
    }

    // 인텐트로부터 받아온 데이터를 TextView에 적용하는 메서드
    fun settingTextView(){
        fragmentPaymentFarmActivityBinding.apply {
            // 모든 상품의 가격을 ","와 "원"을 공백으로 대체하고 가격을 더해준다.
            var totalPriceAll = 0
            var resultPriceString = ""
            farmActivityTotalPriceList.forEachIndexed { index, totalPrice ->
                var totalPriceString = totalPrice.toString()
                totalPriceString = totalPriceString.replace(",","").replace("원","")
                var totalPriceInt = totalPriceString.toInt()
                totalPriceAll += totalPriceInt
                // 천 단위에 콤마를 넣어준다.
                val dec = DecimalFormat("#,###")
                resultPriceString = dec.format(totalPriceAll).toString()
                // 끝에 "원" 글자를 붙혀준다.
                resultPriceString += "원"
                paymentFarmActivityViewModel?.textViewPaymentFarmActivityProductPrice2?.value = resultPriceString
            }
            // 사용 가능 포인트
            paymentFarmActivityViewModel?.textViewPaymentFarmActivityAvailablePoint?.value = "사용가능 : ${userData?.user_point}P"
            // 사용 할 포인트
            paymentFarmActivityViewModel?.textFieldPaymentFarmActivityUsePoint?.value = "0"
            // 할인 받은 포인트
            paymentFarmActivityViewModel?.textViewPaymentFarmActivityUsePoint2?.value = "-0P"
            // 총 결제 금액
            paymentFarmActivityViewModel?.textViewPaymentFarmActivityTotalPayPrice2?.value = resultPriceString
            // 수령인
            paymentFarmActivityViewModel?.textFieldReservationName?.value = "${userData?.user_name}"
            // 연락처
            paymentFarmActivityViewModel?.textFieldReservationPhoneNumber?.value = "${userData?.user_phone}"
        }
    }

    // 사용할 포인트를 사용자가 임의로 수정했을 때
    fun changedUsePoint(){
        fragmentPaymentFarmActivityBinding.apply {
            textFieldPaymentFarmActivityUsePoint.addTextChangedListener {
                val pointString = it.toString()
                // 칸이 비워져 있을 때
                if(it!!.isEmpty()){
                    textInputLayoutPaymentFarmActivityUsePoint.error = "사용할 포인트를 입력하세요"
                    paymentFarmActivityViewModel?.textViewPaymentFarmActivityUsePoint2?.value = "-0P"
                    paymentFarmActivityViewModel?.textViewPaymentFarmActivityTotalPayPrice2?.value =
                        paymentFarmActivityViewModel?.textViewPaymentFarmActivityProductPrice2?.value
                }
                else{
                    textInputLayoutPaymentFarmActivityUsePoint.error = null
                    textInputLayoutPaymentFarmActivityUsePoint.isErrorEnabled = false
                    // 사용 가능한 포인트보다 큰 값을 넣었을 때
                    val pointInt = pointString.toInt()
                    val availablePoint = userData?.user_point
                    if(pointInt>availablePoint!!){
                        textInputLayoutPaymentFarmActivityUsePoint.error = "사용 가능한 최대 포인트는 ${userData?.user_point}P 입니다"
                    }
                    else{
                        textInputLayoutPaymentFarmActivityUsePoint.error = null
                        textInputLayoutPaymentFarmActivityUsePoint.isErrorEnabled = false
                        // 총 할인 받은 포인트 text를 변경한다.
                        paymentFarmActivityViewModel?.textViewPaymentFarmActivityUsePoint2?.value = "-${pointInt}P"
                        // 총 결제 금액 text를 변경한다.
                        // 총 결제 금액을 담을 프로퍼티
                        var resultTotalPrice = paymentFarmActivityViewModel?.textViewPaymentFarmActivityProductPrice2?.value
                        resultTotalPrice = resultTotalPrice?.replace(",","")?.replace("원","")
                        var totalPriceInt = resultTotalPrice?.toInt()
                        var totalPriceInt2 = totalPriceInt?.minus(pointInt)
                        // 천 단위에 콤마를 넣어준다.
                        val dec = DecimalFormat("#,###")
                        var resultPriceString = dec.format(totalPriceInt2).toString()
                        // 끝에 "원" 글자를 붙혀준다.
                        resultPriceString += "원"
                        paymentFarmActivityViewModel?.textViewPaymentFarmActivityTotalPayPrice2?.value = resultPriceString
                    }
                }
            }
            // endIcon을 눌렀을 때
            textInputLayoutPaymentFarmActivityUsePoint.setEndIconOnClickListener {
                // 텍스트 필드 내용이 빈칸이 아닌 0으로 초기화 시켜준다.
                paymentFarmActivityViewModel?.textFieldPaymentFarmActivityUsePoint?.value = "0"
            }
        }
    }

    // 포인트 전체 사용 버튼 눌렀을 때
    fun settingButtonUseAllPoint(){
        fragmentPaymentFarmActivityBinding.apply {
            buttonPaymentFarmActivityUseAllPoint.setOnClickListener {
                // 사용 할 포인트
                paymentFarmActivityViewModel?.textFieldPaymentFarmActivityUsePoint?.value = "${userData?.user_point}"
            }
        }
    }

    // 동의 버튼 초기화
    fun settingCheckBoxAllAgree(){
        fragmentPaymentFarmActivityBinding.apply {
            // 결제버튼 비활성화
            paymentFarmActivityViewModel?.buttonPaymentFarmActivityPayDone?.value = false

            // 체크박스 초기화
            paymentFarmActivityViewModel?.checkBoxPaymentFarmActivityPrivacy1?.value = false
            paymentFarmActivityViewModel?.checkBoxPaymentFarmActivityPrivacy2?.value = false

        }
    }

    // 결제버튼을 눌렀을 때
    fun settingButtonPayDone(){
        fragmentPaymentFarmActivityBinding.apply {
            buttonPaymentFarmActivityPayDone.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    // 주말농장일 경우
                    if(paymentFarmDataList.isNotEmpty()){
                        saveFarmData()
                    }
                    // 체험활동일 경우
                    else{
                        saveActivityData()
                    }
                    paymentActivity.replaceFragment(PaymentFragmentName.PAYMENT_SUCCESS_FRAGMENT,false,true,null)
                }
            }
        }
    }

    // 결제 수단 메서드
    fun settingPaymentType(){
        // 초기화 설정
        fragmentPaymentFarmActivityBinding.radioButtonPaymentFarmActivityCreditOrCheckCard.isChecked = true
        fragmentPaymentFarmActivityBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
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

    // 주말농장일 경우 orderData와 paymentData에 저장하는 메서드
    fun saveFarmData(){
        CoroutineScope(Dispatchers.Main).launch {
            val currentDate = LocalDate.now()
            mapList.forEach {
                val sequence = OrderDao.getOrderSequence()
                OrderDao.updateOrderSequence(sequence + 1)
                var order_idx = sequence + 1
                val format = SimpleDateFormat("yyMMDD")
                var order_product_type = 2
                var order_num =
                    format.format(System.currentTimeMillis()) + order_product_type.toString() + (SecureRandom().nextInt(1000)).toString()
                var order_user_idx = userData?.user_idx!!
                var order_seller_idx = sequence + 1

                var order_product_idx = it["product_idx"] as Int
                var order_label = OrderLabelType.ORDER_LABEL_TYPE_RESERV_DONE.number
                var order_invoice_number = ""
                var order_delivery_address = mutableMapOf<String, String>()
                // 현재 날짜를 기준으로 앞 뒤로 총 5개의 날짜 생성
                val order_reg_date = currentDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                val order_mod_date = order_reg_date
                var order_delivery_start_date = ""
                var order_delivery_done_date = ""
                var order_is_reviewed = false
                var order_reserv_date = ""
                var order_option_detail = mutableListOf<MutableMap<String, String>>(
                    mutableMapOf(
                        "option_name" to "${(it["option"] as HashMap<String,Any>)["optionName"]}",
                        "option_price" to "${(it["option"] as HashMap<String,Any>)["optionPrice"]}")
                )
                var order_total_price = "${paymentFarmActivityViewModel?.textViewPaymentFarmActivityTotalPayPrice2?.value}"
                var order_cancel = mutableMapOf<String, String>()
                var order_status = OrderStatus.ORDER_STATUS_NORMAL.number

                val model = OrderModel(
                    order_idx, order_num, order_user_idx,
                    order_seller_idx, order_product_type,
                    order_product_idx, order_label, order_invoice_number,
                    order_delivery_address, order_reg_date, order_mod_date,
                    order_delivery_start_date, order_delivery_done_date,
                    order_is_reviewed, order_reserv_date, order_option_detail,
                    order_total_price, order_cancel, order_status
                )

                OrderDao.insertOrderData(model)
            }
            // PaymentData를 저장한다.
            val sequence = PaymentDao.getPaymentSequence()
            PaymentDao.updatePaymentSequence(sequence + 1)
            var payment_idx = sequence + 1
            val format = SimpleDateFormat("yyMMDD")
            var payment_order_num =
                format.format(System.currentTimeMillis()) + OrderProductType.ORDER_PRODUCT_TYPE_FARM.number.toString() + (SecureRandom().nextInt(
                    1000
                )).toString()
            var payment_total_price = paymentFarmActivityViewModel.textViewPaymentFarmActivityProductPrice2.value
            var payment_total_discount = paymentFarmActivityViewModel.textViewPaymentFarmActivityUsePoint2.value
            var payment_final_price = paymentFarmActivityViewModel.textViewPaymentFarmActivityTotalPayPrice2.value
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

    // 체험활동일 경우 orderData와 paymentData에 저장하는 메서드
    fun saveActivityData(){
        CoroutineScope(Dispatchers.Main).launch {
            val currentDate = LocalDate.now()
            mapList.forEach {
                val sequence = OrderDao.getOrderSequence()
                OrderDao.updateOrderSequence(sequence + 1)
                var order_idx = sequence + 1
                val format = SimpleDateFormat("yyMMDD")
                var order_product_type = 3
                var order_num =
                    format.format(System.currentTimeMillis()) + order_product_type.toString() + (SecureRandom().nextInt(1000)).toString()
                var order_user_idx = userData?.user_idx!!
                var order_seller_idx = sequence + 1

                var order_product_idx = it["product_idx"] as Int
                var order_label = OrderLabelType.ORDER_LABEL_TYPE_RESERV_DONE.number
                var order_invoice_number = ""
                var order_delivery_address = mutableMapOf<String, String>()
                // 현재 날짜를 기준으로 앞 뒤로 총 5개의 날짜 생성
                val order_reg_date = currentDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                val order_mod_date = order_reg_date
                var order_delivery_start_date = ""
                var order_delivery_done_date = ""
                var order_is_reviewed = false
                var order_reserv_date = ""
                var order_option_detail = mutableListOf<MutableMap<String, String>>(

                )
                for(i in 1..(it["option"] as MutableList<HashMap<String,Any>>).size){
                    var order_map = mutableMapOf<String,String>(
                        "option_name" to "${(it["option"] as HashMap<String,Any>)["optionName"]}",
                        "option_price" to "${(it["option"] as HashMap<String,Any>)["optionPrice"]}",
                        "option_cnt" to "${(it["option"] as HashMap<String,Any>)["optionCnt"]}",
                        "option_time" to "${(it["option"] as HashMap<String,Any>)["optionTime"]}",
                        "option_total_price" to "${(it["option"] as HashMap<String,Any>)["optionTotalPrice"]}"
                    )
                    order_option_detail.add(order_map)
                }
                var order_total_price = "${paymentFarmActivityViewModel.textViewPaymentFarmActivityTotalPayPrice2.value}"
                var order_cancel = mutableMapOf<String, String>()
                var order_status = OrderStatus.ORDER_STATUS_NORMAL.number

                val model = OrderModel(
                    order_idx, order_num, order_user_idx,
                    order_seller_idx, order_product_type,
                    order_product_idx, order_label, order_invoice_number,
                    order_delivery_address, order_reg_date, order_mod_date,
                    order_delivery_start_date, order_delivery_done_date,
                    order_is_reviewed, order_reserv_date, order_option_detail,
                    order_total_price, order_cancel, order_status
                )
                OrderDao.insertOrderData(model)
            }
            // PaymentData를 저장한다.
            val sequence = PaymentDao.getPaymentSequence()
            PaymentDao.updatePaymentSequence(sequence + 1)
            var payment_idx = sequence + 1
            val format = SimpleDateFormat("yyMMDD")
            var payment_order_num =
                format.format(System.currentTimeMillis()) + OrderProductType.ORDER_PRODUCT_TYPE_ACTIVITY.number.toString() + (SecureRandom().nextInt(
                    1000
                )).toString()
            var payment_total_price = paymentFarmActivityViewModel.textViewPaymentFarmActivityProductPrice2.value
            var payment_total_discount = paymentFarmActivityViewModel.textViewPaymentFarmActivityUsePoint2.value
            var payment_final_price = paymentFarmActivityViewModel.textViewPaymentFarmActivityTotalPayPrice2.value
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

    // 필수 동의 내용 버튼
    fun settingDetailButton(){
        fragmentPaymentFarmActivityBinding.apply {
            textViewPaymentFarmActivityPrivacyDetail1.setOnClickListener {
                if(textViewPaymentFarmActivityPrivacyDetailContent1.visibility == View.GONE){
                    textViewPaymentFarmActivityPrivacyDetailContent1.visibility = View.VISIBLE
                }else{
                    textViewPaymentFarmActivityPrivacyDetailContent1.visibility = View.GONE
                }
            }
            textViewPaymentFarmActivityPrivacyDetail2.setOnClickListener {
                if(textViewPaymentFarmActivityPrivacyDetailContent2.visibility == View.GONE){
                    textViewPaymentFarmActivityPrivacyDetailContent2.visibility = View.VISIBLE
                }else{
                    textViewPaymentFarmActivityPrivacyDetailContent2.visibility = View.GONE
                }
            }
        }
    }

    // 이용규칙, 취소 및 환불 규칙 text를 눌렀을 때
    fun settingButtonTermText(){

    }

    // 주말농장 및 체험활동 결제상품 recyclerView 설정
    fun settingRecyclerViewPaymentFarmActivity(){
        fragmentPaymentFarmActivityBinding.apply {
            recyclerViewPaymentProduct.apply {
                adapter = PaymentFarmActivityRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(paymentActivity)
            }
        }
    }

    // 주말농장 및 체험활동 결제상품 RecyclerView Adapter
    inner class PaymentFarmActivityRecyclerViewAdapter: RecyclerView.Adapter<PaymentFarmActivityRecyclerViewAdapter.PaymentFarmActivityViewHolder>(){
        inner class PaymentFarmActivityViewHolder(rowPaymentProductBinding: RowPaymentProductBinding): RecyclerView.ViewHolder(rowPaymentProductBinding.root){
            val rowPaymentProductBinding: RowPaymentProductBinding

            init {
                this.rowPaymentProductBinding = rowPaymentProductBinding
                this.rowPaymentProductBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentFarmActivityViewHolder {
            val rowPaymentProductBinding = DataBindingUtil.inflate<RowPaymentProductBinding>(layoutInflater,R.layout.row_payment_product, parent,false)
            val paymentCropViewModel = PaymentCropViewModel()
            rowPaymentProductBinding.paymentCropViewModel = paymentCropViewModel
            rowPaymentProductBinding.lifecycleOwner = this@PaymentFarmActivityFragment
            return PaymentFarmActivityViewHolder(rowPaymentProductBinding)
        }

        override fun getItemCount(): Int {
            // 주말농장 데이터가 비어있다면 체험활동 리스트 크기만큼 보여준다.
            if(paymentFarmDataList.isEmpty()){
                return paymentActivityDataList.size
            }
            return paymentFarmDataList.size
        }

        override fun onBindViewHolder(holder: PaymentFarmActivityViewHolder, position: Int) {
            // 옵션명
            holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductOption?.value = (mapList[position]["option"] as HashMap<String,Any>)["optionName"].toString()
            // 옵션 개수
            holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductCnt?.value = "${(mapList[position]["option"] as HashMap<String,Any>)["optionCnt"].toString()}개"
            // 총 가격
            holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductPrice?.value = mapList[position]["totalPrice"].toString()

            // 주말농장일 경우
            if(paymentFarmDataList.isNotEmpty()){
                // 상품명
                holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductName?.value = paymentFarmDataList[position].farm_title
                // 이미지
                CoroutineScope(Dispatchers.Main).launch {
                    FarmDao.gettingFarmImage(requireContext(), paymentFarmDataList[position].farm_images[0], holder.rowPaymentProductBinding.imageViewPaymentProduct2)
                }
            }
            // 체험활동일 경우
            else{
                // 상품명
                holder.rowPaymentProductBinding.paymentCropViewModel?.textViewPaymentProductName?.value = paymentActivityDataList[position].activity_title
                // 이미지
                CoroutineScope(Dispatchers.Main).launch {
                    ActivityDao.gettingActivityImage(requireContext(), paymentActivityDataList[position].activity_images[0], holder.rowPaymentProductBinding.imageViewPaymentProduct2)
                }
            }

        }
    }
}