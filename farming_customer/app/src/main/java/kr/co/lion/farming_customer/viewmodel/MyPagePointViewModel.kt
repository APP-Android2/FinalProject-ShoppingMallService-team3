package kr.co.lion.farming_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPagePointViewModel : ViewModel() {
    // 남은 포인트
    val textViewPointHistoryRemainPoint = MutableLiveData<String>()
    // 포인트 내역 날짜
    val textViewRowPointHistoryDate = MutableLiveData<String>()
    // 포인트 내역 이름
    val textViewRowPointHistoryName = MutableLiveData<String>()
    // 포인트 내역 증감
    val textViewRowPointHistoryNumber = MutableLiveData<String>()
}