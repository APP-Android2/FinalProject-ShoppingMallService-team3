package kr.co.lion.farming_customer.viewmodel.farmingLife

import androidx.lifecycle.MutableLiveData

class BottomSheetFarmReservViewModel {
    val textViewReserv_farmName = MutableLiveData<String>()
    val textViewReserv_remainArea = MutableLiveData<String>()
    val textViewReserv_price = MutableLiveData<String>()
}