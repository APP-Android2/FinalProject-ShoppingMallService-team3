package kr.co.lion.farming_customer.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.GridSpaceItemDecoration
import kr.co.lion.farming_customer.LikeType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.activity.farmingLifeTools.FarmingLifeToolsDetailActivity
import kr.co.lion.farming_customer.dao.like.LikeDao
import kr.co.lion.farming_customer.databinding.FragmentLikeBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.databinding.RowLikeFarmActivityBinding
import kr.co.lion.farming_customer.databinding.RowLikePostBinding
import kr.co.lion.farming_customer.databinding.RowLikeRentalBinding
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.FarmModel
import kr.co.lion.farming_customer.model.LikeModel
import kr.co.lion.farming_customer.model.CommunityPostModel
import kr.co.lion.farming_customer.model.RentalModel
import kr.co.lion.farming_customer.viewmodel.LikeViewModel

class LikeFragment : Fragment() {
    // ViewModel 생성자에 Context 객체를 명시적으로 전달합니다.
    // 세션에서 회원 시퀀스 번호 가져오기..
    val sharedPreferences =
        context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    var userIdx: String? = sharedPreferences?.getString("user_idx", "")

    lateinit var fragmentLikeBinding: FragmentLikeBinding
    lateinit var mainActivity: MainActivity

    lateinit var likeViewModel: LikeViewModel

    lateinit var deco: MaterialDividerItemDecoration
    lateinit var deco2: GridSpaceItemDecoration

    lateinit var LikeList: List<LikeModel>
    lateinit var cropList: List<CropModel>
    lateinit var postList: List<CommunityPostModel>
    lateinit var farmList: List<FarmModel>
    lateinit var activityList: List<ActivityModel>
    lateinit var rentalList: List<RentalModel>
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

        lifecycleScope.launch {
            val data = likeViewModel.getLikeListAndLikeTypeList("1")
            selectedType = "CROP"
            LikeList = data.LikeList!!
            cropList = data.cropList!!
            postList = data.postList!!
            farmList = data.farmList!!
            activityList = data.activityList!!
            rentalList = data.rentalList!!
            Log.d("test1124", "전체데이터아다아아아아아------" + data.toString())
            Log.d("test1124", "농산품이다아ㅏㅇ" + cropList.toString())
            Log.d("test1124", "주말농장이가아아" + farmList.toString())
            Log.d("test1124", "체험활동이가아아" + activityList.toString())

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
//                            recyclerViewLike.adapter?.notifyDataSetChanged()
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
//                            recyclerViewLike.adapter?.notifyDataSetChanged()
                        }

                        R.id.buttonLikeActivity -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "ACTIVITY"
                            settingRecyclerViewLikeFarmAndActivity()

//                            recyclerViewLike.adapter?.notifyDataSetChanged()
                        }

                        R.id.buttonLikeRental -> {
                            recyclerViewLike.visibility = View.GONE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.VISIBLE
                            selectedType = "RENTAL"
                            settingRecyclerViewLikeRental()

//                            recyclerViewRental.adapter?.notifyDataSetChanged()

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
                removeItemDecoration(deco)
                if (itemDecorationCount == 0) {
                    addItemDecoration(deco2)
                }
                adapter = LikePostRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
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
            Log.d("test77", cropList.size.toString())
            return cropList.size
        }

        override fun onBindViewHolder(holder: LikeCropViewHolder, position: Int) {
            holder.rowLikeCropBinding.apply {
                if (cropList.isEmpty()) {
                    root.visibility = View.GONE
                    return
                }
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
            // 하트 클릭 리스너
            holder.rowLikeCropBinding.apply {
                likeViewModel!!.apply {
                    constraintLikeCropCancel.setOnClickListener {
                        if (isLike.value!!) {
                            isLike.value = false
                            imageViewHeartCrop.setImageResource(R.drawable.heart_04)
                            textViewLikeCropCnt.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.brown_01
                                )
                            )

                        } else {
                            isLike.value = true
                            imageViewHeartCrop.setImageResource(R.drawable.heart_01)
                            textViewLikeCropCnt.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                        }
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
//                Log.d("test11", postList.toString())
                if (postList.isEmpty()) {
                    root.visibility = View.GONE
                    return
                }
                textViewLikePostLabel.text = postList[position].postType
                textViewLikePostTitle.text = postList[position].postTitle.substring(10)
                textViewLikePostContent.text = postList[position].postContent.substring(20)
                textViewLikePostViewCnt.text = postList[position].postViewCnt.toString()
                textViewLikePostCommentCnt.text = postList[position].postCommentCnt.toString()
                textViewLikePostDate.text = postList[position].postRegDt
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
                        if (isLike.value!!) {
                            isLike.value = false
                            imageView.setImageResource(R.drawable.heart_04)
                            textViewLikePostCnt.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.brown_01
                                )
                            )

                        } else {
                            isLike.value = true
                            imageView.setImageResource(R.drawable.heart_01)
                            textViewLikePostCnt.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
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
//            Log.d("test77", size.toString())
            return size
        }

        override fun onBindViewHolder(holder: LikeFarmAndAcitivityViewHolder, position: Int) {
            holder.rowLikeFarmActivityBinding.apply {
                if (farmList.isEmpty() || activityList.isEmpty()) {
                    root.visibility = View.GONE
                    return
                }

                when (selectedType) {
                    "FARM" -> {
                        textViewLikeFarmAndActivityName.text = farmList[position].farm_title
                        textViewLikeFarmAndActivityLocation.text = farmList[position].farm_address
//                        textViewLikeFarmAndActivityPrice.text =
//                            farmList[position].farm_option_detail.["price_area"]
//                        Log.d("test11", farmList[0].toString())
                    }

                    "ACTIVITY" -> {
                        textViewLikeFarmAndActivityName.text = activityList[position].activity_title
                        textViewLikeFarmAndActivityLocation.text =
                            activityList[position].activity_address
//                        textViewLikeFarmAndActivityPrice.text =
//                            activityList[position].activity_option_detail[0]["option_price"].toString()
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
                            if (isLike.value!!) {
                                isLike.value = false
                                imageViewHeartFarmAndActivity.setImageResource(R.drawable.heart_04)
                                textViewLikeFarmAndActivityCnt.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.brown_01
                                    )
                                )

                            } else {
                                isLike.value = true
                                imageViewHeartFarmAndActivity.setImageResource(R.drawable.heart_01)
                                textViewLikeFarmAndActivityCnt.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.white
                                    )
                                )
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
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeRentalViewHolder {
//            val rowLikeRentalBinding = DataBindingUtil.inflate<RowLikeRentalBinding>(layoutInflater,R.layout.row_like_rental,parent,false)
//            val likeViewModel = LikeViewModel()
//            rowLikeRentalBinding.likeViewModel = likeViewModel
//            rowLikeRentalBinding.lifecycleOwner = this@LikeFragment
//            val likeRentalViewHolder = LikeRentalViewHolder(rowLikeRentalBinding)
//            return likeRentalViewHolder
//        }

        override fun getItemCount(): Int {
            return rentalList.size
        }

        override fun onBindViewHolder(holder: LikeRentalViewHolder, position: Int) {
            holder.rowLikeRentalBinding.apply {
                if (rentalList.isEmpty()) {
                    root.visibility = View.GONE
                    return
                }
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
                        if (isLike.value!!) {
                            isLike.value = false
                            imageViewHeart.setImageResource(R.drawable.heart_04)
                            textViewLikeRentalCnt.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.brown_01
                                )
                            )

                        } else {
                            isLike.value = true
                            imageViewHeart.setImageResource(R.drawable.heart_01)
                            textViewLikeRentalCnt.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                        }
                    }
                }
            }
        }

    }
}

