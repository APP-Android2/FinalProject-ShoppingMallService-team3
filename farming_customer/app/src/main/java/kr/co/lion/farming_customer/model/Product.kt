package kr.co.lion.farming_customer.model

data class Product(
    val imageResourceId: Int,
    val name: String,
    val location: String,
    val price: String,
    var likes: Int,
    val ratings: Double,
    var isLiked: Boolean = false
)