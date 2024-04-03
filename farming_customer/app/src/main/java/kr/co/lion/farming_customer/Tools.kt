package kr.co.lion.farming_customer

class Tools {
}

// MainActivity에서 보여줄 프래그먼트들의 이름
enum class MainFragmentName(var str : String){
    TRADE_FRAGMENT("TradeFragment"),
    BOARD_FRAGMENT("BoardFragment"),
    HOME_FRAGMENT("HomeFragment"),
    LIKE_FRAGMENT("LikeFragment"),
    MY_PAGE_FRAGMENT("MyPageFragment")
}

enum class MyPageServiceCenterFragmentName(var str: String) {
    SERVICE_CENTER_NOTICE_FRAGMENT("MyPageServiceCenterNoticeFragment"),
    SERVICE_CENTER_FAQ_FRAGMENT("MyPageServiceCenterFaqFragment"),
    SERVICE_CENTER_INQUIRY_FRAGMENT("MyPageServiceCenterInquiryFragment")
}