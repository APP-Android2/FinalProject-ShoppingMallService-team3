package kr.co.lion.farming_customer.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentHomeBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowGridItemBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel
import kr.co.lion.farming_customer.viewmodel.HomeViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowGridItemViewModel

class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        homeViewModel = HomeViewModel()
        fragmentHomeBinding.homeViewModel = homeViewModel
        fragmentHomeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        settingRecyclerView()

        return fragmentHomeBinding.root
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
            return 5
        }

        override fun onBindViewHolder(holder: ViewPagerFarmViewHolder, position: Int) {
            holder.rowGridItemBinding.rowGridItemViewModel!!.apply {
                textView_likeCnt.value = "999"
                textView_ItemName.value = "파밍이네 농장"
                textView_location.value = "경기도 파밍시 파밍구"
                textView_price.value = "20,000원 ~"
                isLike.value = false
            }
            // 하트 클릭 리스너
            holder.rowGridItemBinding.apply {
                rowGridItemViewModel!!.apply {
                    constraintLikeCancel.setOnClickListener {
                        if(isLike.value!!){
                            isLike.value = false
                            imageViewHeart.setImageResource(R.drawable.heart_02)
                            textViewLikeCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))

                        }else{
                            isLike.value = true
                            imageViewHeart.setImageResource(R.drawable.heart_01)
                            textViewLikeCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        }
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

            }
        }
    }
}