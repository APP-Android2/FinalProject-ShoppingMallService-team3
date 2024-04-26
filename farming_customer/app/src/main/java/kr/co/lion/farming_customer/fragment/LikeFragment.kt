package kr.co.lion.farming_customer.fragment

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.GridSpaceItemDecoration
import kr.co.lion.farming_customer.LikeType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.activity.farmingLifeTools.FarmingLifeToolsDetailActivity
import kr.co.lion.farming_customer.activity.tradeCrop.TradeSearchActivity
import kr.co.lion.farming_customer.dao.like.LikeDao
import kr.co.lion.farming_customer.databinding.FragmentLikeBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.databinding.RowLikeFarmActivityBinding
import kr.co.lion.farming_customer.databinding.RowLikePostBinding
import kr.co.lion.farming_customer.databinding.RowLikeRentalBinding
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.model.LikeModel
import kr.co.lion.farming_customer.model.CommunityPostModel
import kr.co.lion.farming_customer.model.farminLifeTools.RentalModel
import kr.co.lion.farming_customer.viewmodel.LikeViewModel

class LikeFragment : Fragment() {
    lateinit var fragmentLikeBinding: FragmentLikeBinding
    lateinit var mainActivity: MainActivity

    lateinit var likeViewModel: LikeViewModel

    lateinit var deco: MaterialDividerItemDecoration
    lateinit var deco2: GridSpaceItemDecoration

    lateinit var LikeList: List<LikeModel>
    lateinit var cropList: MutableList<CropModel>
    lateinit var postList: MutableList<CommunityPostModel>
    lateinit var farmList: MutableList<FarmModel>
    lateinit var activityList: MutableList<ActivityModel>
    lateinit var rentalList: MutableList<RentalModel>
    var selectedType = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLikeBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_like, container, false)
        likeViewModel = LikeViewModel()
        fragmentLikeBinding.likeViewModel = likeViewModel
        fragmentLikeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        // ViewModel 생성자에 Context 객체를 명시적으로 전달합니다.
        // 세션에서 회원 시퀀스 번호 가져오기..
        val sharedPreferences = mainActivity.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE)
        val userIdx = sharedPreferences.getInt("loginUserIdx", -1)

        lifecycleScope.launch {
            val data = likeViewModel.getLikeListAndLikeTypeList(userIdx)
            selectedType = "CROP"
            LikeList = data.LikeList!!
            cropList = (data.cropList as MutableList<CropModel>?)!!
            postList = (data.postList as MutableList<CommunityPostModel>?)!!
            farmList = (data.farmList as MutableList<FarmModel>?)!!
            activityList = (data.activityList as MutableList<ActivityModel>?)!!
            rentalList = (data.rentalList as MutableList<RentalModel>?)!!

            settingUI()
            settingToggleButton()
        }
        deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
        deco2 = GridSpaceItemDecoration(2, 40, -10, -10, -10)

        return fragmentLikeBinding.root
    }

    // 초기 설정
    fun settingUI() {
        likeViewModel.settingLikeType(LikeType.CROP)
    }

    // toggleButton 선택에 따라 recyclerView가 바뀌는 메서드
    fun settingToggleButton() {
        fragmentLikeBinding.apply {
            toggleGroupLikeType.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.buttonLikeCrop -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "CROP"
                            settingRecyclerViewLikeCrop()
                        }

                        R.id.buttonLikePost -> {
                            recyclerViewLike.visibility = View.GONE
                            recyclerViewPost.visibility = View.VISIBLE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "POST"
                            settingRecyclerViewLikePost()
                        }

                        R.id.buttonLikeFarm -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "FARM"
                            settingRecyclerViewLikeFarmAndActivity()
                        }

                        R.id.buttonLikeActivity -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "ACTIVITY"
                            settingRecyclerViewLikeFarmAndActivity()
                        }

                        R.id.buttonLikeRental -> {
                            recyclerViewLike.visibility = View.GONE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.VISIBLE
                            selectedType = "RENTAL"
                            settingRecyclerViewLikeRental()
                        }
                    }

                }
            }
        }
    }


    // 농산물 recyclerView 설정
    fun settingRecyclerViewLikeCrop() {
        fragmentLikeBinding.apply {
            recyclerViewLike.apply {
                removeItemDecoration(deco)
                if (itemDecorationCount == 0) {
                    addItemDecoration(deco2)
                }
                adapter = LikeCropRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity, 2)
            }
        }
    }

    // 게시판 recyclerView 설정
    fun settingRecyclerViewLikePost() {
        fragmentLikeBinding.apply {
            recyclerViewPost.apply {
                removeItemDecoration(deco2)

                adapter = LikePostRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                if(recyclerViewPost.itemDecorationCount == 0){
                    addItemDecoration(deco)
                }
            }
        }
    }

    // 주말농장 및 체험활동 recyclerView 설정
    fun settingRecyclerViewLikeFarmAndActivity() {
        fragmentLikeBinding.apply {
            recyclerViewLike.apply {
                removeItemDecoration(deco)
                if (itemDecorationCount == 0) {
                    addItemDecoration(deco2)
                }
                adapter = LikeFarmAndActivityRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity, 2)
            }
        }
    }

    // 농기구 recyclerView 설정
    fun settingRecyclerViewLikeRental() {
        fragmentLikeBinding.apply {
            recyclerViewRental.apply {
                removeItemDecoration(deco)
                adapter = LikeRentalRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }
    }

    // 농산물 recyclerView의 Adapter
    inner class LikeCropRecyclerViewAdapter :
        RecyclerView.Adapter<LikeCropRecyclerViewAdapter.LikeCropViewHolder>() {

        inner class LikeCropViewHolder(rowLikeCropBinding: RowLikeCropBinding) :
            RecyclerView.ViewHolder(rowLikeCropBinding.root) {

            val rowLikeCropBinding: RowLikeCropBinding


            init {
                this.rowLikeCropBinding = rowLikeCropBinding
                this.rowLikeCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeCropViewHolder {
            val rowLikeCropBinding = DataBindingUtil.inflate<RowLikeCropBinding>(
                layoutInflater,
                R.layout.row_like_crop,
                parent,
                false
            )
            val likeViewModel = LikeViewModel()
            rowLikeCropBinding.likeViewModel = likeViewModel
            rowLikeCropBinding.lifecycleOwner = this@LikeFragment
            val likeCropViewHolder = LikeCropViewHolder(rowLikeCropBinding)
            return likeCropViewHolder
        }

        override fun getItemCount(): Int {
            return cropList.size
        }

        override fun onBindViewHolder(holder: LikeCropViewHolder, position: Int) {
            holder.rowLikeCropBinding.apply {
                textViewLikeCropName.text = cropList[position].crop_title
                textViewLikeCropPrice.text =
                    cropList[position].crop_option_detail[0]["crop_option_price"]
                textViewLikeCropCnt.text =
                    cropList[position].crop_like_cnt.toString()
                ratingBarLikeCrop.rating = cropList[position].crop_like_cnt.toFloat()
                CoroutineScope(Dispatchers.Main).launch {
                    if (cropList[position].crop_images != null) {
                        LikeDao.gettingImage(
                            mainActivity,
                            cropList[position].crop_images[0],
                            imageViewLikeCrop,
                            "CROP"
                        )
                    } else {
                        holder.rowLikeCropBinding.imageViewLikeCrop.setImageResource(R.color.white)
                    }
                }
            }

            // 아이템 클릭 이벤트
            holder.rowLikeCropBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                intent.putExtra(
                    "fragmentName",
                    FarmingLifeFragmnetName.FARMING_LIFE_FARM_DETAIL_FARMGNET
                )
                startActivity(intent)

            }

            // 하트 클릭 리스너
            holder.rowLikeCropBinding.apply {
                likeViewModel!!.apply {
                    constraintLikeCropCancel.setOnClickListener {
                       CoroutineScope(Dispatchers.Main).launch {
                           LikeDao.LikeListDelete( LikeList[position].like_idx, 2)
                       }
                        cropList.removeAt(position)
                        fragmentLikeBinding.recyclerViewLike.adapter?.notifyDataSetChanged()

                    }
                }
            }
        }

    }

    // 게시판 recyclerView의 Adapter
    inner class LikePostRecyclerViewAdapter :
        RecyclerView.Adapter<LikePostRecyclerViewAdapter.LikePostViewHolder>() {
        inner class LikePostViewHolder(rowLikePostBinding: RowLikePostBinding) :
            RecyclerView.ViewHolder(rowLikePostBinding.root) {
            val rowLikePostBinding: RowLikePostBinding

            init {
                this.rowLikePostBinding = rowLikePostBinding
                this.rowLikePostBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikePostViewHolder {
            val rowLikePostBinding = DataBindingUtil.inflate<RowLikePostBinding>(
                layoutInflater,
                R.layout.row_like_post,
                parent,
                false
            )
            val likeViewModel = LikeViewModel()
            rowLikePostBinding.likeViewModel = likeViewModel
            rowLikePostBinding.lifecycleOwner = this@LikeFragment
            val likePostViewHolder = LikePostViewHolder(rowLikePostBinding)
            return likePostViewHolder
        }

        override fun getItemCount(): Int {
            return postList.size
        }

        override fun onBindViewHolder(holder: LikePostViewHolder, position: Int) {
            holder.rowLikePostBinding.apply {
                imageView.setImageResource(R.drawable.heart_01)
                textViewLikePostCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                imageViewLikePost.isSelected = true
                textViewLikePostLabel.text = postList[position].postType
                textViewLikePostTitle.text = postList[position].postTitle.substring(10)
                textViewLikePostContent.text = postList[position].postContent.substring(20)
                textViewLikePostViewCnt.text = postList[position].postViewCnt.toString()
                textViewLikePostCommentCnt.text = postList[position].postCommentCnt.toString()
                textViewLikePostDate.text = postList[position].postRegDt
                textViewLikePostCnt.text = postList[position].postLikeCnt.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    if (postList[position].postImages != null) {
                        LikeDao.gettingImage(
                            mainActivity,
                            postList[position].postImages[0],
                            imageViewLikePost,
                            "POST"
                        )
                    } else {
                        holder.rowLikePostBinding.imageViewLikePost.setImageResource(R.color.white)
                    }
                }
            }
            // 아이템 클릭 이벤트
            holder.rowLikePostBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, CommunityActivity::class.java)
                startActivity(intent)
            }


            // 하트 클릭 리스너
            holder.rowLikePostBinding.apply {
                likeViewModel!!.apply {
                    constraintLike.setOnClickListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            LikeDao.LikeListDelete( postList[position].postIdx, 2)
                            postList.removeAt(position)
                            fragmentLikeBinding.recyclerViewLike.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    // 주말농장 및 체험활동 recyclerView의 Adapter
    inner class LikeFarmAndActivityRecyclerViewAdapter :
        RecyclerView.Adapter<LikeFarmAndActivityRecyclerViewAdapter.LikeFarmAndAcitivityViewHolder>() {
        inner class LikeFarmAndAcitivityViewHolder(rowLikeFarmActivityBinding: RowLikeFarmActivityBinding) :
            RecyclerView.ViewHolder(rowLikeFarmActivityBinding.root) {
            val rowLikeFarmActivityBinding: RowLikeFarmActivityBinding

            init {
                this.rowLikeFarmActivityBinding = rowLikeFarmActivityBinding
                this.rowLikeFarmActivityBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

//        override fun onCreateViewHolder(
//            parent: ViewGroup,
//            viewType: Int
//        ): LikeFarmAndAcitivityViewHolder {
//            val rowLikeFarmActivityBinding = RowLikeFarmActivityBinding.inflate(layoutInflater)
//            val likeFarmAndActivityViewHolder = LikeFarmAndAcitivityViewHolder(rowLikeFarmActivityBinding)
//            return likeFarmAndActivityViewHolder
//        }


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): LikeFarmAndAcitivityViewHolder {
            val rowLikeFarmActivityBinding = DataBindingUtil.inflate<RowLikeFarmActivityBinding>(
                layoutInflater,
                R.layout.row_like_farm_activity,
                parent,
                false
            )
            val likeViewModel = LikeViewModel()
            rowLikeFarmActivityBinding.likeViewModel = likeViewModel
            rowLikeFarmActivityBinding.lifecycleOwner = this@LikeFragment
            val likeFarmAndActivityViewHolder =
                LikeFarmAndAcitivityViewHolder(rowLikeFarmActivityBinding)
            return likeFarmAndActivityViewHolder
        }

        override fun getItemCount(): Int {
            var size = 0
            when (selectedType) {
                "FARM" -> {
                    size = farmList.size
                }

                "ACTIVITY" -> {
                    size = activityList.size
                }
            }
            return size
        }

        override fun onBindViewHolder(holder: LikeFarmAndAcitivityViewHolder, position: Int) {
            holder.rowLikeFarmActivityBinding.apply {
                when (selectedType) {
                    "FARM" -> {
                        textViewLikeFarmAndActivityCnt.text = farmList[position].farm_like_cnt.toString()
                        textViewLikeFarmAndActivityPrice.text = farmList[position].farm_option_detail["price_area"].toString()
                        ratingBarLikeFarmAndActivity.rating = farmList[position].farm_star
                        textViewLikeFarmAndActivityName.text = farmList[position].farm_title
                        textViewLikeFarmAndActivityLocation.text = farmList[position].farm_address
                        CoroutineScope(Dispatchers.Main).launch {
                            if (farmList[position].farm_images != null) {
                                LikeDao.gettingImage(
                                    mainActivity,
                                    farmList[position].farm_images[0],
                                    imageViewLikeFarmAndActivity,
                                    "FARM"
                                )
                            } else {
                                holder.rowLikeFarmActivityBinding.imageViewLikeFarmAndActivity.setImageResource(R.color.white)
                            }
                        }
                    }

                    "ACTIVITY" -> {
                        textViewLikeFarmAndActivityCnt.text = activityList[position].activity_like_cnt.toString()
                        textViewLikeFarmAndActivityPrice.text = activityList[position].activity_option_detail[0]["option_price"].toString()
                        ratingBarLikeFarmAndActivity.rating = activityList[position].activity_star
                        textViewLikeFarmAndActivityName.text = activityList[position].activity_title
                        textViewLikeFarmAndActivityLocation.text =
                            activityList[position].activity_address
                        CoroutineScope(Dispatchers.Main).launch {
                            if (activityList[position].activity_images != null) {
                                LikeDao.gettingImage(
                                    mainActivity,
                                    activityList[position].activity_images[0],
                                    imageViewLikeFarmAndActivity,
                                    "ACTIVITY"
                                )
                            } else {
                                holder.rowLikeFarmActivityBinding.imageViewLikeFarmAndActivity.setImageResource(R.color.white)
                            }
                        }
                    }
                }
                // 아이템 클릭 이벤트
                holder.rowLikeFarmActivityBinding.root.setOnClickListener {
                    val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                    intent.putExtra(
                        "fragmentName",
                        FarmingLifeFragmnetName.FARMING_LIFE_FARM_DETAIL_FARMGNET
                    )
                    startActivity(intent)

                }

                // 하트 클릭 리스너
                holder.rowLikeFarmActivityBinding.apply {
                    likeViewModel!!.apply {
                        constraintLikeFarmAndActivityCancel.setOnClickListener {
                            constraintLikeFarmAndActivityCancel.setOnClickListener {
                                CoroutineScope(Dispatchers.Main).launch {
                                    when(selectedType){
                                        "FARM" -> {
                                            LikeDao.LikeListDelete( farmList[position].farm_idx, 2)
                                            farmList.removeAt(position)
                                        }
                                        "ACTIVITY" ->{
                                            LikeDao.LikeListDelete( activityList[position].activity_idx, 2)
                                            activityList.removeAt(position)
                                        }
                                    }
                                    fragmentLikeBinding.recyclerViewLike.adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 농기구 recyclerView의 Adapter
    inner class LikeRentalRecyclerViewAdapter :

        RecyclerView.Adapter<LikeRentalRecyclerViewAdapter.LikeRentalViewHolder>() {
        inner class LikeRentalViewHolder(rowLikeRentalBinding: RowLikeRentalBinding) :
            RecyclerView.ViewHolder(rowLikeRentalBinding.root) {
            val rowLikeRentalBinding: RowLikeRentalBinding

            init {
                this.rowLikeRentalBinding = rowLikeRentalBinding
                this.rowLikeRentalBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): LikeRentalViewHolder {
            val rowLikeRentalBinding = RowLikeRentalBinding.inflate(layoutInflater)
            val likeRentalViewHolder = LikeRentalViewHolder(rowLikeRentalBinding)
            return likeRentalViewHolder
        }

        override fun getItemCount(): Int {
            return rentalList.size
        }

        override fun onBindViewHolder(holder: LikeRentalViewHolder, position: Int) {
            holder.rowLikeRentalBinding.apply {
                textViewLikeRentalName.text = rentalList[position].rental_name
                textViewLikeRentalPhoneNumber.text = rentalList[position].rental_phone
                textViewLikeRentalAddress.text = rentalList[position].rental_address
            }
            // 아이템 클릭 이벤트
            holder.rowLikeRentalBinding.root.setOnClickListener {
                val intent = Intent(context, FarmingLifeToolsDetailActivity::class.java)
                startActivity(intent)
            }

            // 하트 클릭 리스너
            holder.rowLikeRentalBinding.apply {
                likeViewModel!!.apply {
                    constraintLikeRentalHeart.setOnClickListener {
                        constraintLikeRentalHeart.setOnClickListener {
                            CoroutineScope(Dispatchers.Main).launch {
                                LikeDao.LikeListDelete(rentalList[position].rental_idx.toInt(), 2)
                                rentalList.removeAt(position)
                                fragmentLikeBinding.recyclerViewLike.adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }

    }
}

