package kr.co.lion.farming_customer.model

data class CropReview(
    val name: String,
    val imageResourceProfile: Int,
    val rating: Float,
    val imageResourceId: List<Int>,
    val productName: String,
    val optionName: String,
    val date: String
)