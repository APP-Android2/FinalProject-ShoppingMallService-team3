package kr.co.lion.farming_customer.viewmodel.farmingLife

import androidx.lifecycle.MutableLiveData

class RowGridItemViewModel {
    val textView_likeCnt = MutableLiveData<String>()
    val textView_ItemName = MutableLiveData<String>()
    val textView_price = MutableLiveData<String>()
    val isLike = MutableLiveData<Boolean>()
}