package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentTapReservCancleBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryFarmCancledBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryFarmViewModel

class TapReservCancleFragment : Fragment() {
    lateinit var fragmentTapReservCancleBinding: FragmentTapReservCancleBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapReservCancleBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_reserv_cancle, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingRecyclerView()

        return fragmentTapReservCancleBinding.root
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
            return 10
        }

        override fun onBindViewHolder(holder: ReservCancleViewHolder, position: Int) {
            holder.rowOrderHistoryFarmCancledBinding.apply {
                rowOrderHistoryFarmViewModel!!.apply {
                    textViewRowOrderHistoryFarm_orderDate.value = "2024-03-30"
                    textViewRowOrderHistoryFarm_productName.value = " 파밍이네 주말농장"
                    textViewRowOrderHistoryFarm_productOption.value = "이용기간\n2024.03.01 ~ 2024.11.30"
                    textViewRowOrderHistoryFarm_price.value = "10,000원 / 1구획"
                }
            }
            holder.rowOrderHistoryFarmCancledBinding.root.setOnClickListener {
                orderHistoryActivity.replaceFragment(OrderHistoryFragmentName.ORDER_HISTORY_RESERV_DETAIL_FRAGMENT, true, true, null)
            }
        }
    }
}