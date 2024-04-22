package kr.co.lion.farming_customer.viewmodel.myPageMyPost

import androidx.lifecycle.MutableLiveData

class MyPostBoardViewModel {
    // 내가 쓴 글 좋아요 갯수
    val myPageMyPostLikeTextView = MutableLiveData<String>()
    // 내가 쓴 글 라벨
    val myPostTypeLabelTextView = MutableLiveData<String>()
    // 내가 쓴 글 제목
    val myPostTitleTextView = MutableLiveData<String>()
    // 내가 쓴 글 내
    val myPostContentTextView = MutableLiveData<String>()
    // 내가 쓴 글 조회수
    val myPostViewCountTextView = MutableLiveData<String>()
    // 내가 쓴 글 작성일
    val myPostRegDateTextView = MutableLiveData<String>()
    // 내가 쓴 글 댓글 수
    val myPostCommentTextView = MutableLiveData<String>()
}