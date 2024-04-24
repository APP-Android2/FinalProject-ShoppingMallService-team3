package kr.co.lion.farming_customer.model

data class RentalModel(val rental_idx : Int = 0,
                       val rental_name : String = "",
                       val rental_phone : String = "",
                       val rental_address : String = "",
                       val rental_machines : List<String> = emptyList(),
                       val rental_like_cnt : Int = 0,
                       val rental_machine_images : List<String> = emptyList(),
                       val rental_status : Int = 0,)


