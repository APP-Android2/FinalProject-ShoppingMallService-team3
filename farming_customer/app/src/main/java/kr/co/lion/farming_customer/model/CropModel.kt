package kr.co.lion.farming_customer.model

data class CropModel(
    var crop_idx:Int, var crop_seller_idx:Int, var crop_address:String,
    var crop_title:String, var crop_option_detail:MutableList<Map<String,String>>,
    var crop_content_detail:String, var crop_content_warning:String,
    var crop_content_policy:String, var crop_like_cnt:Int,
    var crop_images:MutableList<String>, var crop_content_detail_image:MutableList<String>,
    var crop_reg_dt:String,var crop_mod_dt:String,var like_state:Boolean,
    var crop_rating:Double,var delivery_fee:String, var crop_status:Int) {

    constructor(): this(
        0,0,"","",
        mutableListOf(),"", "",
        "",0, mutableListOf(),
        mutableListOf(),"","",false,0.0,"", 0)
}
