package kr.co.lion.farming_customer.activity.tradeCrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

    // 드롭다운 조건에 맞는 농산품 데이터를 담을 리스트
    var cropDropDownAllList:List<CropModel>? = null

    // 드롭다운 정렬
    var dropDownSort = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTradeSearchBinding.inflate(layoutInflater)
        tradeSearchViewModel = TradeSearchViewModel()
        binding.tradeSearchViewModel = tradeSearchViewModel
        binding.lifecycleOwner = this

        gettingCropData(binding.textViewTradeSearchSort.hint.toString())

        settingDropDown()
        setBack()

        setContentView(binding.root)
    }

    // 드롭다운 설정
    private fun settingDropDown() {
        binding.apply {
            // 드롭다운 설정
            val typeList = listOf("별점순", "찜순", "금액 높은순", "금액 낮은순")
            val typeArrayAdapter = ArrayAdapter(this@TradeSearchActivity, R.layout.item_spinner_search_sort, typeList)
            textViewTradeSearchSort.setAdapter(typeArrayAdapter)


            // 드롭다운 메뉴 선택 이벤트 처리
            textViewTradeSearchSort.setOnItemClickListener { _, _, position, _ ->
                val selectedType = typeList[position]

                // 선택된 항목으로 힌트 텍스트 변경
                textViewTradeSearchSort.hint = selectedType
                dropDownSort = selectedType

                // dropDownSort 텍스트에 따라 농산품 데이터를 분기하여 가져온다.
                gettingCropData(dropDownSort)
            }
        }
    }

    // 검색창 back Button 누르면 Activity 종료
    private fun setBack() {
        binding.apply {
            textInputLayout17.setStartIconOnLongClickListener {
                finish()
                true
            }
        }
    }
    // 농산품 전체 데이터를 가져온다.
    fun gettingCropData(dropDownSort:String) {
        CoroutineScope(Dispatchers.Main).launch {
            cropDropDownAllList = CropDao.gettingCropAllList(dropDownSort)
            setRecyclerView()
        }
    }


    fun setRecyclerView() {
        binding.recyclerViewTradeSearch.apply {
            adapter = CropAdapter()
            layoutManager = GridLayoutManager(this@TradeSearchActivity, 2)
        }
    }


    inner class CropAdapter : RecyclerView.Adapter<CropAdapter.CropViewHolder>() {
        inner class CropViewHolder(binding: RowRelatedCropBinding) : RecyclerView.ViewHolder(binding.root) {
            val binding: RowRelatedCropBinding
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
        override fun getItemCount(): Int = cropDropDownAllList?.size ?: 0

        override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
            holder.binding.apply {
                tradeViewModel!!.apply {
                    // 좋아요 버튼 클릭 이벤트
                    isLike.value = false
                    constraintLikeCropCancel.setOnClickListener {
                        if (isLike.value!!) {
                            isLike.value = false
                            imageViewHeartCrop.setImageResource(R.drawable.heart_02)
                            holder.binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(this@TradeSearchActivity, R.color.brown_01))

                        } else {
                            isLike.value = true
                            imageViewHeartCrop.setImageResource(R.drawable.heart_01)
                            holder.binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(this@TradeSearchActivity, R.color.white))
                        }
                    }
                    textViewLikeCropName.value = cropDropDownAllList?.get(position)?.crop_title
                    textViewLikeCropPrice.value = cropDropDownAllList?.get(position)?.crop_option_detail?.get(0)?.get("crop_option_price")
                    textViewLikeCropCnt.value = cropDropDownAllList?.get(position)?.crop_like_cnt.toString()
                }
                CoroutineScope(Dispatchers.Main).launch {
                    CropDao.gettingCropImage(this@TradeSearchActivity, cropDropDownAllList?.get(position)?.crop_images?.get(0) ?: "", holder.binding.imageViewLikeCrop)
                }
                ratingBarLikeCrop.rating = cropDropDownAllList?.get(position)?.crop_rating?.toFloat() ?: 0F
            }

        }
    }
}