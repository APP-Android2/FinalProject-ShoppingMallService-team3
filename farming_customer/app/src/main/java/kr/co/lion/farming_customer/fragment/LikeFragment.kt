package kr.co.lion.farming_customer.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.activity.farmingLifeTools.FarmingLifeToolsDetailActivity
import kr.co.lion.farming_customer.dao.like.LikeDao
import kr.co.lion.farming_customer.databinding.FragmentLikeBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.databinding.RowLikeFarmActivityBinding
import kr.co.lion.farming_customer.databinding.RowLikePostBinding
import kr.co.lion.farming_customer.databinding.RowLikeRentalBinding
import kr.co.lion.farming_customer.model.ActivityModel
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
            val data = likeViewModel.getLikeListAndLikeTypeList("1", "CROP")
            selectedType = "CROP"
            LikeList = data.LikeList!!
            cropList = data.cropList!!
            postList = data.postList!!
            farmList = data.farmList!!
            activityList = data.activityList!!
            rentalList = data.rentalList!!


//            val Data = likeViewModel.getLikeListAndLikeTypeList(userIdx.toString(), 1)
//            cropLikeList = Data.LikeList!!
//            cropList = Data.cropList!!
            // 여기서 likeList와 cropList를 사용할 수 있습니다.

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
                            loadLikeData(selectedType)
                            settingRecyclerViewLikeCrop()
                        }

                        R.id.buttonLikePost -> {
                            recyclerViewLike.visibility = View.GONE
                            recyclerViewPost.visibility = View.VISIBLE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "POST"
                            loadLikeData(selectedType)
                            settingRecyclerViewLikePost()
                        }

                        R.id.buttonLikeFarm -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "FARM"
                            loadLikeData(selectedType)
                            settingRecyclerViewLikeFarmAndActivity()
                        }

                        R.id.buttonLikeActivity -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            selectedType = "ACTIVITY"
                            loadLikeData(selectedType)
                            settingRecyclerViewLikeFarmAndActivity()
                        }

                        R.id.buttonLikeRental -> {
                            recyclerViewLike.visibility = View.GONE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.VISIBLE
                            selectedType = "RENTAL"
                            loadLikeData(selectedType)
                            settingRecyclerViewLikeRental()
                        }
                    }

//                    // 각 리사이클러뷰 어댑터 직접 생성 및 설정
//                    when (selectedType) {
//                        "CROP" -> {
//                            val cropAdapter = LikeCropRecyclerViewAdapter() // CropListAdapter 생성
//                            recyclerViewLike.adapter = cropAdapter // recyclerViewLike 어댑터 설정
//                        }
//                        "POST" -> {
//                            val postAdapter = LikePostRecyclerViewAdapter() // PostListAdapter 생성
//                            recyclerViewPost.adapter = postAdapter // recyclerViewPost 어댑터 설정
//                        }
//                        "FARM", "ACTIVITY" -> {
//                            val farmActivityAdapter = LikeFarmAndActivityRecyclerViewAdapter() // FarmActivityListAdapter 생성
//                            recyclerViewLike.adapter = farmActivityAdapter // recyclerViewLike 어댑터 설정
//                        }
//                        "RENTAL" -> {
//                            val rentalAdapter = LikeRentalRecyclerViewAdapter() // 매개변수 없이 RentalListAdapter 생성
//                            recyclerViewRental.adapter = rentalAdapter // recyclerViewRental 어댑터 설정
//                        }
//                    }

                }
            }
        }
    }



    private fun loadLikeData(selectedType: String) {
        lifecycleScope.launch {
            val data = likeViewModel.getLikeListAndLikeTypeList("1", selectedType)
            LikeList = data.LikeList!!
            when(selectedType){
                "CROP" -> {
                    cropList = data.cropList!!
                    likeViewModel.settingLikeType(LikeType.CROP)
                }
                "POST" -> {
                    postList = data.postList!!
                    likeViewModel.settingLikeType(LikeType.POST)
                }
                "FARM" -> {
                    farmList = data.farmList!!
                    likeViewModel.settingLikeType(LikeType.FARM)
                }
                "ACTIVITY" -> {
                    activityList = data.activityList!!
                    likeViewModel.settingLikeType(LikeType.ACTIVITY)
                }
                "RENTAL" -> {
                    rentalList = data.rentalList!!
                    likeViewModel.settingLikeType(LikeType.RENTAL)
                }


            }
//            Log.d("test11", farmList.toString())

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
                (adapter as LikeCropRecyclerViewAdapter).notifyDataSetChanged()
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
                (adapter as LikePostRecyclerViewAdapter).notifyDataSetChanged()
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
            val rowLikeCropBinding = RowLikeCropBinding.inflate(layoutInflater)
            val likeCropViewHolder = LikeCropViewHolder(rowLikeCropBinding)
            return likeCropViewHolder
        }

        override fun getItemCount(): Int {
            return LikeList.size
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
                context?.let {
                    Glide.with(it).load(cropList[position].crop_images[0])
                        .into(imageViewLikeCrop)
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
            val rowLikePostBinding = RowLikePostBinding.inflate(layoutInflater)
            val likePostViewHolder = LikePostViewHolder(rowLikePostBinding)
            return likePostViewHolder
        }

        override fun getItemCount(): Int {
            return LikeList.size
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
                        LikeDao.gettingCommunityPostImage(mainActivity, postList[position].postImages!![0], imageViewLikePost)
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

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): LikeFarmAndAcitivityViewHolder {
            val rowLikeFarmActivityBinding = RowLikeFarmActivityBinding.inflate(layoutInflater)
            val likeFarmAndActivityViewHolder =
                LikeFarmAndAcitivityViewHolder(rowLikeFarmActivityBinding)
            return likeFarmAndActivityViewHolder
        }

        override fun getItemCount(): Int {
            return LikeList.size
        }

        override fun onBindViewHolder(holder: LikeFarmAndAcitivityViewHolder, position: Int) {
            holder.rowLikeFarmActivityBinding.apply {
                Log.d("test11", farmList.toString())
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
                    }

                    "ACTIVITY" -> {
                        textViewLikeFarmAndActivityName.text = activityList[position].activity_title
                        textViewLikeFarmAndActivityLocation.text =
                            activityList[position].activity_address
                        textViewLikeFarmAndActivityPrice.text =
                            activityList[position].activity_option_detail[0]["option_price"]
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
            return LikeList.size
        }

        override fun onBindViewHolder(holder: LikeRentalViewHolder, position: Int) {
            holder.rowLikeRentalBinding.apply {
                if (rentalList.isEmpty() ) {
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
        }

    }
}
