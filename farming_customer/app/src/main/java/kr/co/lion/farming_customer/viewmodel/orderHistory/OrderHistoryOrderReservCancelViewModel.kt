package kr.co.lion.farming_customer.viewmodel.orderHistory

import androidx.lifecycle.MutableLiveData

class OrderHistoryOrderReservCancelViewModel {
    val textViewReservCancel_productName = MutableLiveData<String>()
    val textViewReservCancle_option = MutableLiveData<String>()
    val textViewReservCancle_price = MutableLiveData<String>()
    val textViewReservCancel_reason = MutableLiveData<String>()
    val textViewReservCancel_reasonDetail = MutableLiveData<String>()
}