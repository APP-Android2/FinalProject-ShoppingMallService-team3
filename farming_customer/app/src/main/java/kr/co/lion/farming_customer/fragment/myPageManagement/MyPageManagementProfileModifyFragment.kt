package kr.co.lion.farming_customer.fragment.myPageManagement

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.MyPageManagementName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.myPageManagement.MyPageManagementActivity
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementProfileModifyBinding

class MyPageManagementProfileModifyFragment : Fragment() {

    lateinit var fragmentMyPageManagementProfileModifyBinding: FragmentMyPageManagementProfileModifyBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentMyPageManagementProfileModifyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_profile_modify,container,false)
        myPageManagementActivity = activity as MyPageManagementActivity

        settingToolbar()
        modifyNickName()
        settingAlbumLauncher()
        settingImageButton()


        return fragmentMyPageManagementProfileModifyBinding.root
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
                    buttonMyPageManagementProfileModifyDone.setOnClickListener {
                        myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_PROFILE_MODIFY)
                    }
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