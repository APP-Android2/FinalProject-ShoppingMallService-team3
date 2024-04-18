package kr.co.lion.farming_customer.dao.crop

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.CropStatus
import kr.co.lion.farming_customer.model.CropModel
import java.io.File

class CropDao {

    companion object{

        // 농산물 시퀀스 값을 가져오는 메서드
        suspend fun getCropSequence():Int{

            var cropSequence = 0

            val job1 = CoroutineScope(Dispatchers.IO).launch{
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 농산물 시퀀스 값을 가지고 있는 문서에 접근한다.
                val documentReference = collectionReference.document("CropSequence")
                // 문서 내에 있는 데이터를 가져온다.
                val documentSnapshot = documentReference.get().await()
                cropSequence = documentSnapshot.getLong("value")?.toInt()!!
            }
            job1.join()

            return cropSequence
        }

        // 농산물 시퀀스 값을 업데이트 하는 메서드
        suspend fun updateCropSequence(cropSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 농산물 시퀀스 값을 가지고 있는 문서에 접근한다.
                val documentReference = collectionReference.document("CropSequence")

                val map = mutableMapOf<String,Long>()
                map["value"] = cropSequence.toLong()
                documentReference.set(map)
            }
            job1.join()
        }

        // 이미지 데이터를 firebase에 업로드 하는 메서드
        suspend fun uploadImage(context: Context, fileName:String, uploadFileName:String){
            // 외부 저장소 경로
            val filePath = context.getExternalFilesDir(null).toString()
            // 서버로 업로드할 파일의 경로
            val file = File("${filePath}/${fileName}")
            val uri = Uri.fromFile(file)

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                val storageRef = Firebase.storage.reference.child("image/Crop/$uploadFileName")
                // 업로드 한다.
                storageRef.putFile(uri)
            }
            job1.join()
        }

        // 농산품 정보를 저장한다.
        suspend fun insertCropData(cropModel: CropModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("CropData")
                collectionReference.add(cropModel)
            }
            job1.join()

        }

        // 농산품 목록중 좋아요 Top5를 가져온다.
        suspend fun gettingCropLikeTop5List(): List<CropModel> {
            // 농산품 정보를 담을 리스트
            val cropList = mutableListOf<CropModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CropData")
                // 농산품 상태가 정상 상태이며 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                // 농산품 상태가 정상 상태인 것만..
                var query = collectionReference.whereEqualTo("crop_status", CropStatus.NORMAL.num)
                // 농산품 좋아요 개수를 기준으로 내림 차순 정렬..
                query = query.orderBy("crop_like_cnt", Query.Direction.DESCENDING)

                val querySnapshot = query.get().await()

                // 가져온 문서의 수 만큼 반복한다.
                querySnapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val cropModel = it.toObject(CropModel::class.java)
                    // 객체를 리스트에 담는다.
                    cropList.add(cropModel)
                }
            }
            job1.join()

            if(cropList.size > 4){
                return cropList.slice(0..4)
            }
            else{
                return cropList
            }
        }

        // 농산품 목록중 최신에 등록한 Top5를 가져온다.
        suspend fun gettingCropRecentTop5List(): List<CropModel>{
            // 농산품 정보를 담을 리스트
            val cropRecentList = mutableListOf<CropModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CropData")
                // 농산품 상태가 정상 상태이며 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                // 농산품 상태가 정상 상태인 것만..
                var query = collectionReference.whereEqualTo("crop_status", CropStatus.NORMAL.num)
                // 농산품 등록날짜를 기준으로 내림 차순 정렬..
                query = query.orderBy("crop_reg_dt", Query.Direction.DESCENDING)

                val querySnapshot = query.get().await()

                // 가져온 문서의 수 만큼 반복한다.
                querySnapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val cropModel = it.toObject(CropModel::class.java)
                    // 객체를 리스트에 담는다.
                    cropRecentList.add(cropModel)
                }
            }
            job1.join()

            if(cropRecentList.size > 4){
                return cropRecentList.slice(0..4)
            }
            else{
                return cropRecentList
            }
        }

        // 농산품 모든 목록을 가져온다.
        suspend fun gettingCropAllList(dropDownSort:String): MutableList<CropModel>{
            // 농산품 정보를 담을 리스트
            val cropAllList = mutableListOf<CropModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CropData")
                // 농산품 상태가 정상 상태이며 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                // 농산품 상태가 정상 상태인 것만..
                var query = collectionReference.whereEqualTo("crop_status", CropStatus.NORMAL.num)
                when(dropDownSort){
                    "별점순" -> query = query.orderBy("crop_rating",Query.Direction.DESCENDING)
                    "찜순" -> query = query.orderBy("crop_like_cnt",Query.Direction.DESCENDING)
                    "가격 높은순" -> query = query.orderBy("crop_option_price",Query.Direction.DESCENDING)
                    "가격 낮은순" -> query = query.orderBy("crop_option_price",Query.Direction.ASCENDING)
                }

                val querySnapshot = query.get().await()

                // 가져온 문서의 수 만큼 반복한다.
                querySnapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val cropModel = it.toObject(CropModel::class.java)
                    // 객체를 리스트에 담는다.
                    cropAllList.add(cropModel)
                }
            }
            Log.d("test12","aa")
            job1.join()

            return cropAllList
        }

        // 이미지 데이터를 받아오는 메서드
        suspend fun gettingCropImage(context:Context, imageFileName:String, imageView: ImageView){
            CoroutineScope(Dispatchers.IO).launch {
                // 이미지에 접근할 수 있는 객체를 가져온다.
                val storageRef = Firebase.storage.reference.child("$imageFileName")
                // 이미지의 주소를 가지고 있는 Uri 객체를 받아온다.
                val imageUri = storageRef.downloadUrl.await()
                // 이미지 데이터를 받아와 이미지 뷰에 보여준다.
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(context).load(imageUri).into(imageView)
                }
            }
            // 이미지는 용량이 매우 클 수 있다. 즉 이미지 데이터를 내려받는데 시간이 오래걸릴 수도 있다.
            // 이에, 이미지 데이터를 받아와 보여주는 코루틴을 작업이 끝날 때 까지 대기 하지 않는다.
            // 그 이유는 데이터를 받아오는데 걸리는 오랜 시간 동안 화면에 아무것도 나타나지 않을 수 있기 때문이다.
            // 따라서 이 메서드는 제일 마지막에 호출해야 한다.(다른 것들을 모두 보여준 후에...)
        }

        // 농산품 번호를 이용해 농산품 데이터를 가져와 반환하는 메서드
        suspend fun selectCropData(cropIdx:Int):CropModel?{
            var cropModel:CropModel? = null

            val job1 = CoroutineScope(Dispatchers.IO).launch{
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CropData")
                // 문서 필드 중에 cropIdx필드와 매개변수 cropIdx가 같은 Document들을 가져온다.
                val querySnapshot = collectionReference.whereEqualTo("crop_idx",cropIdx).get().await()
                // 가져온 정보를 객체에 담아 반환한다.
                cropModel = querySnapshot.documents[0].toObject(CropModel::class.java)
            }
            job1.join()
            return cropModel
        }

        // 농산품 좋아요 상태 및 개수를 변경하는 메서드
        suspend fun updateCropLikeState(cropModel: CropModel, newLikeState:Boolean){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CropData")
                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("crop_idx", cropModel.crop_idx).get().await()
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any?>()
                // 좋아요 상태와 개수를 업데이트할 필드 추가
                map["like_state"] = newLikeState
                map["crop_like_cnt"] = cropModel.crop_like_cnt

                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }
    }
}