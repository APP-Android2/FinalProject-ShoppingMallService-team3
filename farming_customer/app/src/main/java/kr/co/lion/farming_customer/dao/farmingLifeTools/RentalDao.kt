package kr.co.lion.farming_customer.dao.farmingLifeTools

import android.location.Location
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.model.farminLifeTools.RentalModel
import kr.co.lion.farming_customer.model.orderHistory.OrderModel

class RentalDao {
    companion object{
        // 임대사업소 정보를 저장한다.
        suspend fun insertRentalData(rentalModel: RentalModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("RentalData")
                // 컬럭션에 문서를 추가한다.
                // 문서를 추가할 때 객체나 맵을 지정한다.
                // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름과 동일하게 결정된다.
                collectionReference.add(rentalModel)
            }
            job1.join()
        }

        // 임대사업소 목록을 가져온다.
        suspend fun gettingRentalList():MutableList<RentalModel>{
            val rentalList = mutableListOf<RentalModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("RentalData")
                // 체험활동 상태가 정상 상태
                var query = collectionReference.orderBy("rental_idx", Query.Direction.ASCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val rentalModel = it.toObject(RentalModel::class.java)

                    // 객체를 리스트에 담는다.
                    rentalList.add(rentalModel)
                }
            }
            job1.join()

            return rentalList
        }

        // 내 위치 기준 가까운 순서대로 임대사업소 목록을 가져온다.
        suspend fun gettingRentalListByLocation(location1: Location):MutableList<RentalModel>{
            val rentalList = mutableListOf<RentalModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("RentalData")
                // 체험활동 상태가 정상 상태
                var query = collectionReference.orderBy("rental_idx", Query.Direction.ASCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val rentalModel = it.toObject(RentalModel::class.java)

                    if(rentalModel.rental_latitude != null && rentalModel.rental_longitude != null){
                        // 내 위치와 임대 사업소 사이의 거리
                        val distance = calculateDistance(location1,
                            rentalModel.rental_latitude!!, rentalModel.rental_longitude!!
                        )
                        rentalModel.distanceFromLocation = distance

                        // 객체를 리스트에 담는다.
                        rentalList.add(rentalModel)
                    }
                }
                // 거리에 따라 리스트 정렬
                rentalList.sortBy { it.distanceFromLocation }
            }
            job1.join()

            return rentalList
        }

        // 두 위치 간의 거리 계산 함수
        fun calculateDistance(location1: Location, rental_latitude: Double, rentalLongitude: Double): Double {
            // 여기에 거리 계산 알고리즘을 구현합니다.
            // 예를 들어, 유클리드 거리 또는 Haversine 공식을 사용할 수 있습니다.
            // 여기서는 예시로 두 위치의 x, y 좌표를 사용한 유클리드 거리 계산을 제공합니다.
            val dx = location1.longitude - rentalLongitude
            val dy = location1.latitude - rental_latitude
            return Math.sqrt(dx * dx + dy * dy)
        }


        // 농기계 임대사업소의 농기계 이미지 리스트를 업데이트한다.
        suspend fun updateRentalImageList(rentalIdx:String, imgList: MutableList<String?>){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("RentalData")
                // 컬렉션이 가지고 있는 문서들 중에 orderIdx 필드가 지정된 글 주문 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("rental_idx", rentalIdx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any>()
                // 이미지 리스트
                map["rental_machine_images"] = imgList
                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
            Log.e("inung", "${rentalIdx} update imgList")
        }

        // 농기계 임대소 번호(idx)를 이용해 농기계 임대소 데이터를 가져와 반환한다.
        suspend fun selectRentalData(rentalIdx:String): RentalModel?{

            var rentalModel: RentalModel? = null

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("RentalData")
                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
                val queryShapshot = collectionReference.whereEqualTo("rental_idx", rentalIdx).get().await()
                // 가져온 글 정보를 객체에 담아서 반환 받는다.
                // contentIdx가 같은 글은 존재할 수가 없기 때문에 첫 번째 객체를 바로 추출해서 사용한다.
                // toObject : 지정한 클래스를 가지고 객체를 만든 다음 가져온 데이터의 필드의 이름과 동일한 이름의
                // 프로퍼티에 필드의 값을 담아준다.
                rentalModel = queryShapshot.documents[0].toObject(RentalModel::class.java)
            }
            job1.join()

            return rentalModel
        }


    }

}