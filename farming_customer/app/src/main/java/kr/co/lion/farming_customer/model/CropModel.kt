package kr.co.lion.farming_customer.model

data class CropModel(
    var cropIdx:Int, var cropSellerIdx:Int,
    var cropTitle:String, var cropOptionDetail:MutableList<Map<String,String>>,
    var cropContentDetail:String, var cropContentWarning:String,
    var cropContentPolicy:String, var cropLikeCnt:Int,
    var cropImages:MutableList<String>, var cropContentDetailImage:MutableList<String>,
    var cropStatus:Int) {

    constructor(): this(
        0,0,"",
        mutableListOf(),"", "",
        "",0, mutableListOf(),
        mutableListOf(), 0)
}