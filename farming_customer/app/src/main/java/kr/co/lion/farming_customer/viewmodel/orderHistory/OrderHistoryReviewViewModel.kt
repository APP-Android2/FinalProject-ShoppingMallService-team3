package kr.co.lion.farming_customer.viewmodel.orderHistory

import androidx.lifecycle.MutableLiveData

class OrderHistoryReviewViewModel {
    val textViewWriteReview_productName = MutableLiveData<String>()
    val textViewWriteReview_price = MutableLiveData<String>()
    val textViewWriteReview_reviewContent = MutableLiveData<String>()
}