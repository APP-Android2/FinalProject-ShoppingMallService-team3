package kr.co.lion.farming_customer.activity.tradeCrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.databinding.ActivityTradeSearchBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.databinding.RowRelatedCropBinding
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.ProductCard
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeSearchViewModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeViewModel

class TradeSearchActivity : AppCompatActivity() {

    lateinit var binding: ActivityTradeSearchBinding

    lateinit var tradeSearchViewModel: TradeSearchViewModel

    private var dropDownButtonClicked = false

    // 농산품 데이터를 담을 리스트
    var cropAllList:List<CropModel>? = null

    // 드롭다운 정렬
    var dropDownSort = "별점순"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTradeSearchBinding.inflate(layoutInflater)
        tradeSearchViewModel = TradeSearchViewModel()
        binding.tradeSearchViewModel = tradeSearchViewModel
        binding.lifecycleOwner = this

        //setButton()
        gettingCropData()

        setContentView(binding.root)
    }

    // 농산품 전체 데이터를 가져온다.
    private fun gettingCropData(){
        CoroutineScope(Dispatchers.Main).launch {
            cropAllList = CropDao.gettingCropAllList()
            setRecyclerView()
        }
    }


    private fun setRecyclerView(){
        binding.recyclerViewTradeSearch.apply {
            adapter = CropAdapter()
            layoutManager = GridLayoutManager(this@TradeSearchActivity, 2)
        }
    }


    inner class CropAdapter: RecyclerView.Adapter<CropAdapter.CropViewHolder>(){
        inner class CropViewHolder(binding: RowRelatedCropBinding): RecyclerView.ViewHolder(binding.root) {
            val binding : RowRelatedCropBinding

            init {
                this.binding = binding
                binding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {

            val binding = DataBindingUtil.inflate<RowRelatedCropBinding>(layoutInflater, R.layout.row_related_crop, parent, false)
            val tradeViewModel = TradeViewModel()
            binding.tradeViewModel = tradeViewModel
            binding.lifecycleOwner = this@TradeSearchActivity

            val cropViewHolder = CropViewHolder(binding)
            return cropViewHolder
        }


        override fun getItemCount(): Int = cropAllList?.size?: 0


        override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
            holder.binding.apply {
                tradeViewModel!!.apply {
                    // 좋아요 버튼 클릭 이벤트
                    isLike.value = false
                    constraintLikeCropCancel.setOnClickListener {
                        if(isLike.value!!){
                            isLike.value = false
                            imageViewHeartCrop.setImageResource(R.drawable.heart_02)
                            holder.binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(this@TradeSearchActivity, R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageViewHeartCrop.setImageResource(R.drawable.heart_01)
                            holder.binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(this@TradeSearchActivity, R.color.white))
                        }
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    CropDao.gettingCropImage(this@TradeSearchActivity,
                        cropAllList?.get(position)?.crop_images?.get(0) ?: "", holder.binding.imageViewLikeCrop)
                }
                holder.binding.ratingBarLikeCrop.rating = cropAllList?.get(position)?.crop_rating?.toFloat() ?: 0F
            }

        }
    }
}