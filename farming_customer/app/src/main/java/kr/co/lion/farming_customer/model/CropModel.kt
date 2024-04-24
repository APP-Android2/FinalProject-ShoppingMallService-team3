package kr.co.lion.farming_customer.model

data class CropModel(val crop_idx : Int = 0,
                     val crop_address : String = "",
                     val crop_seller_idx	: Int = 0,
                     val crop_title : String = "",
                     val crop_option_detail	: List<Map<String, String>> = emptyList(),
                     val crop_content_detail	: String = "",
                     val crop_content_detail_images	: String = "",
                     val crop_content_warning : String = "",
                     val crop_content_policy	: String = "",
                     val crop_like_cnt : Int = 0,
                     val crop_images	: List<String> = emptyList(),
                     val crop_reg_dt	: String = "",
                     val crop_mod_dt	: String = "",
                     val crop_status	: Int = 0)