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
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeActivityDetailBinding
import kr.co.lion.farming_customer.databinding.RowFarmingLifeReviewBinding
import kr.co.lion.farming_customer.databinding.RowFarmingLifeReviewImageBinding
import kr.co.lion.farming_customer.databinding.ViewPagerFarmingLifeFarmDetailBinding
import kr.co.lion.farming_customer.viewmodel.farmingLife.FarmingLifeActivityDetailViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowFarmingLifeReviewViewModel

class FarmingLifeActivityDetailFragment : Fragment() {
    lateinit var fragmentFarmingLifeActivityDetailBinding: FragmentFarmingLifeActivityDetailBinding
    lateinit var farmingLifeActivity: FarmingLifeActivity

    lateinit var farmingLifeActivityDetailViewModel: FarmingLifeActivityDetailViewModel

    var isCollapsed_productDetail = true
    var isCollapsed_review = true
    var isLiked = false

    val imageList = arrayOf(
        R.drawable.image_1,
        R.drawable.image_1,
        R.drawable.image_1,
        R.drawable.image_1,
        R.drawable.image_1
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentFarmingLifeActivityDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_farming_life_activity_detail, container, false)
        farmingLifeActivityDetailViewModel = FarmingLifeActivityDetailViewModel()
        fragmentFarmingLifeActivityDetailBinding.farmingLifeActivityDetailViewModel = farmingLifeActivityDetailViewModel
        fragmentFarmingLifeActivityDetailBinding.lifecycleOwner = this
        farmingLifeActivity = activity as FarmingLifeActivity

        settingToolbar()
        settingData()
        settingViewPager()
        settingReview()
        settingTapEvent()
        settingEvent()

        return fragmentFarmingLifeActivityDetailBinding.root
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
                val bottomSheetActivityReservFragment = BottomSheetActivityReservFragment()
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
            textView_address.value = "강원도 횡성군"
            textView_price.value = "구획 당 금액 : 10,000원"
            textView_productDetail.value = "공무원의 직무상 불법행위로 손해를 받은 국민은 법률이 정하는 바에 의하여 국가 또는 공공단체에 정당한 배상을 청구할 수 있다. 이 경우 공무원 자신의 책임은 면제되지 아니한다.\n" +
                    "\n" +
                    "국가는 과학기술의 혁신과 정보 및 인력의 개발을 통하여 국민경제의 발전에 노력하여야 한다. 이 헌법중 공무원의 임기 또는 중임제한에 관한 규정은 이 헌법에 의하여 그 공무원이 최초로 선출 또는 임명된 때로부터 적용한다.\n" +
                    "\n" +
                    "환경권의 내용과 행사에 관하여는 법률로 정한다. 헌법재판소는 법률에 저촉되지 아니하는 범위안에서 심판에 관한 절차, 내부규율과 사무처리에 관한 규칙을 제정할 수 있다.\n" +
                    "\n" +
                    "일반사면을 명하려면 국회의 동의를 얻어야 한다. 대통령은 국무총리·국무위원·행정각부의 장 기타 법률이 정하는 공사의 직을 겸할 수 없다. 모든 국민은 학문과 예술의 자유를 가진다.\n" +
                    "\n" +
                    "명령·규칙 또는 처분이 헌법이나 법률에 위반되는 여부가 재판의 전제가 된 경우에는 대법원은 이를 최종적으로 심사할 권한을 가진다. 대통령은 법률에서 구체적으로 범위를 정하여 위임받은 사항과 법률을 집행하기 위하여 필요한 사항에 관하여 대통령령을 발할 수 있다.\n" +
                    "\n" +
                    "재의의 요구가 있을 때에는 국회는 재의에 붙이고, 재적의원과반수의 출석과 출석의원 3분의 2 이상의 찬성으로 전과 같은 의결을 하면 그 법률안은 법률로서 확정된다.\n" +
                    "\n" +
                    "행정각부의 설치·조직과 직무범위는 법률로 정한다. 의원을 제명하려면 국회재적의원 3분의 2 이상의 찬성이 있어야 한다. 모든 국민은 통신의 비밀을 침해받지 아니한다.\n" +
                    "\n" +
                    "국가는 대외무역을 육성하며, 이를 규제·조정할 수 있다. 국무총리·국무위원 또는 정부위원은 국회나 그 위원회에 출석하여 국정처리상황을 보고하거나 의견을 진술하고 질문에 응답할 수 있다."
            textView_serviceInfo.value = "공무원의 직무상 불법행위로 손해를 받은 국민은 법률이 정하는 바에 의하여 국가 또는 공공단체에 정당한 배상을 청구할 수 있다. 이 경우 공무원 자신의 책임은 면제되지 아니한다.\n" +
                    "\n" +
                    "국가는 과학기술의 혁신과 정보 및 인력의 개발을 통하여 국민경제의 발전에 노력하여야 한다. 이 헌법중 공무원의 임기 또는 중임제한에 관한 규정은 이 헌법에 의하여 그 공무원이 최초로 선출 또는 임명된 때로부터 적용한다.\n" +
                    "\n" +
                    "환경권의 내용과 행사에 관하여는 법률로 정한다. 헌법재판소는 법률에 저촉되지 아니하는 범위안에서 심판에 관한 절차, 내부규율과 사무처리에 관한 규칙을 제정할 수 있다.\n" +
                    "\n" +
                    "일반사면을 명하려면 국회의 동의를 얻어야 한다. 대통령은 국무총리·국무위원·행정각부의 장 기타 법률이 정하는 공사의 직을 겸할 수 없다. 모든 국민은 학문과 예술의 자유를 가진다.\n" +
                    "\n" +
                    "명령·규칙 또는 처분이 헌법이나 법률에 위반되는 여부가 재판의 전제가 된 경우에는 대법원은 이를 최종적으로 심사할 권한을 가진다. 대통령은 법률에서 구체적으로 범위를 정하여 위임받은 사항과 법률을 집행하기 위하여 필요한 사항에 관하여 대통령령을 발할 수 있다.\n" +
                    "\n" +
                    "재의의 요구가 있을 때에는 국회는 재의에 붙이고, 재적의원과반수의 출석과 출석의원 3분의 2 이상의 찬성으로 전과 같은 의결을 하면 그 법률안은 법률로서 확정된다.\n" +
                    "\n" +
                    "행정각부의 설치·조직과 직무범위는 법률로 정한다. 의원을 제명하려면 국회재적의원 3분의 2 이상의 찬성이 있어야 한다. 모든 국민은 통신의 비밀을 침해받지 아니한다.\n" +
                    "\n" +
                    "국가는 대외무역을 육성하며, 이를 규제·조정할 수 있다. 국무총리·국무위원 또는 정부위원은 국회나 그 위원회에 출석하여 국정처리상황을 보고하거나 의견을 진술하고 질문에 응답할 수 있다.z"
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
                imageViewViewPager.setImageResource(imageList[position])
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