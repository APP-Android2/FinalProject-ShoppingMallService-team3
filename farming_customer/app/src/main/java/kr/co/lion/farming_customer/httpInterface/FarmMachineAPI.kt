package kr.co.lion.farming_customer.httpInterface

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FarmMachineAPI {
    @GET("getFarmMachinRentOffice")
    fun fetchData(
        @Query("serviceKey") serviceKey: String,
        @Query("officeCd") officeCd: String,
        @Query("page") page: Int,
    ): Call<String>
}

data class FarmMachineModel(
    @SerializedName("officeNm") val officeName: String,
    @SerializedName("approvalNo") val approvalNumber: String,
    @SerializedName("amdNm") val amdName: String,
    @SerializedName("workNm") val workName: String,
    @SerializedName("formNm") val formName: String,
    @SerializedName("stdNm") val stdName: String,
    @SerializedName("regDate") val regDate: String,
    @SerializedName("rnum") val rnum: Int,
    @SerializedName("idx") val idx: Int,
    @SerializedName("officeCd") val officeCode: String,
    @SerializedName("compNm") val companyName: String,
    @SerializedName("possWomanProp") val possWomanProp: String,
    @SerializedName("posseDepYear") val posseDepYear: String,
    @SerializedName("posseState") val posseState: String
)

data class FarmMachineResponse(
    @SerializedName("result") val result: String,
    @SerializedName("msg") val msg: String,
    @SerializedName("page") val page: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("items") val items: List<FarmMachineModel>
)