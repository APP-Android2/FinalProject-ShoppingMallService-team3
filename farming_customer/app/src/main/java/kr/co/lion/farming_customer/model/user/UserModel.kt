package kr.co.lion.farming_customer.model.user

import kr.co.lion.farming_customer.Gender

data class UserModel(
    var user_idx: Int,              // 유저번호
    var user_name:String,           // 이름
    var user_nickname:String,       // 닉네임
    var user_id:String,             // 아이디
    var user_pw:String,             // 비밀번호
    var user_birth:String,          // 생년월일
    var user_gender: Gender,            // 성별
    var user_phone:String,          // 휴대폰 번호
    var user_address:String?,       // 지역 (선택), null 허용
    var user_service_agree:Boolean, // 서비스 약관 동의
    var user_personal_agree:Boolean,// 개인 정보 수집 약관 동의
    var user_alarm_agree: Boolean, // 알림 서비스 동의 여부 (선택)
    var user_type: Int,             // 사용자 구분 (구매자/ 판매자/ 관리자)
    var user_point: Int,            // 유저 보유 포인트
    var user_profile_image: String, // 사용자 프로필 이미지
) {
    constructor() : this(
        0,
        "",
        "",
        "",
        "",
        "",
        Gender.MALE,
        "",
        "",
        false,
        false,
        false,
        0,
        0,
        ""
    )
}