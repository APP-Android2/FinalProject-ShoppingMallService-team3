package kr.co.lion.farming_customer.model.farminLife

data class FarmModel(
    var farm_idx : Int, // 주말농장 번호
    var farm_seller_idx : Int, // 주말농장 판매자 번호
    var farm_title : String, // 주말농장 이름
    var farm_option_detail : Map<String, Any>, // 주말농장 옵션 상세
    var farm_address : String, // 주말농장 주소
    var farm_content_detail :String, // 주말농장 상세 내용
    var farm_content_warning :String, // 주말농장 주의 사항
    var farm_content_policy : String, // 주말농장 교환 환불 정책
    var farm_apply_date_start : String, // 주말농장 신청 기간 시작
    var farm_apply_date_end :String, // 주말농장 신청 기간 마감
    var farm_use_date_start :String, // 주말농장 이용기간 시작
    var farm_use_date_end : String, // 주말농장 이용 기간 마감
    var farm_can_crop : MutableList<String>, // 주말 농장 생산 가능 작물
    var farm_utility : Map<String, Boolean>, // 주말농장 편의시설
    var farm_like_cnt : Int, // 주말농장 좋아요 개수
    var farm_images : MutableList<String>, // 주말농장 이미지 첨부파일
    var farm_reg_dt : String, // 주말농장 등록 날짜
    var farm_mod_dt : String, // 주말농장 수정 날짜
    var farm_status : Int // 주말농장 상태
    ){
    constructor() : this(
        0,
        0,
        "",
        mutableMapOf(),
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        mutableListOf(),
        mutableMapOf(),
        0,
        mutableListOf(),
        "",
        "",
        0
        )
}
