package kr.co.lion.farming_customer.dao.orderHistory

import android.content.Context
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.OrderLabelType
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.OrderStatus
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import java.text.SimpleDateFormat

class OrderDao {
    companion object{
        // 주문 시퀀스 값을 가져온다.
        suspend fun getOrderSequence():Int{

            var orderSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("OrderSequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                orderSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return orderSequence
        }

        // 주문 시퀀스 값을 업데이트 한다.
        suspend fun updateOrderSequence(orderSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("OrderSequence")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                map["value"] = orderSequence.toLong()
                // 저장한다.
                documentReference.set(map)
            }
            job1.join()
        }

        // 주문 정보를 저장한다.
        suspend fun insertOrderData(orderModel: OrderModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 컬럭션에 문서를 추가한다.
                // 문서를 추가할 때 객체나 맵을 지정한다.
                // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름과 동일하게 결정된다.
                collectionReference.add(orderModel)
            }
            job1.join()
        }

        // 주문 번호(idx)를 이용해 주문 데이터를 가져와 반환한다.
        suspend fun selectOrderData(orderIdx:Int): OrderModel?{

            var orderModel: OrderModel? = null

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
                val queryShapshot = collectionReference.whereEqualTo("order_idx", orderIdx).get().await()
                // 가져온 글 정보를 객체에 담아서 반환 받는다.
                // contentIdx가 같은 글은 존재할 수가 없기 때문에 첫 번째 객체를 바로 추출해서 사용한다.
                // toObject : 지정한 클래스를 가지고 객체를 만든 다음 가져온 데이터의 필드의 이름과 동일한 이름의
                // 프로퍼티에 필드의 값을 담아준다.
                orderModel = queryShapshot.documents[0].toObject(OrderModel::class.java)
            }
            job1.join()

            return orderModel
        }

        // 유저 번호(idx)를 이용해 주문 목록을 가져온다.
        suspend fun gettingOrderList(user_idx : Int):MutableList<OrderModel>{
            val orderList = mutableListOf<OrderModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 주문 상태가 정상 상태
                var query = collectionReference.whereEqualTo("order_status", OrderStatus.ORDER_STATUS_NORMAL.number)
                // 체험활동 번호를 기준으로 내림 차순 정렬
                query = query.orderBy("order_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val activityModel = it.toObject(OrderModel::class.java)
                    // 객체를 리스트에 담는다.
                    orderList.add(activityModel)
                }
            }
            job1.join()

            return orderList
        }

        // 주문 번호로 주문 목록을 가져온다.
        suspend fun gettingOrderListByOrderNum(order_num : String):MutableList<OrderModel>{
            val orderList = mutableListOf<OrderModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 주문 상태가 정상 상태 and 주문 번호 일치 검사
                var query = collectionReference.whereEqualTo("order_status", OrderStatus.ORDER_STATUS_NORMAL.number).whereEqualTo("order_num", order_num)
                // 체험활동 번호를 기준으로 내림 차순 정렬
                query = query.orderBy("order_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val activityModel = it.toObject(OrderModel::class.java)
                    // 객체를 리스트에 담는다.
                    orderList.add(activityModel)
                }
            }
            job1.join()

            return orderList
        }

        // 농산물 주문 목록 가져온다.
        suspend fun gettingOrderListCrop(orderLabelType: OrderLabelType, user_idx: Int):MutableList<OrderModel>{
            val orderList = mutableListOf<OrderModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 주문 상태가 정상 상태 and 상품 타입이 농산물
                var query = collectionReference
                    .whereEqualTo("order_status", OrderStatus.ORDER_STATUS_NORMAL.number)
                    .whereEqualTo("order_product_type", OrderProductType.ORDER_PRODUCT_TYPE_CROP.number)
                    .whereEqualTo("order_label", orderLabelType.number)
                    .whereEqualTo("order_user_idx", user_idx)
                    .orderBy("order_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val activityModel = it.toObject(OrderModel::class.java)
                    // 객체를 리스트에 담는다.
                    orderList.add(activityModel)
                }
            }
            job1.join()

            return orderList
        }

        // 라벨 여러개 가져오기
        suspend fun gettingOrderListCrop(orderLabelTypes: List<OrderLabelType>, user_idx: Int): MutableList<OrderModel> {
            val orderList = mutableListOf<OrderModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 주문 상태가 정상 상태 and 상품 타입이 농산물
                var query = collectionReference
                    .whereEqualTo("order_status", OrderStatus.ORDER_STATUS_NORMAL.number)
                    .whereEqualTo("order_product_type", OrderProductType.ORDER_PRODUCT_TYPE_CROP.number)
                    .whereEqualTo("order_user_idx", user_idx)
                    .whereIn("order_label", orderLabelTypes.map { it.number }) // 여러 개의 orderLabelType을 비교합니다.
                    .orderBy("order_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val activityModel = it.toObject(OrderModel::class.java)
                    // 객체를 리스트에 담는다.
                    orderList.add(activityModel)
                }
            }
            job1.join()

            return orderList
        }

        // 주말농장 주문 목록 전체를 가져온다.
        suspend fun gettingOrderListFarm(orderLabelType: OrderLabelType, userIdx: Int):MutableList<OrderModel>{
            val orderList = mutableListOf<OrderModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 주문 상태가 정상 상태 and 상품 타입이 농산물
                var query = collectionReference
                    .whereEqualTo("order_status", OrderStatus.ORDER_STATUS_NORMAL.number)
                    .whereEqualTo("order_product_type", OrderProductType.ORDER_PRODUCT_TYPE_FARM.number)
                    .whereEqualTo("order_label", orderLabelType.number)
                    .whereEqualTo("order_user_idx", userIdx)
                // 체험활동 번호를 기준으로 내림 차순 정렬
                query = query.orderBy("order_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val activityModel = it.toObject(OrderModel::class.java)
                    // 객체를 리스트에 담는다.
                    orderList.add(activityModel)
                }
            }
            job1.join()

            return orderList
        }

        // 체험활동 주문 목록 전체를 가져온다.
        suspend fun gettingOrderListActivity(orderLabelType: OrderLabelType, userIdx: Int):MutableList<OrderModel>{
            val orderList = mutableListOf<OrderModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 주문 상태가 정상 상태 and 상품 타입이 농산물
                var query = collectionReference
                    .whereEqualTo("order_status", OrderStatus.ORDER_STATUS_NORMAL.number)
                    .whereEqualTo("order_product_type", OrderProductType.ORDER_PRODUCT_TYPE_ACTIVITY.number)
                    .whereEqualTo("order_label", orderLabelType.number)
                    .whereEqualTo("order_user_idx", userIdx)
                // 체험활동 번호를 기준으로 내림 차순 정렬
                query = query.orderBy("order_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val activityModel = it.toObject(OrderModel::class.java)
                    // 객체를 리스트에 담는다.
                    orderList.add(activityModel)
                }
            }
            job1.join()

            return orderList
        }

        // 주문 라벨 상태를 변경하는 메서드
        suspend fun updateOrderState(order_idx:Int, newState:OrderLabelType){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 컬렉션이 가지고 있는 문서들 중에 orderIdx 필드가 지정된 글 주문 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("order_idx", order_idx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any>()
                // 라벨
                map["order_label"] = newState.number.toLong()
                // 상태 수정 날짜
                map["order_mod_date"] = SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())
                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }

        // 교환 환불 신청 메서드
        suspend fun applyExchangeReturn(order_idx:Int, cancleMap: MutableMap<String, Any>){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 컬렉션이 가지고 있는 문서들 중에 orderIdx 필드가 지정된 글 주문 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("order_idx", order_idx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any>()

                // 취소 정보
                map["order_cancle"] = cancleMap
                // 라벨
                if(cancleMap["cancle_type"] == "교환"){
                    map["order_label"] = OrderLabelType.ORDER_LABEL_TYPE_EXCHANGE_APPLY.number.toLong()
                }else{
                    map["order_label"] = OrderLabelType.ORDER_LABEL_TYPE_RETURN_APPLY.number.toLong()
                }
                // 상태 수정 날짜
                map["order_mod_date"] = SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())

                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }

        suspend fun cancleReserv(order_idx: Int, cancleMap: MutableMap<String, Any>) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("OrderData")
                // 컬렉션이 가지고 있는 문서들 중에 orderIdx 필드가 지정된 글 주문 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("order_idx", order_idx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any>()

                // 취소 정보
                map["order_cancle"] = cancleMap

                // 라벨 변경
                map["order_label"] = OrderLabelType.ORDER_LABEL_TYPE_RESERV_CANCLE.number.toLong()

                // 상태 수정 날짜
                map["order_mod_date"] = SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())

                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }

        // 이미지 데이터를 firebase storage에 업로드는 메서드
        suspend fun uploadImage(
            context: Context,
            orderIdx: Int,
            uriList: MutableList<Uri>
        ): MutableList<String> {
            val uploadFileList = mutableListOf<String>()
            uriList.forEach {
                val uploadFileName = "order_review_${orderIdx}_${System.currentTimeMillis()}.jpg"
                uploadFileList.add(uploadFileName)
                val job1 = CoroutineScope(Dispatchers.IO).launch {
                    // Storage에 접근할 수 있는 객체를 가져온다.(폴더의 이름과 파일이름을 저장해준다.
                    val storageRef =
                        Firebase.storage.reference.child("image/review/$uploadFileName")
                    // 업로드한다.
                    storageRef.putFile(it)
                }
                job1.join()
            }
            return uploadFileList
        }


    }
}