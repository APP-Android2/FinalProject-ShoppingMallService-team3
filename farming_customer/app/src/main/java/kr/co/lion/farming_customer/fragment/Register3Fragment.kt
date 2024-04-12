package kr.co.lion.farming_customer.fragment

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
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.LoginActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentRegister3Binding
import kr.co.lion.farming_customer.viewmodel.Register3ViewModel

class Register3Fragment : Fragment() {

    lateinit var fragmentRegister3Binding: FragmentRegister3Binding
    lateinit var loginActivity: LoginActivity

    lateinit var register3ViewModel: Register3ViewModel

    lateinit var albumLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentRegister3Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register3, container, false)
        register3ViewModel = Register3ViewModel()
        fragmentRegister3Binding.register3ViewModel = register3ViewModel
        fragmentRegister3Binding.lifecycleOwner = this

        loginActivity = activity as LoginActivity
        settingButton()
        settingAlbumLauncher()
        // TODO("텍스트 뷰 관련 함수 필요")

        return fragmentRegister3Binding.root
    }

    fun settingButton(){
        fragmentRegister3Binding.apply {
            // 건너뛰기 버튼
            buttonReg3Pass.setOnClickListener {
                val intent = Intent(loginActivity, MainActivity::class.java)
                startActivity(intent)
                loginActivity.finish()
            }

            // 확인 버튼
            buttonReg3Confirm.setOnClickListener {
                //TODO("이 버튼 클릭 시 프로필 사진을 서버에 저장해야 함.")
                val intent = Intent(loginActivity, MainActivity::class.java)
                startActivity(intent)
                loginActivity.finish()
            }

            // 카메라 버튼
            imageButtonReg3Camera.setOnClickListener{
                startAlbumLauncher()
            }
        }
    }

    // 앨범 런처 설정
    fun settingAlbumLauncher() {
        // 앨범 실행을 위한 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract2){
            // 사진 선택을 완료한 후 돌아왔다면
            if(it.resultCode == AppCompatActivity.RESULT_OK){
                // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체 리스트를 추출한다.
                val uri = it.data?.data

                if(uri != null){
                    // 안드로이드 Q(10) 이상이라면
                    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지를 생성할 수 있는 객체를 생성한다.
                        val source = ImageDecoder.createSource(loginActivity.contentResolver, uri)
                        // Bitmap을 생성한다.
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근한다.
                        val cursor = loginActivity.contentResolver.query(uri, null, null, null, null)
                        if(cursor != null){
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성한다
                            BitmapFactory.decodeFile(source)
                        }  else {
                            null
                        }
                    }

                    // 회전 각도값을 가져온다.
                    val degree = Tools.getDegree(loginActivity, uri)
                    // 회전 이미지를 가져온다
                    val bitmap2 = Tools.rotateBitmap(bitmap!!, degree.toFloat())
                    // 크기를 줄인 이미지를 가져온다.
                    val bitmap3 = Tools.resizeBitmap(bitmap2, 1024)


                    fragmentRegister3Binding.imageViewReg3.setImageBitmap(bitmap3)
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