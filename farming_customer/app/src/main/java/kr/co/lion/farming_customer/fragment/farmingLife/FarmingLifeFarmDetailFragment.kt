package kr.co.lion.farming_customer.fragment.farmingLife

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeFarmDetailBinding
import kr.co.lion.farming_customer.databinding.RowFarmingLifeReviewBinding
import kr.co.lion.farming_customer.databinding.RowFarmingLifeReviewImageBinding
import kr.co.lion.farming_customer.databinding.ViewPagerFarmingLifeFarmDetailBinding
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.FarmingLifeFarmDetailViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowFarmingLifeReviewViewModel

class FarmingLifeFarmDetailFragment(data: Bundle?) : Fragment() {
    lateinit var fragmentFarmingLifeFarmDetailBinding: FragmentFarmingLifeFarmDetailBinding
    lateinit var farmingLifeActivity : FarmingLifeActivity

    lateinit var farmingLifeFarmDetailViewModel : FarmingLifeFarmDetailViewModel

    var isCollapsed_productDetail = true
    var isCollapsed_review = true
    var isLiked = false

    var farmModel : FarmModel? = null
    var imageList = mutableListOf<String>()

    var idx : Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFarmingLifeFarmDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_farming_life_farm_detail, container, false)
        farmingLifeFarmDetailViewModel = FarmingLifeFarmDetailViewModel()
        fragmentFarmingLifeFarmDetailBinding.farmingLifeFarmDetailViewModel = farmingLifeFarmDetailViewModel
        fragmentFarmingLifeFarmDetailBinding.lifecycleOwner = this
        farmingLifeActivity = activity as FarmingLifeActivity

        // 번들 객체에서 idx 추출
        idx = arguments?.getInt("idx", 0)

        settingToolbar()
        settingInitData()
        settingTapEvent()
        settingEvent()

        return fragmentFarmingLifeFarmDetailBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            // 매개변수로 받은 idx 값으로 파이어베이스에서 farmModel 객체를 가져온다.
            farmModel = FarmDao.selectFarmData(idx!!)
            imageList = farmModel!!.farm_images
            settingData()
            settingViewPager()
            settingReview()
        }
    }

    private fun settingEvent() {
        fragmentFarmingLifeFarmDetailBinding.apply {
            // 상품 상세 더보기
            buttonMoreInfo.setOnClickListener {
                if(isCollapsed_productDetail){
                    // 접힌 상태. 펼친다.
                    isCollapsed_productDetail = false
                    buttonMoreInfo.text = "접기"
                    textViewProductDetail.maxLines = Int.MAX_VALUE
                }else{
                    // 펼쳐진 상태. 다시 접는다.
                    isCollapsed_productDetail = true
                    buttonMoreInfo.text = "더보기"
                    textViewProductDetail.maxLines = 5
                }

            }
            // 리뷰 더보기
            buttonMoreReview.setOnClickListener {
                if(isCollapsed_review){
                    // 접힌 상태. 펼친다
                    isCollapsed_review = false
                    buttonMoreReview.text = "접기"
                    recycvlerViewReview.adapter!!.notifyDataSetChanged()
                }else{
                    // 펼쳐진 상태. 다시 접는다.
                    isCollapsed_review = true
                    buttonMoreReview.text = "더보기"
                    recycvlerViewReview.adapter!!.notifyDataSetChanged()
                }
            }
            // 좋아요
            imageButtonLike.setOnClickListener {
                if(isLiked){
                    // 하트 비우기
                    isLiked = false
                    imageButtonLike.setImageResource(R.drawable.heart_02)
                }else{
                    // 하트 채우기
                    isLiked = true
                    imageButtonLike.setImageResource(R.drawable.heart_01)
                }
            }
            // 판매자에게 전화하기
            imageButtonCallSeller.setOnClickListener {
                // 전화 걸기 화면으로 이동
                val phoneNumber = "010-1234-5678" // 전화번호
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            }
            // 예약 버튼
            buttonReservation.setOnClickListener {
                val bottomSheetFarmReservFragment = BottomSheetFarmReservFragment(idx!!)
                bottomSheetFarmReservFragment.show(farmingLifeActivity.supportFragmentManager, "BottomSheetFarmReservFragment")
            }
        }
    }

    private fun settingReview() {
        fragmentFarmingLifeFarmDetailBinding.apply {
            recycvlerViewReview.apply {
                adapter = ReviewRecyclerAdapter()
                layoutManager = LinearLayoutManager(farmingLifeActivity)
            }
        }
    }

    private fun settingTapEvent() {
        fragmentFarmingLifeFarmDetailBinding.apply {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            // 상품상세
                            val viewLocation = IntArray(2)
                            val scrollLocation = IntArray(2)
                            textView13.getLocationInWindow(viewLocation)
                            scrollViewFarmDetail.getLocationInWindow(scrollLocation)
                            ObjectAnimator.ofArgb(scrollViewFarmDetail, "scrollY", scrollViewFarmDetail.scrollY, scrollViewFarmDetail.scrollY + viewLocation[1] - scrollLocation[1] - tabLayout.layoutParams.height).apply {
                                duration = 1000
                                start()
                            }
                        }

                        1 -> {
                            // 이용안내
                            val viewLocation = IntArray(2)
                            val scrollLocation = IntArray(2)
                            textView18.getLocationInWindow(viewLocation)
                            scrollViewFarmDetail.getLocationInWindow(scrollLocation)
                            ObjectAnimator.ofArgb(scrollViewFarmDetail, "scrollY", scrollViewFarmDetail.scrollY, scrollViewFarmDetail.scrollY + viewLocation[1] - scrollLocation[1] - tabLayout.layoutParams.height).apply {
                                duration = 1000
                                start()
                            }
                        }

                        2 -> {
                            // 리뷰
                            val viewLocation = IntArray(2)
                            val scrollLocation = IntArray(2)
                            textView20.getLocationInWindow(viewLocation)
                            scrollViewFarmDetail.getLocationInWindow(scrollLocation)
                            ObjectAnimator.ofArgb(scrollViewFarmDetail, "scrollY", scrollViewFarmDetail.scrollY, scrollViewFarmDetail.scrollY + viewLocation[1] - scrollLocation[1] - tabLayout.layoutParams.height).apply {
                                duration = 1000
                                start()
                            }
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }

    private fun settingViewPager() {
        fragmentFarmingLifeFarmDetailBinding.apply {
            viewPager.apply {
                adapter = ViewPagerAdapter()
            }
            circleIndicatorFarmDetail.setViewPager(viewPager)
        }
    }

    private fun settingData() {
        farmingLifeFarmDetailViewModel.apply {
            textView_sellerName.value = "김파밍"
            textView_address.value = farmModel?.farm_address
            textView_applicationDate.value = "신청기간 : ${farmModel?.farm_apply_date_start} ~ ${farmModel?.farm_apply_date_end}"
            textView_serviceDate.value = "이용기간 : ${farmModel?.farm_use_date_start} ~ ${farmModel?.farm_use_date_end}"
            textView_remainCnt.value = "남은 구획수 : ${farmModel?.farm_option_detail?.get("remain_area")}개"
            textView_price.value = "구획 당 금액 : ${farmModel?.farm_option_detail?.get("price_area")}"
            textView_productDetail.value = farmModel?.farm_content_detail
            textView_serviceInfo.value = "생산 가능 작물\n" +
                    buildString {
                        append(farmModel?.farm_can_crop!!.joinToString(separator = ", "))
                    } +
                    "\n\n" +
                    "이용기간 : ${farmModel?.farm_use_date_start} ~ ${farmModel?.farm_use_date_end}" +
                    "\n\n" +
                    "구획 당 크기 : ${farmModel?.farm_option_detail?.get("size_area")}" +
                    "\n\n" +
                    "주의사항\n${farmModel?.farm_content_warning}" +
                    "\n" +
                    "편의시설" +
                    "\n" +
                    "\t주차장 : ${if(farmModel?.farm_utility?.get("park") == true)"가능" else "불가능"}" +
                    "\n\t화장실 : ${if(farmModel?.farm_utility?.get("bathroom") == true)"가능" else "불가능"}" +
                    "\n\t수도 : ${if(farmModel?.farm_utility?.get("water") == true)"가능" else "불가능"}" +
                    "\n\t휴식공간 : ${if(farmModel?.farm_utility?.get("rest_space") == true)"가능" else "불가능"}"
        }
    }

    private fun settingToolbar() {
        fragmentFarmingLifeFarmDetailBinding.apply {
            toolbarFarmingLifeFarmDetail.apply {
                setNavigationOnClickListener {
                    farmingLifeActivity.finish()
                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuItemFarmingLifeDetail_cart -> {
                            val intent = Intent(farmingLifeActivity, CartActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    true
                }
            }
        }
    }

    // 뷰페이저 어댑터
    inner class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>(){
        inner class ViewPagerViewHolder(viewPagerFarmingLifeFarmDetailBinding: ViewPagerFarmingLifeFarmDetailBinding) : RecyclerView.ViewHolder(viewPagerFarmingLifeFarmDetailBinding.root){
            val viewPagerFarmingLifeFarmDetailBinding : ViewPagerFarmingLifeFarmDetailBinding

            init{
                this.viewPagerFarmingLifeFarmDetailBinding = viewPagerFarmingLifeFarmDetailBinding

                viewPagerFarmingLifeFarmDetailBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
            val viewPagerFarmingLifeFarmDetailBinding = DataBindingUtil.inflate<ViewPagerFarmingLifeFarmDetailBinding>(layoutInflater, R.layout.view_pager_farming_life_farm_detail, parent, false)

            val ViewPagerViewHolder = ViewPagerViewHolder(viewPagerFarmingLifeFarmDetailBinding)
            return ViewPagerViewHolder
        }

        override fun getItemCount(): Int {
            return imageList.size
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
            holder.viewPagerFarmingLifeFarmDetailBinding.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    FarmDao.gettingFarmImage(farmingLifeActivity, imageList[position], imageViewViewPager)
                }
            }
        }
    }

    // 리뷰 리사이클러 어댑터
    inner class ReviewRecyclerAdapter : RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewViewHolder>(){
        inner class ReviewViewHolder(rowFarmingLifeReviewBinding: RowFarmingLifeReviewBinding) : RecyclerView.ViewHolder(rowFarmingLifeReviewBinding.root){
            val rowFarmingLifeReviewBinding : RowFarmingLifeReviewBinding

            init{
                this.rowFarmingLifeReviewBinding = rowFarmingLifeReviewBinding

                rowFarmingLifeReviewBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
            val rowFarmingLifeReviewBinding = DataBindingUtil.inflate<RowFarmingLifeReviewBinding>(layoutInflater, R.layout.row_farming_life_review, parent, false)
            val rowFarmingLifeReviewViewHolder = RowFarmingLifeReviewViewModel()
            rowFarmingLifeReviewBinding.rowFarmingLifeReviewViewModel = rowFarmingLifeReviewViewHolder
            rowFarmingLifeReviewBinding.lifecycleOwner = this@FarmingLifeFarmDetailFragment

            val ReviewViewHolder = ReviewViewHolder(rowFarmingLifeReviewBinding)
            return ReviewViewHolder
        }

        override fun getItemCount(): Int {
            if(isCollapsed_review){
                // 접힌상태. 아이템 갯수가 1개로 제한.
                return 2
            }else{
                // 펼쳐진 상태. 모든 아이템을 보여준다.
                return 10
            }
        }

        override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
            holder.rowFarmingLifeReviewBinding.rowFarmingLifeReviewViewModel!!.apply {
                textView_name.value = "김파밍"
                textView_reviewDate.value = "2024.04.09"
                textView_productName.value = "파밍이네 주말농장"
                textView_optionName.value = "파밍이네 주말농장 1구획"
                textView_content.value = "사장님도 친절하시고 위치도 너무 좋습니다!!"
            }
            holder.rowFarmingLifeReviewBinding.apply {
                recyclerViewReviewImage.apply {
                    adapter = ReviewImageRecyclerAdater()
                    layoutManager = LinearLayoutManager(farmingLifeActivity, RecyclerView.HORIZONTAL, false)
                }
            }
        }
    }

    // 리뷰 이미지 리사이클러 어댑터
    inner class ReviewImageRecyclerAdater : RecyclerView.Adapter<ReviewImageRecyclerAdater.ReviewImageViewHolder>(){
        inner class ReviewImageViewHolder(rowFarmingLifeReviewImageBinding : RowFarmingLifeReviewImageBinding) : RecyclerView.ViewHolder(rowFarmingLifeReviewImageBinding.root){
            val rowFarmingLifeReviewImageBinding : RowFarmingLifeReviewImageBinding
            init{
                this.rowFarmingLifeReviewImageBinding = rowFarmingLifeReviewImageBinding

                rowFarmingLifeReviewImageBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewImageViewHolder {
            val rowFarmingLifeReviewImageBinding = DataBindingUtil.inflate<RowFarmingLifeReviewImageBinding>(layoutInflater, R.layout.row_farming_life_review_image, parent, false)
            val ReviewImageViewHolder = ReviewImageViewHolder(rowFarmingLifeReviewImageBinding)

            return ReviewImageViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: ReviewImageViewHolder, position: Int) {
            holder.rowFarmingLifeReviewImageBinding.apply {
                imageView.setImageResource(R.drawable.image_1)
            }
        }
    }
}