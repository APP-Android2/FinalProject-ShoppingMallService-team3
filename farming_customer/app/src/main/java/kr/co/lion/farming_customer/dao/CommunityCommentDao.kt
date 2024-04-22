package kr.co.lion.farming_customer.dao

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.CommentStatus
import kr.co.lion.farming_customer.model.CommunityCommentModel

class CommunityCommentDao {

    companion object {

        // 댓글 번호 시퀀스값을 가져온다.
        suspend fun getCommunityCommentSequence():Int{

            var communityCommentSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 댓글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("CommunityCommentSequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                communityCommentSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return communityCommentSequence
        }

        // 댓글 시퀀스 값을 업데이트 한다.
        suspend fun updateCommunityCommentSequence(commentSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 댓글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("CommunityCommentSequence")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                map["value"] = commentSequence.toLong()
                // 저장한다.
                documentReference.set(map)
            }
            job1.join()
        }

        // 댓글 정보를 저장한다.
        suspend fun insertCommunityCommentData(communityCommentModel: CommunityCommentModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityCommentData")
                // 컬럭션에 문서를 추가한다.
                // 문서를 추가할 때 객체나 맵을 지정한다.
                // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름과 동일하게 결정된다.
                collectionReference.add(communityCommentModel)
            }
            job1.join()
        }

        // 댓글 목록을 가져온다.
        suspend fun gettingCommunityCommentList(postIdx:Int):MutableList<CommunityCommentModel>{
            // 댓글 정보를 담을 리스트
            val communityCommentList = mutableListOf<CommunityCommentModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityCommentData")
                // 댓글 상태가 정상 상태이고 댓글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                // 댓글 상태가 정상 상태인 것만..
                var query = collectionReference.whereIn("commentStatus", listOf(CommentStatus.COMMENT_STATUS_NORMAL.number, CommentStatus.COMMENT_STATUS_MODIFY.number))
                // 글 번호에 해당하는 것들만
                query = query.whereEqualTo("commentPostIdx", postIdx)
                // 댓글 번호를 기준으로 내림 차순 정렬..
                query = query.orderBy("commentIdx", Query.Direction.ASCENDING)
                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val communityCommentModel = it.toObject(CommunityCommentModel::class.java)
                    // 객체를 리스트에 담는다.
                    communityCommentList.add(communityCommentModel)
                }

            }
            job1.join()

            return communityCommentList
        }

        // 댓글의 상태를 변경하는 메서드
        suspend fun updateCommunityCommentStatus(commentIdx:Int, newState:CommentStatus){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityCommentData")
                // 컬렉션이 가지고 있는 문서들 중에 commentIdx 필드가 지정된 댓글 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("commentIdx", commentIdx).get().await()

                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Any>()
                map["commentStatus"] = newState.number.toLong()
                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(map)
            }
            job1.join()
        }

        // 댓글의 내용을 변경하는 메서드
        suspend fun updateCommunityCommentData(commentIdx: Int, mapComment: MutableMap<String, Any>){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("CommunityCommentData")
                // 컬렉션이 가지고 있는 문서들 중에 commentIdx 필드가 지정된 댓글 번호값하고 같은 Document들을 가져온다.
                val query = collectionReference.whereEqualTo("commentIdx", commentIdx).get().await()

                // 저장한다.
                // 가져온 문서 중 첫 번째 문서에 접근하여 데이터를 수정한다.
                query.documents[0].reference.update(mapComment)
            }
            job1.join()
        }
    }
}