package kr.co.lion.farming_customer.model.myPageServiceCenterModel

class InquiryModel(var inquiry_idx: Int, var inquiry_user_idx: Int, var inquiry_type: Int, var inquiry_content: String,
    var inquiry_question_reg_dt: String, var inquiry_is_answered: Boolean, var inquiry_answer: String,
    var inquiry_images: MutableList<String?>, var inquiry_status: Int) {

    constructor(): this(0, 0, 0, "", "", false, "", mutableListOf(),  0)
}