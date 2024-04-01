package kr.co.lion.farming_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomDialogViewModel : ViewModel() {
    // YesNo 다이얼로그 제목
    val textViewDialogYesNoSubject = MutableLiveData<String>()
    // YesNo 다이얼로그 내용
    val textViewDialogYesNoText = MutableLiveData<String>()

    // Yes 다이얼로그 제목
    val textViewDialogYesSubject = MutableLiveData<String>()
    // Yes 다이얼로그 내용1
    val textViewDialogYesText = MutableLiveData<String>()
    // Yes 다이얼로그 내용2
    val textViewDialogYesText2 = MutableLiveData<String>()
}