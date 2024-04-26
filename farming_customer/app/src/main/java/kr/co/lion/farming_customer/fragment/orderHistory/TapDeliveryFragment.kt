package kr.co.lion.farming_customer.fragment.orderHistory

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.OrderLabelType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentTapDeliveryBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropBinding
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryCropViewModel

class TapDeliveryFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentTapDeliveryBinding: FragmentTapDeliveryBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    var orderList : MutableList<OrderModel>? = null

    var orderCheck_pos : Int = -1

    var userModel : UserModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapDeliveryBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_delivery, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingUserData()


        return fragmentTapDeliveryBinding.root
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
            orderList = OrderDao.gettingOrderListCrop(OrderLabelType.ORDER_LABEL_TYPE_DELIVERY, userModel!!.user_idx)
            settingRecyclerView()
        }
    }

    private fun settingRecyclerView() {
        fragmentTapDeliveryBinding.apply {
            recyclerViewDelivery.apply {
                adapter = DeliveryRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(orderHistoryActivity)
            }
        }
    }

    inner class DeliveryRecyclerViewAdapter : RecyclerView.Adapter<DeliveryRecyclerViewAdapter.DeliveryViewHolder>(){
        inner class DeliveryViewHolder(rowOrderHistoryCropBinding : RowOrderHistoryCropBinding) : RecyclerView.ViewHolder(rowOrderHistoryCropBinding.root) {
            val rowOrderHistoryCropBinding: RowOrderHistoryCropBinding

            init {
                this.rowOrderHistoryCropBinding = rowOrderHistoryCropBinding

                rowOrderHistoryCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
            val rowOrderHistoryCropBinding = DataBindingUtil.inflate<RowOrderHistoryCropBinding>(layoutInflater, R.layout.row_order_history_crop, parent, false)
            val rowOrderHistoryCropViewModel = RowOrderHistoryCropViewModel()
            rowOrderHistoryCropBinding.rowOrderHistoryCropViewModel = rowOrderHistoryCropViewModel
            rowOrderHistoryCropBinding.lifecycleOwner = this@TapDeliveryFragment

            val deliveryViewHolder = DeliveryViewHolder(rowOrderHistoryCropBinding)
            return deliveryViewHolder
        }

        override fun getItemCount(): Int {
            return orderList!!.size
        }

        override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
            holder.rowOrderHistoryCropBinding.apply {
                rowOrderHistoryCropViewModel!!.apply {
                    textViewRowOrderHistoryCrop_orderDate.value = orderList!![position].order_reg_date
                    textViewRowOrderHistoryCrop_productName.value = "파밍이네 감자" // 상품 데이터 가져와야함
                    textViewRowOrderHistoryCrop_productOption.value = "${orderList!![position].order_option_detail[0]["option_name"]}...외 ${orderList!![position].order_option_detail.size-1}개"
                    textViewRowOrderHistoryCrop_price.value = orderList!![position].order_total_price
                    buttonRowOrderHistoryCrop_productInside.value = "수취확인"
                }
            }
            // 아이템 클릭 리스터
            holder.rowOrderHistoryCropBinding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("orderNum", orderList!![position].order_num)
                orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_ORDER_DETAIL_FRAGMENT, true, true, bundle)
            }
            // 수취확인
            holder.rowOrderHistoryCropBinding.buttonRowOrderHistoryCropProductInside.setOnClickListener {
                orderCheck_pos = position
                val dialog = DialogYesNo(
                    this@TapDeliveryFragment,
                    "수취 확인",
                    "상품을 무사히 받으셨나요?",
                    orderHistoryActivity,
                    yes_text = "수취확인"
                )
                dialog.show(orderHistoryActivity.supportFragmentManager, "DialogYesNo")
            }

            CoroutineScope(Dispatchers.Main).launch {
                val cropData = CropDao.selectCropData(orderList!![position].order_product_idx)
                CropDao.gettingCropImage(requireContext(), cropData!!.crop_images[0], holder.rowOrderHistoryCropBinding.imageViewOrderHistoryCropProductImage)
            }
        }
    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity) {
        // 상태 라벨 배송완료로 바꾸기
        CoroutineScope(Dispatchers.Main).launch {
            OrderDao.updateOrderState(orderList!![orderCheck_pos].order_idx, OrderLabelType.ORDER_LABEL_TYPE_DELIVERY_DONE)
            orderList!!.removeAt(orderCheck_pos)
            fragmentTapDeliveryBinding.recyclerViewDelivery.adapter?.notifyItemRemoved(orderCheck_pos)
        }
    }
}