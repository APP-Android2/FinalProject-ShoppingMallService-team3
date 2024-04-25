package kr.co.lion.farming_customer.dao.payment

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.model.payment.PaymentModel

class PaymentDao {
    companion object{

        // 결제 시퀀스 값을 가져오는 메서드
        suspend fun getPaymentSequence():Int{

            var paymentSequence = 0

            val job1 = CoroutineScope(Dispatchers.IO).launch{
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 결제 시퀀스 값을 가지고 있는 문서에 접근한다.
                val documentReference = collectionReference.document("PaymentSequence")
                // 문서 내에 있는 데이터를 가져온다.
                val documentSnapshot = documentReference.get().await()
                paymentSequence = documentSnapshot.getLong("value")?.toInt()!!
            }
            job1.join()

            return paymentSequence
        }

        // 결제 시퀀스 값을 업데이트 하는 메서드
        suspend fun updatePaymentSequence(paymentSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 결제 시퀀스 값을 가지고 있는 문서에 접근한다.
                val documentReference = collectionReference.document("PaymentSequence")

                val map = mutableMapOf<String,Long>()
                map["value"] = paymentSequence.toLong()
                documentReference.set(map)
            }
            job1.join()
        }

        // 결제 정보를 저장한다.
        suspend fun insertPaymentData(paymentModel: PaymentModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근한다.
                val collectionReference = Firebase.firestore.collection("PaymentData")
                collectionReference.add(paymentModel)
            }
            job1.join()

        }
    }
}