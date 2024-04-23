package kr.co.lion.farming_customer.model.myPageServiceCenterModel

data class NoticeModel(var notice_idx: Int, var notice_title: String, var notice_user_idx: Int,
    var notice_content: String, var notice_reg_dt: String, var notice_mod_dt: String, var notice_fix: Boolean, var notice_status: Int) {
    constructor():this(0, "", 0, "", "", "", true, 0)
}