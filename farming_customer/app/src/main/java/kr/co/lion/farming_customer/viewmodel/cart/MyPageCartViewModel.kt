package kr.co.lion.farming_customer.viewmodel.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageCartViewModel : ViewModel() {
    // 장바구니 농산물 이름
    val textViewRowCartTabCropName = MutableLiveData<String>()
    // 장바구니 농산물 옵션
    val textViewRowCartTabCropOption = MutableLiveData<String>()
    // 장바구니 농산물 가격
    val textViewRowCartTabCropPrice = MutableLiveData<String>()
    // 장바구니 농산물 구매 개수
    val textViewRowCartTabCropBuyCount = MutableLiveData<String>()

    // 장바구니 주말농장 이름
    val textViewRowCartTabFarmName = MutableLiveData<String>()
    // 장바구니 주말농장 옵션
    val textViewRowCartTabFarmOption = MutableLiveData<String>()
    // 장바구니 주말농장 가격
    val textViewRowCartTabFarmPrice = MutableLiveData<String>()

    // 장바구니 체험활동 이름
    val textViewRowCartTabActivityName = MutableLiveData<String>()
    // 장바구니 체험활동 옵션
    val textViewRowCartTabActivityOption = MutableLiveData<String>()
    // 장바구니 체험활동 가격
    val textViewRowCartTabActivityPrice = MutableLiveData<String>()
    // 장바구니 체험활동 구매 개수
    val textViewRowCartTabActivityBuyCount = MutableLiveData<String>()
}