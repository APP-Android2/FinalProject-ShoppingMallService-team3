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

enum class MyPageMyPostName(var str : String){
    MY_PAGE_MY_POST_BOARD_FRAGMENT("MyPageMyPostBoardFragment"),
    MY_PAGE_MY_POST_comment_FRAGMENT("MyPageMyPostCommentFragment"),
}