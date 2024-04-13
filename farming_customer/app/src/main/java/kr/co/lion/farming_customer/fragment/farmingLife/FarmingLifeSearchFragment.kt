package kr.co.lion.farming_customer.fragment.farmingLife

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeSearchBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel

class FarmingLifeSearchFragment : Fragment() {
    lateinit var fragmentFarmingLifeSearchBinding: FragmentFarmingLifeSearchBinding
    lateinit var farmingLifeActivity : FarmingLifeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFarmingLifeSearchBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_farming_life_search, container, false)
        farmingLifeActivity = activity as FarmingLifeActivity

        settingRecyclerView()


        return fragmentFarmingLifeSearchBinding.root
    }

    private fun settingRecyclerView() {
        fragmentFarmingLifeSearchBinding.apply {
            recyclerViewSearchResult.apply {
                adapter = SearchRecyclerViewAdapter()
                layoutManager = GridLayoutManager(farmingLifeActivity, 2)
            }
        }
    }

    inner class SearchRecyclerViewAdapter : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>(){
        inner class SearchViewHolder(rowGridItemBinding: RowGridItemBinding) : RecyclerView.ViewHolder(rowGridItemBinding.root){
            val rowGridItemBinding : RowGridItemBinding
            init {
                this.rowGridItemBinding = rowGridItemBinding

                rowGridItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val rowGridItemBinding = DataBindingUtil.inflate<RowGridItemBinding>(layoutInflater, R.layout.row_grid_item, parent, false)
            val rowGridItemViewModel = RowGridItemViewModel()
            rowGridItemBinding.rowGridItemViewModel = rowGridItemViewModel
            rowGridItemBinding.lifecycleOwner = this@FarmingLifeSearchFragment

            val SearchViewHolder = SearchViewHolder(rowGridItemBinding)
            return SearchViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            holder.rowGridItemBinding.apply {
                rowGridItemViewModel!!.apply {
                    textView_likeCnt.value = "999"
                    textView_ItemName.value = "파밍이네 농장"
                    textView_location.value = "경기도 파밍시 파밍구"
                    textView_price.value = "10,000원~"
                    isLike.value = false
                }
            }
            holder.rowGridItemBinding.root.setOnClickListener {
                farmingLifeActivity.replaceFragment(FarmingLifeFragmnetName.FARMING_LIFE_FARM_DETAIL_FARMGNET, true, true, null)
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