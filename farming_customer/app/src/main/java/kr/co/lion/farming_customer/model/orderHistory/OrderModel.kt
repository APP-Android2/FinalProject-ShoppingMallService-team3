package kr.co.lion.farming_customer.model.orderHistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
    var order_idx: Int, // 주문 번호
    var order_num: String, // 주문 고유 번호
    var order_user_idx: Int, // 주문한 사람
    var order_seller_idx: Int, // 주문을 조회할 판매자 번호
    var order_product_type: Int, // 주문한 상품의 종류
    var order_product_idx: Int, // 상품 번호
    var order_label: Int, // 주문 상태 라벨
    var order_invoice_number: String, // 송장번호
    var order_delivery_address: MutableMap<String, String>, // 배송지
    var order_reg_date: String, // 주문날짜
    var order_mod_date: String, // 주문 상태 수정 날짜
    var order_delivery_start_date: String, // 출고일
    var order_delivery_done_date: String, // 배송완료일
    var order_is_reviewed: Boolean, // 리뷰 여부
    var order_option_detail: MutableList<MutableMap<String, String>>, // 주문 옵션 상세
    var order_total_price: String, // 주문 전체 가격
    var order_cancel_reason: String, // 취소 사유
    var order_cancel_reason_detail: String, // 취소 사유 내용
    var order_status: Int, // 주문 상태
) : Parcelable {
    constructor() : this(
        0,
        "",
        0,
        0,
        0,
        0,
        0,
        "",
        mutableMapOf(),
        "",
        "",
        "",
        "",
        false,
        mutableListOf(),
        "",
        "",
        "",
        0
    )
}
