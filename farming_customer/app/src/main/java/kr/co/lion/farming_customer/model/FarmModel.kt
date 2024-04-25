package kr.co.lion.farming_customer.model

data class FarmModel(val farm_idx : Int = 0,
                     val farm_seller_idx : Int = 0,
                     val farm_title : String = "",
                     val farm_option_detail	: Map<String, Any> = emptyMap<String, Any>(),
                     val farm_address : String = "",
                     val farm_content_detail : String = "",
                     val farm_content_warning : String = "",
                     val farm_content_policy : String = "",
                     val farm_apply_date_start : String = "",
                     val farm_apply_date_end : String = "",
                     val farm_use_date_start : String = "",
                     val farm_use_date_end : String = "",
                     val farm_can_crop	: List<String> = emptyList(),
                     val farm_utility	: Map<String, Any> = emptyMap<String, Any>(),
                     val farm_like_cnt : Int = 0,
                     val farm_images	: List<String> = emptyList(),
                     val farm_reg_dt : String = "",
                     val farm_mod_dt : String = "",
                     val farm_status : Int = 0,
                     val farm_star : Int =0)


