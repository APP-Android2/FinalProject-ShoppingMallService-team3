package kr.co.lion.farming_customer.model.payment

class PaymentModel(var payment_idx:Int, var payment_order_num:String,
    var payment_total_price:String, var payment_total_discount:String,
    var payment_final_price:String, var payment_type:Int,
    var payment_status:Int) {

    constructor(): this(
        0,"","","",
        "",0,0
    )
}