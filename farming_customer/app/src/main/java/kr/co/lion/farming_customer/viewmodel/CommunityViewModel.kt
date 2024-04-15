package kr.co.lion.farming_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunityViewModel: ViewModel() {
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
    // 커뮤니티 글 상세조회 댓글 작성자
    val textViewRowCommunityReadCommentWriter = MutableLiveData<String>()
    // 커뮤니티 글 상세조회 댓글 내용
    val textViewRowCommunityReadCommentContent = MutableLiveData<String>()

    // 커뮤니티 글 리스트 검색 라벨
    val textViewCommunityListLabelSearch = MutableLiveData<String>()
    // 커뮤니티 글 리스트 검색 제목
    val textViewCommunityListTitleSearch = MutableLiveData<String>()
    // 커뮤니티 글 리스트 검색 내용
    val textViewCommunityListContentSearch = MutableLiveData<String>()
    // 커뮤니티 글 리스트 검색 조회수
    val textViewCommunityListViewCntSearch = MutableLiveData<String>()
    // 커뮤니티 글 리스트 검색 댓글수
    val textViewCommunityListCommentCntSearch = MutableLiveData<String>()
    // 커뮤니티 글 리스트 검색 날짜
    val textViewCommunityListDateSearch = MutableLiveData<String>()
    // 커뮤니티 글 리스트 검색 좋아요
    val textViewCommunityListLikeCntSearch = MutableLiveData<String>()

    // 커뮤니티 글 리스트 전체 탭 라벨
    val textViewCommunityListLabelAll = MutableLiveData<String>()
    // 커뮤니티 글 리스트 전체 탭 제목
    val textViewCommunityListTitleAll = MutableLiveData<String>()
    // 커뮤니티 글 리스트 전체 탭 내용
    val textViewCommunityListContentAll = MutableLiveData<String>()
    // 커뮤니티 글 리스트 전체 탭 조회수
    val textViewCommunityListViewCntAll = MutableLiveData<String>()
    // 커뮤니티 글 리스트 전체 탭 댓글수
    val textViewCommunityListCommentCntAll = MutableLiveData<String>()
    // 커뮤니티 글 리스트 전체 탭 날짜
    val textViewCommunityListDateAll = MutableLiveData<String>()
    // 커뮤니티 글 리스트 전체 탭 좋아요
    val textViewCommunityListLikeCntAll = MutableLiveData<String>()
    // 커뮤니티 글 하트 좋아요 여부
    var isLike = MutableLiveData<Boolean>()

    // 커뮤니티 글 리스트 정보 탭 라벨
    val textViewCommunityListLabelInformation = MutableLiveData<String>()
    // 커뮤니티 글 리스트 정보 탭 제목
    val textViewCommunityListTitleInformation = MutableLiveData<String>()
    // 커뮤니티 글 리스트 정보 탭 내용
    val textViewCommunityListContentInformation = MutableLiveData<String>()
    // 커뮤니티 글 리스트 정보 탭 조회수
    val textViewCommunityListViewCntInformation = MutableLiveData<String>()
    // 커뮤니티 글 리스트 정보 탭 댓글수
    val textViewCommunityListCommentCntInformation = MutableLiveData<String>()
    // 커뮤니티 글 리스트 정보 탭 날짜
    val textViewCommunityListDateInformation = MutableLiveData<String>()
    // 커뮤니티 글 리스트 정보 탭 좋아요수
    val textViewCommunityListLikeCntInformation = MutableLiveData<String>()

    // 커뮤니티 글 리스트 소통 탭 라벨
    val textViewCommunityListLabelSocial = MutableLiveData<String>()
    // 커뮤니티 글 리스트 소통 탭 제목
    val textViewCommunityListTitleSocial = MutableLiveData<String>()
    // 커뮤니티 글 리스트 소통 탭 내용
    val textViewCommunityListContentSocial = MutableLiveData<String>()
    // 커뮤니티 글 리스트 소통 탭 조회수
    val textViewCommunityListViewCntSocial = MutableLiveData<String>()
    // 커뮤니티 글 리스트 소통 탭 댓글수
    val textViewCommunityListCommentCntSocial = MutableLiveData<String>()
    // 커뮤니티 글 리스트 소통 탭 날짜
    val textViewCommunityListDateSocial = MutableLiveData<String>()
    // 커뮤니티 글 리스트 소통 탭 좋아요수
    val textViewCommunityListLikeCntSocial = MutableLiveData<String>()

    // 커뮤니티 글 리스트 구인구직 탭 라벨
    val textViewCommunityListLabelJob = MutableLiveData<String>()
    // 커뮤니티 글 리스트 구인구직 탭 제목
    val textViewCommunityListTitleJob = MutableLiveData<String>()
    // 커뮤니티 글 리스트 구인구직 탭 내용
    val textViewCommunityListContentJob = MutableLiveData<String>()
    // 커뮤니티 글 리스트 구인구직 탭 조회수
    val textViewCommunityListViewCntJob = MutableLiveData<String>()
    // 커뮤니티 글 리스트 구인구직 탭 댓글수
    val textViewCommunityListCommentCntJob = MutableLiveData<String>()
    // 커뮤니티 글 리스트 구인구직 탭 날짜
    val textViewCommunityListDateJob = MutableLiveData<String>()
    // 커뮤니티 글 리스트 구인구직 탭 좋아요수
    val textViewCommunityListLikeCntJob = MutableLiveData<String>()

    // 커뮤니티 글 작성 제목
    val textViewCommunityAddSubject = MutableLiveData<String>()
    // 커뮤니티 글 작성 내용
    val textViewCommunityAddContent = MutableLiveData<String>()

    // 커뮤니티 글 수정 제목
    val textViewCommunityModifySubject = MutableLiveData<String>()
    // 커뮤니티 글 수정 내용
    val textViewCommunityModifyContent = MutableLiveData<String>()
}