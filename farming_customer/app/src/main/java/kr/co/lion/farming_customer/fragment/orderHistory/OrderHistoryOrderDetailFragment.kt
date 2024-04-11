package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryOrderDetailBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryOrderDetailBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryOrderDetailOptionBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryOrderDetailViewModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryOrderDetailOptionViewModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryOrderDetailViewModel

class OrderHistoryOrderDetailFragment : Fragment() {
    lateinit var fragmentOrderHistoryOrderDetailBinding: FragmentOrderHistoryOrderDetailBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryOrderDetailViewModel: OrderHistoryOrderDetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentOrderHistoryOrderDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_order_detail, container, false)
        orderHistoryOrderDetailViewModel = OrderHistoryOrderDetailViewModel()
        fragmentOrderHistoryOrderDetailBinding.orderHistoryOrderDetailViewModel = orderHistoryOrderDetailViewModel
        fragmentOrderHistoryOrderDetailBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingToolbar()
        settingEvent()
        settingData()
        settingRecyclerView()

        return fragmentOrderHistoryOrderDetailBinding.root
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
                textviewOrderDetail_orderDate.value = "2024.04.04"
                textviewOrderDetail_orderNum.value = "주문번호 : 12341234"

                textviewOrderDetail_receiverName.value = "김파밍"
                textviewOrderDetail_address.value = "[12312] 서울특별시 파밍구 파밍동 12345"
                textviewOrderDetail_receivePhoneNum.value = "010-1234-1234"
                textviewOrderDetail_request.value= "부재 시 경비실에 맡겨주세요."

                textViewOrderDetail_reservName.value = "김파밍"
                textViewOrderDetail_phoneNum.value = "010-12341234"

                textViewOrderDetail_productPrice.value = "10,000원"
                textViewOrderDetail_deliveryPrice.value = "12,000원(3건)"
                textViewOrderDetail_discountPrice.value = "-0P"
                textViewOrderDetail_totalPrice.value = "140,000원"
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
            return 3
        }

        override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
            holder.rowOrderHistoryOrderDetailBinding.apply {
                rowOrderHistoryOrderDetailViewModel!!.apply {
                    textviewOrderHistoryOrderDetail_productName.value = "파밍이네 감자"
                    textviewOrderHistoryOrderDetail_option.value = "못난이 감자 5kg ..외 3개"
                    textviewOrderHistoryOrderDetail_price.value = "10,000원"
                }
                recyclerViewOptionDetail.apply {
                    this.adapter = OrderDetailOptionRecyclerViewAdapter()
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
        }
    }

    inner class OrderDetailOptionRecyclerViewAdapter : RecyclerView.Adapter<OrderDetailOptionRecyclerViewAdapter.OrderDetailOptionViewHolder>(){
        inner class OrderDetailOptionViewHolder(rowOrderHistoryOrderDetailOptionBinding: RowOrderHistoryOrderDetailOptionBinding) : RecyclerView.ViewHolder(rowOrderHistoryOrderDetailOptionBinding.root){
            val rowOrderHistoryOrderDetailOptionBinding : RowOrderHistoryOrderDetailOptionBinding

            init{
                this.rowOrderHistoryOrderDetailOptionBinding = rowOrderHistoryOrderDetailOptionBinding

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

            val orderDetailOptionViewHolder = OrderDetailOptionViewHolder(rowOrderHistoryOrderDetailOptionBinding)
            return orderDetailOptionViewHolder
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: OrderDetailOptionViewHolder, position: Int) {
            holder.rowOrderHistoryOrderDetailOptionBinding.apply {
                rowOrderHistoryDetailOptionViewModel!!.apply {
                    textviewRowOrderDetail_optionName.value = "파밍 감자 10kg 1개"
                    textviewRowOrderDetail_price.value = "5,000원"
                }
            }
        }
    }

}