package kr.co.lion.farming_customer.activity.tradeCrop

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.databinding.ActivityTradeSearchBinding
import kr.co.lion.farming_customer.databinding.RowRelatedCropBinding
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeSearchViewModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeViewModel

class TradeSearchActivity : AppCompatActivity() {

    lateinit var binding: ActivityTradeSearchBinding

    lateinit var tradeSearchViewModel: TradeSearchViewModel

    private var dropDownButtonClicked = false

    // 드롭다운 조건에 맞는 농산품 데이터를 담을 리스트
    var cropDropDownAllList:MutableList<CropModel>? = null

    // 드롭다운 정렬
    var dropDownSort = ""

    // 검색 화면의 RecyclerView 구성을 위한 리스트
    var searchList = mutableListOf<CropModel>()

    // 검색어를 받을 프로퍼티
    var keyword:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTradeSearchBinding.inflate(layoutInflater)
        tradeSearchViewModel = TradeSearchViewModel()
        binding.tradeSearchViewModel = tradeSearchViewModel
        binding.lifecycleOwner = this

        // 처음 실행시 검색어에 커서와 함께 키보드가 올라온다.
        Tools.showSoftInput(this@TradeSearchActivity, binding.textFieldTradeSearchCrop)

        // 기본세팅이 별점순이기 때문에 데이터를 별점순에 맞게 가져온다.
        gettingCropData(binding.textViewTradeSearchSort.hint.toString())

        settingDropDown()
        setBack()
        settingSearchView()
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

                // 검색어가 있을 때 리사이클러뷰 재갱신
                if(keyword != null){
                    gettingCropData(dropDownSort)
                }
                else{
                    // dropDownSort 텍스트에 따라 농산품 데이터를 분기하여 가져온다.
                    gettingCropData(dropDownSort)
                }
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

    // 검색 설정
    fun settingSearchView(){
        binding.apply {
            // 키보드로 검색어를 작성하고 엔터키를 누르면 동작하는 리스너
            textFieldTradeSearchCrop.setOnEditorActionListener { v, actionId, event ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    gettingSearchData()
                    Tools.hideSoftInput(this@TradeSearchActivity)
                }
                false
            }
        }
    }

    // 검색 결과의 데이터를 가져와 검색 화면 RecyclerView를 갱신한다.
    fun gettingSearchData(){
        // 검색어에 작성한 입력을 추출하여 내용을 가져온다.
        keyword = binding.textFieldTradeSearchCrop.text.toString()

        // 검색 리스트를 비워준다.
        searchList.clear()

        // 현재 드롭다운에 맞는 농산품 데이터들을 가져온다.
        val tempList = cropDropDownAllList

        // 검색어란이 비어있지 않을 때 searchList에 데이터를 추가한다.
        if(keyword!!.trim() != ""){
            tempList?.forEach {
                if(it.crop_title.contains(keyword!!)){
                    searchList.add(it)
                }
            }
            setRecyclerView()
        }
    }

    // 드롭다운 조건에 맞게 농산품 데이터를 가져온다.
    fun gettingCropData(dropDownSort:String) {
        cropDropDownAllList?.clear()
        CoroutineScope(Dispatchers.Main).launch {
            // 처음 activity가 실행이 되면 gettingCropData가 호출되어 cropDropDownAllList에 별점순에 맞게 데이터를 가져온다.
            // 데이터를 가져오고 난 후에 gettingSearchData를 실행하여도 검색어가 비워져 있기 때문에 RecyclerView에 보여질 searchList는 빈 리스트라서 아무것도 보여지지 않는다.
            // 검색어를 입력하고 enter를 쳤을 때 settingSearchView가 호출되고  gettingSearchData가 호출된다.
            // 여기서는 드롭다운을 건들지 않았기 때문에 검색어에 맞는 데이터가 별점순으로 보이게 된다.
            // 만약 검색어가 비워져 있고 드롭다운을 변경시 드롭다운 조건에 맞는 리스트는 항상 최신화가 된다.
            // 검색어가 있는상황이고 여기서 드롭다운을 변경하게 되면 settingDropDown 안에 리스너가 호출되고 똑같이 gettingCropData가 호출되어
            // 데이터를 최신화 한 다음 gettingSearchData가 호출되고 keyword에 단어가 있기 때문에
            // 단어에 맞는 데이터를 searchList에 추가하여 recyclerView를 보여지게 한다.
            // 내가 하고도 너무 복잡한거 같아서 적어놓음
            cropDropDownAllList = CropDao.gettingCropAllList(dropDownSort)
            gettingSearchData()

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
        override fun getItemCount(): Int = searchList.size

        override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
            holder.binding.apply {
                tradeViewModel!!.apply {
                    // 좋아요 버튼을 눌렀을 떄 동작하는 리스너
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
                    // textViewLikeCropName.value = cropDropDownAllList?.get(position)?.crop_title
                    // textViewLikeCropPrice.value = cropDropDownAllList?.get(position)?.crop_option_detail?.get(0)?.get("crop_option_price")
                    // textViewLikeCropCnt.value = cropDropDownAllList?.get(position)?.crop_like_cnt.toString()
                    textViewLikeCropName.value = searchList[position].crop_title
                    textViewLikeCropPrice.value = searchList[position].crop_option_detail[0]["crop_option_price"]
                    textViewLikeCropCnt.value = searchList[position].crop_like_cnt.toString()
                }
                // firebase에 저장한 0번째 이미지를 가져온다
                CoroutineScope(Dispatchers.Main).launch {
                    CropDao.gettingCropImage(this@TradeSearchActivity, searchList[position].crop_images[0], holder.binding.imageViewLikeCrop)
                }
                // position에 맞게 별점조정
                // ratingBarLikeCrop.rating = cropDropDownAllList?.get(position)?.crop_rating?.toFloat() ?: 0F
                ratingBarLikeCrop.rating = searchList[position].crop_rating.toFloat()

                // 항목을 눌렀을 때 동작하는 리스너
                root.setOnClickListener {
                    val intent = Intent(this@TradeSearchActivity,TradeDetailActivity::class.java)
                    intent.putExtra("crop_idx", cropDropDownAllList?.get(position)?.crop_idx ?: 0)
                    startActivity(intent)
                }
            }

        }
    }
}