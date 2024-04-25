package kr.co.lion.farming_customer.dao.cart

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.CartCropStatus
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
                // user_idx와 같은 모든 사용자 정보를 가져온다. 이 때, cart_status가 CART_CROP_STATUS_NORMAL인 것만
                val querySnapshot = Firebase.firestore.collection("CartCropData").
                whereEqualTo("cart_user_idx", userIdx).
                whereEqualTo("cart_status", CartCropStatus.CART_CROP_STATUS_NORMAL.number).
                get().await()
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
                        "cart_image_file_name" to cartModel.cart_image_file_name,
                        "cart_crop_delivery_fee" to cartModel.cart_crop_delivery_fee
                    )
                )
            }
            job1.join()
        }

        // 이미지 데이터를 받아오는 메서드
        suspend fun gettingContentImage(context: Context, imageFileName:String, imageView: ImageView){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 이미지에 접근할 수 있는 객체를 가져온다.
                val storageRef = Firebase.storage.reference.child(imageFileName)
                // 이미지의 주소를 가지고 있는 Uri 객체를 받아온다.
                val imageUri = storageRef.downloadUrl.await()
                // 이미지 데이터를 받아와 이미지 뷰에 보여준다.
                val job2 = CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(context).load(imageUri).into(imageView)
                    // 이미지 뷰가 나타나게 한다.
                    imageView.visibility = View.VISIBLE
                }
                job2.join()
            }
            job1.join()

            // 이미지는 용량이 매우 클 수 있다. 즉 이미지 데이터를 내려받는데 시간이 오래걸릴 수도 있다.
            // 이에, 이미지 데이터를 받아와 보여주는 코루틴을 작업이 끝날 때 까지 대기 하지 않는다.
            // 그 이유는 데이터를 받아오는데 걸리는 오랜 시간 동안 화면에 아무것도 나타나지 않을 수 있기 때문이다.
            // 따라서 이 메서드는 제일 마지막에 호출해야 한다.(다른 것들을 모두 보여준 후에...)
        }

    }
}