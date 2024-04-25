package kr.co.lion.farming_customer.model

data class ActivityModel(val activity_idx : Int = 0,
                         val activity_seller_idx : Int = 0,
                         val activity_title	: String = "",
                         val activity_option_detail : List<Map<String, Any>> = emptyList(),
                         val activity_content_detail : String = "",
                         val activity_content_warning : String = "",
                         val activity_content_policy : String = "",
                         val activity_utility : Map<String, Any> = emptyMap(),
                         val activity_like_cnt : Int = 0,
                         val activity_images : List<String> = emptyList(),
                         val activity_reg_dt  : String = "",
                         val activity_mod_dt : String = "",
                         val activity_status : Int = 0,
                         val activity_address : String = "",
                         val activity_star : Int = 0,)

