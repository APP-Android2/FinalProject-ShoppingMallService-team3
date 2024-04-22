package kr.co.lion.farming_customer.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.FarmingLifeFragmnetName
import kr.co.lion.farming_customer.GridSpaceItemDecoration
import kr.co.lion.farming_customer.LikeType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.activity.farmingLifeTools.FarmingLifeToolsDetailActivity
import kr.co.lion.farming_customer.databinding.FragmentLikeBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.databinding.RowLikeFarmActivityBinding
import kr.co.lion.farming_customer.databinding.RowLikePostBinding
import kr.co.lion.farming_customer.databinding.RowLikeRentalBinding
import kr.co.lion.farming_customer.viewmodel.LikeViewModel

class LikeFragment : Fragment() {
    lateinit var fragmentLikeBinding: FragmentLikeBinding
    lateinit var mainActivity: MainActivity

    lateinit var likeViewModel : LikeViewModel

    lateinit var deco:MaterialDividerItemDecoration
    lateinit var deco2:GridSpaceItemDecoration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLikeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_like, container, false)
        likeViewModel = LikeViewModel()
        fragmentLikeBinding.likeViewModel = likeViewModel
        fragmentLikeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
        deco2 = GridSpaceItemDecoration(2,40,-10,-10,-10)

        settingUI()
        settingToggleButton()


        return fragmentLikeBinding.root
    }

    // 초기 설정
    fun settingUI(){
        likeViewModel.settingLikeType(LikeType.CROP)
    }

    // toggleButton 선택에 따라 recyclerView가 바뀌는 메서드
    fun settingToggleButton(){
        fragmentLikeBinding.apply {
            toggleGroupLikeType.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if(isChecked){
                    when(checkedId){
                        R.id.buttonLikeCrop -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            settingRecyclerViewLikeCrop()
                        }
                        R.id.buttonLikePost -> {
                            recyclerViewLike.visibility = View.GONE
                            recyclerViewPost.visibility = View.VISIBLE
                            recyclerViewRental.visibility = View.GONE
                            settingRecyclerViewLikePost()
                        }
                        R.id.buttonLikeFarm -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            settingRecyclerViewLikeFarmAndActivity()
                        }
                        R.id.buttonLikeActivity -> {
                            recyclerViewLike.visibility = View.VISIBLE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.GONE
                            settingRecyclerViewLikeFarmAndActivity()
                        }
                        R.id.buttonLikeRental -> {
                            recyclerViewLike.visibility = View.GONE
                            recyclerViewPost.visibility = View.GONE
                            recyclerViewRental.visibility = View.VISIBLE
                            settingRecyclerViewLikeRental()
                        }
                    }
                }
            }
        }
    }
    // 농산물 recyclerView 설정
    fun settingRecyclerViewLikeCrop(){
        fragmentLikeBinding.apply {
            recyclerViewLike.apply {
                removeItemDecoration(deco)
                if(itemDecorationCount == 0){
                    addItemDecoration(deco2)
                }
                adapter = LikeCropRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity,2)

            }
        }
    }

    // 게시판 recyclerView 설정
    fun settingRecyclerViewLikePost(){
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
    fun settingRecyclerViewLikeFarmAndActivity(){
        fragmentLikeBinding.apply {
            recyclerViewLike.apply {
                removeItemDecoration(deco)
                if(itemDecorationCount == 0){
                    addItemDecoration(deco2)
                }
                adapter = LikeFarmAndActivityRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity, 2)
            }
        }
    }

    // 농기구 recyclerView 설정
    fun settingRecyclerViewLikeRental(){
        fragmentLikeBinding.apply {
            recyclerViewRental.apply {
                removeItemDecoration(deco)
                adapter = LikeRentalRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }
    }

    // 농산물 recyclerView의 Adapter
    inner class LikeCropRecyclerViewAdapter:RecyclerView.Adapter<LikeCropRecyclerViewAdapter.LikeCropViewHolder>(){
        inner class LikeCropViewHolder(rowLikeCropBinding:RowLikeCropBinding):RecyclerView.ViewHolder(rowLikeCropBinding.root){
            val rowLikeCropBinding:RowLikeCropBinding

            init {
                this.rowLikeCropBinding = rowLikeCropBinding
                this.rowLikeCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeCropViewHolder {
            val rowLikeCropBinding = DataBindingUtil.inflate<RowLikeCropBinding>(layoutInflater,R.layout.row_like_crop,parent,false)
            val likeViewModel = LikeViewModel()
            rowLikeCropBinding.likeViewModel = likeViewModel
            rowLikeCropBinding.lifecycleOwner = this@LikeFragment
            val likeCropViewHolder = LikeCropViewHolder(rowLikeCropBinding)
            return likeCropViewHolder
        }

        override fun getItemCount(): Int {
            return 9
        }

        override fun onBindViewHolder(holder: LikeCropViewHolder, position: Int) {
            holder.rowLikeCropBinding.textViewLikeCropName.text = "못난이 감자$position"
            holder.rowLikeCropBinding.textViewLikeCropPrice.text = "${position}0,000원"
            holder.rowLikeCropBinding.likeViewModel?.textView_likeCropCnt?.value = "999"
            holder.rowLikeCropBinding.likeViewModel?.isLike?.value = true

            // 하트 클릭 리스너
            holder.rowLikeCropBinding.apply {
                likeViewModel!!.apply {
                    constraintLikeCropCancel.setOnClickListener {
                        if(isLike.value!!){
                            isLike.value = false
                            imageViewHeartCrop.setImageResource(R.drawable.heart_04)
                            textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageViewHeartCrop.setImageResource(R.drawable.heart_01)
                            textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                    }
                }
            }
        }
    }

    // 게시판 recyclerView의 Adapter
    inner class LikePostRecyclerViewAdapter:RecyclerView.Adapter<LikePostRecyclerViewAdapter.LikePostViewHolder>(){
        inner class LikePostViewHolder(rowLikePostBinding: RowLikePostBinding):RecyclerView.ViewHolder(rowLikePostBinding.root){
            val rowLikePostBinding:RowLikePostBinding

            init{
                this.rowLikePostBinding = rowLikePostBinding
                this.rowLikePostBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikePostViewHolder {
            val rowLikePostBinding = DataBindingUtil.inflate<RowLikePostBinding>(layoutInflater,R.layout.row_like_post,parent,false)
            val likeViewModel = LikeViewModel()
            rowLikePostBinding.likeViewModel = likeViewModel
            rowLikePostBinding.lifecycleOwner = this@LikeFragment
            val likePostViewHolder = LikePostViewHolder(rowLikePostBinding)
            return likePostViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: LikePostViewHolder, position: Int) {
            holder.rowLikePostBinding.apply {
                textViewLikePostLabel.text = "정보"
                textViewLikePostTitle.text = "글 제목$position"
                textViewLikePostContent.text = "글 내용입니다요 $position"
                textViewLikePostViewCnt.text = "$position"
                textViewLikePostCommentCnt.text = "$position"
                textViewLikePostDate.text = "2024-04-09"
                likeViewModel?.isLike?.value = true
                likeViewModel?.textView_likePostCnt?.value = "999"
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
                        if(isLike.value!!){
                            isLike.value = false
                            imageView.setImageResource(R.drawable.heart_04)
                            textViewLikePostCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageView.setImageResource(R.drawable.heart_01)
                            textViewLikePostCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                    }
                }
            }
        }
    }

    // 주말농장 및 체험활동 recyclerView의 Adapter
    inner class LikeFarmAndActivityRecyclerViewAdapter:RecyclerView.Adapter<LikeFarmAndActivityRecyclerViewAdapter.LikeFarmAndAcitivityViewHolder>(){
        inner class LikeFarmAndAcitivityViewHolder(rowLikeFarmActivityBinding: RowLikeFarmActivityBinding):RecyclerView.ViewHolder(rowLikeFarmActivityBinding.root){
            val rowLikeFarmActivityBinding:RowLikeFarmActivityBinding

            init {
                this.rowLikeFarmActivityBinding = rowLikeFarmActivityBinding
                this.rowLikeFarmActivityBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeFarmAndAcitivityViewHolder {
            val rowLikeFarmActivityBinding = DataBindingUtil.inflate<RowLikeFarmActivityBinding>(layoutInflater,R.layout.row_like_farm_activity,parent,false)
            val likeViewModel = LikeViewModel()
            rowLikeFarmActivityBinding.likeViewModel = likeViewModel
            rowLikeFarmActivityBinding.lifecycleOwner = this@LikeFragment
            val likeFarmAndActivityViewHolder = LikeFarmAndAcitivityViewHolder(rowLikeFarmActivityBinding)
            return likeFarmAndActivityViewHolder
        }

        override fun getItemCount(): Int {
            return 9
        }

        override fun onBindViewHolder(holder: LikeFarmAndAcitivityViewHolder, position: Int) {
            holder.rowLikeFarmActivityBinding.apply {
                textViewLikeFarmAndActivityName.text = "주말농장 이름$position"
                textViewLikeFarmAndActivityLocation.text = "00광역시 00구 00면 $position"
                textViewLikeFarmAndActivityPrice.text = "${position}0,000원"
                likeViewModel?.isLike?.value = true
                likeViewModel?.textView_likeFarmActivityCnt?.value = "999"
            }
            // 아이템 클릭 이벤트
            holder.rowLikeFarmActivityBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, FarmingLifeActivity::class.java)
                intent.putExtra("fragmentName", FarmingLifeFragmnetName.FARMING_LIFE_FARM_DETAIL_FARMGNET)
                startActivity(intent)

            }

            // 하트 클릭 리스너
            holder.rowLikeFarmActivityBinding.apply {
                likeViewModel!!.apply {
                    constraintLikeFarmAndActivityCancel.setOnClickListener {
                        if(isLike.value!!){
                            isLike.value = false
                            imageViewHeartFarmAndActivity.setImageResource(R.drawable.heart_04)
                            textViewLikeFarmAndActivityCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageViewHeartFarmAndActivity.setImageResource(R.drawable.heart_01)
                            textViewLikeFarmAndActivityCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                    }
                }
            }
        }
    }

    // 농기구 recyclerView의 Adapter
    inner class LikeRentalRecyclerViewAdapter:RecyclerView.Adapter<LikeRentalRecyclerViewAdapter.LikeRentalViewHolder>(){
        inner class LikeRentalViewHolder(rowLikeRentalBinding: RowLikeRentalBinding):RecyclerView.ViewHolder(rowLikeRentalBinding.root){
            val rowLikeRentalBinding:RowLikeRentalBinding

            init {
                this.rowLikeRentalBinding = rowLikeRentalBinding
                this.rowLikeRentalBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeRentalViewHolder {
            val rowLikeRentalBinding = DataBindingUtil.inflate<RowLikeRentalBinding>(layoutInflater,R.layout.row_like_rental,parent,false)
            val likeViewModel = LikeViewModel()
            rowLikeRentalBinding.likeViewModel = likeViewModel
            rowLikeRentalBinding.lifecycleOwner = this@LikeFragment
            val likeRentalViewHolder = LikeRentalViewHolder(rowLikeRentalBinding)
            return likeRentalViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: LikeRentalViewHolder, position: Int) {
            holder.rowLikeRentalBinding.apply {
                textViewLikeRentalName.text = "농기구점 이름$position"
                textViewLikeRentalPhoneNumber.text = "010-1111-111$position"
                textViewLikeRentalAddress.text = "00광역시 00구 00면 $position"
                likeViewModel?.isLike?.value = true
                likeViewModel?.textView_likeRentalCnt?.value = "999"
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
                        if(isLike.value!!){
                            isLike.value = false
                            imageViewHeart.setImageResource(R.drawable.heart_04)
                            textViewLikeRentalCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageViewHeart.setImageResource(R.drawable.heart_01)
                            textViewLikeRentalCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
                    }
                }
            }
        }

    }
}

