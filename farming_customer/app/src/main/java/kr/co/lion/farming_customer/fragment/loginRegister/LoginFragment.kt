package kr.co.lion.farming_customer.fragment.loginRegister

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentLoginBinding
import kr.co.lion.farming_customer.viewmodel.loginRegister.LoginViewModel

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var loginActivity: LoginActivity

    lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        loginViewModel = LoginViewModel()
        fragmentLoginBinding.loginViewModel = loginViewModel
        fragmentLoginBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButtonLogin()
        settingButtonRegister()
        settingButtonLoginWithoutReg()
        settingButtonFindIdPw()

        return fragmentLoginBinding.root
    }

    // 로그인 버튼 관련 함수
    fun settingButtonLogin(){
        fragmentLoginBinding.apply {
            // 버튼 클릭 시
            buttonLoginLogin.setOnClickListener {
                // 일단 홈화면으로 넘어간다.
                val intent = Intent(loginActivity, MainActivity::class.java)
                startActivity(intent)
                loginActivity.finish()
                // TODO("나중엔 DB 연결")
            }
        }
    }

    // 회원가입 버튼 관련 함수
    fun settingButtonRegister(){
        fragmentLoginBinding.apply {
            // 회원가입 버튼 클릭 시
            buttonLoginRegister.setOnClickListener {
                loginActivity.replaceFragment(LoginFragmentName.REGISTER_FRAGMENT,
                    addToBackStack = true, isAnimate = true, data = null)
            }
        }
    }

    // 비회원으로 로그인 버튼 관련 함수
    fun settingButtonLoginWithoutReg(){
        fragmentLoginBinding.apply {

            // 버튼 클릭 시 MainActivity를 보여준다.
            buttonLoginWithoutReg.setOnClickListener {
                val intent = Intent(loginActivity, MainActivity::class.java)
                startActivity(intent)
                loginActivity.finish()
            }
        }
    }

    // 아이디/비번 찾기
    fun settingButtonFindIdPw(){
        fragmentLoginBinding.apply {

            // 버튼 클릭 시 findAccountFragment로 이동
            buttonLoginForgetIdPw.setOnClickListener {
                loginActivity.replaceFragment(LoginFragmentName.FIND_ACCOUNT_FRAGMENT,
                    addToBackStack = true, isAnimate = true, data = null)
            }
        }
    }
}