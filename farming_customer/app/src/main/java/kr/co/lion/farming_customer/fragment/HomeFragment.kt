package kr.co.lion.farming_customer.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.OrderStatus
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.databinding.FragmentHomeBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.model.farminLife.ActivityModel
import kr.co.lion.farming_customer.model.farminLife.FarmModel
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel
import kr.co.lion.farming_customer.viewmodel.HomeViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel
import java.security.SecureRandom
import java.text.SimpleDateFormat

class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    lateinit var homeViewModel: HomeViewModel

    var farmAndActivityList = mutableListOf<Any>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        homeViewModel = HomeViewModel()
        fragmentHomeBinding.homeViewModel = homeViewModel
        fragmentHomeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        settingData()
        settingRecyclerView()

        return fragmentHomeBinding.root
    }

    private fun settingInput() {

        CoroutineScope(Dispatchers.Main).launch {
            // 농산물 더미 데이터
            for(j in 1 .. 10){ // 상품 라벨 당 10개
                for(k in 1..9){ // 라벨 넘버
                    val sequence = OrderDao.getOrderSequence()
                    OrderDao.updateOrderSequence(sequence + 1)
                    var order_idx = sequence + 1
                    val format = SimpleDateFormat("yyMMDD")
                    var order_product_type = 1
                    var order_num = format.format(System.currentTimeMillis()) + order_product_type.toString() + (SecureRandom().nextInt(1000)).toString()
                    var order_user_idx = j
                    var order_seller_idx = j

                    var order_product_idx = j
                    var order_label = k
                    var order_invoice_number = ""
                    var order_delivery_address = mutableMapOf(
                        "receiver" to "김파밍", "address" to "경기도 파밍시 파밍구 123-4", "phone" to "010-1234-1234", "request" to "부재 시 경비실에 맡겨 주세요."
                    )
                    var order_reg_date = "2024.04." + String.format("%02d", sequence + 1)
                    var order_mod_date = "2024.04." + String.format("%02d", sequence + 1)
                    var order_delivery_start_date = ""
                    var order_delivery_done_date = ""
                    var order_is_reviewed = false
                    var order_option_detail = mutableListOf<MutableMap<String, Any>>(
                        mutableMapOf("option_name" to "감자 5kg", "option_cnt" to 1, "option_price" to "20,000원", "option_total_price" to "20,000원"),
                        mutableMapOf("option_name" to "감자 3kg", "option_cnt" to 2, "option_price" to "12,000원", "option_total_price" to "24,000원")
                    )
                    var order_total_price = "44,000원"
                    var order_cancel_reason = ""
                    var order_cancel_reason_detail = ""
                    var order_status = OrderStatus.ORDER_STATUS_NORMAL.number

                    val model = OrderModel(order_idx, order_num, order_user_idx, order_seller_idx, order_product_type, order_product_idx, order_label, order_invoice_number, order_delivery_address, order_reg_date, order_mod_date, order_delivery_start_date, order_delivery_done_date, order_is_reviewed, order_option_detail, order_total_price, order_cancel_reason, order_cancel_reason_detail, order_status)
                    OrderDao.insertOrderData(model)
                }

            }
            // 주말농장 더미 데이터
            for(j in 1 .. 5) { // 상품 라벨 당 5개
                for(k in 9..10){
                    val sequence = OrderDao.getOrderSequence()
                    OrderDao.updateOrderSequence(sequence + 1)
                    var order_idx = sequence + 1
                    val format = SimpleDateFormat("yyMMDD")
                    var order_product_type = 2
                    var order_num = format.format(System.currentTimeMillis()) + order_product_type.toString() + (SecureRandom().nextInt(1000)).toString()
                    var order_user_idx = j
                    var order_seller_idx = j

                    var order_product_idx = j
                    var order_label = k
                    var order_invoice_number = ""
                    var order_delivery_address = mutableMapOf<String, String>()
                    var order_reg_date = "2024.04." + String.format("%02d", sequence + 1)
                    var order_mod_date = "2024.04." + String.format("%02d", sequence + 1)
                    var order_delivery_start_date = ""
                    var order_delivery_done_date = ""
                    var order_is_reviewed = false
                    var order_option_detail = mutableListOf<MutableMap<String, Any>>(
                        mutableMapOf("option_name" to "파밍이네 주말농장", "option_price" to "30,000원")
                    )
                    var order_total_price = "30,000원"
                    var order_cancel_reason = ""
                    var order_cancel_reason_detail = ""
                    var order_status = OrderStatus.ORDER_STATUS_NORMAL.number

                    val model = OrderModel(order_idx, order_num, order_user_idx, order_seller_idx, order_product_type, order_product_idx, order_label, order_invoice_number, order_delivery_address, order_reg_date, order_mod_date, order_delivery_start_date, order_delivery_done_date, order_is_reviewed, order_option_detail, order_total_price, order_cancel_reason, order_cancel_reason_detail, order_status)
                    OrderDao.insertOrderData(model)
                }
            }
            // 체험활동 더미 데이터
            for(j in 1 .. 5) { // 상품 라벨 당 5개
                for(k in 9..10){
                    val sequence = OrderDao.getOrderSequence()
                    OrderDao.updateOrderSequence(sequence + 1)
                    var order_idx = sequence + 1
                    val format = SimpleDateFormat("yyMMDD")
                    var order_product_type = 3
                    var order_num = format.format(System.currentTimeMillis()) + order_product_type.toString() + (SecureRandom().nextInt(1000)).toString()
                    var order_user_idx = j
                    var order_seller_idx = j

                    var order_product_idx = j
                    var order_label = k
                    var order_invoice_number = ""
                    var order_delivery_address = mutableMapOf<String, String>()
                    var order_reg_date = "2024.04." + String.format("%02d", sequence + 1)
                    var order_mod_date = "2024.04." + String.format("%02d", sequence + 1)
                    var order_delivery_start_date = ""
                    var order_delivery_done_date = ""
                    var order_is_reviewed = false
                    var order_option_detail = mutableListOf<MutableMap<String, Any>>(
                        mutableMapOf("option_name" to "딸기따기", "option_price" to "30,000원", "option_cnt" to 1, "option_total_price" to "30,000원", "option_time" to "10:00"),
                        mutableMapOf("option_name" to "딸기 아이스크림 만들기", "option_price" to "15,000원", "option_cnt" to 1, "option_total_price" to "15,000원", "option_time" to "12:00"),
                        mutableMapOf("option_name" to "딸기 케이크 만들기", "option_price" to "30,000원", "option_cnt" to 1, "option_total_price" to "30,000원", "option_time" to "14:00")
                    )
                    var order_total_price = "75,000원"
                    var order_cancel_reason = ""
                    var order_cancel_reason_detail = ""
                    var order_status = OrderStatus.ORDER_STATUS_NORMAL.number

                    val model = OrderModel(order_idx, order_num, order_user_idx, order_seller_idx, order_product_type, order_product_idx, order_label, order_invoice_number, order_delivery_address, order_reg_date, order_mod_date, order_delivery_start_date, order_delivery_done_date, order_is_reviewed, order_option_detail, order_total_price, order_cancel_reason, order_cancel_reason_detail, order_status)
                    OrderDao.insertOrderData(model)
                }
            }

        }
    }

    private fun settingData() {
        CoroutineScope(Dispatchers.Main).launch {
            // 주말농장 데이터를 가져온다.
            val farmList = FarmDao.gettingFarmListOrderByLikeCnt()
            // 체험활동 데이터를 가져온다.
            val activityList = ActivityDao.gettingActivityListOrderByLikeCnt()

            var index = 0
            while (farmAndActivityList.size < 6){
                if(farmList[index].farm_like_cnt > activityList[index].activity_like_cnt){
                    farmAndActivityList.add(farmList[index])
                }else if(farmList[index].farm_like_cnt == activityList[index].activity_like_cnt){
                    farmAndActivityList.add(farmList[index])
                    farmAndActivityList.add(activityList[index])
                }else{
                    farmAndActivityList.add(activityList[index])
                }
                index ++
            }
            fragmentHomeBinding.viewPagerFarm.adapter?.notifyDataSetChanged()
        }
    }

    private fun settingRecyclerView() {
        fragmentHomeBinding.apply {
            // 농산물 뷰 페이저 어댑터
            viewPagerCrop.apply {
                adapter = ViewPagerCropAdapter()
            }

            // 주말농장 / 체험활동 뷰페이저 어댑터
            viewPagerFarm.apply {
                adapter = ViewPagerFarmAdapter()
            }

            // 커뮤니티 리사이클러뷰 어댑터
            recyclerViewBoard.apply {
                adapter = BoardRecyClerViewAdatper()
                layoutManager = LinearLayoutManager(mainActivity)
                addItemDecoration(MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL))
            }
        }
    }

    // 추천 농산물 뷰 페이저 어댑터
    inner class ViewPagerCropAdapter : RecyclerView.Adapter<ViewPagerCropAdapter.ViewPagerCropViewHolder>(){
        inner class ViewPagerCropViewHolder(rowLikeCropBinding: RowLikeCropBinding) : RecyclerView.ViewHolder(rowLikeCropBinding.root){
            val rowLikeCropBinding : RowLikeCropBinding
            init {
                this.rowLikeCropBinding = rowLikeCropBinding

                rowLikeCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerCropViewHolder {
            val rowLikeCropBinding = DataBindingUtil.inflate<RowLikeCropBinding>(layoutInflater, R.layout.row_like_crop, parent, false)

            val viewPagerCropViewHolder = ViewPagerCropViewHolder(rowLikeCropBinding)
            return viewPagerCropViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: ViewPagerCropViewHolder, position: Int) {
            holder.rowLikeCropBinding.apply {
                textViewLikeCropName.text = "고랭지 배추"
                textViewLikeCropPrice.text = "10,000원 / kg"
                textViewLikeCropCnt.text = "999"
            }
        }
    }

    // 주말 농장, 체험활동 뷰 페이저 어댑터
    inner class ViewPagerFarmAdapter : RecyclerView.Adapter<ViewPagerFarmAdapter.ViewPagerFarmViewHolder>(){
        inner class ViewPagerFarmViewHolder(rowGridItemBinding: RowGridItemBinding) : RecyclerView.ViewHolder(rowGridItemBinding.root){
            val rowGridItemBinding : RowGridItemBinding
            init {
                this.rowGridItemBinding = rowGridItemBinding
                rowGridItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerFarmViewHolder {
            val rowGridItemBinding = DataBindingUtil.inflate<RowGridItemBinding>(layoutInflater, R.layout.row_grid_item, parent, false)
            val rowGridItemViewModel = RowGridItemViewModel()
            rowGridItemBinding.rowGridItemViewModel = rowGridItemViewModel
            rowGridItemBinding.lifecycleOwner = this@HomeFragment

            val viewPagerFarmViewHolder = ViewPagerFarmViewHolder(rowGridItemBinding)
            return viewPagerFarmViewHolder
        }

        override fun getItemCount(): Int {
            return farmAndActivityList.size
        }

        override fun onBindViewHolder(holder: ViewPagerFarmViewHolder, position: Int) {
            holder.rowGridItemBinding.rowGridItemViewModel!!.apply {
                var model = farmAndActivityList[position]
                if(model is FarmModel){
                    textView_likeCnt.value = model.farm_like_cnt.toString()
                    textView_ItemName.value = model.farm_title
                    textView_location.value = model.farm_address
                    textView_price.value = model.farm_option_detail["price_area"].toString()
                }else if(model is ActivityModel){
                    textView_likeCnt.value = model.activity_like_cnt.toString()
                    textView_ItemName.value = model.activity_title
                    textView_location.value = model.activity_address

                    // 옵션 중 가장 최소 가격 표시
                    var minPrice = Int.MAX_VALUE
                    var minPrice_pos = -1
                    model.activity_option_detail.forEachIndexed { index, mutableMap ->
                        val priceString = mutableMap["option_price"] as String
                        val numberString = priceString.replace(",", "").replace("원", "")
                        val priceInt = numberString.toInt()

                        if(priceInt < minPrice){
                            minPrice = priceInt
                            minPrice_pos = index
                        }
                    }
                    textView_price.value = model.activity_option_detail[minPrice_pos]["option_price"] as String + " ~"
                }

                isLike.value = false
            }
            // 하트 클릭 리스너
            holder.rowGridItemBinding.apply {
                rowGridItemViewModel!!.apply {
                    constraintLikeCancel.setOnClickListener {
                        if(isLike.value!!){
                            isLike.value = false
                            imageViewHeart.setImageResource(R.drawable.heart_04)
                            textViewLikeCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageViewHeart.setImageResource(R.drawable.heart_01)
                            textViewLikeCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                    }
                }
            }
            // 뷰페이저 아이템 클릭 리스너
            holder.rowGridItemBinding.root.setOnClickListener {
                val model = farmAndActivityList[position]
                // 주말농장인지 체험활동인지 구분해야함
                if(model is FarmModel){
                    val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                    intent.putExtra("fragmentName", FarmingLifeFragmnetName.FARMING_LIFE_FARM_DETAIL_FARMGNET)
                    intent.putExtra("idx", model.farm_idx)
                    startActivity(intent)
                }else if(model is ActivityModel){
                    val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                    intent.putExtra("fragmentName", FarmingLifeFragmnetName.FARMING_LIFE_ACTIVITY_DETAIL_FRAGMENT)
                    intent.putExtra("idx", model.activity_idx)
                    startActivity(intent)
                }

            }
            holder.rowGridItemBinding.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    val model = farmAndActivityList[position]
                    if(model is FarmModel){
                        FarmDao.gettingFarmImage(mainActivity, model.farm_images[0], imageView)
                    }else if(model is ActivityModel){
                        ActivityDao.gettingActivityImage(mainActivity, model.activity_images[0], imageView)
                    }
                }
            }
        }
    }

    // 좋아요가 많은 게시판 리사이클러뷰 어댑터
    inner class BoardRecyClerViewAdatper : RecyclerView.Adapter<BoardRecyClerViewAdatper.BoardViewHolder>(){
        inner class BoardViewHolder(rowCommunityTabAllBinding: RowCommunityTabAllBinding) : RecyclerView.ViewHolder(rowCommunityTabAllBinding.root){
            val rowCommunityTabAllBinding : RowCommunityTabAllBinding
            init {
                this.rowCommunityTabAllBinding = rowCommunityTabAllBinding

                rowCommunityTabAllBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
            val rowCommunityTabAllBinding = DataBindingUtil.inflate<RowCommunityTabAllBinding>(layoutInflater, R.layout.row_community_tab_all, parent, false)
            val communityViewModel = CommunityViewModel()
            rowCommunityTabAllBinding.communityViewModel = communityViewModel
            rowCommunityTabAllBinding.lifecycleOwner = this@HomeFragment

            val boardViewHolder = BoardViewHolder(rowCommunityTabAllBinding)
            return boardViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
            holder.rowCommunityTabAllBinding.communityViewModel!!.apply {
                textViewCommunityListLabelAll.value = "전체"
                textViewCommunityListTitleAll.value = "글 제목"
                textViewCommunityListContentAll.value = "글 내용입니다. 글 내용입니다. 글 내용입니다. 글 내용입니다. 글 내용입니다. "
                textViewCommunityListViewCntAll.value = "99"
                textViewCommunityListCommentCntAll.value = "99"
                textViewCommunityListDateAll.value = "2024.04.12"
                textViewCommunityListLikeCntAll.value = "999"
                isLike.value = false
            }
            // 좋아요 클릭 리스너
            holder.rowCommunityTabAllBinding.apply {
                imageViewCommunityListLikeAll.setOnClickListener {
                    if(communityViewModel!!.isLike.value!!){
                        communityViewModel!!.isLike.value = false
                        imageViewCommunityListLikeAll.setImageResource(R.drawable.heart_02)
                        textViewCommunityListLikeCntAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))
                    }else{
                        communityViewModel!!.isLike.value = true
                        imageViewCommunityListLikeAll.setImageResource(R.drawable.heart_01)
                        textViewCommunityListLikeCntAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                }
            }
            // 아이템 클릭 리스너
            holder.rowCommunityTabAllBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, CommunityActivity::class.java)
                startActivity(intent)
            }
        }
    }
}