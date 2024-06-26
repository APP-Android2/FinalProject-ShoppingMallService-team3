package kr.co.lion.farming_customer.dao

import android.content.Context
import android.net.Uri
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
import kr.co.lion.farming_customer.PostStatus
import kr.co.lion.farming_customer.PostType
import kr.co.lion.farming_customer.model.CommunityModel
import java.io.File

class CommunityPostDao {
    companion object {

        // 이미지 데이터를 firebase storage에 업로드는 메서드
        suspend fun uploadImage(context: Context, postIdx:Int, uriList:MutableList<Uri>) : MutableList<String> {

            val uploadFileList = mutableListOf<String>()
            uriList.forEach {
                val uploadFileName = "community_${postIdx}_${System.currentTimeMillis()}.jpg"

                uploadFileList.add(uploadFileName)

                val job1 = CoroutineScope(Dispatchers.IO).launch {
                    // Storage에 접근할 수 있는 객체를 가져온다.(폴더의 이름과 파일이름을 저장해준다.
                    val storageRef = Firebase.storage.reference.child("image/community/$uploadFileName")
                    // 업로드한다.
                    storageRef.putFile(it)
                }

                job1.join()
            }
            return  uploadFileList
        }

        // 이미지 데이터를 받아오는 메서드
        suspend fun gettingCommunityPostImage(context:Context, imageFileName:String, imageView: ImageView){
                val job1 = CoroutineScope(Dispatchers.IO).launch {
                    // 이미지에 접근할 수 있는 객체를 가져온다.
                    val storageRef = Firebase.storage.reference.child("image/community/$imageFileName")
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

        // 게시글 번호 시퀀스값을 가져온다.
        suspend fun getCommunityPostSequence():Int{

            var communityPostSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("CommunityPostSequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                communityPostSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return communityPostSequence
        }

        // 게시글 시퀀스 값을 업데이트 한다.
        suspend fun updateCommunityPostSequence(communityPostSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("CommunityPostSequence")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                map["value"] = communityPostSequence.toLong()
                // 저장한다.
                documentReference.set(map)
            }
            job1.join()
        }

        // 게시글 정보를 저장한다.
        suspend fun insertCommunityPostData(communityModel: CommunityModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 컬럭션에 문서를 추가한다.
                // 문서를 추가할 때 객체나 맵을 지정한다.
                // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름과 동일하게 결정된다.
                collectionReference.add(communityModel)
            }
            job1.join()
        }

        // 글 번호를 이용해 글 데이터를 가져와 반환한다.
        suspend fun selectCommunityPostData(postIdx: Int):CommunityModel?{

            var communityModel:CommunityModel? = null

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 컬렉션이 가지고 있는 문서들 중에 postIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
                val queryShapshot = collectionReference.whereEqualTo("postIdx", postIdx).get().await()
                // 가져온 글 정보를 객체에 담아서 반환 받는다.
                // postIdx 가 같은 글은 존재할 수가 없기 때문에 첫 번째 객체를 바로 추출해서 사용한다.
                // toObject : 지정한 클래스를 가지고 객체를 만든 다음 가져온 데이터의 필드의 이름과 동일한 이름의
                // 프로퍼티에 필드의 값을 담아준다.
                communityModel = queryShapshot.documents[0].toObject(CommunityModel::class.java)
            }
            job1.join()

            return communityModel
        }

        // 게시글 목록을 가져온다.
        suspend fun gettingCommunityPostList(toggleType:String) : MutableList<CommunityModel> {
            // 게시글 정보를 담을 리스트
            val communityPostList = mutableListOf<CommunityModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 게시글 상태가 정상 상태이고 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는 Query 를 생성한다.
                // 게시글 상태가 정상 또는 수정 상태인 것만 가져오기 위한 조건 설정
                val validStatus = listOf(PostStatus.POST_STATUS_NORMAL.number, PostStatus.POST_STATUS_MODIFY.number)
                var query = collectionReference.whereIn("postStatus", validStatus)
                // 게시글 번호를 기준으로 내림차순 정렬
                when(toggleType){
                    "인기순" -> query = query.orderBy("postLikeCnt",Query.Direction.DESCENDING)
                    "최신순" -> query = query.orderBy("postRegDt",Query.Direction.DESCENDING)
                    "조회순" -> query = query.orderBy("postViewCnt",Query.Direction.DESCENDING)
                    "번호순" -> query = query.orderBy("postIdx",Query.Direction.DESCENDING)
                }
                val querySnapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                querySnapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val communityPostModel = it.toObject(CommunityModel::class.java)
                    // 객체를 리스트에 담는다.
                    communityPostList.add(communityPostModel)
                }
            }
            job1.join()

            return communityPostList
        }

        // 글의 상태를 변경하는 메서드
        suspend fun updateCommunityPostStatus(postIdx: Int, newState:PostStatus) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document 들을 가져온다.
                val query = collectionReference.whereEqualTo("postIdx", postIdx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any>()
                map["postStatus"] = newState.number.toLong()
                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }

        // 글 조회수를 구하는 메서드
        suspend fun updateCommunityPostViewCnt(postIdx: Int, postViewCnt:Int) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document 들을 가져온다.
                val query = collectionReference.whereEqualTo("postIdx", postIdx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any>()
                map["postViewCnt"] = postViewCnt.toLong()
                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
        }

        // 글 데이터를 수정하는 메서드
        suspend fun updateCommunityPostData(communityModel: CommunityModel) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 컬렉션이 가지고 있는 문서들 중에 수정할 글 정보를 가져온다.
                val query = collectionReference.whereEqualTo("postIdx", communityModel.postIdx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any?>()
                map["postTitle"] = communityModel.postTitle
                map["postContent"] = communityModel.postContent
                map["postType"] = communityModel.postType

                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }

        // 글 좋아요 상태 및 개수를 변경하는 메서드
        suspend fun updateCommunityPostLikeState(communityModel: CommunityModel, newLikeState:Boolean){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 컬렉션이 가지고 있는 문서들 중에 postIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("postIdx", communityModel.postIdx).get().await()
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any?>()
                // 좋아요 상태와 개수를 업데이트할 필드 추가
                map["postLikeState"] = newLikeState
                map["postLikeCnt"] = communityModel.postLikeCnt

                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }

        // 게시글 목록중 좋아요 Top5를 가져온다.
        suspend fun gettingCommunityPostLikeTop5List(): List<CommunityModel> {
            // 농산품 정보를 담을 리스트
            val communityPostList = mutableListOf<CommunityModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityPostData")
                // 농산품 상태가 정상 상태이며 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                // 게시글 상태가 정상 또는 수정 상태인 것만 가져오기 위한 조건 설정
                val validStatus = listOf(PostStatus.POST_STATUS_NORMAL.number, PostStatus.POST_STATUS_MODIFY.number)
                var query = collectionReference.whereIn("postStatus", validStatus)
                // 게시글 좋아요 개수를 기준으로 내림 차순 정렬..
                query = query.orderBy("postLikeCnt", Query.Direction.DESCENDING)

                val querySnapshot = query.get().await()

                // 가져온 문서의 수 만큼 반복한다.
                querySnapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val communityPostModel = it.toObject(CommunityModel::class.java)
                    // 객체를 리스트에 담는다.
                    communityPostList.add(communityPostModel)
                }
            }
            job1.join()

            if(communityPostList.size > 4){
                return communityPostList.slice(0..4)
            }
            else{
                return communityPostList
            }
        }

    }
}