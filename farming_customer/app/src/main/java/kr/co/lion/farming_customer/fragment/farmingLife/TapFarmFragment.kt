package kr.co.lion.farming_customer.fragment.farmingLife

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.GridSpaceItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.databinding.FragmentTapFarmBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel

class TapFarmFragment : Fragment() {
    lateinit var fragmentTapFarmBinding: FragmentTapFarmBinding
    lateinit var mainActivity: MainActivity

    lateinit var deco: MaterialDividerItemDecoration
    lateinit var deco2: GridSpaceItemDecoration
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTapFarmBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_farm, container, false)
        mainActivity = activity as MainActivity

        deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
        deco2 = GridSpaceItemDecoration(2,40,-10,-10,-10)
        settingRecyclerView()

        return fragmentTapFarmBinding.root
    }

    private fun settingRecyclerView() {
        fragmentTapFarmBinding.apply {
            recyclerViewTapFarm.apply {
                removeItemDecoration(deco)
                if(itemDecorationCount == 0){
                    addItemDecoration(deco2)
                }
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
                    ViewGroup.LayoutParams.MATCH_PARENT,
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
                    isLike.value = false
                }
            }
            holder.rowGridItemBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                intent.putExtra("fragmentName", FarmingLifeFragmnetName.FARMING_LIFE_FARM_DETAIL_FARMGNET)
                startActivity(intent)
            }
            // 하트
            holder.rowGridItemBinding.apply {
                constraintLikeCancel.setOnClickListener {
                    if(rowGridItemViewModel!!.isLike.value!!){
                        rowGridItemViewModel!!.isLike.value = false
                        imageViewHeart.setImageResource(R.drawable.heart_02)
                    }else{
                        rowGridItemViewModel!!.isLike.value = true
                        imageViewHeart.setImageResource(R.drawable.heart_01)
                    }
                }
            }
        }
    }

}