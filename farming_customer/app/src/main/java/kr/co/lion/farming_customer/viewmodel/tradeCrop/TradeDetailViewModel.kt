package kr.co.lion.farming_customer.viewmodel.tradeCrop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TradeDetailViewModel:ViewModel() {

    // 농작물 이름
    val textViewTradeDetailCropName = MutableLiveData<String>()
    // 농작물 주소
    val textViewTradeDetailLocation = MutableLiveData<String>()
    // 농작물 택배비
    val textViewTradeDetailFee = MutableLiveData<String>()
    // 농작물 상세내용
    val textViewTradeTabDetailContents = MutableLiveData<String>()
    // 농작물 주의사항
    val textViewTradeTabDetailWarning = MutableLiveData<String>()
    // 농작물 교환 및 환불 정책
    val textViewTradeTabDetailPolicy = MutableLiveData<String>()
}