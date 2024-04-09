package kr.co.lion.farming_customer.fragment.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityReadBinding
import kr.co.lion.farming_customer.databinding.RowCommunityReadCommentBinding
import kr.co.lion.farming_customer.databinding.RowCommunityReadImageBinding
import kr.co.lion.farming_customer.fragment.FarmingLifeBottomSheetFragment
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel

class CommunityReadFragment : Fragment() {
    lateinit var fragmentCommunityReadBinding: FragmentCommunityReadBinding
    lateinit var communityActivity: CommunityActivity
    lateinit var communityViewModel: CommunityViewModel

    // 좋아요 개수
    var communityReadLikeCnt = 999
    // 댓글 개수
    var communityReadCommentCnt = 999

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityReadBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_community_read, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityReadBinding.communityViewModel = communityViewModel
        fragmentCommunityReadBinding.lifecycleOwner = this

        communityActivity = activity as CommunityActivity

        settingToolbar()
        settingViewPager2CommunityReadImage()
        settingTextViewCommunityReadInput()
        settingRecyclerViewCommunityReadComment()


        return fragmentCommunityReadBinding.root
    }

    // 툴바 설정
    fun settingToolbar() {
        fragmentCommunityReadBinding.apply {
            toolbarCommunityRead.apply {
                communityViewModel?.textViewCommunityReadToolbarTitle?.value = "정보 게시판"
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    communityActivity.finish()
                }
                inflateMenu(R.menu.menu_community)
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.menuItemCommunityKebab -> {
                            showCommunityBottom()
                        }
                    }
                    true
                }
            }
        }
    }

    // 커뮤니티 BottomSheet를 띄워준다.
    fun showCommunityBottom() {
        val communityBottomSheetFragment = CommunityBottomSheetFragment(this)
        communityBottomSheetFragment.show(communityActivity.supportFragmentManager, "CommunityBottomSheetFragment")
    }

    // 좋아요 개수, 댓글 개수, 글 작성자, 글 내용 설정
    fun settingTextViewCommunityReadInput() {
        fragmentCommunityReadBinding.apply {
            // 좋아요 개수
            communityViewModel?.textViewCommunityReadLikeCnt?.value = communityReadLikeCnt.toString()
            // 댓글 개수
            communityViewModel?.textViewCommunityReadCommentCnt?.value = communityReadCommentCnt.toString()
            // 글 작성자
            communityViewModel?.textViewCommunityReadWriter?.value = "글 작성자"
            // 글 제목
            communityViewModel?.textViewCommunityReadSubject?.value = "글 제목입니다"
            // 글 내용
            communityViewModel?.textViewCommunityReadContent?.value = "글 내용입니다 글 내용입니다 글 내용입니다 글 내용입니다 \n" +
                    "글 내용입니다 글 내용입니다 글 내용입니다 글 내용입니다 \n" +
                    "글 내용입니다 글 내용입니다 글 내용입니다"
        }
    }

    // 커뮤니티 글 상세조회 이미지 뷰페이저 설정
    fun settingViewPager2CommunityReadImage() {
        fragmentCommunityReadBinding.apply {
            viewPager2CommunityReadImage.apply {
                adapter = CommunityReadImageViewPager2Adapter()
                Log.d("test1234", "test1234")
            }

            val circleIndicatorCommunityRead = circleIndicatorCommunityRead
            circleIndicatorCommunityRead.setViewPager(viewPager2CommunityReadImage)
        }
    }

    // 커뮤니티 글 상세조회 이미지 뷰페이저 어댑터
    inner class CommunityReadImageViewPager2Adapter : RecyclerView.Adapter<CommunityReadImageViewPager2Adapter.CommunityReadImageViewHolder>() {
        inner class CommunityReadImageViewHolder(rowCommunityReadImageBinding: RowCommunityReadImageBinding) : RecyclerView.ViewHolder(rowCommunityReadImageBinding.root) {
            val rowCommunityReadImageBinding: RowCommunityReadImageBinding

            init {
                this.rowCommunityReadImageBinding = rowCommunityReadImageBinding

                this.rowCommunityReadImageBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): CommunityReadImageViewHolder {
            val rowCommunityReadImageBinding = DataBindingUtil.inflate<RowCommunityReadImageBinding>(layoutInflater, R.layout.row_community_read_image, parent,false)
            val communityViewModel = CommunityViewModel()
            rowCommunityReadImageBinding.communityViewModel = communityViewModel
            rowCommunityReadImageBinding.lifecycleOwner = this@CommunityReadFragment

            val communityReadImageViewHolder = CommunityReadImageViewHolder(rowCommunityReadImageBinding)

            return communityReadImageViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: CommunityReadImageViewHolder, position: Int) {
            holder.rowCommunityReadImageBinding.imageViewRowCommunityRead.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    // 커뮤니티 댓글 리사이클러뷰 설정
    fun settingRecyclerViewCommunityReadComment() {
        fragmentCommunityReadBinding.apply {
            recyclerViewCommunityReadComment.apply {
                adapter = CommunityReadCommentRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(communityActivity)
                val deco = MaterialDividerItemDecoration(communityActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 커뮤니티 댓글 리사이클러뷰 어댑터 설정
    inner class CommunityReadCommentRecyclerViewAdapter : RecyclerView.Adapter<CommunityReadCommentRecyclerViewAdapter.CommunityReadViewHolder>() {
        inner class CommunityReadViewHolder(rowCommunityReadCommentBinding: RowCommunityReadCommentBinding) : RecyclerView.ViewHolder(rowCommunityReadCommentBinding.root) {
            val rowCommunityReadCommentBinding: RowCommunityReadCommentBinding

            init {
                this.rowCommunityReadCommentBinding = rowCommunityReadCommentBinding

                this.rowCommunityReadCommentBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityReadViewHolder {
            val rowCommunityReadCommentBinding = DataBindingUtil.inflate<RowCommunityReadCommentBinding>(layoutInflater,R.layout.row_community_read_comment, parent, false)
            val communityViewModel = CommunityViewModel()
            rowCommunityReadCommentBinding.communityViewModel = communityViewModel
            rowCommunityReadCommentBinding.lifecycleOwner = this@CommunityReadFragment

            val communityReadImageViewHolder = CommunityReadViewHolder(rowCommunityReadCommentBinding)

            return communityReadImageViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CommunityReadViewHolder, position: Int) {
            holder.rowCommunityReadCommentBinding.communityViewModel?.textViewRowCommunityReadCommentWriter?.value = "댓글 작성자 $position"
            holder.rowCommunityReadCommentBinding.communityViewModel?.textViewRowCommunityReadCommentContent?.value = "댓글입니다 댓글입니다 댓글입니다 댓글입니다"
        }
    }

}