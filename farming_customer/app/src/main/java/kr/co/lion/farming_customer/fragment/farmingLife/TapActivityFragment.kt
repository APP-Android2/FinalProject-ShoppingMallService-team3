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
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.databinding.FragmentTapActivityBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel

class TapActivityFragment : Fragment() {
    lateinit var fragmentTapActivityBinding: FragmentTapActivityBinding
    lateinit var mainActivity: MainActivity

    lateinit var deco: MaterialDividerItemDecoration
    lateinit var deco2: GridSpaceItemDecoration

    var activityList : MutableList<ActivityModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTapActivityBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tap_activity, container, false)
        mainActivity = activity as MainActivity

        deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
        deco2 = GridSpaceItemDecoration(2,40,-10,-10,-10)

        settingInitData()

        return fragmentTapActivityBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            activityList = ActivityDao.gettingActivityList()
            settingRecyclerView()
        }
    }

    private fun settingRecyclerView() {
        fragmentTapActivityBinding.apply {
            recyclerViewTapActivity.apply {
                removeItemDecoration(deco)
                if(itemDecorationCount == 0){
                    addItemDecoration(deco2)
                }
                adapter = TapActivityRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity, 2)
            }
        }
    }

    inner class TapActivityRecyclerViewAdapter : RecyclerView.Adapter<TapActivityRecyclerViewAdapter.TapActivityViewHolder>(){
        inner class TapActivityViewHolder(rowGridItemBinding: RowGridItemBinding) : RecyclerView.ViewHolder(rowGridItemBinding.root){
            val rowGridItemBinding : RowGridItemBinding
            init {
                this.rowGridItemBinding = rowGridItemBinding

                rowGridItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TapActivityViewHolder {
            val rowGridItemBinding = DataBindingUtil.inflate<RowGridItemBinding>(layoutInflater, R.layout.row_grid_item, parent, false)
            val rowGridItemViewModel = RowGridItemViewModel()
            rowGridItemBinding.rowGridItemViewModel = rowGridItemViewModel
            rowGridItemBinding.lifecycleOwner = this@TapActivityFragment

            val TapActivityViewHolder = TapActivityViewHolder(rowGridItemBinding)
            return TapActivityViewHolder
        }

        override fun getItemCount(): Int {
            return activityList!!.size
        }

        override fun onBindViewHolder(holder: TapActivityViewHolder, position: Int) {
            holder.rowGridItemBinding.apply {
                rowGridItemViewModel!!.apply {
                    textView_likeCnt.value = activityList!![position].activity_like_cnt.toString()
                    textView_ItemName.value = activityList!![position].activity_title
                    textView_location.value = activityList!![position].activity_address

                    // 옵션 중 가장 최소 가격 표시
                    var minPrice = Int.MAX_VALUE
                    var minPrice_pos = -1
                    activityList!![position].activity_option_detail.forEachIndexed { index, mutableMap ->
                        val priceString = mutableMap["option_price"] as String
                        val numberString = priceString.replace(",", "").replace("원", "")
                        val priceInt = numberString.toInt()

                        if(priceInt < minPrice){
                            minPrice = priceInt
                            minPrice_pos = index
                        }
                    }
                    textView_price.value = "금액 : ${
                        activityList!![position].activity_option_detail[minPrice_pos]["option_price"]
                    } ~"
                    isLike.value = false
                }
                ratingBar.rating = activityList!![position].activity_star
            }
            // 아이템 클릭 리스너
            holder.rowGridItemBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                intent.putExtra("fragmentName", FarmingLifeFragmnetName.FARMING_LIFE_ACTIVITY_DETAIL_FRAGMENT)
                intent.putExtra("idx", activityList!![position].activity_idx)
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
                    ActivityDao.gettingActivityImage(mainActivity, activityList!![position].activity_images[0], imageView)
                }
            }
        }
    }
}