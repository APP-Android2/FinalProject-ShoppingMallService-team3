package kr.co.lion.farming_customer.dao.like

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.FarmModel
import kr.co.lion.farming_customer.model.LikeModel
import kr.co.lion.farming_customer.model.CommunityPostModel
import kr.co.lion.farming_customer.model.RentalModel

class LikeDao {

    companion object {

        //      농산품 좋아요 리스트
        suspend fun getLikeList(userIdx:Int): List<LikeModel>{
            return withContext(Dispatchers.IO) {
                val likeListRef = Firebase.firestore.collection("LikeData")
                val query = likeListRef.whereEqualTo("like_status", 1)
                    .whereEqualTo("like_user_idx", userIdx)
                    .orderBy("like_idx", Query.Direction.ASCENDING)
                val querySnapshot = query.get().await()
                querySnapshot.map { it.toObject(LikeModel::class.java) }
            }
        }

        //      좋아요 클릭한 농산품 데이터 가져오기
        suspend fun getCropList(like_type_idx:Int): List<CropModel>{
            return withContext(Dispatchers.IO) {
                val likeListRef = Firebase.firestore.collection("CropData")
                val query = likeListRef.whereEqualTo("crop_idx", like_type_idx)
                val querySnapshot = query.get().await()
                querySnapshot.map { it.toObject(CropModel::class.java) }
            }
        }



//        //      게시판 좋아요 리스트
//        suspend fun getLikeList(userIdx:Int): List<LikeModel>{
//            return withContext(Dispatchers.IO) {
//                val likeListRef = Firebase.firestore.collection("LikeData")
//                val query = likeListRef.whereEqualTo("like_status", 1)
//                    .whereEqualTo("like_user_idx", userIdx)
//                    .orderBy("like_idx", Query.Direction.ASCENDING)
//                val querySnapshot = query.get().await()
//                querySnapshot.map { it.toObject(LikeModel::class.java) }
//            }
//        }

        //      좋아요 클릭한 게시판 데이터 가져오기
        suspend fun getPostList(like_type_idx:Int): List<CommunityPostModel>{
            return withContext(Dispatchers.IO) {
                val likeListRef = Firebase.firestore.collection("CommunityPostData")
                val query = likeListRef.whereEqualTo("postIdx", like_type_idx)
                    .whereEqualTo("postStatus", 1)
                val querySnapshot = query.get().await()
                querySnapshot.map { it.toObject(CommunityPostModel::class.java) }
            }
        }

//        //      주말농장 좋아요 리스트
//        suspend fun getFarmLikeList(userIdx:Int): List<LikeModel>{
//            return withContext(Dispatchers.IO) {
//                val likeListRef = Firebase.firestore.collection("LikeData")
//                val query = likeListRef.whereEqualTo("like_status", 1)
//                    .whereEqualTo("like_user_idx", userIdx)
//                    .whereEqualTo("like_type", 3)
//                    .orderBy("like_idx", Query.Direction.ASCENDING)
//                val querySnapshot = query.get().await()
//                querySnapshot.map { it.toObject(LikeModel::class.java) }
//            }
//        }

        //      좋아요 클릭한 주말농장 데이터 가져오기
        suspend fun getFarmList(like_type_idx:Int): List<FarmModel> {
            return withContext(Dispatchers.IO) {
                val likeListRef = Firebase.firestore.collection("FarmData")
                val query = likeListRef.whereEqualTo("farm_idx", like_type_idx)
                val querySnapshot = query.get().await()
                querySnapshot.map { it.toObject(FarmModel::class.java) }
            }
        }

//        //      체험활동 좋아요 리스트
//        suspend fun getActivityLikeList(userIdx:Int): List<LikeModel>{
//            return withContext(Dispatchers.IO) {
//                val likeListRef = Firebase.firestore.collection("LikeData")
//                val query = likeListRef.whereEqualTo("like_status", 1)
//                    .whereEqualTo("like_user_idx", userIdx)
//                    .whereEqualTo("like_type", 4)
//                    .orderBy("like_idx", Query.Direction.ASCENDING)
//                val querySnapshot = query.get().await()
//                querySnapshot.map { it.toObject(LikeModel::class.java) }
//            }
//        }

        //      좋아요 클릭한 체험활동 데이터 가져오기
        suspend fun getActivityList(like_type_idx:Int): List<ActivityModel> {
            return withContext(Dispatchers.IO) {
                val likeListRef = Firebase.firestore.collection("ActivityData")
                val query = likeListRef.whereEqualTo("activity_idx", like_type_idx)
                val querySnapshot = query.get().await()
                querySnapshot.map { it.toObject(ActivityModel::class.java) }
            }
        }

//        //      농기구 좋아요 리스트
//        suspend fun getRentalLikeList(userIdx:Int): List<LikeModel>{
//            return withContext(Dispatchers.IO) {
//                val likeListRef = Firebase.firestore.collection("LikeData")
//                val query = likeListRef.whereEqualTo("like_status", 1)
//                    .whereEqualTo("like_user_idx", userIdx)
//                    .whereEqualTo("like_type", 5)
//                    .orderBy("like_idx", Query.Direction.ASCENDING)
//                val querySnapshot = query.get().await()
//                querySnapshot.map { it.toObject(LikeModel::class.java) }
//            }
//        }

        //      좋아요 클릭한 체험활동 데이터 가져오기
        suspend fun getRentalList(like_type_idx:Int): List<RentalModel> {
            return withContext(Dispatchers.IO) {
                val likeListRef = Firebase.firestore.collection("RentalData")
                val query = likeListRef.whereEqualTo("rental_idx", like_type_idx)
                val querySnapshot = query.get().await()
                querySnapshot.map { it.toObject(RentalModel::class.java) }
            }
        }

        // 이미지 데이터를 받아오는 메서드
        suspend fun gettingImage(context: Context, imageFileName:String, imageView: ImageView, type: String){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 이미지에 접근할 수 있는 객체를 가져온다.
                var storageRef : StorageReference

                if( type == "POST"){
                    storageRef = Firebase.storage.reference.child("image/community/$imageFileName")
                } else {
                    storageRef = Firebase.storage.reference.child(imageFileName)
                }

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