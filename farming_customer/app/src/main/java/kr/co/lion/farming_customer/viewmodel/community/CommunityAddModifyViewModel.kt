package kr.co.lion.farming_customer.viewmodel.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.lion.farming_customer.PostType
import kr.co.lion.farming_customer.R

class CommunityAddModifyViewModel: ViewModel() {
    // 커뮤니티 글 작성 제목
    val textViewCommunityAddSubject = MutableLiveData<String>()
    // 커뮤니티 글 작성 내용
    val textViewCommunityAddContent = MutableLiveData<String>()

    // 커뮤니티 글 수정 제목
    val textViewCommunityModifySubject = MutableLiveData<String>()
    // 커뮤니티 글 수정 내용
    val textViewCommunityModifyContent = MutableLiveData<String>()

    // 커뮤니티 글 작성 게시판 종류
    val communityPostAddType = MutableLiveData<String>()
    // 커뮤니티 글 수정 게시판 종류
    val communityPostModifyType = MutableLiveData<String>()


}