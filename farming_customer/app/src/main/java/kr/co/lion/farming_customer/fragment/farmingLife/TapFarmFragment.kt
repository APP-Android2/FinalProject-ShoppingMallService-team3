package kr.co.lion.farming_customer.fragment.farmingLife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTapFarmBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel

class TapFarmFragment : Fragment() {
    lateinit var fragmentTapFarmBinding: FragmentTapFarmBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTapFarmBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_farm, container, false)
        mainActivity = activity as MainActivity

        settingRecyclerView()

        return fragmentTapFarmBinding.root
    }

    private fun settingRecyclerView() {
        fragmentTapFarmBinding.apply {
            recyclerViewTapFarm.apply {
                adapter = TapFarmRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity, 2)
            }
        }
    }

    inner class TapFarmRecyclerViewAdapter : RecyclerView.Adapter<TapFarmRecyclerViewAdapter.TapFarmViewHolder>(){
        inner class TapFarmViewHolder(rowGridItemBinding: RowGridItemBinding) : RecyclerView.ViewHolder(rowGridItemBinding.root){
            val rowGridItemBinding : RowGridItemBinding
            init {
                this.rowGridItemBinding = rowGridItemBinding

                rowGridItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TapFarmViewHolder {
            val rowGridItemBinding = DataBindingUtil.inflate<RowGridItemBinding>(layoutInflater, R.layout.row_grid_item, parent, false)
            val rowGridItemViewModel = RowGridItemViewModel()
            rowGridItemBinding.rowGridItemViewModel = rowGridItemViewModel
            rowGridItemBinding.lifecycleOwner = this@TapFarmFragment

            val TapFarmViewHolder = TapFarmViewHolder(rowGridItemBinding)
            return TapFarmViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: TapFarmViewHolder, position: Int) {
            holder.rowGridItemBinding.apply {
                rowGridItemViewModel!!.apply {
                    textView_likeCnt.value = "999"
                    textView_ItemName.value = "파밍이네 농장\n경기도 파밍시 파밍구"
                    textView_price.value = "10,000원~"
                }
            }
        }
    }

}