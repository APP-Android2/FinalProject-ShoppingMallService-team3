package kr.co.lion.farming_customer.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.LoginActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentRegister3Binding
import kr.co.lion.farming_customer.viewmodel.Register3ViewModel

class Register3Fragment : Fragment() {

    lateinit var fragmentRegister3Binding: FragmentRegister3Binding
    lateinit var loginActivity: LoginActivity

    lateinit var register3ViewModel: Register3ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentRegister3Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register3, container, false)
        register3ViewModel = Register3ViewModel()
        fragmentRegister3Binding.register3ViewModel = register3ViewModel
        fragmentRegister3Binding.lifecycleOwner = this

        loginActivity = activity as LoginActivity
        settingButton()

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
            imageButton.setOnClickListener{
                // TODO("앨범 연결 필요")
            }
        }
    }
}