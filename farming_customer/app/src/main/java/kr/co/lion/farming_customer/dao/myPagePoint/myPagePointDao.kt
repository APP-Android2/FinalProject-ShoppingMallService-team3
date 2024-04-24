package kr.co.lion.farming_customer.dao.myPagePoint

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.ActivityStatus
import kr.co.lion.farming_customer.PointStatus
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.model.myPagePoint.PointModel

class myPagePointDao {
    companion object{
        // 포인트 번호 시퀀스값을 가져온다.
        suspend fun getPointSequence():Int{

            var pointSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("PointSequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                pointSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return pointSequence
        }

        // 주말농장 시퀀스 값을 업데이트 한다.
        suspend fun updatePointSequence(pointSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("PointSequence")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                map["value"] = pointSequence.toLong()
                // 저장한다.
                documentReference.set(map)
            }
            job1.join()
        }

        // 포인트 정보를 저장한다.
        suspend fun insertPointData(pointModel: PointModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("PointData")
                // 컬럭션에 문서를 추가한다.
                // 문서를 추가할 때 객체나 맵을 지정한다.
                // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름과 동일하게 결정된다.
                collectionReference.add(pointModel)
            }
            job1.join()
        }

        // 포인트 목록을 가져온다.
        suspend fun gettingPointList():MutableList<PointModel>{
            val pointList = mutableListOf<PointModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("PointData")
                // 체험활동 상태가 정상 상태
                var query = collectionReference.whereEqualTo("point_status", PointStatus.POINT_STATE_NORMAL.number)
                // 체험활동 번호를 기준으로 내림 차순 정렬
                query = query.orderBy("point_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val pointModel = it.toObject(PointModel::class.java)
                    // 객체를 리스트에 담는다.
                    pointList.add(pointModel)
                }
            }
            job1.join()

            return pointList
        }

    }
}