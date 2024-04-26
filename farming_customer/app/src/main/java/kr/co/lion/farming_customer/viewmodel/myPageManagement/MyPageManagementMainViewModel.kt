package kr.co.lion.farming_customer.viewmodel.myPageManagement

import androidx.lifecycle.MutableLiveData

class MyPageManagementMainViewModel {
    val textViewMyPageManagementMain_NickName = MutableLiveData<String>()

    val textViewMyPageManagementMain_UserName = MutableLiveData<String>()
    val textViewMyPageManagementMain_UserId = MutableLiveData<String>()
    val textViewMyPageManagementMain_UserPhoneNumber = MutableLiveData<String>()

    val textViewMyPageManagementMain_RecipientName = MutableLiveData<String>()
    val textViewMyPageManagementMain_AddressName = MutableLiveData<String>()
    val textViewMyPageManagementMain_AddressPhoneNumber = MutableLiveData<String>()

}