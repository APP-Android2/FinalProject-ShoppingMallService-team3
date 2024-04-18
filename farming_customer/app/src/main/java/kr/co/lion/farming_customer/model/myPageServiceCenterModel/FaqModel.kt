package kr.co.lion.farming_customer.model.myPageServiceCenterModel

class FaqModel(var faq_idx: Int, var faq_title: String, var faq_user_idx: Int, var faq_content: String,
    var faq_reg_dt: String, var faq_mod_dt: String, var faq_status: Int) {

    constructor(): this(0, "", 0, "", "", "", 0)
}