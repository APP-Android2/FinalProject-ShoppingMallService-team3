package kr.co.lion.farming_customer.fragment.community

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.CommentStatus
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.SwipeHelperCallback
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.dao.CommunityCommentDao
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityReadBinding
import kr.co.lion.farming_customer.databinding.RowCommunityReadCommentBinding
import kr.co.lion.farming_customer.databinding.RowCommunityReadImageBinding
import kr.co.lion.farming_customer.model.CommunityCommentModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityCommentViewModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityReadViewModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel
import java.text.SimpleDateFormat
import java.util.Date

class CommunityReadFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentCommunityReadBinding: FragmentCommunityReadBinding
    lateinit var communityActivity: CommunityActivity
    lateinit var communityReadViewModel: CommunityReadViewModel

    // 댓글 모델
    var model:CommunityCommentModel? = null

    // 이미지 저장용 리스트
    var imageCommunityStringList = mutableListOf<String>()

    // 댓글 정보를 가지고 있는 리스트
    var commentList = mutableListOf<CommunityCommentModel>()

    // 댓글 유저 리스트
    var userList = mutableListOf<UserModel>()

    // 현재 글 번호를 담을 변수
    var postIdx = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityReadBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_community_read, container, false)
        communityReadViewModel = CommunityReadViewModel()
        fragmentCommunityReadBinding.communityReadViewModel = communityReadViewModel
        fragmentCommunityReadBinding.lifecycleOwner = this

        communityActivity = activity as CommunityActivity

        // 글 번호를 받는다.
        postIdx = arguments?.getInt("postIdx")!!

        settingToolbar()
        settingImageViewCommunityReadLike()
        settingInputForm()
        commentDoneProcess()
        settingTextViewCommunityReadInput()
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
                menu.findItem(R.id.menuItemCommunityKebab).isVisible = false

                CoroutineScope(Dispatchers.Main).launch {
                    // 현재 글 번호에 해당하는 글 데이터를 가져온다.
                    val communityPostModel = CommunityPostDao.selectCommunityPostData(postIdx)
                    val sharedPreferences = communityActivity.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE)
                    val userIdx = sharedPreferences.getInt("loginUserIdx", -1)


                    if (communityPostModel!!.postUserIdx == userIdx) {
                        menu.findItem(R.id.menuItemCommunityKebab).isVisible = true

                    }

                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.menuItemCommunityKebab -> {
                                showCommunityBottom()
                            }
                        }
                        true
                    }
                }

            }
        }
    }

    // 커뮤니티 BottomSheet를 띄워준다.
    fun showCommunityBottom() {
        val communityBottomSheetFragment = CommunityBottomSheetFragment(this, postIdx)
        communityBottomSheetFragment.show(communityActivity.supportFragmentManager, "CommunityBottomSheetFragment")
    }


    // 좋아요 누르기
    fun settingImageViewCommunityReadLike() {

    }

    // 커뮤니티 글 상세조회 이미지 뷰페이저 설정
    fun settingViewPager2CommunityReadImage() {
        fragmentCommunityReadBinding.apply {
            viewPager2CommunityReadImage.apply {
                adapter = CommunityReadImageViewPager2Adapter()
            }

            circleIndicatorCommunityRead.setViewPager(viewPager2CommunityReadImage)
            viewPager2CommunityReadImage.adapter?.registerAdapterDataObserver(
                circleIndicatorCommunityRead.adapterDataObserver
            )
        }
    }

    // 커뮤니티 글 상세조회 이미지 뷰페이저 어댑터
    inner class CommunityReadImageViewPager2Adapter :
        RecyclerView.Adapter<CommunityReadImageViewPager2Adapter.CommunityReadImageViewHolder>() {
        inner class CommunityReadImageViewHolder(rowCommunityReadImageBinding: RowCommunityReadImageBinding) :
            RecyclerView.ViewHolder(rowCommunityReadImageBinding.root) {
            val rowCommunityReadImageBinding: RowCommunityReadImageBinding

            init {
                this.rowCommunityReadImageBinding = rowCommunityReadImageBinding

                this.rowCommunityReadImageBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityReadImageViewHolder {
            val rowCommunityReadImageBinding = DataBindingUtil.inflate<RowCommunityReadImageBinding>(layoutInflater, R.layout.row_community_read_image, parent, false)
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
            // 사용자 정보를 가져온다.
            val userModel = UserDao.gettingUserInfoByUserIdx(communityPostModel!!.postUserIdx)

            // 댓글 데이터
            model = generatingCommentObject()

            if (communityPostModel?.postImages != null) {
                imageCommunityStringList = communityPostModel.postImages!!
            } else {
                fragmentCommunityReadBinding.viewPager2CommunityReadImage.visibility = View.GONE
                fragmentCommunityReadBinding.circleIndicatorCommunityRead.visibility = View.GONE
            }

            // 가져온 데이터를 보여준다.
            communityReadViewModel.textViewCommunityReadSubject.value = communityPostModel?.postTitle
            communityReadViewModel.textViewCommunityReadContent.value = communityPostModel?.postContent
            communityReadViewModel.textViewCommunityReadDate.value = communityPostModel?.postRegDt
            communityReadViewModel.textViewCommunityReadWriter.value = userModel!!.user_nickname
            communityReadViewModel.textViewCommunityReadToolbarTitle.value = communityPostModel?.postType

            fragmentCommunityReadBinding.apply {
                if (communityPostModel?.postLikeState == true) {
                    imageViewCommunityReadLike.setImageResource(R.drawable.heart_01)
                    communityReadViewModel?.textViewCommunityReadLikeCnt?.value = communityPostModel.postLikeCnt.toString()
                } else {
                    imageViewCommunityReadLike.setImageResource(R.drawable.heart_04)
                    communityReadViewModel?.textViewCommunityReadLikeCnt?.value = communityPostModel?.postLikeCnt.toString()
                }

                imageViewCommunityReadLike.setOnClickListener {
                    if (communityPostModel?.postLikeState == false) {
                        communityPostModel.postLikeState = true
                        imageViewCommunityReadLike.setImageResource(R.drawable.heart_01)
                        communityPostModel.postLikeCnt += 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(communityPostModel, communityPostModel.postLikeState)
                        }
                    } else {
                        communityPostModel?.postLikeState = false
                        imageViewCommunityReadLike.setImageResource(R.drawable.heart_04)
                        communityPostModel!!.postLikeCnt -= 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(communityPostModel!!, communityPostModel.postLikeState)
                        }
                    }
                    communityReadViewModel?.textViewCommunityReadLikeCnt?.value = communityPostModel?.postLikeCnt.toString()
                }
            }

            val job2 = CoroutineScope(Dispatchers.IO).launch {
                communityReadViewModel.textViewCommunityReadCommentCnt.postValue(model!!.commentCnt.toString())
            }
            job2.join()

            val job3 = CoroutineScope(Dispatchers.Main).launch {
                UserDao.gettingUserImage(communityActivity, userModel.user_profile_image, fragmentCommunityReadBinding.imageViewCommunityReadProfile)
            }
            job3.join()

            settingViewPager2CommunityReadImage()

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
            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(fragmentCommunityReadBinding.recyclerViewCommunityReadComment)

            // 다른 곳 터치 시 기존 선택했던 뷰 닫기
            fragmentCommunityReadBinding.recyclerViewCommunityReadComment.setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(fragmentCommunityReadBinding.recyclerViewCommunityReadComment)
                false
            }

            gettingCommentData()
        }
    }

    // 커뮤니티 댓글 리사이클러뷰 어댑터 설정
    inner class CommunityReadCommentRecyclerViewAdapter :
        RecyclerView.Adapter<CommunityReadCommentRecyclerViewAdapter.CommunityReadViewHolder>() {
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
            val rowCommunityReadCommentBinding = DataBindingUtil.inflate<RowCommunityReadCommentBinding>(layoutInflater, R.layout.row_community_read_comment, parent, false)
            val communityCommentViewModel = CommunityCommentViewModel()
            rowCommunityReadCommentBinding.communityCommentViewModel = communityCommentViewModel
            rowCommunityReadCommentBinding.lifecycleOwner = this@CommunityReadFragment

            val communityReadImageViewHolder = CommunityReadViewHolder(rowCommunityReadCommentBinding)

            return communityReadImageViewHolder
        }

        override fun getItemCount(): Int {
            return commentList.size
        }

        override fun onBindViewHolder(holder: CommunityReadViewHolder, position: Int) {

            userList.forEach {
                if (it.user_idx == commentList[position].commentUserIdx) {
                    holder.rowCommunityReadCommentBinding.communityCommentViewModel?.textViewRowCommunityReadCommentWriter?.value = it.user_nickname

                    CoroutineScope(Dispatchers.Main).launch {
                        UserDao.gettingUserImage(communityActivity, it.user_profile_image, holder.rowCommunityReadCommentBinding.imageViewRowCommunityReadCommentProfile)
                    }

                }
            }

            holder.rowCommunityReadCommentBinding.communityCommentViewModel?.textViewRowCommunityReadCommentContent?.value = commentList[position].commentContent

            val sharedPreferences = communityActivity.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE)
            val userIdx = sharedPreferences.getInt("loginUserIdx", -1)

            // 로그인한 사용자와 댓글 작성자가 같다면
            if (userIdx == commentList[position].commentUserIdx) {

                holder.rowCommunityReadCommentBinding.imageViewRowCommunityReadCommentModify.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {

                        // 수정 기능
                        Tools.showSoftInput(requireContext(), fragmentCommunityReadBinding.textInputCommunityReadSendComment)
                        communityReadViewModel.textInputCommunityReadSendComment.value = commentList[position].commentContent
                        fragmentCommunityReadBinding.imageViewCommunityReadSendComment.setOnClickListener {
                            commentModifyDoneProcess(position, commentList[position].commentIdx)
                        }

                    }
                }

                holder.rowCommunityReadCommentBinding.imageViewRowCommunityReadCommentDelete.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {

                        // 댓글 수가 1보다 큰 경우에만
                        if (communityReadViewModel.textViewCommunityReadCommentCnt.value!!.toInt() > 1 ) {
                            communityReadViewModel.textViewCommunityReadCommentCnt.value = (communityReadViewModel.textViewCommunityReadCommentCnt.value!!.toInt() - 1).toString()
                            CommunityCommentDao.updateCommunityCommentStatus(commentList[position].commentIdx, CommentStatus.COMMENT_STATUS_REMOVE)
                            removingCommentData(position)
                        } else {
                            communityReadViewModel.textViewCommunityReadCommentCnt.value = "0"
                            CommunityCommentDao.updateCommunityCommentStatus(commentList[0].commentIdx, CommentStatus.COMMENT_STATUS_REMOVE)
                            removingCommentData(0)
                        }
                    }
                }
            } else {
                holder.rowCommunityReadCommentBinding.imageViewRowCommunityReadCommentModify.isVisible = false
                holder.rowCommunityReadCommentBinding.imageViewRowCommunityReadCommentDelete.isVisible = false
            }


        }
    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }

    // 댓글 입력 요소 설정
    fun settingInputForm() {
        communityReadViewModel?.textInputCommunityReadSendComment?.value = ""
    }

    // 댓글 입력 객체 생성
    suspend fun generatingCommentObject(): CommunityCommentModel {

        var communityCommentModel = CommunityCommentModel()
        val sharedPreferences = communityActivity.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE)
        val userIdx = sharedPreferences.getInt("loginUserIdx", -1)

        val job1 = CoroutineScope(Dispatchers.Main).launch {
            // 댓글 번호를 가져온다.
            val commentSequence = CommunityCommentDao.getCommunityCommentSequence()
            // 댓글 번호를 업데이트 한다.
            CommunityCommentDao.updateCommunityCommentSequence(commentSequence + 1)
            // 저장할 데이터를 담는다.
            communityCommentModel.commentIdx = commentSequence + 1
            communityCommentModel.commentUserIdx = userIdx
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
            communityCommentModel.commentRegDt = simpleDateFormat.format(Date())
            communityCommentModel.commentModDt = ""
            communityCommentModel.commentPostIdx = postIdx
            communityCommentModel.commentContent = communityReadViewModel?.textInputCommunityReadSendComment?.value!!
            communityCommentModel.commentCnt = commentList.size
            communityCommentModel.commentStatus = CommentStatus.COMMENT_STATUS_NORMAL.number
        }
        job1.join()

        return communityCommentModel
    }

    // 댓글 입력 완료 처리
    fun commentDoneProcess() {
        fragmentCommunityReadBinding.imageViewCommunityReadSendComment.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                model = generatingCommentObject()
                CommunityCommentDao.insertCommunityCommentData(model!!)
                gettingCommentData()
                Tools.hideSoftInput(communityActivity)
                settingInputForm()

            }
        }
    }

    // 댓글 수정 완료 처리
    fun commentModifyDoneProcess(position: Int, commentIdx:Int) {

            CoroutineScope(Dispatchers.Main).launch {
                var map = mutableMapOf<String, Any>(
                    "commentContent" to communityReadViewModel?.textInputCommunityReadSendComment?.value!!,
                    "commentModDt" to SimpleDateFormat("yyyy.MM.dd").format(Date()),
                    "commentStatus" to CommentStatus.COMMENT_STATUS_MODIFY.number
                )
                CommunityCommentDao.updateCommunityCommentData(commentIdx, map)
                commentList[position].commentContent = communityReadViewModel?.textInputCommunityReadSendComment?.value!!
                fragmentCommunityReadBinding.recyclerViewCommunityReadComment.adapter?.notifyItemChanged(position)
                Tools.hideSoftInput(communityActivity)
                settingInputForm()
            }

    }

    // 서버로 부터 데이터를 가져와 리사이클러뷰를 갱신한다.
    fun removingCommentData(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            // 댓글 리스트에서 삭제한다.
            commentList.removeAt(position)
            // 사용자 정보를 가져온다.
//            userList = UserDao.getUserAll()
            // 리사이클러뷰를 갱신한다.
            fragmentCommunityReadBinding.recyclerViewCommunityReadComment.adapter?.notifyItemRemoved(position)
        }
    }

    // 댓글 정보를 가져온다
    fun gettingCommentData() {
        CoroutineScope(Dispatchers.Main).launch {
            // 댓글 정보를 가져온다
            commentList = CommunityCommentDao.gettingCommunityCommentList(postIdx)
            // 사용자 정보를 가져온다.
            userList = UserDao.getUserAll()
            // 리사이클러뷰를 갱신한다.
            fragmentCommunityReadBinding.recyclerViewCommunityReadComment.adapter?.notifyDataSetChanged()
        }
    }


}