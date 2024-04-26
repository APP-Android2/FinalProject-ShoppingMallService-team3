package kr.co.lion.farming_customer.httpInterface

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FarmMachineOfficeAPI {
    @GET("getFarmMachinOffice")

    fun fetchData(@Query("serviceKey") serviceKey: String): Call<ApiResponse>
}

data class FarmMachinOfficeModel(
    @SerializedName("officeCd") val officeCode: String, // 임대사업소코드
    @SerializedName("officeNm") val officeName: String, // 임대사업소명
    @SerializedName("addr") val address: String, // 임대사업소 주소
    @SerializedName("phone") val phone: String, // 임대사업소 전화번호
    @SerializedName("useYn") val useYn: String, // 사용여부 (운영여부)
    @SerializedName("regDate") val registrationDate: String // 등록일
)

data class ApiResponse(
    @SerializedName("result") val result: String,
    @SerializedName("msg") val message: String,
    @SerializedName("items") val offices: List<FarmMachinOfficeModel>
)