package kr.co.lion.farming_customer.viewmodel.myPageServiceCenter

import androidx.lifecycle.MutableLiveData

class ServiceCenterNoticeViewModel {
    // 공지 제목
    val noticeTitle = MutableLiveData<String>()
    // 공지 내용
    val noticeContent = MutableLiveData<String>()
    // 공지 날짜
    val noticeRegDt = MutableLiveData<String>()
    // 공지 수정 날짜
    val noticeModDt = MutableLiveData<String>()
}