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

// MyPageManagementActivity에서 보여줄 프래그먼트들의 이름
enum class MyPageManagementName(var str:String){
    MY_PAGE_MANAGEMENT_MAIN("MyPageManagementMainFragment"),
    MY_PAGE_MANAGEMENT_PROFILE_MODIFY("MyPageManagementProfileModifyFragment"),
    MY_PAGE_MANAGEMENT_CHECK_USER_INFO_MODIFY("MyPageManagementCheckUserInfoModifyFragment"),
    MY_PAGE_MANAGEMENT_USER_INFO_MODIFY("MyPageManagementUserInfoModifyFragment"),
    MY_PAGE_MANAGEMENT_DELIVERY_ADDRESS("MyPageManagementDeliveryAddressFragment"),
    MY_PAGE_MANAGEMENT_ADD_DELIVERY_ADDRESS("MyPageManagementAddDeliveryAddressFragment"),
    MY_PAGE_MANAGEMENT_MODIFY_DELIVERY_ADDRESS("MyPageManagementModifyDeliveryAddressFragment")
}