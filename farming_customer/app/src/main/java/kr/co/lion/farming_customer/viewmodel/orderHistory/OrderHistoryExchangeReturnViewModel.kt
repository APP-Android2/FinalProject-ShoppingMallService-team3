package kr.co.lion.farming_customer.viewmodel.orderHistory

import androidx.lifecycle.MutableLiveData

class OrderHistoryExchangeReturnViewModel {
    val textViewExchangeReturn_productName = MutableLiveData<String>()
    val textViewExchangeReturn_option = MutableLiveData<String>()
    val textViewExchangeReturn_price = MutableLiveData<String>()

    val textViewExchangeReturn_type = MutableLiveData<String>()
    val textViewExchangeReturn_reason = MutableLiveData<String>()
    val textViewExchangeReturn_reasonDetail = MutableLiveData<String>()


}