package kr.co.lion.farming_customer.viewmodel.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunityCommentViewModel: ViewModel() {
    // 커뮤니티 글 상세조회 댓글 작성자
    val textViewRowCommunityReadCommentWriter = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 댓글 내용
    val textViewRowCommunityReadCommentContent = MutableLiveData<String>()
}