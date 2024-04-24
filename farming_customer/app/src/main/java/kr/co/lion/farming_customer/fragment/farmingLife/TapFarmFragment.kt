package kr.co.lion.farming_customer.fragment.farmingLife

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.GridSpaceItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.databinding.FragmentTapFarmBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel

class TapFarmFragment : Fragment() {
    lateinit var fragmentTapFarmBinding: FragmentTapFarmBinding
    lateinit var mainActivity: MainActivity

    lateinit var deco: MaterialDividerItemDecoration
    lateinit var deco2: GridSpaceItemDecoration

    var farmList : MutableList<FarmModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTapFarmBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_farm, container, false)
        mainActivity = activity as MainActivity

        deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
        deco2 = GridSpaceItemDecoration(2,40,-10,-10,-10)

        settingInitData()

        return fragmentTapFarmBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            farmList = FarmDao.gettingFarmList()
            settingRecyclerView()
        }
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
            return farmList!!.size
        }

        override fun onBindViewHolder(holder: TapFarmViewHolder, position: Int) {
            holder.rowGridItemBinding.apply {
                rowGridItemViewModel!!.apply {
                    textView_likeCnt.value = farmList!![position].farm_like_cnt.toString()
                    textView_ItemName.value = farmList!![position].farm_title
                    textView_location.value = farmList!![position].farm_address
                    textView_price.value = farmList!![position].farm_option_detail["price_area"].toString()
                    isLike.value = false
                }
                ratingBar.rating = farmList!![position].farm_star
            }
            holder.rowGridItemBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                intent.putExtra("fragmentName", FarmingLifeFragmnetName.FARMING_LIFE_FARM_DETAIL_FARMGNET)
                intent.putExtra("idx", farmList!![position].farm_idx)
                startActivity(intent)
            }
            // 하트
            holder.rowGridItemBinding.apply {
                constraintLikeCancel.setOnClickListener {
                    if(rowGridItemViewModel!!.isLike.value!!){
                        rowGridItemViewModel!!.isLike.value = false
                        imageViewHeart.setImageResource(R.drawable.heart_04)
                        textViewLikeCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))
                    }else{
                        rowGridItemViewModel!!.isLike.value = true
                        imageViewHeart.setImageResource(R.drawable.heart_01)
                        textViewLikeCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                }
            }
            // 이미지
            holder.rowGridItemBinding.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    FarmDao.gettingFarmImage(mainActivity, farmList!![position].farm_images[0], imageView)
                }
            }
        }
    }

}