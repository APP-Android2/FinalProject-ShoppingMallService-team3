package kr.co.lion.farming_customer.dao.myPageReview

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.ReviewState
import kr.co.lion.farming_customer.ReviewType
import kr.co.lion.farming_customer.model.myPageReview.ReviewModel

class MyPageReviewDao {
    companion object {

        suspend fun gettingCropReviewList():MutableList<ReviewModel>{
            // 게시글 정보를 담을 리스트
            val reviewList = mutableListOf<ReviewModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("ReviewData")
                // 게시글 상태가 정상 상태이고 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                var query = collectionReference
                    .whereEqualTo("review_status", ReviewState.REVIEW_STATE_NORMAL.number) // 게시글 상태가 정상인 것만
                    .whereEqualTo("review_type", ReviewType.TYPE_CROP.number) // 리뷰 타입이 0인 데이터만 가져옴

                // 게시글 번호를 기준으로 내림 차순 정렬..
                query = query.orderBy("review_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val reviewModel = it.toObject(ReviewModel::class.java)
                    // 객체를 리스트에 담는다.
                    reviewList.add(reviewModel)
                }
            }
            job1.join()

            return reviewList
        }

        suspend fun gettingFarmReviewList():MutableList<ReviewModel>{
            // 게시글 정보를 담을 리스트
            val reviewList = mutableListOf<ReviewModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("ReviewData")
                // 게시글 상태가 정상 상태이고 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                var query = collectionReference
                    .whereEqualTo("review_status", ReviewState.REVIEW_STATE_NORMAL.number) // 게시글 상태가 정상인 것만
                    .whereEqualTo("review_type", ReviewType.TYPE_FARM.number) // 리뷰 타입이 1인 데이터만 가져옴

                // 게시글 번호를 기준으로 내림 차순 정렬..
                query = query.orderBy("review_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val reviewModel = it.toObject(ReviewModel::class.java)
                    // 객체를 리스트에 담는다.
                    reviewList.add(reviewModel)
                }
            }
            job1.join()

            return reviewList
        }

        suspend fun gettingActivityReviewList():MutableList<ReviewModel>{
            // 게시글 정보를 담을 리스트
            val reviewList = mutableListOf<ReviewModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("ReviewData")
                // 게시글 상태가 정상 상태이고 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                var query = collectionReference
                    .whereEqualTo("review_status", ReviewState.REVIEW_STATE_NORMAL.number) // 게시글 상태가 정상인 것만
                    .whereEqualTo("review_type", ReviewType.TYPE_ACTIVITY.number) // 리뷰 타입이 1인 데이터만 가져옴

                // 게시글 번호를 기준으로 내림 차순 정렬..
                query = query.orderBy("review_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val reviewModel = it.toObject(ReviewModel::class.java)
                    // 객체를 리스트에 담는다.
                    reviewList.add(reviewModel)
                }
            }
            job1.join()

            return reviewList
        }

        // 이미지 데이터를 받아오는 메서드
        suspend fun gettingCropReviewPostImage(context: Context, imageFileName:String?, imageView: ImageView){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 이미지에 접근할 수 있는 객체를 가져온다.
                val storageRef = Firebase.storage.reference.child("image/review/$imageFileName")
                // 이미지의 주소를 가지고 있는 Uri 객체를 받아온다.
                val imageUri = storageRef.downloadUrl.await()
                //Log.e("cropImage", imageUri.toString())
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

//        // 글의 상태를 변경하는 메서드
//        suspend fun updateReviewState(reviewIdx: Int, newState: ReviewState){
//            val job1 = CoroutineScope(Dispatchers.IO).launch {
//                // 컬렉션에 접근할 수 있는 객체를 가져온다.
//                val collectionReference = Firebase.firestore.collection("ReviewData")
//                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
//                val query = collectionReference.whereEqualTo("review_idx", reviewIdx)
//                val documents = query.get().await().documents
//
//                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
//                val documentReference = documents.firstOrNull()?.reference
//                documentReference?.update("review_status", newState.number)
//            }
//            job1.join()
//        }

        // 글의 상태를 변경하는 메서드
        suspend fun updateReviewState(reviewIdx:Int, newState:ReviewState){
            Log.e("reviewIdxddddd dao", reviewIdx.toString())
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("ReviewData")
                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("review_idx", reviewIdx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                map["review_status"] = newState.number.toLong()
                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.set(map)
            }
            job1.join()
        }


        // 게시글 번호 시퀀스값을 가져온다.
        suspend fun getReviewSequence():Int{

            var reviewSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("ReviewSequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                reviewSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return reviewSequence
        }

    }
}