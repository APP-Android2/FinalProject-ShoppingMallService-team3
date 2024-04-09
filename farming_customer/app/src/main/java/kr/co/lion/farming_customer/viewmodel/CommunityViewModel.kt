package kr.co.lion.farming_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunityViewModel: ViewModel() {
    // 커뮤니티 글 읽는 화면 툴바 게시판 유형
    val textViewCommunityReadToolbarTitle = MutableLiveData<String>()
    // 커뮤니티 글 읽는 화면 좋아요 개수
    val textViewCommunityReadLikeCnt = MutableLiveData<String>()
    // 커뮤니티 글 읽는 화면 댓글 개수
    val textViewCommunityReadCommentCnt = MutableLiveData<String>()
    // 커뮤니티 글 작성자
    val textViewCommunityReadWriter = MutableLiveData<String>()
    // 커뮤니티 글 작성 날짜
    val textViewCommunityReadDate = MutableLiveData<String>()
    // 커뮤니티 글 제목
    val textViewCommunityReadSubject = MutableLiveData<String>()
    // 커뮤니티 글 내용
    val textViewCommunityReadContent = MutableLiveData<String>()
    // 커뮤니티 댓글 작성자
    val textViewRowCommunityReadCommentWriter = MutableLiveData<String>()
    // 커뮤니티 댓글 내용
    val textViewRowCommunityReadCommentContent = MutableLiveData<String>()
}