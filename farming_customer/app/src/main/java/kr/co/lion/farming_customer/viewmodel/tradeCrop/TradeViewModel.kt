package kr.co.lion.farming_customer.viewmodel.tradeCrop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TradeViewModel:ViewModel() {
    // 농작물 이름
    val textViewCropName = MutableLiveData<String>()
    // 농작물 주소
    val textViewCropLocation = MutableLiveData<String>()
    // 농작물 기본옵션 가격
    val textViewTradePrice = MutableLiveData<String>()
    // 좋아요 수
    val textViewTradeLike = MutableLiveData<String>()

    // 좋아요 상태
    val isLike = MutableLiveData<Boolean>()

    // 최신등록 농작물 이름
    val textViewLikeCropName = MutableLiveData<String>()
    // 최신등록 농작물 가격
    val textViewLikeCropPrice = MutableLiveData<String>()
    // 최신등록 좋아요 수
    val textViewLikeCropCnt = MutableLiveData<String>()
}