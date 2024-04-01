package kr.co.lion.farming_customer.fragment.orderHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.databinding.FragmentTapReservDoneBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryCropLabeledBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryFarmBinding
import kr.co.lion.farming_customer.viewmodel.orderHistory.RowOrderHistoryFarmViewModel

class TapReservDoneFragment : Fragment() {
    lateinit var fragmentTapReservDoneBinding: FragmentTapReservDoneBinding
    lateinit var orderHistoryActivity : OrderHistoryActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTapReservDoneBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_reserv_done, container, false)
        orderHistoryActivity = activity as OrderHistoryActivity

        settingRecyclerView()

        return fragmentTapReservDoneBinding.root
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
            return 10
        }

        override fun onBindViewHolder(holder: ReservDoneViewHolder, position: Int) {
            holder.rowOrderHistoryFarmBinding.apply {
                rowOrderHistoryFarmViewModel!!.apply {
                    textViewRowOrderHistoryFarm_orderDate.value = "2024-03-30"
                    textViewRowOrderHistoryFarm_productName.value = "파밍이네 주말농장"
                    textViewRowOrderHistoryFarm_productOption.value = "이용기간\n2024.03.01 ~ 2024.11.30"
                    textViewRowOrderHistoryFarm_price.value = "10,000원 / 1구획"
                }
            }
        }
    }
}