package kr.co.lion.farming_customer.viewmodel.orderHistory

import androidx.lifecycle.MutableLiveData

class RowOrderHistoryCropViewModel {
    val textViewRowOrderHistoryCrop_orderDate = MutableLiveData<String>()
    val textViewRowOrderHistoryCrop_productName = MutableLiveData<String>()
    val textViewRowOrderHistoryCrop_productOption = MutableLiveData<String>()
    val textViewRowOrderHistoryCrop_price = MutableLiveData<String>()
    val buttonRowOrderHistoryCrop_productInside = MutableLiveData<String>()
    val buttonRowOrderHistoryCrop_productOutside = MutableLiveData<String>()
    val buttonRowOrderHistoryCrop_label = MutableLiveData<String>()
}
