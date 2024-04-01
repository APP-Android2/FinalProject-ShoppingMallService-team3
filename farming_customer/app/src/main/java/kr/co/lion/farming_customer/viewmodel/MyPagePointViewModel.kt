package kr.co.lion.farming_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPagePointViewModel : ViewModel() {
    // 남은 포인트
    val textViewPointHistoryRemainPoint = MutableLiveData<String>()
}