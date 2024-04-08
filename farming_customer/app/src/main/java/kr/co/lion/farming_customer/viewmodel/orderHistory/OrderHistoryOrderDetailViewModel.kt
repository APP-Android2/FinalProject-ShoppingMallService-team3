package kr.co.lion.farming_customer.viewmodel.orderHistory

import androidx.lifecycle.MutableLiveData

class OrderHistoryOrderDetailViewModel {
    val textviewOrderDetail_orderDate = MutableLiveData<String>()
    val textviewOrderDetail_orderNum = MutableLiveData<String>()


    val textviewOrderDetail_receiverName = MutableLiveData<String>()
    val textviewOrderDetail_address = MutableLiveData<String>()
    val textviewOrderDetail_receivePhoneNum = MutableLiveData<String>()
    val textviewOrderDetail_request = MutableLiveData<String>()

    val textViewOrderDetail_reservName = MutableLiveData<String>()
    val textViewOrderDetail_phoneNum = MutableLiveData<String>()

    val textViewOrderDetail_productPrice = MutableLiveData<String>()
    val textViewOrderDetail_deliveryPrice = MutableLiveData<String>()
    val textViewOrderDetail_discountPrice = MutableLiveData<String>()
    val textViewOrderDetail_totalPrice = MutableLiveData<String>()
}