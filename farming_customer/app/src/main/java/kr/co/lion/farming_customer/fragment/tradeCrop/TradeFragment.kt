package kr.co.lion.farming_customer.fragment.tradeCrop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.CropStatus
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.tradeCrop.TradeDetailActivity
import kr.co.lion.farming_customer.activity.tradeCrop.TradeSearchActivity
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.databinding.FragmentTradeBinding
import kr.co.lion.farming_customer.databinding.ItemProductBinding
import kr.co.lion.farming_customer.databinding.RowRelatedCropBinding
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeViewModel
import java.text.SimpleDateFormat
import java.util.Date

class TradeFragment : Fragment() {
    lateinit var fragmentTradeBinding: FragmentTradeBinding
    lateinit var mainActivity: MainActivity

    lateinit var tradeViewModel: TradeViewModel

    var cropLikeTop5List:List<CropModel>? = null
    var cropRecentTop5List:List<CropModel>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTradeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trade, container, false)
        tradeViewModel = TradeViewModel()
        fragmentTradeBinding.tradeViewModel = tradeViewModel
        fragmentTradeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        settingInputForm()
        setToolbar()


        return fragmentTradeBinding.root
    }

    override fun onResume() {
        super.onResume()
        settingInputForm()
        fragmentTradeBinding.viewPagerTradeTrend.adapter?.notifyDataSetChanged()
    }

    // 툴바 설정
    private fun setToolbar(){
        fragmentTradeBinding.apply {
            toolbarTrade.apply {

                // 메뉴 등록
                inflateMenu(R.menu.menu_trade)

                // 검색 버튼 눌렀을 때
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuItemTradeSearch -> {
                            val intent = Intent(mainActivity, TradeSearchActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    true
                }
            }
        }
    }

    // 입력 요소 초기화
    fun settingInputForm(){
        tradeViewModel.textViewCropName.value = ""
        tradeViewModel.textViewCropLocation.value = ""
        tradeViewModel.textViewTradePrice.value = ""
        tradeViewModel.textViewTradeLike.value = ""



        CoroutineScope(Dispatchers.Main).launch {

            //uploadCropData()

            // 인기 농작물 데이터 가져오기
            cropLikeTop5List = CropDao.gettingCropLikeTop5List()
            // 최신등록 농작물 데이터 가져오기
            cropRecentTop5List = CropDao.gettingCropRecentTop5List()

            setupViewPager()
            setupViewPager2()
            setupRecyclerView()

        }
    }

    //    firebase에 올리는 메서드
    fun uploadCropData() {
        CoroutineScope(Dispatchers.Main).launch {

            // 농작물 시퀀스 값을 가져온다.
            val cropSequence = CropDao.getCropSequence()
            // 시퀀스 값 업데이트
            CropDao.updateCropSequence(cropSequence + 1)

            // 업로드 할 데이터
            val cropIdx = cropSequence + 1
            val cropSellerIdx = cropSequence + 1
            val cropAddress = "파밍시 파밍동 ..."
            val cropTitle = "파밍이네 농작물${cropSequence + 1}"
            val cropOptionDetail = mutableListOf<Map<String, String>>()
            val map1 = mutableMapOf<String, String>()
            map1["crop_option_name"] = "감자 10kg"
            map1["crop_option_price"] = "10,000원"
            map1["crop_option_inventory"] = "재고 있음"
            cropOptionDetail.add(map1)
            val map2 = mutableMapOf<String, String>()
            map2["crop_option_name"] = "감자 20kg"
            map2["crop_option_price"] = "19,000원"
            map2["crop_option_inventory"] = "재고 있음"
            cropOptionDetail.add(map2)

            val cropContentDetail = "여기는 농작물 상세내용 글입니다"
            val cropContentWarning = "여기는 농작물 주의사항 글입니다"
            val cropContentPolicy = "여기는 농작물 취소환불 글입니다"
            val cropLikeCnt = 100 + cropIdx
            val cropImage = mutableListOf<String>(
                "image/crop/딸기.jpg",
                "image/crop/배.jpg",
                "image/crop/복숭아.jpg",
                "image/crop/사과.jpg",
                "image/crop/오렌지렌지.jpg"
            )

            val cropContentDetailImage = mutableListOf<String>(
                "image/crop/딸기.jpg"
            )
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cropRegDt = simpleDateFormat.format(Date())
            val cropModDt = simpleDateFormat.format(Date())

            val likeState = false

            val cropRating = cropIdx * 0.5

            val cropDeliveryFee = (cropIdx * 500).toString()

            val cropStatus = CropStatus.NORMAL.num

            val cropModel = CropModel(
                cropIdx, cropSellerIdx, cropAddress, cropTitle,
                cropOptionDetail, cropContentDetail,
                cropContentWarning, cropContentPolicy,
                cropLikeCnt, cropImage, cropContentDetailImage,
                cropRegDt, cropModDt, likeState, cropRating,cropDeliveryFee, cropStatus
            )

            CropDao.insertCropData(cropModel)
        }
    }

    // 특가 농산물 관련 설정
    private fun setupViewPager() {
        // TODO("특가 농산물 DB와 연결필요, 여기선 임시로 products 추가")
        val products = cropLikeTop5List
        fragmentTradeBinding.viewPagerTradeSpecialPrice.adapter = ProductPagerAdapter(products!!)
    }

    // 인기 농산물 관련 설정
    private fun setupViewPager2() {
        // TODO("인기 농작물 DB와 연결 필요, 여기선 임시로 products 추가")
        val products = cropLikeTop5List

        fragmentTradeBinding.viewPagerTradeTrend.adapter = ProductPagerAdapter(products!!)
    }

    // 최신 등록 농산물 관련 설정
    private fun setupRecyclerView(){
        // TODO("최신 농작물 DB와 연결 필요, 여기선 임시로 products 추가")
        // 한 행에 표시될 항목의 수 2로 설정
        val layoutManager = GridLayoutManager(context, 2)
        fragmentTradeBinding.recyclerViewTradeNew.layoutManager = layoutManager

        val products = cropRecentTop5List

        //어댑터 설정
        fragmentTradeBinding.recyclerViewTradeNew.adapter = TradeNewAdapter(products!!)
    }

    // 특가, 인기 농산물 Adapter
    inner class ProductPagerAdapter(private val products: List<CropModel>) : RecyclerView.Adapter<ProductPagerAdapter.ProductViewHolder>() {
        inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(product: CropModel) {
                binding.tradeViewModel?.textViewCropName?.value = product.crop_title
                binding.tradeViewModel?.textViewCropLocation?.value = product.crop_address
                binding.tradeViewModel?.textViewTradePrice?.value = product.crop_option_detail[1]["crop_option_price"]
                binding.tradeViewModel?.textViewTradeLike?.value = if (product.crop_like_cnt >= 1000) "999+"  // 좋아요 개수 1000이상이면 초기값 999+
                else "${product.crop_like_cnt}"

                binding.RatingBarTrade.rating = product.crop_rating.toFloat()


//                binding.imageButtonProductLike.setOnClickListener {
//                    product.like_state = !product.like_state // 좋아요 선택
//
//                    if (product.like_state) {
//                        product.crop_like_cnt++ // 좋아요 상태면 좋아요 수 증가, 텍스트 흰색
//                        binding.textViewTradeLike.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//                        CoroutineScope(Dispatchers.Main).launch {
//                            CropDao.updateCropLikeState(product,product.like_state)
//                        }
//                    } else {
//                        product.crop_like_cnt-- // 좋아요 취소면 좋아요 수 감소, 텍스트 갈색
//                        binding.textViewTradeLike.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))
//                        CoroutineScope(Dispatchers.Main).launch {
//                            CropDao.updateCropLikeState(product,product.like_state)
//                        }
//                    }
//                    // 좋아요 수가 1000 이상이면 999+로 표기
//                    binding.textViewTradeLike.text = if (product.crop_like_cnt >= 1000) "999+"
//                    else "${product.crop_like_cnt}"
//
//
//
//
//                    binding.imageButtonProductLike.setImageResource(
//                        if (product.like_state) R.drawable.heart_01 else R.drawable.heart_02
//                    )
//                }

                // 아이템 클릭 이벤트 처리
                itemView.setOnClickListener {
                    val adapterPosition = adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val intent = Intent(itemView.context, TradeDetailActivity::class.java)
                        intent.putExtra("crop_idx", product.crop_idx) // 상품 ID를 인텐트에 추가
                        intent.putExtra("position", adapterPosition) // 클릭된 아이템의 포지션 추가
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val binding = DataBindingUtil.inflate<ItemProductBinding>(layoutInflater,R.layout.item_product,parent,false)
            val tradeViewModel =TradeViewModel()
            binding.tradeViewModel = tradeViewModel
            binding.lifecycleOwner = this@TradeFragment
            return ProductViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.bind(products[position])

            holder.binding.tradeViewModel?.isLike?.value = false
            holder.binding.imageButtonProductLike.setOnClickListener {
                if(holder.binding.tradeViewModel?.isLike?.value!!){
                    holder.binding.tradeViewModel?.isLike?.value = false
                    holder.binding.imageButtonProductLike.setImageResource(R.drawable.heart_02)
                    holder.binding.textViewTradeLike.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                }else{
                    holder.binding.tradeViewModel?.isLike?.value = true
                    holder.binding.imageButtonProductLike.setImageResource(R.drawable.heart_01)
                    holder.binding.textViewTradeLike.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                CropDao.gettingCropImage(mainActivity,products[position].crop_images[position],holder.binding.imageViewCropImage)
            }

        }

        override fun getItemCount(): Int = products.size
    }

    // 최신 등록 농산물 Adapter
    inner class TradeNewAdapter(private val products: List<CropModel>): RecyclerView.Adapter<TradeNewAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: RowRelatedCropBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(product: CropModel){
                binding.apply {

                    tradeViewModel?.textViewLikeCropName?.value = product.crop_title
                    tradeViewModel?.textViewLikeCropPrice?.value = product.crop_option_detail[1]["crop_option_price"]
                    tradeViewModel?.textViewLikeCropCnt?.value = if (product.crop_like_cnt >= 1000) "999+"  // 좋아요 개수 1000이상이면 초기값 999+
                    else "${product.crop_like_cnt}"

                    ratingBarLikeCrop.rating = product.crop_rating.toFloat()


//
//                    imageViewHeartCrop.setOnClickListener {
//                        if (product.like_state) {
//                            product.crop_like_cnt++ // 좋아요 상태면 좋아요 수 증가, 텍스트 흰색
//                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//                            CoroutineScope(Dispatchers.Main).launch {
//                                CropDao.updateCropLikeState(product,product.like_state)
//                            }
//                        } else {
//                            product.crop_like_cnt-- // 좋아요 취소면 좋아요 수 감소, 텍스트 갈색
//                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))
//                            CoroutineScope(Dispatchers.Main).launch {
//                                CropDao.updateCropLikeState(product,product.like_state)
//                            }
//                        }
//                        // 좋아요 수가 1000 이상이면 999+로 표기
//                        binding.textViewLikeCropCnt.text = if (product.crop_like_cnt >= 1000) "999+"
//                        else "${product.crop_like_cnt}"
//                        binding.imageViewHeartCrop.setImageResource(
//                            if (product.like_state) R.drawable.heart_01 else R.drawable.heart_02
//                        )
//                    }

                    // 아이템 클릭 이벤트 처리
                    itemView.setOnClickListener {
                        val adapterPosition = adapterPosition
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            val intent = Intent(itemView.context, TradeDetailActivity::class.java)
                            intent.putExtra("crop_idx", product.crop_idx) // 상품 ID를 인텐트에 추가
                            intent.putExtra("position", adapterPosition) // 클릭된 아이템의 포지션 추가
                            itemView.context.startActivity(intent)
                        }
                    }
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeNewAdapter.ViewHolder {
            val binding = DataBindingUtil.inflate<RowRelatedCropBinding>(layoutInflater,R.layout.row_related_crop,parent,false)
            val tradeViewModel =TradeViewModel()
            binding.tradeViewModel = tradeViewModel
            binding.lifecycleOwner = this@TradeFragment
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TradeNewAdapter.ViewHolder, position: Int) {
            val product = products[position]
            holder.bind(product)
            holder.binding.apply {
                tradeViewModel!!.apply {
                    isLike.value = false
                    constraintLikeCropCancel.setOnClickListener {
                        if(isLike.value!!){
                            isLike.value = false
                            imageViewHeartCrop.setImageResource(R.drawable.heart_02)
                            holder.binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageViewHeartCrop.setImageResource(R.drawable.heart_01)
                            holder.binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                    }

                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                CropDao.gettingCropImage(mainActivity,product.crop_images[position],holder.binding.imageViewLikeCrop)
            }
        }

        override fun getItemCount(): Int = products.size

    }
}