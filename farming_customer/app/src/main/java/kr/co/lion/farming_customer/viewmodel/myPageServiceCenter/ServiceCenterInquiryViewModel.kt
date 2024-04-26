package kr.co.lion.farming_customer.viewmodel.myPageServiceCenter

import androidx.lifecycle.MutableLiveData
import kr.co.lion.farming_customer.InquiryType

class ServiceCenterInquiryViewModel {
    // 문의 유형
    var inquiryType = MutableLiveData<String>()
    // 문의 내용
    val inquiryContent = MutableLiveData<String>()
    // 문의 답변 여부
    val inquiryIsAnswered = MutableLiveData<Boolean>()
    // 문의 답변
    val inquiryAnswer = MutableLiveData<String>()
    // 문의 이미지 첨부파일
    val inquiryImages = mutableListOf<String>()
    // 문의 상태
    val inquiryStatus = MutableLiveData<Int>()
    // 문의 라벨
    val inquiryLabel = MutableLiveData<String>()

    // MutableLiveData에 담긴 버튼의 ID 값을 통해 게시판 타입값을 반환한다.
    fun gettingContentType(): InquiryType = when(inquiryType.value){
        "0" -> InquiryType.TYPE_CROP
        "1" -> InquiryType.TYPE_FARM
        "2" -> InquiryType.TYPE_ACTIVITY
        "3" -> InquiryType.TYPE_JOB
        "4" -> InquiryType.TYPE_TOOLS
        "5" -> InquiryType.TYPE_COMMUNITY
        "6" -> InquiryType.TYPE_OTHER
        else -> InquiryType.TYPE_OTHER
    }

    // 드롭다운에서 선택된 항목에 따라 selectedInquiryType을 업데이트하는 메서드
    fun settingInquiryType(selectedItem: String) {
        inquiryType.value= when (selectedItem) {
            "농산물" -> "0"
            "주말농장" -> "1"
            "체험활동" -> "2"
            "구인구직" -> "3"
            "농기구" -> "4"
            "게시판" -> "5"
            "기타" -> "6"
            else -> "6" // 기본값으로 설정
        }
    }

}