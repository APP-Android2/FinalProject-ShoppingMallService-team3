package kr.co.lion.farming_customer.fragment.tradeCrop

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.Tools.Companion.dpToPx
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeDetailBinding
import kr.co.lion.farming_customer.databinding.RowCropImageBinding
import kr.co.lion.farming_customer.databinding.RowReviewCropBinding
import kr.co.lion.farming_customer.model.CropReview
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeDetailViewModel

class TradeDetailFragment : Fragment() {

    private lateinit var fragmentTradeDetailBinding: FragmentTradeDetailBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var tradeDetailViewModel: TradeDetailViewModel

    private val constraintSet = ConstraintSet()

    // 버튼이 클릭된 상태인지 확인
    private var isButtonClicked = false

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

        return fragmentTradeDetailBinding.root
    }

    // 툴바 설정
    private fun setToolbar(){
        fragmentTradeDetailBinding.apply {
            toolbarTradeDetail.apply {
                // 제목
                title = "농산물 판매"

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

    //
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

    inner class ReviewAdapter(private val reviews: List<CropReview>): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){
        inner class ReviewViewHolder(private val  binding: RowReviewCropBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(review: CropReview){
                binding.apply {
                    TextViewRowLikeCropReviewer.text = review.name
                    circleImageViewRowLikeCrop.setImageResource(review.imageResourceProfile)
                    baseRatingBar2.rating = review.rating
                    textViewRowReviewCropDate.text = review.date
                    textViewCropName.text = review.productName
                    textViewOptionName.text = review.optionName

                    // 이미지 리사이클러뷰 설정
                    recyclerViewCropReviewImage.adapter = TODO("어댑터 연결")
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowReviewCropBinding.inflate(inflater, parent, false)
            return ReviewViewHolder(binding)
        }

        override fun getItemCount(): Int = reviews.size


        override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
            holder.bind(reviews[position])
        }
    }
}