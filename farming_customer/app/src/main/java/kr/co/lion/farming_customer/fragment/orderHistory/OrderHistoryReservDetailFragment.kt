package kr.co.lion.farming_customer.fragment.orderHistory

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryReservDetailBinding
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryReservDetailViewModel

class OrderHistoryReservDetailFragment : Fragment() {
    lateinit var fragmentOrderHistoryReservDetailBinding: FragmentOrderHistoryReservDetailBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryReservDetailViewModel: OrderHistoryReservDetailViewModel

    var orderModel : OrderModel? = null
    var productType : Int? = null
    var farmModel : FarmModel? = null
    var activityModel : ActivityModel? = null
    var userModel : UserModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryReservDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_reserv_detail, container, false)
        orderHistoryReservDetailViewModel = OrderHistoryReservDetailViewModel()
        fragmentOrderHistoryReservDetailBinding.orderHistoryReservDetailViewModel = orderHistoryReservDetailViewModel
        fragmentOrderHistoryReservDetailBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingUserData()
        settingToolbar()
        settingEvent()

        return fragmentOrderHistoryReservDetailBinding.root
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
            orderModel = OrderDao.selectOrderData(arguments?.getInt("orderIdx")!!)
            productType = arguments?.getInt("orderProductType")
            if(productType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                // 주말농장
                farmModel = FarmDao.selectFarmData(arguments?.getInt("orderProductIdx")!!)
            }else{
                // 체험활동
                activityModel = ActivityDao.selectActivityData(arguments?.getInt("orderProductIdx")!!)
            }
            settingData()
            settingImage()
        }

    }

    private fun settingImage() {
        fragmentOrderHistoryReservDetailBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                if(productType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                    FarmDao.gettingFarmImage(orderHistoryActivity, farmModel!!.farm_images[0], imageViewReservDetailProductImage)
                }else{
                    ActivityDao.gettingActivityImage(orderHistoryActivity, activityModel!!.activity_images[0], imageViewReservDetailProductImage)
                }
            }
        }
    }

    private fun settingData() {
        fragmentOrderHistoryReservDetailBinding.apply {
            orderHistoryReservDetailViewModel!!.apply {
                textviewReservDetail_orderDate.value = orderModel!!.order_reg_date
                textviewReservDetail_orderNum.value = "주문번호 : ${orderModel!!.order_num}"

                if(productType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                    // 주말농장
                    textViewReservDetail_productName.value = farmModel!!.farm_title
                    textViewReservDetail_option.value = "이용기간\n${farmModel!!.farm_use_date_start} - ${farmModel!!.farm_use_date_end}"
                    textViewReservDetail_price.value = "${orderModel!!.order_total_price} / 1구획"
                }else{
                    // 체험활동
                    textViewReservDetail_productName.value = activityModel!!.activity_title
                    if(orderModel!!.order_option_detail.size > 1){
                        textViewReservDetail_option.value = "체험 날짜 : ${orderModel!!.order_option_detail[0]["option_time"]} ~"
                    }else{
                        textViewReservDetail_option.value = "체험 날짜 : ${orderModel!!.order_option_detail[0]["option_time"]}"
                    }
                    textViewReservDetail_price.value = orderModel!!.order_total_price
                }
                // 사용자 모델
                textViewReservDetail_reservName.value = userModel!!.user_name
                textViewReservDetail_phoneNum.value = userModel!!.user_phone

                textViewReservDetail_productPrice.value = orderModel!!.order_total_price
                textViewReservDetail_productNumber.value = "${orderModel!!.order_option_detail.size}개"
                // 결제 모델
                textViewReservDetail_discountPrice.value = "-0원"
                textViewReservDetail_totalPrice.value = "10,000원"
            }
        }
    }

    private fun settingEvent() {
        fragmentOrderHistoryReservDetailBinding.apply {
            buttonReservDetailCheck.setOnClickListener {
                orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_DETAIL_FRAGMENT)
            }
        }
    }

    private fun settingToolbar() {
        fragmentOrderHistoryReservDetailBinding.apply {
            toolbarReservDetail.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_DETAIL_FRAGMENT)
                }
            }

        }
    }
}