package kr.co.lion.farming_customer.dao.myPageServiceCenter

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
import kr.co.lion.farming_customer.InquiryState
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.InquiryModel

class MyPageServiceCenterInquiryDao {
    companion object {
        // 문의사항 목록을 가져온다
        suspend fun gettingInquiryList(): MutableList<InquiryModel> {
            // 게시글 정보를 담을 리스트
            val inquiryList = mutableListOf<InquiryModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference =
                    Firebase.firestore.collection("InquiryData")
                // 게시글 상태가 정상 상태이고 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                // 게시글 상태가 정상 상태인 것만..
                var query = collectionReference.whereEqualTo(
                    "inquiry_status",
                    InquiryState.INQUIRY_STATE_NORMAL.number
                )
                // 게시글 번호를 기준으로 내림 차순 정렬..
                query = query.orderBy("inquiry_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val inquiryModel = it.toObject(InquiryModel::class.java)
                    // 객체를 리스트에 담는다.
                    inquiryList.add(inquiryModel)
                }
            }
            job1.join()

            return inquiryList
        }

        // 이미지 데이터를 firebase storage에 업로드는 메서드
        suspend fun uploadImage(
            context: Context,
            inquiryIdx: Int,
            uriList: MutableList<Uri>
        ): MutableList<String> {
            val uploadFileList = mutableListOf<String>()
            uriList.forEach {
                val uploadFileName = "inquiry_${inquiryIdx}_${System.currentTimeMillis()}.jpg"
                uploadFileList.add(uploadFileName)
                val job1 = CoroutineScope(Dispatchers.IO).launch {
                    // Storage에 접근할 수 있는 객체를 가져온다.(폴더의 이름과 파일이름을 저장해준다.
                    val storageRef =
                        Firebase.storage.reference.child("image/inquiry/$uploadFileName")
                    // 업로드한다.
                    storageRef.putFile(it)
                }
                job1.join()
            }
            return uploadFileList
        }

        // 게시글 번호 시퀀스값을 가져온다.
        suspend fun getContentSequence(): Int {

            var contentSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("InquirySequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                contentSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return contentSequence
        }

        // 게시글 시퀀스 값을 업데이트 한다.
        suspend fun updateContentSequence(inquirySequence: Int) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("InquirySequence")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                map["value"] = inquirySequence.toLong()
                // 저장한다.
                documentReference.set(map)
            }
            job1.join()
        }

        // 게시글 정보를 저장한다.
        suspend fun insertContentData(inquiryModel: InquiryModel) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("InquiryData")
                // 컬럭션에 문서를 추가한다.
                // 문서를 추가할 때 객체나 맵을 지정한다.
                // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름과 동일하게 결정된다.
                collectionReference.add(inquiryModel)
            }
            job1.join()
        }

        // 글 번호를 이용해 글 데이터를 가져와 반환한다.
        suspend fun selectInquiryData(inquiryIdx: Int): InquiryModel? {

            var inquiryModel: InquiryModel? = null

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("InquiryData")
                // 컬렉션이 가지고 있는 문서들 중에 contentIdx 필드가 지정된 글 번호값하고 같은 Document들을 가져온다.
                val queryShapshot =
                    collectionReference.whereEqualTo("inquiry_idx", inquiryIdx).get().await()
                // 가져온 글 정보를 객체에 담아서 반환 받는다.
                // contentIdx가 같은 글은 존재할 수가 없기 때문에 첫 번째 객체를 바로 추출해서 사용한다.
                // toObject : 지정한 클래스를 가지고 객체를 만든 다음 가져온 데이터의 필드의 이름과 동일한 이름의
                // 프로퍼티에 필드의 값을 담아준다.
                inquiryModel = queryShapshot.documents[0].toObject(InquiryModel::class.java)
            }
            job1.join()

            return inquiryModel
        }
    }
}