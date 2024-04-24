package kr.co.lion.farming_customer.model.farmingLife


data class ActivityModel(
    var activity_idx: Int, // 체험활동 번호
    var activity_seller_idx: Int, // 체험활동 판매자 번호
    var activity_title: String, // 체험활동 이름
    var activity_option_detail: MutableList<MutableMap<String, Any>>, // 체험활동 옵션 상세
    var activity_address: String, // 체험활동 주소
    var activity_content_detail: String, // 체험활동 상세 내용
    var activity_content_warning: String, // 체험활동 주의사항
    var activity_content_policy: String, // 체험활동 교환환불정책
    var activity_utility: Map<String, Boolean>, // 체험활동 편의시설
    var activity_like_cnt: Int, // 체험활동 좋아요 개수
    var activity_star : Float, // 체험활동 별점
    var activity_images: MutableList<String>, // 체험활동 이미지 첨부파일
    var activity_reg_dt: String, // 체험활동 등록 날짜
    var activity_mod_dt: String, // 체험활동 수정 날짜
    var activity_status: Int // 체험활동 상태
) {
    constructor() : this(
        0,
        0,
        "",
        mutableListOf(),
        "",
        "",
        "",
        "",
        mutableMapOf(),
        0,
        0F,
        mutableListOf(),
        "",
        "",
        0
    )
}
