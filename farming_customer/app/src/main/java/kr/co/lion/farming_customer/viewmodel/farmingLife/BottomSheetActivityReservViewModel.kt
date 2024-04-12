package kr.co.lion.farming_customer.viewmodel.farmingLife

import androidx.lifecycle.MutableLiveData

class BottomSheetActivityReservViewModel {
    val textView_selectedDate = MutableLiveData<String>()
    val textView_option = MutableLiveData<String>()
    val textView_totalPrice = MutableLiveData<String>()
}