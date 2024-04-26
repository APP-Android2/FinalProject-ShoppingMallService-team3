package kr.co.lion.farming_customer.fragment.myPageManagement

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.MyPageManagementName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.myPageManagement.MyPageManagementActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementProfileModifyBinding
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.myPageManagement.MyPageManagementProfileModifyViewModel

class MyPageManagementProfileModifyFragment : Fragment() {

    lateinit var fragmentMyPageManagementProfileModifyBinding: FragmentMyPageManagementProfileModifyBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity
    lateinit var albumLauncher: ActivityResultLauncher<Intent>
    lateinit var myPageManagementProfileModifyViewModel : MyPageManagementProfileModifyViewModel

    var isDuplicated = true
    var isImageChanged = false
    var userModel : UserModel? = null
    var imgUrl : Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentMyPageManagementProfileModifyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_profile_modify,container,false)
        myPageManagementProfileModifyViewModel = MyPageManagementProfileModifyViewModel()
        fragmentMyPageManagementProfileModifyBinding.myPageManagementProfileModifyViewModel = myPageManagementProfileModifyViewModel
        fragmentMyPageManagementProfileModifyBinding.lifecycleOwner = this
        myPageManagementActivity = activity as MyPageManagementActivity

        settingUserData()
        settingToolbar()
        modifyNickName()
        settingAlbumLauncher()
        settingImageButton()


        return fragmentMyPageManagementProfileModifyBinding.root
    }

    private fun settingUserData() {
        val sharedPreferences = myPageManagementActivity.getSharedPreferences("AutoLogin",
            Context.MODE_PRIVATE)
        val userIdx = sharedPreferences.getInt("loginUserIdx", -1)
        CoroutineScope(Dispatchers.Main).launch {
            userModel = UserDao.gettingUserInfoByUserIdx(userIdx)
            UserDao.gettingUserImage(requireContext(), userModel!!.user_profile_image, fragmentMyPageManagementProfileModifyBinding.buttonMyPageManagementProfileModifyModifyProfileImage)
            settingEvent()
            fragmentMyPageManagementProfileModifyBinding.myPageManagementProfileModifyViewModel!!.buttonMyPageManagementProfileModifyCheckUserNickName?.value = "중복확인"
            fragmentMyPageManagementProfileModifyBinding.myPageManagementProfileModifyViewModel!!.textFieldMyPageManagementProfileModify_ModifyUserNickName.value = userModel!!.user_nickname
        }

    }

    private fun settingEvent() {
        fragmentMyPageManagementProfileModifyBinding.apply {
            // 닉네임 중복확인
            buttonMyPageManagementProfileModifyCheckUserNickName.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val nickname = myPageManagementProfileModifyViewModel!!.textFieldMyPageManagementProfileModify_ModifyUserNickName.value
                    if(nickname != null){
                        if(UserDao.checkUserNickNameExist(nickname)){
                            // 사용 가능
                            isDuplicated = false
                            myPageManagementProfileModifyViewModel!!.buttonMyPageManagementProfileModifyCheckUserNickName.value = "사용가능"
                            it.isEnabled = false
                        }else{
                            // 사용 불가
                            textInputLayoutCheckNickName.error = "이미 존재하는 닉네임입니다."
                            isDuplicated = true
                        }
                    }
                }
            }
            // 프로필 수정
            buttonMyPageManagementProfileModifyDone.setOnClickListener {
                if(!isDuplicated || isImageChanged){
                    CoroutineScope(Dispatchers.Main).launch {
                        val nickname = myPageManagementProfileModifyViewModel!!.textFieldMyPageManagementProfileModify_ModifyUserNickName.value
                        if (nickname != null) {
                            userModel!!.user_nickname = nickname
                            if(imgUrl != null){
                                var imgString = UserDao.uploadImage(requireContext(),userModel!!.user_idx, imgUrl!!)
                                userModel!!.user_profile_image = "image/user/" + imgString
                                UserDao.updateUserData(userModel!!)
                            }else{
                                UserDao.updateUserData(userModel!!)
                            }
                            myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_PROFILE_MODIFY)
                        }
                    }
                }
            }
        }
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageManagementProfileModifyBinding.apply {
            toolbarMyPageManagementProfileModify.apply {
                // 네비게이션 Back
                setNavigationOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_PROFILE_MODIFY)
                }
            }
        }
    }

    // 닉네임을 수정했을 때 완료버튼 비활성화 메서드
    fun modifyNickName(){
        fragmentMyPageManagementProfileModifyBinding.apply {
            textFieldMyPageManagementProfileModifyModifyUserNickName.addTextChangedListener {
                isDuplicated = true
                myPageManagementProfileModifyViewModel!!.buttonMyPageManagementProfileModifyCheckUserNickName.value = "중복확인"
                buttonMyPageManagementProfileModifyCheckUserNickName.isEnabled = true
                if(it!!.isEmpty()){
                    textInputLayoutCheckNickName.error = "닉네임을 작성해주세요"
                    // 완료버튼 비활성화
                    buttonMyPageManagementProfileModifyDone.isEnabled = false
                }
                else{
                    textInputLayoutCheckNickName.error = null
                    textInputLayoutCheckNickName.isErrorEnabled = false

                    // 완료버튼 활성화
                    buttonMyPageManagementProfileModifyDone.isEnabled = true
                }
            }
        }
    }

    fun settingImageButton(){
        fragmentMyPageManagementProfileModifyBinding.apply {
            buttonMyPageManagementProfileModifyModifyProfileImage.setOnClickListener {
                startAlbumLauncher()
            }
            buttonMyPageManagementProfileModifyModifyProfileImage2.setOnClickListener {
                startAlbumLauncher()
            }
        }
    }

    fun settingAlbumLauncher() {
        // 앨범 실행을 위한 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract2) {
            // 사진 선택을 완료한 후 돌아왔다면
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체 리스트를 추출한다.
                val uriclip = it.data?.data
                imgUrl = it.data?.data
                isImageChanged = true
                if (uriclip != null) {
                    // 안드로이드 Q(10) 이상이라면
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // 이미지를 생성할 수 있는 객체를 생성한다.
                        val source = ImageDecoder.createSource(
                            myPageManagementActivity.contentResolver,
                            uriclip
                        )
                        // Bitmap을 생성한다.
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근한다.
                        val cursor = myPageManagementActivity.contentResolver.query(
                            uriclip,
                            null,
                            null,
                            null,
                            null
                        )
                        if (cursor != null) {
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성한다
                            BitmapFactory.decodeFile(source)
                        } else {
                            null
                        }
                    }

                    // 회전 각도값을 가져온다.
                    val degree = Tools.getDegree(myPageManagementActivity, uriclip)
                    // 회전 이미지를 가져온다
                    val bitmap2 = Tools.rotateBitmap(bitmap!!, degree.toFloat())
                    // 크기를 줄인 이미지를 가져온다.
                    val bitmap3 = Tools.resizeBitmap(bitmap2, 1024)

                    fragmentMyPageManagementProfileModifyBinding.buttonMyPageManagementProfileModifyModifyProfileImage.setImageBitmap(bitmap3)
                }
            }
        }
    }

    // 앨범 런처를 실행하는 메서드
    fun startAlbumLauncher(){
        // 앨범에서 사진을 선택할 수 있도록 셋팅된 인텐트를 생성한다.
        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것이 뜨게 한다)
        albumIntent.setType("image/*")

        // 이미지 여러개 선택 가능
        albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, true)
        // 액티비티를 실행한다.
        albumLauncher.launch(albumIntent)
    }
}