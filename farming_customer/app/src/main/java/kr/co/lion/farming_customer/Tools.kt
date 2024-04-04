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
    POINT_HISTORY_FRAGMENT("PointHistoryFragment")
}

// ReviewActivity에서 보여줄 프래그먼트들의 이름
enum class ReviewFragmentName(var str: String) {
    REVIEW_HISTORY_FRAGMENT("ReviewHistoryFragment"),
    REVIEW_TAB_CROP_FRAGMENT("ReviewTabCropFragment"),
    REVIEW_TAB_FARM_FRAGMENT("ReviewTabFarmFragment"),
    REVIEW_TAB_ACTIVITY_FRAGMENT("ReviewTabActivityFragment")
}

// CartActivity에서 보여줄 프래그먼트들의 이름
enum class CartFragmentName(var str: String) {
    CART_FRAGMENT("CartFragment"),
    CART_TAB_CROP_FRAGMENT("CartTabCropFragment"),
    CART_TAB_FARM_FRAGMENT("CartTabFarmFragment"),
    CART_TAB_ACTIVITY_FRAGMENT("CartTabActivityFragment")
}
