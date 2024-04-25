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
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryOrderDetailBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryOrderDetailBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryOrderDetailOptionBinding
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryOrderDetailViewModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryOrderDetailOptionViewModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryOrderDetailViewModel

class OrderHistoryOrderDetailFragment : Fragment() {
    lateinit var fragmentOrderHistoryOrderDetailBinding: FragmentOrderHistoryOrderDetailBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryOrderDetailViewModel: OrderHistoryOrderDetailViewModel

    var orderNum : String? = null
    var orderList : MutableList<OrderModel>? = null

    var userModel : UserModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentOrderHistoryOrderDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_order_detail, container, false)
        orderHistoryOrderDetailViewModel = OrderHistoryOrderDetailViewModel()
        fragmentOrderHistoryOrderDetailBinding.orderHistoryOrderDetailViewModel = orderHistoryOrderDetailViewModel
        fragmentOrderHistoryOrderDetailBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingUserData()
        settingToolbar()
        settingEvent()



        return fragmentOrderHistoryOrderDetailBinding.root
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
        orderNum = arguments?.getString("orderNum")
        // 주문번호로 주문 목록 가져오기
        CoroutineScope(Dispatchers.Main).launch {
            orderList = OrderDao.gettingOrderListByOrderNum(orderNum!!)
            settingData()
            settingRecyclerView()
        }

    }

    private fun settingRecyclerView() {
        fragmentOrderHistoryOrderDetailBinding.apply {
            recyclerViewOrderDetail.apply {
                adapter = OrderDetailRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(orderHistoryActivity)
            }
        }
    }

    private fun settingData() {
        fragmentOrderHistoryOrderDetailBinding.apply {
            orderHistoryOrderDetailViewModel!!.apply {
                textviewOrderDetail_orderDate.value = orderList!![0].order_reg_date
                textviewOrderDetail_orderNum.value = "주문번호 : ${orderList!![0].order_num}"

                textviewOrderDetail_receiverName.value = orderList!![0].order_delivery_address["receiver"]
                textviewOrderDetail_address.value = orderList!![0].order_delivery_address["address"]
                textviewOrderDetail_receivePhoneNum.value = orderList!![0].order_delivery_address["phone"]
                textviewOrderDetail_request.value= orderList!![0].order_delivery_address["request"]

                textViewOrderDetail_reservName.value = userModel!!.user_name
                textViewOrderDetail_phoneNum.value = userModel!!.user_phone

                textViewOrderDetail_productPrice.value = "10,000원" // 결제 데이터 가져와야함
                textViewOrderDetail_deliveryPrice.value = "12,000원(3건)" // 결제 데이터 가져와야함
                textViewOrderDetail_discountPrice.value = "-0P" // 결제 데이터 가져와야함
                textViewOrderDetail_totalPrice.value = "140,000원" // 결제 데이터 가져와야함
            }
        }

    }

    private fun settingEvent() {
        fragmentOrderHistoryOrderDetailBinding.apply {
            buttonOrderDetailCheck.setOnClickListener {
                orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_ORDER_DETAIL_FRAGMENT)
            }
        }
    }

    private fun settingToolbar() {
        fragmentOrderHistoryOrderDetailBinding.apply {
            toolbarReservDetail.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_ORDER_DETAIL_FRAGMENT)
                }
            }
        }
    }

    inner class OrderDetailRecyclerViewAdapter : RecyclerView.Adapter<OrderDetailRecyclerViewAdapter.OrderDetailViewHolder>(){
        inner class OrderDetailViewHolder(rowOrderHistoryOrderDetailBinding: RowOrderHistoryOrderDetailBinding) : RecyclerView.ViewHolder(rowOrderHistoryOrderDetailBinding.root){
            val rowOrderHistoryOrderDetailBinding : RowOrderHistoryOrderDetailBinding

            init {
                this.rowOrderHistoryOrderDetailBinding = rowOrderHistoryOrderDetailBinding

                rowOrderHistoryOrderDetailBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
            val rowOrderHistoryOrderDetailBinding = DataBindingUtil.inflate<RowOrderHistoryOrderDetailBinding>(layoutInflater, R.layout.row_order_history_order_detail, parent, false)
            val rowOrderHistoryOrderDetailViewModel = RowOrderHistoryOrderDetailViewModel()
            rowOrderHistoryOrderDetailBinding.rowOrderHistoryOrderDetailViewModel = rowOrderHistoryOrderDetailViewModel
            rowOrderHistoryOrderDetailBinding.lifecycleOwner = this@OrderHistoryOrderDetailFragment

            val orderDetailViewHolder = OrderDetailViewHolder(rowOrderHistoryOrderDetailBinding)
            return orderDetailViewHolder
        }

        override fun getItemCount(): Int {
            return orderList!!.size
        }

        override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
            CoroutineScope(Dispatchers.Main).launch {
                var productModel = CropDao.selectCropData(orderList!![position].order_product_idx)

                holder.rowOrderHistoryOrderDetailBinding.apply {
                    rowOrderHistoryOrderDetailViewModel!!.apply {
                        textviewOrderHistoryOrderDetail_productName.value = productModel!!.crop_title
                        textviewOrderHistoryOrderDetail_option.value = "${orderList!![position].order_option_detail[0]["option_name"]}...외 ${orderList!![position].order_option_detail.size-1}개"
                        textviewOrderHistoryOrderDetail_price.value = orderList!![position].order_total_price
                    }
                    recyclerViewOptionDetail.apply {
                        this.adapter = OrderDetailOptionRecyclerViewAdapter(orderList!![position].order_option_detail)
                        this.layoutManager = LinearLayoutManager(orderHistoryActivity)
                    }
                    imageButton2.setOnClickListener {
                        if(divider10.visibility != View.VISIBLE){
                            imageButton2.setImageResource(R.drawable.up)
                            divider10.visibility = View.VISIBLE
                            recyclerViewOptionDetail.visibility = View.VISIBLE
                        }else{
                            imageButton2.setImageResource(R.drawable.down)
                            divider10.visibility = View.GONE
                            recyclerViewOptionDetail.visibility = View.GONE

                        }

                    }
                }
                CropDao.gettingCropImage(requireContext(), productModel!!.crop_images[0], holder.rowOrderHistoryOrderDetailBinding.imageViewOrderHistoryOrderDetailProductImage)
            }
        }
    }

    inner class OrderDetailOptionRecyclerViewAdapter(orderOptionDetail: MutableList<MutableMap<String, String>>) : RecyclerView.Adapter<OrderDetailOptionRecyclerViewAdapter.OrderDetailOptionViewHolder>(){
        val orderOptionDetail = orderOptionDetail
        inner class OrderDetailOptionViewHolder(rowOrderHistoryOrderDetailOptionBinding: RowOrderHistoryOrderDetailOptionBinding, orderOptionDetail: MutableList<MutableMap<String, String>>) : RecyclerView.ViewHolder(rowOrderHistoryOrderDetailOptionBinding.root){
            val rowOrderHistoryOrderDetailOptionBinding : RowOrderHistoryOrderDetailOptionBinding
            val orderOptionDetail : MutableList<MutableMap<String, String>>
            init{
                this.rowOrderHistoryOrderDetailOptionBinding = rowOrderHistoryOrderDetailOptionBinding
                this.orderOptionDetail = orderOptionDetail

                rowOrderHistoryOrderDetailOptionBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailOptionViewHolder {
            val rowOrderHistoryOrderDetailOptionBinding = DataBindingUtil.inflate<RowOrderHistoryOrderDetailOptionBinding>(layoutInflater, R.layout.row_order_history_order_detail_option, parent, false)
            val rowOrderHistoryOrderDetailOptionViewModel = RowOrderHistoryOrderDetailOptionViewModel()
            rowOrderHistoryOrderDetailOptionBinding.rowOrderHistoryDetailOptionViewModel = rowOrderHistoryOrderDetailOptionViewModel
            rowOrderHistoryOrderDetailOptionBinding.lifecycleOwner = this@OrderHistoryOrderDetailFragment

            val orderDetailOptionViewHolder = OrderDetailOptionViewHolder(rowOrderHistoryOrderDetailOptionBinding, orderOptionDetail)
            return orderDetailOptionViewHolder
        }

        override fun getItemCount(): Int {
            return orderOptionDetail.size
        }

        override fun onBindViewHolder(holder: OrderDetailOptionViewHolder, position: Int) {
            holder.rowOrderHistoryOrderDetailOptionBinding.apply {
                rowOrderHistoryDetailOptionViewModel!!.apply {
                    textviewRowOrderDetail_optionName.value = "${orderOptionDetail[position]["option_name"]} ${orderOptionDetail[position]["option_cnt"]}개"
                    textviewRowOrderDetail_price.value = orderOptionDetail[position]["option_total_price"]
                }
            }
        }
    }

}