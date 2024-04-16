package kr.co.lion.farming_customer.model

data class ProductCard(
    val imageResourceId: Int,
    val name: String,
    val price: String,
    var likes: Int,
    val ratings: Double,
    var isLiked: Boolean = false
)