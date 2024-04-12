package kr.co.lion.farming_customer.model

data class SelectedOptionModel(var programName : String, var date : String, var time : String, var price : String, var cnt : Int) {
    constructor() : this("", "", "", "", 0)
}