package kr.co.lion.farming_customer.dao.myPageServiceCenter

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.FaqState
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.FaqModel

class MyPageServiceCenterFaqDao {
    companion object {
        suspend fun gettingFaqList():MutableList<FaqModel>{
            // 게시글 정보를 담을 리스트
            val faqList = mutableListOf<FaqModel>()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = com.google.firebase.Firebase.firestore.collection("FaqData")
                // 게시글 상태가 정상 상태이고 게시글 번호를 기준으로 내림차순 정렬되게 데이터를 가져올 수 있는
                // Query를 생성한다.
                // 게시글 상태가 정상 상태인 것만..
                var query = collectionReference.whereEqualTo("faq_status", FaqState.FAQ_STATE_NORMAL.number)
                // 게시글 번호를 기준으로 내림 차순 정렬..
                query = query.orderBy("faq_idx", Query.Direction.DESCENDING)

                val queryShapshot = query.get().await()
                // 가져온 문서의 수 만큼 반복한다.
                queryShapshot.forEach {
                    // 현재 번째의 문서를 객체로 받아온다.
                    val faqModel = it.toObject(FaqModel::class.java)
                    // 객체를 리스트에 담는다.
                    faqList.add(faqModel)
                }
            }
            job1.join()

            return faqList
        }
    }
}