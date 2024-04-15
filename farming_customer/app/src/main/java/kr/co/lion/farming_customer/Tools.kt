package kr.co.lion.farming_customer

import android.content.Context
import android.util.TypedValue

class Tools {
    companion object{
        // dp를 픽셀로 변환하는 함수
        fun dpToPx(context: Context, dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }
}

// MainActivity에서 보여줄 프래그먼트들의 이름
enum class MainFragmentName(var str : String){
    TRADE_FRAGMENT("TradeFragment"),
    BOARD_FRAGMENT("BoardFragment"),
    HOME_FRAGMENT("HomeFragment"),
    LIKE_FRAGMENT("LikeFragment"),
    MY_PAGE_FRAGMENT("MyPageFragment"),
    TRADE_DETAIL_FRAGMENT("TradeDetailFragment"),
    TRADE_TAB_DETAIL_FRAGMENT("TradeTabDetailFragment")
}

enum class LoginFragmentName(var str: String){
    LOGIN_FRAGMENT("LoginFragment"),
    REGISTER_FRAGMENT("RegisterFragment"),
    REGISTER2_FRAGMENT("Register2Fragment"),
    REGISTER3_FRAGMENT("Register3Fragment"),
    FIND_ACCOUNT_FRAGMENT("FindAccountFragment"),
    FIND_ID_DONE_FRAGMENT("FindIdDoneFragment"),
    FIND_PW_DONE_FRAGMENT("FindPwDoneFragment"),
}