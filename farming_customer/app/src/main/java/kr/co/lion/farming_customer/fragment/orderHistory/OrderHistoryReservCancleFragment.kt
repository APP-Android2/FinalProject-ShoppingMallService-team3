package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYes
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryReservCancleBinding
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryOrderReservCancelViewModel

class OrderHistoryReservCancleFragment : Fragment() {
    lateinit var fragmentOrderHistoryReservCancleBinding: FragmentOrderHistoryReservCancleBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryOrderReservCancelViewModel: OrderHistoryOrderReservCancelViewModel

    var orderModel : OrderModel? = null
    var productType : Int? = null
    var farmModel : FarmModel? = null
    var activityModel : ActivityModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryReservCancleBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_reserv_cancle, container, false)
        orderHistoryOrderReservCancelViewModel = OrderHistoryOrderReservCancelViewModel()
        fragmentOrderHistoryReservCancleBinding.orderHistoryOrderReservCancelViewModel = orderHistoryOrderReservCancelViewModel
        fragmentOrderHistoryReservCancleBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingInitData()
        settingToolbar()


        return fragmentOrderHistoryReservCancleBinding.root
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
            settingEvent()
        }
    }

    private fun settingEvent() {
        fragmentOrderHistoryReservCancleBinding.buttonCancle.setOnClickListener {
            val reason = orderHistoryOrderReservCancelViewModel!!.textViewReservCancel_reason.value
            val reason_detail = orderHistoryOrderReservCancelViewModel!!.textViewReservCancel_reasonDetail.value

            if(reason.isNullOrBlank()){
                // 신청 사유 선택을 하지 않았을 때
                val dialog = DialogYes("입력 오류", "신청사유를 선택해주세요.", null, orderHistoryActivity)
                dialog.show(this@OrderHistoryReservCancleFragment.parentFragmentManager, "DialogYes")
            }else{
                CoroutineScope(Dispatchers.Main).launch {
                    val map = mutableMapOf<String, Any>()
                    map["cancle_reason"] = reason
                    if(reason_detail != null){
                        map["cancle_reason_detail"] = reason_detail
                    }
                    OrderDao.cancleReserv(orderModel!!.order_idx, map)
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_CANCLE_FRAGMENT)
                }
            }
        }
    }

    private fun settingImage() {
        fragmentOrderHistoryReservCancleBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                if(productType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                    FarmDao.gettingFarmImage(orderHistoryActivity, farmModel!!.farm_images[0], imageViewReservCancleProductImage)
                }else{
                    ActivityDao.gettingActivityImage(orderHistoryActivity, activityModel!!.activity_images[0], imageViewReservCancleProductImage)
                }
            }
        }
    }

    private fun settingData() {
        fragmentOrderHistoryReservCancleBinding.apply {
            // 드롭다운 설정
            val reseaonArray = resources.getStringArray(R.array.reserv_cancel_reason)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_reserv_cancle, reseaonArray)
            textViewReservCancelReason.setAdapter(arrayAdapter)
            // 주문 데이터 설정
            orderHistoryOrderReservCancelViewModel!!.apply {
                if(productType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                    // 주말농장
                    textViewReservCancel_productName.value = farmModel!!.farm_title
                    textViewReservCancle_option.value = "이용기간\n${farmModel!!.farm_use_date_start} - ${farmModel!!.farm_use_date_end}"
                    textViewReservCancle_price.value = "${orderModel!!.order_total_price} / 1구획"
                }else{
                    // 체험활동
                    textViewReservCancel_productName.value = activityModel!!.activity_title
                    if(orderModel!!.order_option_detail.size > 1){
                        textViewReservCancle_option.value = "체험 날짜 : ${orderModel!!.order_option_detail[0]["option_time"]} ~"
                    }else{
                        textViewReservCancle_option.value = "체험 날짜 : ${orderModel!!.order_option_detail[0]["option_time"]}"
                    }
                    textViewReservCancle_price.value = orderModel!!.order_total_price
                }
            }
        }
    }

    private fun settingToolbar() {
        fragmentOrderHistoryReservCancleBinding.apply {
            toolbarOrderHistoryReservCancle.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_CANCLE_FRAGMENT)
                }
            }
        }
    }
}