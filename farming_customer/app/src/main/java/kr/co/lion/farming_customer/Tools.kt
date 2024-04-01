package kr.co.lion.farming_customer

class Tools {
}

// MainActivity에서 보여줄 프래그먼트들의 이름
enum class MainFragmentName(var str : String){
    TRADE_FRAGMENT("TradeFragment"),
    BOARD_FRAGMENT("BoardFragment"),
    HOME_FRAGMENT("HomeFragment"),
    LIKE_FRAGMENT("LikeFragment"),
    MY_PAGE_FRAGMENT("MyPageFragment"),
    DIALOG_LOGIN_FRAGMENT("DialogLoginFragment")
}

// PointActivity에서 보여줄 프래그먼트들의 이름
enum class PointFragmentName(var str: String) {
    POINT_HISTORY_FRAGMENT("PointHistoryFragment"),
    DIALOG_POINT_FRAGMENT("DialogPointFragment")
}