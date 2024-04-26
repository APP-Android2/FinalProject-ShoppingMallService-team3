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
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeActivityDetailBinding
import kr.co.lion.farming_customer.databinding.RowFarmingLifeReviewBinding
import kr.co.lion.farming_customer.databinding.RowFarmingLifeReviewImageBinding
import kr.co.lion.farming_customer.databinding.ViewPagerFarmingLifeFarmDetailBinding
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.FarmingLifeActivityDetailViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowFarmingLifeReviewViewModel

class FarmingLifeActivityDetailFragment(data: Bundle?) : Fragment() {
    lateinit var fragmentFarmingLifeActivityDetailBinding: FragmentFarmingLifeActivityDetailBinding
    lateinit var farmingLifeActivity: FarmingLifeActivity

    lateinit var farmingLifeActivityDetailViewModel: FarmingLifeActivityDetailViewModel

    var isCollapsed_productDetail = true
    var isCollapsed_review = true
    var isLiked = false

    var activityModel : ActivityModel? = null
    var imageList = mutableListOf<String>()

    var idx : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentFarmingLifeActivityDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_farming_life_activity_detail, container, false)
        farmingLifeActivityDetailViewModel = FarmingLifeActivityDetailViewModel()
        fragmentFarmingLifeActivityDetailBinding.farmingLifeActivityDetailViewModel = farmingLifeActivityDetailViewModel
        fragmentFarmingLifeActivityDetailBinding.lifecycleOwner = this
        farmingLifeActivity = activity as FarmingLifeActivity

        // 번들 객체에서 idx 추출
        idx = arguments?.getInt("idx", 0)

        settingToolbar()
        settingInitData()
        settingData()
        settingViewPager()
        settingReview()
        settingTapEvent()
        settingEvent()

        return fragmentFarmingLifeActivityDetailBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            // 매개변수로 받은 idx 값으로 파이어베이스에서 farmModel 객체를 가져온다.
            activityModel = ActivityDao.selectActivityData(idx!!)
            imageList = activityModel!!.activity_images
            settingData()
            settingViewPager()
            settingReview()
        }
    }

    private fun settingEvent() {
        fragmentFarmingLifeActivityDetailBinding.apply {
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
            // 예약하기 버튼
            buttonReservation.setOnClickListener {
                val bottomSheetActivityReservFragment = BottomSheetActivityReservFragment(idx!!)
                bottomSheetActivityReservFragment.show(farmingLifeActivity.supportFragmentManager, "BottomSheetActivityReservFragment")
            }
        }
    }

    private fun settingReview() {
        fragmentFarmingLifeActivityDetailBinding.apply {
            recycvlerViewReview.apply {
                adapter = ReviewRecyclerAdapter()
                layoutManager = LinearLayoutManager(farmingLifeActivity)
            }
        }
    }

    private fun settingTapEvent() {
        fragmentFarmingLifeActivityDetailBinding.apply {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            // 상품상세
                            val viewLocation = IntArray(2)
                            val scrollLocation = IntArray(2)
                            textView13.getLocationInWindow(viewLocation)
                            scrollViewActivityDetail.getLocationInWindow(scrollLocation)
                            ObjectAnimator.ofArgb(scrollViewActivityDetail, "scrollY", scrollViewActivityDetail.scrollY, scrollViewActivityDetail.scrollY + viewLocation[1] - scrollLocation[1] - tabLayout.layoutParams.height).apply {
                                duration = 1000
                                start()
                            }
                        }

                        1 -> {
                            // 이용안내
                            val viewLocation = IntArray(2)
                            val scrollLocation = IntArray(2)
                            textView18.getLocationInWindow(viewLocation)
                            scrollViewActivityDetail.getLocationInWindow(scrollLocation)
                            ObjectAnimator.ofArgb(scrollViewActivityDetail, "scrollY", scrollViewActivityDetail.scrollY, scrollViewActivityDetail.scrollY + viewLocation[1] - scrollLocation[1] - tabLayout.layoutParams.height).apply {
                                duration = 1000
                                start()
                            }
                        }

                        2 -> {
                            // 리뷰
                            val viewLocation = IntArray(2)
                            val scrollLocation = IntArray(2)
                            textView20.getLocationInWindow(viewLocation)
                            scrollViewActivityDetail.getLocationInWindow(scrollLocation)
                            ObjectAnimator.ofArgb(scrollViewActivityDetail, "scrollY", scrollViewActivityDetail.scrollY, scrollViewActivityDetail.scrollY + viewLocation[1] - scrollLocation[1] - tabLayout.layoutParams.height).apply {
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
        fragmentFarmingLifeActivityDetailBinding.apply {
            viewPager.apply {
                adapter = ViewPagerAdapter()
            }
            circleIndicatorActivityDetail.setViewPager(viewPager)
        }
    }

    private fun settingData() {
        farmingLifeActivityDetailViewModel.apply {
            textView_sellerName.value = "김파밍"
            textView_address.value = activityModel?.activity_address

            // 옵션 중 가장 최소 가격 표시
            var minPrice = Int.MAX_VALUE
            var minPrice_pos = -1
            activityModel?.activity_option_detail?.forEachIndexed { index, mutableMap ->
                val priceString = mutableMap["option_price"] as String
                val numberString = priceString.replace(",", "").replace("원", "")
                val priceInt = numberString.toInt()

                if(priceInt < minPrice){
                    minPrice = priceInt
                    minPrice_pos = index
                }
            }
            textView_price.value = "금액 : ${
                activityModel?.activity_option_detail?.get(minPrice_pos)
                ?.get("option_price")} ~"
            textView_productDetail.value = activityModel?.activity_content_detail
            val optionNameList = mutableListOf<String>()
            activityModel?.activity_option_detail?.forEach {
                optionNameList.add(it["option_name"].toString())
            }
            textView_serviceInfo.value = "체험활동" +
                    "\n\n" +
                    buildString {
                        append("\t")
                        append(optionNameList.joinToString(separator = "\n\n\t"))
                    } +
                    "\n\n주의사항\n${activityModel?.activity_content_warning}" +
                    "\n" +
                    "편의시설" +
                    "\n" +
                    "\t주차장 : ${if(activityModel?.activity_utility?.get("park") == true)"가능" else "불가능"}" +
                    "\n\t화장실 : ${if(activityModel?.activity_utility?.get("bathroom") == true)"가능" else "불가능"}" +
                    "\n\t수도 : ${if(activityModel?.activity_utility?.get("water") == true)"가능" else "불가능"}" +
                    "\n\t휴식공간 : ${if(activityModel?.activity_utility?.get("rest_space") == true)"가능" else "불가능"}"
        }
    }

    private fun settingToolbar() {
        fragmentFarmingLifeActivityDetailBinding.apply {
            toolbarFarmingLifeActivityDetail.apply {
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
                    ActivityDao.gettingActivityImage(farmingLifeActivity, imageList[position], imageViewViewPager)
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
            rowFarmingLifeReviewBinding.lifecycleOwner = this@FarmingLifeActivityDetailFragment

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