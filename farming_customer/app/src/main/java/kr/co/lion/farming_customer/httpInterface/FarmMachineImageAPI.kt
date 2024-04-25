package kr.co.lion.farming_customer.httpInterface

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FarmMachineImageAPI {

    @GET("getFarmMachinCompanyGoods")

    fun fetchData(
        @Query("serviceKey") serviceKey: String,
        @Query("compCd") compCd : String,
        @Query("page") page : Int
    ): Call<String>
}

data class FarmMachineImageModel(
    @SerializedName("stdNm") val stdName: String, // 규격명
    @SerializedName("isPossGov") val isPossGov: String, // 융자대상
    @SerializedName("regDate") val regDate: String, // 등록일
    @SerializedName("rnum") val rnum: Int, // 페이징 일련번호
    @SerializedName("compNm") val compNm: String, // 제조사명
    @SerializedName("idx") val idx: Int, // 번호
    @SerializedName("compCd") val compCd: String, // 제조사코드
    @SerializedName("workNm") val workNm: String, // 작업기명
    @SerializedName("formNm") val formNm: String, // 형식명
    @SerializedName("amdNm") val amdNm: String, // 동력기명
    @SerializedName("posseAmtGov") val posseAmtGov: String, // 융자지원한도
    @SerializedName("imgUrl") val imgUrl: String // 농기계 이미지 주소
)

data class FarmMachineImageResponse(
    @SerializedName("result") val result: String,
    @SerializedName("msg") val msg: String,
    @SerializedName("page") val page: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<FarmMachineImageModel>
)