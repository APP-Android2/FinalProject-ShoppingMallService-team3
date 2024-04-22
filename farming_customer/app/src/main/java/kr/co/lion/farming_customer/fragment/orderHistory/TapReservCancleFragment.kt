package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
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
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentTapReservCancleBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryFarmCancledBinding
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryFarmViewModel

class TapReservCancleFragment : Fragment() {
    lateinit var fragmentTapReservCancleBinding: FragmentTapReservCancleBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    var orderType : Int? = null
    var orderList = mutableListOf<OrderModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapReservCancleBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_reserv_cancle, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingInitData()

        return fragmentTapReservCancleBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            orderType = arguments?.getInt("orderType")
            if(orderType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                orderList = OrderDao.gettingOrderListFarm(OrderLabelType.ORDER_LABEL_TYPE_RESERV_CANCLE)
            }else{
                orderList = OrderDao.gettingOrderListActivity(OrderLabelType.ORDER_LABEL_TYPE_RESERV_CANCLE)
            }
            settingRecyclerView()
        }
    }

    private fun settingRecyclerView() {
        fragmentTapReservCancleBinding.apply {
            recyclerViewReservCancle.apply {
                adapter = ReservCancleRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(orderHistoryActivity)
            }
        }
    }

    inner class ReservCancleRecyclerViewAdapter : RecyclerView.Adapter<ReservCancleRecyclerViewAdapter.ReservCancleViewHolder>(){
        inner class ReservCancleViewHolder(rowOrderHistoryFarmCancledBinding: RowOrderHistoryFarmCancledBinding) : RecyclerView.ViewHolder(rowOrderHistoryFarmCancledBinding.root){
            val rowOrderHistoryFarmCancledBinding : RowOrderHistoryFarmCancledBinding
            init{
                this.rowOrderHistoryFarmCancledBinding = rowOrderHistoryFarmCancledBinding

                rowOrderHistoryFarmCancledBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservCancleViewHolder {
            val rowOrderHistoryFarmCancledBinding = DataBindingUtil.inflate<RowOrderHistoryFarmCancledBinding>(layoutInflater, R.layout.row_order_history_farm_cancled, parent, false)
            val rowOrderHistoryFarmViewModel = RowOrderHistoryFarmViewModel()
            rowOrderHistoryFarmCancledBinding.rowOrderHistoryFarmViewModel = rowOrderHistoryFarmViewModel
            rowOrderHistoryFarmCancledBinding.lifecycleOwner = this@TapReservCancleFragment

            val reservCancelViewHolder = ReservCancleViewHolder(rowOrderHistoryFarmCancledBinding)
            return reservCancelViewHolder
        }

        override fun getItemCount(): Int {
            return orderList.size
        }

        override fun onBindViewHolder(holder: ReservCancleViewHolder, position: Int) {
            holder.rowOrderHistoryFarmCancledBinding.apply {
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
                        ActivityDao.gettingActivityImage(orderHistoryActivity, activityModel!!.activity_images[0], imageViewOrderHistoryFarmProductImage)
                    }
                }
            }
            // 아이템 클릭 리스너
            holder.rowOrderHistoryFarmCancledBinding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("orderIdx", orderList[position].order_idx)
                bundle.putInt("orderProductIdx", orderList[position].order_product_idx)
                bundle.putInt("orderProductType", orderList[position].order_product_type)
                orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_DETAIL_FRAGMENT, true, true, bundle)
            }
        }
    }
}