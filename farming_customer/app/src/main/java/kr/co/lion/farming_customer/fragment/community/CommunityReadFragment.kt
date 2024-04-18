package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.SwipeHelperCallback
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityReadBinding
import kr.co.lion.farming_customer.databinding.RowCommunityReadCommentBinding
import kr.co.lion.farming_customer.databinding.RowCommunityReadImageBinding
import kr.co.lion.farming_customer.viewmodel.community.CommunityCommentViewModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityReadViewModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel

class CommunityReadFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentCommunityReadBinding: FragmentCommunityReadBinding
    lateinit var communityActivity: CommunityActivity
    lateinit var communityReadViewModel: CommunityReadViewModel

    // 좋아요 수
    var communityReadLikeCnt = 0
    // 댓글 수
    var communityReadCommentCnt = 999

    // 이미지 저장용 리스트
    var imageCommunityStringList = mutableListOf<String>()


    // 현재 글 번호를 담을 변수
    var postIdx = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityReadBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_community_read, container, false)
        communityReadViewModel = CommunityReadViewModel()
        fragmentCommunityReadBinding.communityReadViewModel = communityReadViewModel
        fragmentCommunityReadBinding.lifecycleOwner = this

        communityActivity = activity as CommunityActivity

        // 글 번호를 받는다.
        postIdx = arguments?.getInt("postIdx")!!

        settingToolbar()
        settingTextViewCommunityReadInput()
        settingImageViewCommunityReadLike()
        settingRecyclerViewCommunityReadComment()


        return fragmentCommunityReadBinding.root
    }

    // 툴바 설정
    fun settingToolbar() {
        fragmentCommunityReadBinding.apply {
            toolbarCommunityRead.apply {
                communityReadViewModel?.textViewCommunityReadToolbarTitle?.value = "정보 게시판"
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


    // 좋아요 누르기
    fun settingImageViewCommunityReadLike() {
        fragmentCommunityReadBinding.apply {
            imageViewCommunityReadLike.setOnClickListener {
                if (imageViewCommunityReadLike.isSelected) {
                    communityReadLikeCnt++
                    !it.isSelected
                } else {
                    communityReadLikeCnt--
                }

            }
        }
    }

    // 커뮤니티 글 상세조회 이미지 뷰페이저 설정
    fun settingViewPager2CommunityReadImage() {
        fragmentCommunityReadBinding.apply {
            viewPager2CommunityReadImage.apply {
                adapter = CommunityReadImageViewPager2Adapter()
            }

            circleIndicatorCommunityRead.setViewPager(viewPager2CommunityReadImage)
            viewPager2CommunityReadImage.adapter?.registerAdapterDataObserver(circleIndicatorCommunityRead.adapterDataObserver)
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
            return imageCommunityStringList.size
        }

        override fun onBindViewHolder(holder: CommunityReadImageViewHolder, position: Int) {

            CoroutineScope(Dispatchers.Main).launch {

                CommunityPostDao.gettingCommunityPostImage(communityActivity, imageCommunityStringList[position], holder.rowCommunityReadImageBinding.imageViewRowCommunityRead)
            }
        }
    }

    // 커뮤니티 댓글 리사이클러뷰 설정
    fun settingRecyclerViewCommunityReadComment() {
        fragmentCommunityReadBinding.apply {
            recyclerViewCommunityReadComment.apply {
                adapter = CommunityReadCommentRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(communityActivity)

            }

            // 리사이클러뷰에 스와이프, 드래그 기능 달기
            val swipeHelperCallback = SwipeHelperCallback(CommunityReadCommentRecyclerViewAdapter()).apply {
                // 스와이프한 뒤 고정시킬 위치 지정
                setClamp(resources.displayMetrics.widthPixels.toFloat() * 2 / 7)    // 1080 / 4 = 270
            }
            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(recyclerViewCommunityReadComment)

            // 다른 곳 터치 시 기존 선택했던 뷰 닫기
            recyclerViewCommunityReadComment.setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(recyclerViewCommunityReadComment)
                false
            }
        }
    }

    // 커뮤니티 댓글 리사이클러뷰 어댑터 설정
    inner class CommunityReadCommentRecyclerViewAdapter : RecyclerView.Adapter<CommunityReadCommentRecyclerViewAdapter.CommunityReadViewHolder>() {
        inner class CommunityReadViewHolder(rowCommunityReadCommentBinding: RowCommunityReadCommentBinding) :
            RecyclerView.ViewHolder(rowCommunityReadCommentBinding.root) {
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
            val rowCommunityReadCommentBinding =
                DataBindingUtil.inflate<RowCommunityReadCommentBinding>(layoutInflater, R.layout.row_community_read_comment, parent, false)
            val communityCommentViewModel = CommunityCommentViewModel()
            rowCommunityReadCommentBinding.communityCommentViewModel = communityCommentViewModel
            rowCommunityReadCommentBinding.lifecycleOwner = this@CommunityReadFragment

            val communityReadImageViewHolder = CommunityReadViewHolder(rowCommunityReadCommentBinding)

            return communityReadImageViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CommunityReadViewHolder, position: Int) {
            holder.rowCommunityReadCommentBinding.communityCommentViewModel?.textViewRowCommunityReadCommentWriter?.value =
                "댓글 작성자 $position"
            holder.rowCommunityReadCommentBinding.communityCommentViewModel?.textViewRowCommunityReadCommentContent?.value =
                "댓글입니다 댓글입니다 댓글입니다 댓글입니다 $position"

            holder.rowCommunityReadCommentBinding.imageViewRowCommunityReadCommentModify.setOnClickListener {
                // 수정 기능
                Tools.showSoftInput(requireContext(), fragmentCommunityReadBinding.textInputCommunityReadSendComment)
            }

            holder.rowCommunityReadCommentBinding.imageViewRowCommunityReadCommentDelete.setOnClickListener {
                val dialog = DialogYesNo(
                    this@CommunityReadFragment,
                    "댓글을 삭제하시겠습니까?",
                    "한 번 삭제한 댓글은 복원할 수 없습니다.",
                    communityActivity,
                    position
                )
                dialog.show(communityActivity.supportFragmentManager, "DialogYesNo")
            }
        }

    }

    override fun onYesButtonClick(id: Int) {
        fragmentCommunityReadBinding.recyclerViewCommunityReadComment.adapter!!.notifyItemRemoved(id)
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }

    // 입력 요소에 값을 넣어준다.
    fun settingTextViewCommunityReadInput() {

        // 입력 요소에 띄어쓰기를 넣어준다.
        communityReadViewModel.textViewCommunityReadSubject.value = " "
        communityReadViewModel.textViewCommunityReadContent.value = " "
        communityReadViewModel.textViewCommunityReadDate.value = " "
        communityReadViewModel.textViewCommunityReadWriter.value = " "
        communityReadViewModel.textViewCommunityReadToolbarTitle.value = " "
        communityReadViewModel.textViewCommunityReadLikeCnt.value = " "
        communityReadViewModel.textViewCommunityReadCommentCnt.value = " "

        CoroutineScope(Dispatchers.Main).launch {
            // 현재 글 번호에 해당하는 글 데이터를 가져온다.
            val communityPostModel = CommunityPostDao.selectCommunityPostData(postIdx)
            imageCommunityStringList = communityPostModel?.postImages!!

            // 가져온 데이터를 보여준다.
            communityReadViewModel.textViewCommunityReadSubject.value = communityPostModel?.postTitle
            communityReadViewModel.textViewCommunityReadContent.value = communityPostModel?.postContent
            communityReadViewModel.textViewCommunityReadDate.value = communityPostModel?.postRegDt
            communityReadViewModel.textViewCommunityReadWriter.value = "글 작성자" // UserDao를 받아와서 userModel의 닉네임 갖고오기
            communityReadViewModel.textViewCommunityReadToolbarTitle.value = communityPostModel?.postType
            communityReadViewModel.textViewCommunityReadLikeCnt.value = communityPostModel?.postLikeCnt.toString()
            communityReadViewModel.textViewCommunityReadCommentCnt.value = communityPostModel?.postCommentCnt.toString()

            settingViewPager2CommunityReadImage()
        }
    }
}