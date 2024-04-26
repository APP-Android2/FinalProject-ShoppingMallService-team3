package kr.co.lion.farming_customer.fragment.myPageMyPost

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.myPageMyPost.MyPageMyPostActivity
import kr.co.lion.farming_customer.dao.CommunityCommentDao
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentMyPageMyPostBoardBinding
import kr.co.lion.farming_customer.databinding.RowMyPageMyPostBinding
import kr.co.lion.farming_customer.model.CommunityCommentModel
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.myPageMyPost.MyPostBoardViewModel

class MyPageMyPostBoardFragment : Fragment() {

    lateinit var binding : FragmentMyPageMyPostBoardBinding
    lateinit var  myPageMyPostActivity: MyPageMyPostActivity

    lateinit var myPostBoardViewModel : MyPostBoardViewModel
//    lateinit var boardAdapter: BoardAdapter

    // 게시글 전체 리스트
    var allList = mutableListOf<CommunityModel>()
    // 내가쓴 글 리스트
    var myPostList = mutableListOf<CommunityModel>()
    // 댓글 정보를 가지고 있는 리스트
    var commentList = mutableListOf<CommunityCommentModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page_my_post_board, container, false)
        myPostBoardViewModel = MyPostBoardViewModel()

        binding.lifecycleOwner = this
        myPageMyPostActivity = activity as MyPageMyPostActivity

        settingCommunityPostAllList()
        settingRecyclerMain()

        return binding.root
    }

    // 로그인 한 사용자와 게시글 작성자가 같은 글 리스트 가져오기
    fun settingCommunityPostAllList() {
        CoroutineScope(Dispatchers.Main).launch {
            val sharedPreferences = myPageMyPostActivity.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE)
            val userIdx = sharedPreferences.getInt("loginUserIdx", -1)

            allList = CommunityPostDao.gettingCommunityPostList("번호순")


            allList.forEach {
                if (it.postUserIdx == userIdx) {
                    myPostList.add(it)
                }
            }

            binding.myPostRecyclerView.adapter?.notifyDataSetChanged()
        }
    }


    // RecyclerView 설정
    fun settingRecyclerMain() {
        binding.apply {
            myPostRecyclerView.apply {
                // adapter 설정
                adapter = RecyclerViewAdapter()
                // 레이아웃 매니저 설정 (RecyclerView 의 항목을 보여줄 방식을 설정)
                layoutManager = LinearLayoutManager(myPageMyPostActivity)
            }
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {
        inner class ViewHolderClass(rowBinding: RowMyPageMyPostBinding) :
            RecyclerView.ViewHolder(rowBinding.root) {
            val rowBinding: RowMyPageMyPostBinding
            init {
                this.rowBinding = rowBinding
                rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val rowBinding = DataBindingUtil.inflate<RowMyPageMyPostBinding>(layoutInflater, R.layout.row_my_page_my_post, parent, false)
            val rowViewModel = MyPostBoardViewModel()
            rowBinding.myPageMyPostViewModel = rowViewModel
            rowBinding.lifecycleOwner = this@MyPageMyPostBoardFragment

            val RowViewHolder = ViewHolderClass(rowBinding)
            return RowViewHolder

        }

        override fun getItemCount(): Int {
            return myPostList.size
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.rowBinding.apply {
                myPageMyPostViewModel?.myPostTypeLabelTextView?.value = myPostList[position].postType
                myPageMyPostViewModel?.myPostTitleTextView?.value = allList[position].postTitle
                myPageMyPostViewModel?.myPostContentTextView?.value = allList[position].postContent
                myPageMyPostViewModel?.myPostViewCountTextView?.value = allList[position].postViewCnt.toString()
                myPageMyPostViewModel?.myPostRegDateTextView?.value = allList[position].postRegDt

                if (myPostList[position].postLikeState == true) {
                    myPageMyPostLikeImageView.setImageResource(R.drawable.heart_01)
                    myPageMyPostLikeTextView.setTextColor(Color.parseColor("#FFFFFFFF"))
                    myPageMyPostViewModel?.myPageMyPostLikeTextView?.value = myPostList[position].postLikeCnt.toString()
                } else {
                    myPageMyPostLikeImageView.setImageResource(R.drawable.heart_04)
                    myPageMyPostLikeTextView.setTextColor(Color.parseColor("#413514"))
                    myPageMyPostViewModel?.myPageMyPostLikeTextView?.value = myPostList[position].postLikeCnt.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    // 댓글 정보를 가져온다
                    commentList = CommunityCommentDao.gettingCommunityCommentList(myPostList[position].postIdx)
                    myPageMyPostViewModel?.myPostCommentTextView?.value = commentList.size.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (myPostList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(myPageMyPostActivity, myPostList[position].postImages!![0], myPostImageView)
                    } else {
                        holder.rowBinding.myPostImageView.setImageResource(R.color.white)
                    }
                }

                linearLayout2.setOnClickListener {

                    // 조회수
                    CoroutineScope(Dispatchers.Main).launch {
                        myPostList[position].postViewCnt += 1
                        CommunityPostDao.updateCommunityPostViewCnt(myPostList[position].postIdx, myPostList[position].postViewCnt)
                    }

                    val communityIntent = Intent(myPageMyPostActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", myPostList[position].postIdx)
                    startActivity(communityIntent)
                }

                myPageMyPostLikeImageView.setOnClickListener {
                    if (myPostList[position].postLikeState == false) {
                        myPostList[position].postLikeState = true
                        myPageMyPostLikeImageView.setImageResource(R.drawable.heart_01)
                        myPageMyPostLikeTextView.setTextColor(Color.parseColor("#FFFFFFFF"))
                        myPostList[position].postLikeCnt += 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(myPostList[position], myPostList[position].postLikeState)
                        }
                    } else {
                        myPostList[position].postLikeState = false
                        myPageMyPostLikeImageView.setImageResource(R.drawable.heart_04)
                        myPageMyPostLikeTextView.setTextColor(Color.parseColor("#413514"))
                        myPostList[position].postLikeCnt -= 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(myPostList[position], myPostList[position].postLikeState)
                        }
                    }
                    myPageMyPostViewModel?.myPageMyPostLikeTextView?.value = myPostList[position].postLikeCnt.toString()
                }
            }
        }
    }
}