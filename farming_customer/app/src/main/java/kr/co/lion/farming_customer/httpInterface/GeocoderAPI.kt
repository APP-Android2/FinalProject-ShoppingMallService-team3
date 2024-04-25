package kr.co.lion.farming_customer.httpInterface

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface GeocoderAPI {
    @GET("req/address")
    fun getAddressCoordinates(
        @Query("service") service: String = "address",
        @Query("request") request: String = "getcoord",
        @Query("address") address: String,
        @Query("simple") simple: Boolean = true,
        @Query("type") type: String = "road",
        @Query("key") key: String
    ): Call<GeocoderResponse>
}

data class GeocoderResponse(
    @SerializedName("response")
    val response: Response? = null
)

data class Response(
    @SerializedName("service")
    val service: Service? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("result")
    val result: Result? = null
)

data class Service(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("version")
    val version: Double? = null,
    @SerializedName("operation")
    val operation: String? = null,
    @SerializedName("time")
    val time: String? = null
)

data class Result(
    @SerializedName("crs")
    val crs: String? = null,
    @SerializedName("point")
    val point: Point? = null
)

data class Point(
    @SerializedName("x")
    val x: Double? = null,
    @SerializedName("y")
    val y: Double? = null
)