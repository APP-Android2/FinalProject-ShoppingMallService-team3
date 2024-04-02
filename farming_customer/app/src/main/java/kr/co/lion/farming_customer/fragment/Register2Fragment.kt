package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentRegister2Binding
import kr.co.lion.farming_customer.viewmodel.Register2ViewModel

class Register2Fragment : Fragment() {

    lateinit var fragmentRegister2Binding: FragmentRegister2Binding
    lateinit var loginActivity: LoginActivity

    lateinit var register2ViewModel: Register2ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        fragmentRegister2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register2, container, false)
        register2ViewModel = Register2ViewModel()
        fragmentRegister2Binding.register2ViewModel = register2ViewModel
        fragmentRegister2Binding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingToolbar()
        settingButton()

        return fragmentRegister2Binding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentRegister2Binding.apply {
            toolbarRegister2.apply {
                // 제목
                title = "회원가입"

                // back 버튼
                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener {
                    loginActivity.removeFragment(LoginFragmentName.REGISTER2_FRAGMENT)
                }
            }
        }
    }

    // 버튼 관련 설정
    fun settingButton(){
        fragmentRegister2Binding.apply {

            // 닉네임 중복 확인 버튼
            buttonReg2NickName.setOnClickListener {
                // TODO("중복 확인 버튼 클릭 시 DB내 동일 닉네임 있는지 확인")
            }

            // 아이디 중복 확인 버튼
            buttonReg2Id.setOnClickListener {
                // TODO("중복 확인 버튼 클릭 시 DB내 동일 아이디 있는지 확인")
            }

            // 성별 버튼
            // TODO("토글 버튼을 통해 성별 구분 필요")


            // 휴대폰 본인 인증 버튼
            buttonReg2Authorize.setOnClickListener {
                // TODO("본인 인증 버튼 클릭 시 본인 인증 진행")
            }

            // 회원가입 버튼
            buttonReg2Reg.setOnClickListener {
                // TODO("회원가입 버튼 클릭 시 안쓴 필드가 있는지, 중복확인 했는지 등 확인 필요")
                loginActivity.replaceFragment(LoginFragmentName.REGISTER3_FRAGMENT,
                    addToBackStack = true, isAnimate = true, data = null)
            }
        }
    }
}