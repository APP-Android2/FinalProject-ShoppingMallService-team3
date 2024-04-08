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

enum class LikeType(var str:String, var num:Int){
    CROP("Crop",1),
    POST("Post",2),
    FARM("Farm",3),
    ACTIVITY("Activity",4),
    RENTAL("Rental",5)
}