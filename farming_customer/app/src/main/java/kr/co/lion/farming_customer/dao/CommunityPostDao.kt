package kr.co.lion.farming_customer.dao

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
//        /storage/emulated/0/Pictures/IMG_20240312_113302.jpg
//        file:///storage/emulated/0/Android/data/kr.co.lion.farming_customer/files/content%3A/com.google.android.apps.photos.contentprovider/-1/1/content%253A%252F%252Fmedia%252Fexternal%252Fimages%252Fmedia%252F1000000033/ORIGINAL/NONE/image%252Fjpeg/1126321260
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
    }
}