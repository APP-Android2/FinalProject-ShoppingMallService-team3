package kr.co.lion.farming_customer.viewmodel.tradeCrop


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BottomSheetTradeCropViewModel : ViewModel() {
    // 옵션 수량
    val optionCounts = MutableLiveData<Int>()

    // 선택된 농산물 이름
    val cropName = MutableLiveData<String>()


    // 선택된 농산물 가격
    val selectedCropPrice = MutableLiveData<String>()


    // 배송비
    val fee = MutableLiveData<String>()


    // 총 가격
    val totalPrice = MutableLiveData<String>()


    init {
        // 초기 값 설정
        optionCounts.value = 1  // 기본 옵션 수량
        cropName.value = "감자 10kg"
        selectedCropPrice.value = "0원"  // 기본 가격
        fee.value = "2,500원"  // 기본 배송비
        totalPrice.value = "2,500원"  // 초기 총 가격
    }

    fun plusOptionCount() {
        val currentCount = optionCounts.value ?: 1 // 현재 값이 null이면 1을 사용
        optionCounts.value = currentCount + 1 // 현재 값에 1을 더해서 다시 설정
    }

    fun minusOptionCount() {
        val currentCount = optionCounts.value ?: 1 // 현재 값이 null이면 1을 사용
        if (currentCount > 1) { // 0 이하로 내려가지 않게 조건 추가
            optionCounts.value = currentCount - 1 // 현재 값에서 1을 빼서 다시 설정
        }
    }
}