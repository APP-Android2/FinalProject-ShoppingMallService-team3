package kr.co.lion.farming_customer.fragment.orderHistory

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.OrderLabelType
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentTapReservDoneBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryFarmBinding
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryFarmViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TapReservDoneFragment : Fragment() {
    lateinit var fragmentTapReservDoneBinding: FragmentTapReservDoneBinding
    lateinit var orderHistoryActivity : OrderHistoryActivity

    var orderType : Int? = null
    var orderList = mutableListOf<OrderModel>()
    var userModel : UserModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapReservDoneBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_reserv_done, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingUserData()

        return fragmentTapReservDoneBinding.root
    }

    private fun settingUserData() {
        val sharedPreferences = orderHistoryActivity.getSharedPreferences("AutoLogin",
            Context.MODE_PRIVATE)
        val userIdx = sharedPreferences.getInt("loginUserIdx", -1)

        CoroutineScope(Dispatchers.Main).launch {
            userModel = UserDao.gettingUserInfoByUserIdx(userIdx)
            settingInitData()
        }
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            orderType = arguments?.getInt("orderType")
            if(orderType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                orderList = OrderDao.gettingOrderListFarm(
                    OrderLabelType.ORDER_LABEL_TYPE_RESERV_DONE,
                    userModel!!.user_idx
                )
            }else{
                orderList = OrderDao.gettingOrderListActivity(
                    OrderLabelType.ORDER_LABEL_TYPE_RESERV_DONE,
                    userModel!!.user_idx
                )
            }
            settingRecyclerView()
        }
    }

    private fun settingRecyclerView() {
        fragmentTapReservDoneBinding.apply {
            recyclerViewReservDone.apply {
                adapter = ReservDoneRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(orderHistoryActivity)
            }
        }
    }

    inner class ReservDoneRecyclerViewAdapter : RecyclerView.Adapter<ReservDoneRecyclerViewAdapter.ReservDoneViewHolder>(){
        inner class ReservDoneViewHolder(rowOrderHistoryFarmBinding: RowOrderHistoryFarmBinding) : RecyclerView.ViewHolder(rowOrderHistoryFarmBinding.root){
            val rowOrderHistoryFarmBinding : RowOrderHistoryFarmBinding
            init {
                this.rowOrderHistoryFarmBinding = rowOrderHistoryFarmBinding

                rowOrderHistoryFarmBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservDoneViewHolder {
            val rowOrderHistoryFarmBinding = DataBindingUtil.inflate<RowOrderHistoryFarmBinding>(layoutInflater, R.layout.row_order_history_farm, parent, false)
            val rowOrderHistoryFarmViewModel = RowOrderHistoryFarmViewModel()
            rowOrderHistoryFarmBinding.rowOrderHistoryFarmViewModel = rowOrderHistoryFarmViewModel
            rowOrderHistoryFarmBinding.lifecycleOwner = this@TapReservDoneFragment

            val reservDoneViewHolder = ReservDoneViewHolder(rowOrderHistoryFarmBinding)
            return reservDoneViewHolder
        }

        override fun getItemCount(): Int {
            return orderList.size
        }

        override fun onBindViewHolder(holder: ReservDoneViewHolder, position: Int) {
            holder.rowOrderHistoryFarmBinding.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    if(orderList[position].order_product_type == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                        // 주말농장
                        val farmModel = FarmDao.selectFarmData(orderList[position].order_product_idx)
                        rowOrderHistoryFarmViewModel!!.apply {
                            textViewRowOrderHistoryFarm_orderDate.value = orderList[position].order_reg_date
                            textViewRowOrderHistoryFarm_productName.value = farmModel!!.farm_title
                            textViewRowOrderHistoryFarm_productOption.value = "이용기간\n${farmModel.farm_use_date_start} ~ ${farmModel.farm_use_date_end}"
                            textViewRowOrderHistoryFarm_price.value = "${orderList[position].order_total_price} / 1구획"
                        }
                        FarmDao.gettingFarmImage(orderHistoryActivity, farmModel!!.farm_images[0], imageViewOrderHistoryFarmProductImage)
                    }else{
                        // 체험활동
                        val activityModel = ActivityDao.selectActivityData(orderList[position].order_product_idx)
                        rowOrderHistoryFarmViewModel!!.apply {
                            textViewRowOrderHistoryFarm_orderDate.value = orderList[position].order_reg_date
                            textViewRowOrderHistoryFarm_productName.value = activityModel!!.activity_title
                            if(orderList[position].order_option_detail.size > 1){
                                textViewRowOrderHistoryFarm_productOption.value = "체험 날짜 : ${orderList[position].order_option_detail[0]["option_time"]} ~"
                            }else{
                                textViewRowOrderHistoryFarm_productOption.value = "체험 날짜 : ${orderList[position].order_option_detail[0]["option_time"]}"
                            }
                            textViewRowOrderHistoryFarm_price.value = orderList[position].order_total_price
                        }
                        val calc_date = calculateDaysUntil(orderList[position].order_reserv_date)
                        if(calc_date < 0){
                            // 예약 날짜가 지났다면 취소 버튼 비활성화
                            buttonRowOrderHistoryFarmProductInside.setTextColor(context?.getColorStateList(R.color.grey_02))
                            buttonRowOrderHistoryFarmProductInside.strokeColor = context?.getColorStateList(R.color.grey_02)
                            buttonRowOrderHistoryFarmProductInside.isEnabled = false
                        }else{
                            buttonRowOrderHistoryFarmProductInside.setTextColor(context?.getColorStateList(R.color.green_main))
                            buttonRowOrderHistoryFarmProductInside.strokeColor = context?.getColorStateList(R.color.green_main)
                            buttonRowOrderHistoryFarmProductInside.isEnabled = true
                        }
                        ActivityDao.gettingActivityImage(orderHistoryActivity, activityModel!!.activity_images[0], imageViewOrderHistoryFarmProductImage)
                    }


                    // 예약 취소
                    buttonRowOrderHistoryFarmProductInside.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putInt("orderIdx", orderList[position].order_idx)
                        bundle.putInt("orderProductIdx", orderList[position].order_product_idx)
                        bundle.putInt("orderProductType", orderList[position].order_product_type)
                        orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_CANCLE_FRAGMENT, true, true, bundle)
                    }
                    // 리뷰 작성
                    buttonRowOrderHistoryFarmProductOutSide.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putInt("orderIdx", orderList[position].order_idx)
                        bundle.putInt("orderProductIdx", orderList[position].order_product_idx)
                        bundle.putInt("orderProductType", orderList[position].order_product_type)
                        orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_WRITE_REVIEW_FRAGMENT, true, true, bundle)
                    }
                }
            }
            // 아이템 클릭 리스너
            holder.rowOrderHistoryFarmBinding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("orderIdx", orderList[position].order_idx)
                bundle.putInt("orderProductIdx", orderList[position].order_product_idx)
                bundle.putInt("orderProductType", orderList[position].order_product_type)
                orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_DETAIL_FRAGMENT, true, true, bundle)
            }

        }
    }

    private fun calculateDaysUntil(targetDateStr: String): Long {
        // 입력된 문자열을 LocalDate로 파싱합니다.
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val targetDate = LocalDate.parse(targetDateStr, formatter)

        // 현재 날짜를 가져옵니다.
        val currentDate = LocalDate.now()

        // 날짜 차이를 계산하여 반환합니다.
        return java.time.temporal.ChronoUnit.DAYS.between(currentDate, targetDate)
    }
}