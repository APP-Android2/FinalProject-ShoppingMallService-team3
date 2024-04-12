package kr.co.lion.farming_customer.viewmodel.farmingLife

import androidx.lifecycle.MutableLiveData

class FarmingLifeFarmDetailViewModel {
    val textView_sellerName = MutableLiveData<String>()
    val textView_address = MutableLiveData<String>()
    val textView_applicationDate = MutableLiveData<String>()
    val textView_serviceDate = MutableLiveData<String>()
    val textView_remainCnt = MutableLiveData<String>()
    val textView_price = MutableLiveData<String>()

    val textView_productDetail = MutableLiveData<String>()
    val textView_serviceInfo = MutableLiveData<String>()
}