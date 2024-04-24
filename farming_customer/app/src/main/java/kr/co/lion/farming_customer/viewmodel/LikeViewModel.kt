package kr.co.lion.farming_customer.viewmodel

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.lion.farming_customer.LikeType
import kr.co.lion.farming_customer.MaterialButtonToggleGroupWithRadius
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.dao.like.LikeDao
import kr.co.lion.farming_customer.model.ActivityModel
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.FarmModel
import kr.co.lion.farming_customer.model.LikeModel
import kr.co.lion.farming_customer.model.CommunityPostModel
import kr.co.lion.farming_customer.model.RentalModel

class LikeViewModel() :ViewModel(){
    data class LikeData(
        val LikeList: List<LikeModel>?,
        val cropList: List<CropModel>?,
        val postList: List<CommunityPostModel>?,
        val farmList: List<FarmModel>?,
        val activityList: List<ActivityModel>?,
        val rentalList: List<RentalModel>?,
    )

    // 농산물 좋아요 리스트
    suspend fun getLikeListAndLikeTypeList(userIdx: String, type: String): LikeData = withContext(Dispatchers.IO) {
        var LikeList = mutableListOf<LikeModel>()
        var cropList = mutableListOf<CropModel>()
        var postList = mutableListOf<CommunityPostModel>()
        var farmList = mutableListOf<FarmModel>()
        var activityList = mutableListOf<ActivityModel>()
        var rentalList = mutableListOf<RentalModel>()


        when (type) {
            "CROP" -> {
                LikeList = LikeDao.getCropLikeList(userIdx.toInt()).toMutableList()
                val likeTypeIndices = LikeList.map { it.like_type_idx }

                for (likeTypeIdx in likeTypeIndices) {
                    val crop = LikeDao.getCropList(likeTypeIdx)
                    cropList.addAll(crop)
                }
            }

            "POST" -> {
                LikeList = LikeDao.getPostLikeList(userIdx.toInt()).toMutableList()
                val likeTypeIndices = LikeList.map { it.like_type_idx }

                for (likeTypeIdx in likeTypeIndices) {
                    val post = LikeDao.getPostList(likeTypeIdx)
                    postList.addAll(post)
                }
            }

            "FARM" -> {
                LikeList = LikeDao.getFarmLikeList(userIdx.toInt()).toMutableList()
                val likeTypeIndices = LikeList.map { it.like_type_idx }

                for (likeTypeIdx in likeTypeIndices) {
                    val farm = LikeDao.getFarmList(likeTypeIdx)
                    farmList.addAll(farm)
                }
            }

            "ACTIVITY" -> {
                LikeList = LikeDao.getActivityLikeList(userIdx.toInt()).toMutableList()
                val likeTypeIndices = LikeList.map { it.like_type_idx }

                for (likeTypeIdx in likeTypeIndices) {
                    val activity = LikeDao.getActivityList(likeTypeIdx)
                    activityList.addAll(activity)
                }
            }

            "RENTAL" -> {
                LikeList = LikeDao.getRentalLikeList(userIdx.toInt()).toMutableList()
                val likeTypeIndices = LikeList.map { it.like_type_idx }

                for (likeTypeIdx in likeTypeIndices) {
                    val rental = LikeDao.getRentalList(likeTypeIdx)
                    rentalList.addAll(rental)
                }
            }
        }


        LikeData(LikeList, cropList, postList, farmList, activityList, rentalList)
    }

    // 좋아요 타입
    val toggleLikeType = MutableLiveData<Int>()
    val isLike = MutableLiveData<Boolean>()
    val textView_likePostCnt = MutableLiveData<String>()
    val textView_likeCropCnt = MutableLiveData<String>()
    val textView_likeFarmActivityCnt = MutableLiveData<String>()
    val textView_likeRentalCnt = MutableLiveData<String>()
    fun settingLikeType(likeType: LikeType){
        when(likeType){
            LikeType.CROP -> {
                toggleLikeType.value = R.id.buttonLikeCrop
            }
            LikeType.POST -> {
                toggleLikeType.value = R.id.buttonLikePost
            }
            LikeType.FARM -> {
                toggleLikeType.value = R.id.buttonLikeFarm
            }
            LikeType.ACTIVITY -> {
                toggleLikeType.value = R.id.buttonLikeActivity
            }
            LikeType.RENTAL -> {
                toggleLikeType.value = R.id.buttonLikeRental
            }
        }
    }


    companion object{
        // ViewModel에 값을 설정하여 화면에 반영하는 작업을 할 때 호출된다.
        // () 안에는 속성의 이름을 넣어준다. 속성의 이름을 자유롭게 해주면 되지만 기존의 속성 이름과 중복되지 않게
        // 해줘야 한다.
        @BindingAdapter("android:checkedButtonId")
        @JvmStatic
        // 매개변수 : 값이 설정된 View 객체, ViewModel을 통해 설정되는 값
        fun setCheckedButtonId(group:MaterialButtonToggleGroupWithRadius, buttonId:Int){
            group.check(buttonId)
        }

        // 값을 속성에 넣어주는 것을 순방향이라고 부른다. 반대로 속성의 값이 변경되어 MutableLive 데이터로
        // 전달하는 것을 역방햐이라고 한다.
        // 순방향만 구현해주면 단방향이 되는 것이고 순방향과 역방향을 모두 구현해주면 양방향
        // 화면 요소가 가진 속성애 새로운 값이 설정되면 ViewModel의 변수에 값이 설정될 때 호출된다.
        // 리스너 역할을 할 속성을 만들어준다.
        @BindingAdapter("checkedButtonChangeListener")
        @JvmStatic
        fun checkedButtonChangeListener(group:MaterialButtonToggleGroupWithRadius, inverseBindingListener: InverseBindingListener){
            group.addOnButtonCheckedListener { group, checkedId, isChecked ->
                inverseBindingListener.onChange()
            }
        }
        // 역방향 바인딩이 벌어질 때 호출된다.
        @InverseBindingAdapter(attribute = "android:checkedButtonId", event="checkedButtonChangeListener")
        @JvmStatic
        fun getCheckedButtonId(group:MaterialButtonToggleGroupWithRadius):Int{
            return group.checkedButtonId
        }
    }


}