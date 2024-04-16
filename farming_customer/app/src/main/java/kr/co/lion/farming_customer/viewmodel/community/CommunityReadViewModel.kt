package kr.co.lion.farming_customer.viewmodel.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunityReadViewModel: ViewModel() {
    // 커뮤니티 글 상세조회 읽는 화면 툴바 게시판 유형
    val textViewCommunityReadToolbarTitle = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 읽는 화면 좋아요 개수
    val textViewCommunityReadLikeCnt = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 읽는 화면 댓글 개수
    val textViewCommunityReadCommentCnt = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 작성자
    val textViewCommunityReadWriter = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 작성 날짜
    val textViewCommunityReadDate = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 제목
    val textViewCommunityReadSubject = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 내용
    val textViewCommunityReadContent = MutableLiveData<String>()
}