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

// OrderHistoryActivity에서 보여줄 프래그먼트들의 이름
enum class OrderHistoryFragmentName(var str:String){
    ORDER_HISTORY_CROP_FRAGMENT("OrderHistoryCropFragment"),
    ORDER_HISTORY_FARM_FRAMGNET("OrderHistoryFarmFragment"),
    ORDER_HISTORY_ACTIVITY_FRAGMENT("OrderHistoryActivityFragment"),
    TAP_DELIVERY_DONE_FRAGMENT("TapDeliveryDoneFragment"),
    TAP_DELIVERY_FRAGMENT("TapDeliveryFragment"),
    TAP_PAYMENT_DONE_FRAGMENT("TapPaymentDoneFragment")
}