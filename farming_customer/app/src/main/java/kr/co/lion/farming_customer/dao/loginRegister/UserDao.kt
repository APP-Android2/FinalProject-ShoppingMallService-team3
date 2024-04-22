package kr.co.lion.farming_customer.dao.loginRegister

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.google.firebase.Firebase
import com.bumptech.glide.Glide
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kr.co.lion.farming_customer.model.user.UserModel

class UserDao {
    companion object{
        // 유저 번호 시퀀스값을 가져온다.
        suspend fun getUserSequence():Int{

            var userSequence = -1

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 유저 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("UserSequence")
                // 문서내에 있는 데이터를 가져올 수 있는 객체를 가져온다.
                val documentSnapShot = documentReference.get().await()
                userSequence = documentSnapShot.getLong("value")?.toInt()!!
            }
            job1.join()

            return userSequence
        }

        // 유저번호 시퀀스 값을 업데이트 한다.
        suspend fun updateUserSequence(userSequence:Int){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("Sequence")
                // 게시글 번호 시퀀스 값을 가지고 있는 문서에 접근할 수 있는 객체를 가져온다.
                val documentReference = collectionReference.document("UserSequence")
                // 저장할 데이터를 담을 HashMap을 만들어준다.
                val map = mutableMapOf<String, Long>()
                map["value"] = userSequence.toLong()
                // 저장한다.
                documentReference.set(map)
            }
            job1.join()
        }

        // 사용자 정보를 저장한다.
        suspend fun insertUserData(userModel: UserModel){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("UserData")
                // 컬럭션에 문서를 추가한다.
                // 문서를 추가할 때 객체나 맵을 지정한다.
                // 추가된 문서 내부의 필드는 객체가 가진 프로퍼티의 이름이나 맵에 있는 데이터의 이름과 동일하게 결정된다.
                collectionReference.add(userModel)
            }
            job1.join()
        }

        // 입력한 아이디가 저장되어 있는 문서가 있는지 확인한다(중복처리)
        // 사용할 수 있는 아이디라면 true, 존재하는 아이디라면 false를 반환한다.
        suspend fun checkUserIdExist(joinUserId:String) : Boolean{

            var chk = false

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("UserData")
                // UserId 필드가 사용자가 입력한 아이디와 같은 문서들을 가져온다.
                // whereEqualTo : 같은것
                // whereGreaterThan : 큰것
                // whereGreaterThanOrEqualTo : 크거나 같은 것
                // whereLessThan : 작은 것
                // whereLessThanOrEqualTo : 작거나 같은 것
                // whereNotEqualTo : 다른 것
                // 필드의 이름, 값 형태로 넣어준다
                val queryShapshot = collectionReference.whereEqualTo("user_id", joinUserId).get().await()
                // 반환되는 리스트에 담긴 문서 객체가 없다면 존재하는 아이디로 취급한다.
                chk = queryShapshot.isEmpty
            }

            job1.join()

            return chk
        }

        // 입력한 닉네임이 저장되어 있는 문서가 있는지 확인한다(중복처리)
        // 사용할 수 있는 닉네임이라면 true, 존재하는 닉네임이라면 false를 반환한다.
        suspend fun checkUserNickNameExist(joinUserId:String) : Boolean{

            var chk = false

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("UserData")
                val queryShapshot = collectionReference.whereEqualTo("user_nickname", joinUserId).get().await()
                // 반환되는 리스트에 담긴 문서 객체가 없다면 존재하는 아이디로 취급한다.
                chk = queryShapshot.isEmpty
            }

            job1.join()

            return chk
        }

        // 사용자 번호를 통해 사용자 정보를 가져와 반환한다
        suspend fun gettingUserInfoByUserIdx(userIdx:Int) : UserModel? {

            var userModel:UserModel? = null

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // UserData 컬렉션 접근 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("UserData")
                // userIdx 필드가 매개변수로 들어오는 userIdx와 같은 문서들을 가져온다.
                val querySnapshot = collectionReference.whereEqualTo("user_idx", userIdx).get().await()
                // 가져온 문서객체들이 들어 있는 리스트에서 첫 번째 객체를 추출한다.
                // 회원 번호가 동일한 사용는 없기 때문에 무조건 하나만 나오기 때문이다
                userModel = querySnapshot.documents[0].toObject(UserModel::class.java)
            }
            job1.join()

            return userModel
        }

        // 사용자 정보 수정
        suspend fun updateUserData(userModel: UserModel) {
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 컬렉션에 접근할 수 있는 객체를 가져온다.
                val collectionReference = Firebase.firestore.collection("UserData")

                // 컬렉션이 가지고 있는 문서들 중에 수정할 사용자 정보를 가져온다.
                val query = collectionReference.whereEqualTo("user_idx", userModel.user_idx).get().await()

                // 쿼리 결과에서 첫 번째 문서 참조를 가져와서 해당 문서를 업데이트한다.
                // 주의: 여기서는 첫 번째 문서만 업데이트하고 있음
                query.documents.firstOrNull()?.reference?.update("user_profile_image", userModel.user_profile_image)
            }
            job1.join()
        }

        // 이미지 데이터를 firebase storage에 업로드하는 메서드
        suspend fun uploadImage(context: Context, postIdx: Int, uri: Uri): String {
            val uploadFileName = "user${postIdx}_${System.currentTimeMillis()}.jpg"
            val storageRef = Firebase.storage.reference.child("image/user/$uploadFileName")

            return try {
                // 업로드 작업을 수행하고 결과를 기다림
                val uploadTask = storageRef.putFile(uri).await()
                uploadFileName
            } catch (e: Exception) {
                // 실패시 에러 처리
                e.printStackTrace()
                ""
            }
        }

        // 이미지 데이터를 받아오는 메서드
        suspend fun gettingUserImage(context:Context, imageFileName:String, imageView: ImageView){
            val job1 = CoroutineScope(Dispatchers.IO).launch {
                // 이미지에 접근할 수 있는 객체를 가져온다.
                val storageRef = Firebase.storage.reference.child(imageFileName)
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