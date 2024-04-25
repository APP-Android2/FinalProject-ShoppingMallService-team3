package kr.co.lion.farming_customer.model.review

class ReviewModel(
    var review_idx: Int, var review_user_idx: Int, var review_order_idx: Int, var review_seller_idx: Int,
    var review_rate: Double, var review_content: String, var review_title: String, var review_option: String,
    var review_images: MutableList<String?>, var review_is_read: Boolean, var review_reg_dt: String, var review_mod_dt: String,
    var review_status: Int, var review_type: Int
    ) {

    constructor(): this(0, 0, 0, 0, 0.0, "", "", "", mutableListOf(), false, "", "", 0, 0)

}