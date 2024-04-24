package kr.co.lion.farming_customer.model.farminLifeTools

import android.location.Location

data class RentalModel(
    var rental_idx: String, // 임대소 번호
    var rental_name: String, // 임대소 이름
    var rental_phone: String, // 임대소 전화번호
    var rental_address: String, // 임대소 주소
    var rental_latitude: Double?, // 임대소 위도
    var rental_longitude: Double?, // 임대소 경도
    var rental_machines: MutableList<MutableMap<String, String>>, // 임대 기계
    var rental_like_cnt: Int, // 임대소 좋아요 개수
    var rental_machine_images: MutableList<String>, // 임대소 소유 기계 이미지
    var rental_status: String, // 임대소 운영 여부
    var distanceFromLocation : Double = 0.0
){
    constructor() : this(
        "",
        "",
        "",
        "",
        0.0,
        0.0,
        mutableListOf(),
        0,
        mutableListOf(),
        ""
    )
}
