package kr.co.lion.farming_customer.dao.cart

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.cart.CartModel
import kr.co.lion.farming_customer.model.user.UserModel

class CartDao {
    companion object{
        // 농작물 카트 번호 시퀀스값을 가져온다.
        suspend fun getCartCropSequence():Int{

            var cartSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 유저 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("CartCropSequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                cartSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return cartSequence
        }

        // 농산물 카트 시퀀스 값을 업데이트 하는 메서드
        suspend fun updateCartCropSequence(cropSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 농산물 시퀀스 값을 가지고 있는 문서에 접근한다.
                val documentReference = collectionReference.document("CartCropSequence")

                val map = mutableMapOf<String,Long>()
                map["value"] = cropSequence.toLong()
                documentReference.set(map)
            }
            job1.join()
        }

        // 카트 정보를 저장한다.
        suspend fun insertCartCropData(cartModel: CartModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("CartCropData")
                collectionReference.add(cartModel)
            }
            job1.join()

        }

        // 모든 카트 정보를 가져온다.
        suspend fun getAllCropCart(userIdx: Int) : MutableList<CartModel>{
            // 사용자 정보를 담을 리스트
            val cartCropList = mutableListOf<CartModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // user_idx와 같은 모든 사용자 정보를 가져온다
                val querySnapshot = Firebase.firestore.collection("CartCropData").
                whereEqualTo("cart_user_idx", userIdx).get().await()
                // 가져온 문서의 수 만큼 반복한다.
                querySnapshot.forEach {
                    // UserModel 객체에 담는다.
                    val cartModel = it.toObject(CartModel::class.java)
                    // 리스트에 담는다.
                    cartCropList.add(cartModel)
                }
            }
            job1.join()

            return cartCropList
        }

        // 카트 정보 수정
        suspend fun updateCartCropData(cartModel: CartModel) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CartCropData")

                // 컬렉션이 가지고 있는 문서들 중에 수정할 사용자 정보를 가져온다.
                val query = collectionReference.whereEqualTo("cart_idx", cartModel.cart_idx).get().await()

                // 쿼리 결과에서 첫 번째 문서 참조를 가져와서 해당 문서를 업데이트한다.
                query.documents.firstOrNull()?.reference?.update(
                    mapOf(
                        "cart_count" to cartModel.cart_count,
                        "cart_crop_name" to cartModel.cart_crop_name,
                        "cart_crop_option" to cartModel.cart_crop_option,
                        "cart_idx" to cartModel.cart_idx,
                        "cart_price" to cartModel.cart_price,
                        "cart_status" to cartModel.cart_status,
                        "cart_user_idx" to cartModel.cart_user_idx,
                    )
                )
            }
            job1.join()
        }
    }
}