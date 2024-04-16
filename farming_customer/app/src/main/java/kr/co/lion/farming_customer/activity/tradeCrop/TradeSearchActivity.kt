package kr.co.lion.farming_customer.activity.tradeCrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.ActivityTradeSearchBinding
import kr.co.lion.farming_customer.databinding.RowRelatedCropBinding
import kr.co.lion.farming_customer.model.ProductCard
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeSearchViewModel

class TradeSearchActivity : AppCompatActivity() {

    lateinit var binding: ActivityTradeSearchBinding

    lateinit var tradeSearchViewModel: TradeSearchViewModel

    private var dropDownButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTradeSearchBinding.inflate(layoutInflater)
        tradeSearchViewModel = TradeSearchViewModel()
        binding.tradeSearchViewModel = tradeSearchViewModel
        binding.lifecycleOwner = this

        settingDropDown()
        setRecyclerView()

        setContentView(binding.root)
    }

    // 드롭다운 설정
    private fun settingDropDown() {
        binding.apply {
            // 드롭다운 설정
            val typeList = listOf("별점순", "찜순", "금액 높은순", "금액 낮은순")
            val typeArrayAdapter = ArrayAdapter(this@TradeSearchActivity, R.layout.item_spinner_search_sort, typeList)
            textViewTradeSearchSort.setAdapter(typeArrayAdapter)
        }
    }

    private fun setRecyclerView(){
        val crops = listOf(
            ProductCard(R.drawable.farming_mark, "사과", "10,000원", 999, 1.0),
            ProductCard(R.drawable.farming_mark, "배", "10,000원", 999, 2.0),
            ProductCard(R.drawable.farming_mark, "감자", "10,000원", 999, 3.0),
            ProductCard(R.drawable.farming_mark, "바나나", "10,000원", 999, 4.0),
            ProductCard(R.drawable.farming_mark, "사과", "10,000원", 999, 5.0),
        )

        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewTradeSearch.layoutManager = layoutManager
        binding.recyclerViewTradeSearch.adapter = CropAdapter(crops)
    }


    inner class CropAdapter(private val crops: List<ProductCard>): RecyclerView.Adapter<CropAdapter.CropViewHolder>(){
        inner class CropViewHolder(private val binding: RowRelatedCropBinding): RecyclerView.ViewHolder(binding.root) {
            fun binding(crop: ProductCard) {
                binding.apply {
                    textViewLikeCropName.text = crop.name
                    textViewLikeCropPrice.text = crop.price
                    ratingBarLikeCrop.rating = crop.ratings.toFloat()
                    imageViewLikeCrop.setImageResource(crop.imageResourceId)
                    textViewLikeCropCnt.text = if (crop.likes > 999) "999+"
                    else crop.likes.toString()
                }

                binding.apply {
                    imageViewHeartCrop.setOnClickListener {
                        crop.isLiked = !crop.isLiked // 좋아요 선택
                        if (crop.isLiked) {
                            crop.likes++ // 좋아요 상태면 좋아요 수 증가, 텍스트 흰색
                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(this@TradeSearchActivity, R.color.white))
                        } else {
                            crop.likes-- // 좋아요 취소면 좋아요 수 감소, 텍스트 갈색
                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(this@TradeSearchActivity, R.color.brown_01))
                        }
                        // 좋아요 수가 1000 이상이면 999+로 표기
                        binding.textViewLikeCropCnt.text = if (crop.likes >= 1000) "999+"
                        else "${crop.likes}"
                        binding.imageViewHeartCrop.setImageResource(
                            if (crop.isLiked) R.drawable.heart_01 else R.drawable.heart_02
                        )
                    }
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowRelatedCropBinding.inflate(inflater, parent, false)
            return CropViewHolder(binding)
        }


        override fun getItemCount(): Int = crops.size


        override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
            holder.binding(crops[position])
        }
    }
}