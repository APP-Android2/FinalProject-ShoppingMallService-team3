package kr.co.lion.farming_customer.viewmodel.myPageServiceCenter

import androidx.lifecycle.MutableLiveData

class ServiceCenterFaqViewModel {
    // faq 제목
    val faqTitle = MutableLiveData<String>()
    // faq 내용
    val faqContent = MutableLiveData<String>()
    // faq 작성 날짜
    val faqRegDt = MutableLiveData<String>()
    // faq 수정 날짜
    val faqModDt = MutableLiveData<String>()
}