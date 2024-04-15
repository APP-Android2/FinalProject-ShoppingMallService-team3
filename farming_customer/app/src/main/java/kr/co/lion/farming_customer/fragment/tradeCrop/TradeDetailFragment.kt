package kr.co.lion.farming_customer.fragment.tradeCrop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeDetailBinding
import kr.co.lion.farming_customer.databinding.RowCropImageBinding
import kr.co.lion.farming_customer.databinding.RowRelatedCropBinding
import kr.co.lion.farming_customer.databinding.RowReviewCropBinding
import kr.co.lion.farming_customer.databinding.RowReviewCropImageBinding
import kr.co.lion.farming_customer.model.CropReview
import kr.co.lion.farming_customer.model.ProductCard
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeDetailViewModel

class TradeDetailFragment : Fragment() {

    private lateinit var fragmentTradeDetailBinding: FragmentTradeDetailBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var tradeDetailViewModel: TradeDetailViewModel

    private val constraintSet = ConstraintSet()

    // 버튼이 클릭된 상태인지 확인
    private var isButtonClicked = false

    // 리뷰 더보기 버튼이 클릭된 상태인지 확인
    private var isReviewSeeMoreButtonClicked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        fragmentTradeDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trade_detail, container, false)
        tradeDetailViewModel = TradeDetailViewModel()
        fragmentTradeDetailBinding.tradeDetailViewModel = tradeDetailViewModel
        fragmentTradeDetailBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        setToolbar()
        setTab()
        setButton()
        setImageAdapter()
        setRecyclerView()
        setRecyclerRelatedCrop()

        return fragmentTradeDetailBinding.root
    }

    // 툴바 설정
    private fun setToolbar(){
        fragmentTradeDetailBinding.apply {
            toolbarTradeDetail.apply {
                // 제목
                title = "농산물 판매"

                inflateMenu(R.menu.menu_trade_detail)

                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainFragmentName.TRADE_DETAIL_FRAGMENT)
                }
            }
        }
    }

    // 탭 설정
    private fun setTab(){
        fragmentTradeDetailBinding.tabLayoutDetailAndReview.apply {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 탭 눌렀을 때 반응 추가

                when (tab.position) {
                    // 상세 정보 탭
                    0 -> {
                        val ypos = fragmentTradeDetailBinding.tabLayoutDetailAndReview.top
                        fragmentTradeDetailBinding.stickyScrollViewTradeDetail.smoothScrollTo(0, ypos)
                    }
                    // 리뷰 탭
                    1 -> {
                        val ypos = fragmentTradeDetailBinding.textView4.top
                        fragmentTradeDetailBinding.stickyScrollViewTradeDetail.smoothScrollTo(0, ypos)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택 해제될 때 필요할 경우 사용
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택될 때 필요한 경우 사용
            }
        })
        }
    }

    // 버튼 설정
    private fun setButton(){

        var buttonLikeClicked = false

        // 더보기 버튼 클릭
        constraintSet.clone(fragmentTradeDetailBinding.constraintLayoutTradeDetail)

        fragmentTradeDetailBinding.buttonTradeDetailSeeMore.setOnClickListener {
            // 버튼이 클릭 상태가 아니라면
            if (!isButtonClicked) {
                isButtonClicked = true // 상태를 클릭 상태로 변경
                // 버튼의 상단 제약을 textViewTradeTabDetailPolicy의 하단으로 변경
                constraintSet.connect(fragmentTradeDetailBinding.buttonTradeDetailSeeMore.id,
                    ConstraintSet.TOP, fragmentTradeDetailBinding.textViewTradeTabDetailPolicy.id,
                    ConstraintSet.BOTTOM)
                constraintSet.applyTo(fragmentTradeDetailBinding.constraintLayoutTradeDetail)

                // marginTop을 50으로 설정
                val layoutParams = fragmentTradeDetailBinding.buttonTradeDetailSeeMore.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = Tools.dpToPx(requireContext(), 50) //requireContext()는 context가 null이 아님을 증명
                fragmentTradeDetailBinding.buttonTradeDetailSeeMore.layoutParams = layoutParams

                //text 변경
                fragmentTradeDetailBinding.buttonTradeDetailSeeMore.text = "접기"

                fragmentTradeDetailBinding.textView5.visibility = View.VISIBLE
                fragmentTradeDetailBinding.textViewTradeTabDetailContents.visibility = View.VISIBLE
                fragmentTradeDetailBinding.textView6.visibility = View.VISIBLE
                fragmentTradeDetailBinding.textViewTradeTabDetailWarning.visibility = View.VISIBLE
                fragmentTradeDetailBinding.textView7.visibility = View.VISIBLE
                fragmentTradeDetailBinding.textViewTradeTabDetailPolicy.visibility = View.VISIBLE

            } else {
                // 버튼이 클릭 상태라면
                isButtonClicked = false // 상태를 클릭 상태로 변경
                // 버튼의 상단 제약을  imageViewTradeDetail 하단으로 변경
                constraintSet.connect(fragmentTradeDetailBinding.buttonTradeDetailSeeMore.id,
                    ConstraintSet.TOP, fragmentTradeDetailBinding.imageViewTradeDetail.id,
                    ConstraintSet.BOTTOM)

                constraintSet.applyTo(fragmentTradeDetailBinding.constraintLayoutTradeDetail)

                //text 변경
                fragmentTradeDetailBinding.buttonTradeDetailSeeMore.text = "더보기"

                fragmentTradeDetailBinding.textView5.visibility = View.GONE
                fragmentTradeDetailBinding.textViewTradeTabDetailContents.visibility = View.GONE
                fragmentTradeDetailBinding.textView6.visibility = View.GONE
                fragmentTradeDetailBinding.textViewTradeTabDetailWarning.visibility = View.GONE
                fragmentTradeDetailBinding.textView7.visibility = View.GONE
                fragmentTradeDetailBinding.textViewTradeTabDetailPolicy.visibility = View.GONE

            }
        }

        fragmentTradeDetailBinding.apply {
            // 좋아요 버튼 누르면
            imageButtonLike.setOnClickListener {
                if (!buttonLikeClicked){
                    buttonLikeClicked = true
                    imageButtonLike.setImageResource(R.drawable.heart_01)
                    //TODO("좋아요 항목에 해당 아이템 추가, 좋아요 count 증가")
                } else {
                    buttonLikeClicked = false
                    imageButtonLike.setImageResource(R.drawable.heart_02)
                    //TODO("좋아요 항목에서 해당 아이템 제거, 좋아요 count 감소")
                }
            }
            // 구매하기 버튼 누르면
            buttonTradeDetailPurchase.setOnClickListener {
                val bottomSheet = BottomSheetTradeCrop()
                bottomSheet.show(mainActivity.supportFragmentManager, bottomSheet.tag)
            }
        }

    }

    // 이미지 설정
    private fun setImageAdapter(){
        // 임의로 이미지 리소스를 가져옴
        // TODO("DB에서 이미지 가져오는 작업 필요")
        val images = listOf(R.drawable.farming_mark, R.drawable.logo_01, R.drawable.logo_02)
        // ViewPager2에 이미지 어댑터 설정
        fragmentTradeDetailBinding.viewPager2TradeDetail.adapter = ImageAdpater(images)

        // ViewPager2에 어댑터 데이터가 변경되었을 때 인디케이터를 업데이트 하도록 설정
        fragmentTradeDetailBinding.circleIndicatorTradeDetail.setViewPager(fragmentTradeDetailBinding.viewPager2TradeDetail)
        fragmentTradeDetailBinding.viewPager2TradeDetail.adapter?.registerAdapterDataObserver(
            fragmentTradeDetailBinding.circleIndicatorTradeDetail.adapterDataObserver)
    }

    // 이미지 연결 어댑터
    inner class ImageAdpater(private val images: List<Int>): RecyclerView.Adapter<ImageAdpater.ImageViewHolder>(){

        inner class ImageViewHolder(private val binding: RowCropImageBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(imageRes: Int) {
                binding.imageView4.setImageResource(imageRes)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<RowCropImageBinding>(inflater, R.layout.row_crop_image, parent, false)
            return ImageViewHolder(binding)
        }

        override fun getItemCount(): Int = images.size

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            holder.bind(images[position])
        }
    }

    // 리뷰 설정
    private fun setRecyclerView(){
        val reviews = listOf(
            CropReview("홍길동", R.drawable.profile, 4.5, null,
                "감자", "10kg", "2024.03.10"),
            CropReview("김길동", R.drawable.home_01, 4.0, listOf(R.drawable.farming_mark, R.drawable.logo_01),
                "감자", "10kg", "2024.03.11"),
            CropReview("최길동", R.drawable.home_02, 5.0, listOf(R.drawable.farming_mark),
                "감자", "10kg", "2024.03.10")
        )

        val adapter = ReviewAdapter(reviews)
        fragmentTradeDetailBinding.recyclerViewReview.layoutManager = LinearLayoutManager(context)
        fragmentTradeDetailBinding.recyclerViewReview.adapter = adapter

        fragmentTradeDetailBinding.buttonTradeReviewSeeMore.setOnClickListener {
            isReviewSeeMoreButtonClicked = !isReviewSeeMoreButtonClicked // 상태 토글
            fragmentTradeDetailBinding.buttonTradeReviewSeeMore.text = if (isReviewSeeMoreButtonClicked) "접기" else "더보기"
            adapter.notifyDataSetChanged() // RecyclerView 갱신
        }
    }

    // 리뷰 어댑터 설정
    inner class ReviewAdapter(private val reviews: List<CropReview>): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){
        inner class ReviewViewHolder(private val  binding: RowReviewCropBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(review: CropReview){
                binding.apply {
                    TextViewRowLikeCropReviewer.text = review.name
                    circleImageViewRowLikeCrop.setImageResource(review.imageResourceProfile)
                    baseRatingBar2.rating = review.rating.toFloat()
                    textViewRowReviewCropDate.text = review.date
                    textViewCropName.text = review.productName
                    textViewOptionName.text = review.optionName

                    // 이미지 리사이클러뷰 설정
                    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    recyclerViewCropReviewImage.layoutManager = layoutManager
                    recyclerViewCropReviewImage.adapter = review.imageResourceIds?.let {
                        ReviewImageAdapter(
                            it
                        )
                    }
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowReviewCropBinding.inflate(inflater, parent, false)
            return ReviewViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return if (!isReviewSeeMoreButtonClicked)
                2
            else
                reviews.size
        }


        override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
            holder.bind(reviews[position])
        }


    }

    // 리뷰 내 이미지 설정
    inner class ReviewImageAdapter(private val images: List<Int>): RecyclerView.Adapter<ReviewImageAdapter.ReviewImageViewHolder>(){
        inner class ReviewImageViewHolder(private val  binding: RowReviewCropImageBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(images: Int){
                binding.apply {
                    imageView.setImageResource(images)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewImageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowReviewCropImageBinding.inflate(inflater, parent, false)
            return ReviewImageViewHolder(binding)
        }

        override fun getItemCount(): Int = images.size


        override fun onBindViewHolder(holder: ReviewImageViewHolder, position: Int) {
            holder.bind(images[position])
        }
    }

    // 관련 농작물 설정
    private fun setRecyclerRelatedCrop(){

        val crops = listOf(
            ProductCard(R.drawable.farming_mark, "사과", "10,000원", 999, 1.0),
            ProductCard(R.drawable.farming_mark, "배", "10,000원", 999, 2.0),
            ProductCard(R.drawable.farming_mark, "감자", "10,000원", 999, 3.0),
            ProductCard(R.drawable.farming_mark, "바나나", "10,000원", 999, 4.0),
            ProductCard(R.drawable.farming_mark, "사과", "10,000원", 999, 5.0),
        )
        fragmentTradeDetailBinding.apply {
            recyclerViewRelatedCrop.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewRelatedCrop.adapter = RelatedCropAdpter(crops)
        }
    }

    // 관련 농작물 recyclerView Adapter
    inner class RelatedCropAdpter(private val crops: List<ProductCard>): RecyclerView.Adapter<RelatedCropAdpter.RelatedCropViewHolder>(){
        inner class RelatedCropViewHolder(private val binding: RowRelatedCropBinding): RecyclerView.ViewHolder(binding.root) {
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
                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        } else {
                            crop.likes-- // 좋아요 취소면 좋아요 수 감소, 텍스트 갈색
                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedCropViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowRelatedCropBinding.inflate(inflater, parent, false)
            return RelatedCropViewHolder(binding)
        }

        override fun getItemCount(): Int = crops.size


        override fun onBindViewHolder(holder: RelatedCropViewHolder, position: Int) {
            holder.binding(crops[position])
        }
    }
}