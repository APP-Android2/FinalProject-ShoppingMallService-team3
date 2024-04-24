package kr.co.lion.farming_customer.model.myPagePoint

data class PointModel(
    var point_idx : Int, // 포인트 번호
    var point_user_idx : Int, // 받은사람
    var point_type : Int, // 포인트 타입
    var point_changed : Int, // 변화량
    var point_reason : String, // 사유
    var point_date : String, // 적립날짜
    var point_status : Int, // 포인트 상태
) {
    constructor() : this(
        0,
        0,
        0,
        0,
        "",
        "",
        0
    )
}
