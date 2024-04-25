package kr.co.lion.farming_customer.model.cart

data class CartModel (
    // 장바구니 번호
    var cart_idx: Int,
    // 농작물 이름
    var cart_crop_name: String,
    // 장바구니 사용자 번호
    var cart_user_idx: Int,
    // 옵션
    var cart_crop_option: String,
    // 가격
    var cart_price: String,
    // 개수
    var cart_count: Int,
    // 장바구니 상태
    var cart_status: Int,
    // 이미지 파일 명
    var cart_image_file_name: String
) {
    constructor(): this(0, "", 0, "", "", 0, 0, "")
}