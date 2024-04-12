package kr.co.lion.farming_customer.viewmodel.orderHistory

import androidx.lifecycle.MutableLiveData

class OrderHistoryReservDetailViewModel {
    val textviewReservDetail_orderDate = MutableLiveData<String>()
    val textviewReservDetail_orderNum = MutableLiveData<String>()

    val textViewReservDetail_productName = MutableLiveData<String>()
    val textViewReservDetail_option = MutableLiveData<String>()
    val textViewReservDetail_price = MutableLiveData<String>()

    val textViewReservDetail_reservName = MutableLiveData<String>()
    val textViewReservDetail_phoneNum = MutableLiveData<String>()

    val textViewReservDetail_productPrice = MutableLiveData<String>()
    val textViewReservDetail_productNumber = MutableLiveData<String>()
    val textViewReservDetail_discountPrice = MutableLiveData<String>()
    val textViewReservDetail_totalPrice = MutableLiveData<String>()

}