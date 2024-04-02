package kr.co.lion.farming_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageReviewViewModel : ViewModel() {
    // 리뷰 탭 농작물 리뷰 개수
    val textViewReviewTabCropCount = MutableLiveData<String>()
    // 리뷰 탭 농작물 리뷰 작성 날짜
    val textViewRowReviewTabCropDate = MutableLiveData<String>()
    // 리뷰 탭 농작물 리뷰 이름
    val textViewRowReviewTabCropName = MutableLiveData<String>()
    // 리뷰 탭 농작물 리뷰 내용
    val textViewRowReviewTabCropText = MutableLiveData<String>()
    // 리뷰 탭 농작물 리뷰 옵션 라벨
    val textViewRowReviewTabCropLabel = MutableLiveData<String>()

    // 리뷰 탭 주말농장 리뷰 개수
    val textViewReviewTabFarmCount = MutableLiveData<String>()
    // 리뷰 탭 주말농장 리뷰 작성 날짜
    val textViewRowReviewTabFarmDate = MutableLiveData<String>()
    // 리뷰 탭 주말농장 리뷰 이름
    val textViewRowReviewTabFarmName = MutableLiveData<String>()
    // 리뷰 탭 주말농장 리뷰 내용
    val textViewRowReviewTabFarmText = MutableLiveData<String>()
    // 리뷰 탭 주말농장 리뷰 옵션 라벨
    val textViewRowReviewTabFarmLabel = MutableLiveData<String>()

    // 리뷰 탭 체험활동 리뷰 개수
    val textViewReviewTabActivityCount = MutableLiveData<String>()
    // 리뷰 탭 체험활동 리뷰 작성 날짜
    val textViewRowReviewTabActivityDate = MutableLiveData<String>()
    // 리뷰 탭 체험활동 리뷰 이름
    val textViewRowReviewTabActivityName = MutableLiveData<String>()
    // 리뷰 탭 체험활동 리뷰 내용
    val textViewRowReviewTabActivityText = MutableLiveData<String>()
    // 리뷰 탭 체험활동 리뷰 옵션 라벨
    val textViewRowReviewTabActivityLabel = MutableLiveData<String>()
}