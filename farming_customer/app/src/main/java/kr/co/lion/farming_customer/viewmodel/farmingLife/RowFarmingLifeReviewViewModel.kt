package kr.co.lion.farming_customer.viewmodel.farmingLife

import androidx.lifecycle.MutableLiveData

class RowFarmingLifeReviewViewModel {
    val textView_name = MutableLiveData<String>()
    val rating = MutableLiveData<Double>()

    val textView_reviewDate = MutableLiveData<String>()

    val textView_productName = MutableLiveData<String>()
    val textView_optionName = MutableLiveData<String>()
    val textView_content = MutableLiveData<String>()
}