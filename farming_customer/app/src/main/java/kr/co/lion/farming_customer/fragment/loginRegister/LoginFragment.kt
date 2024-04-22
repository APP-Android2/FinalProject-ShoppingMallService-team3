package kr.co.lion.farming_customer.fragment.loginRegister

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.UserState
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentLoginBinding
import kr.co.lion.farming_customer.viewmodel.loginRegister.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var loginActivity: LoginActivity

    private lateinit var loginViewModel: LoginViewModel
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
    private fun settingButtonLogin(){
        fragmentLoginBinding.apply {
            // 버튼 클릭 시
            buttonLoginLogin.setOnClickListener {

                // 유효성 검사 (아이디 / 비밀번호 입력 했는지)
                val chk = checkInputForm()

                if (chk){
                    loginPro()
                }

                // 일단 홈화면으로 넘어간다.
//                val intent = Intent(loginActivity, MainActivity::class.java)
//                startActivity(intent)
//                loginActivity.finish()
                // TODO("나중엔 DB 연결")
            }
        }
    }

    // 회원가입 버튼 관련 함수
    private fun settingButtonRegister(){
        fragmentLoginBinding.apply {
            // 회원가입 버튼 클릭 시
            buttonLoginRegister.setOnClickListener {
                loginActivity.replaceFragment(LoginFragmentName.REGISTER_FRAGMENT,
                    addToBackStack = true, isAnimate = true, data = null)
            }
        }
    }

    // 비회원으로 로그인 버튼 관련 함수
    private fun settingButtonLoginWithoutReg(){
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
    private fun settingButtonFindIdPw(){
        fragmentLoginBinding.apply {

            // 버튼 클릭 시 findAccountFragment로 이동
            buttonLoginForgetIdPw.setOnClickListener {
                loginActivity.replaceFragment(LoginFragmentName.FIND_ACCOUNT_FRAGMENT,
                    addToBackStack = true, isAnimate = true, data = null)
            }
        }
    }

    private fun checkInputForm(): Boolean{
        // 입력한 값들을 가져온다.
        val userId = loginViewModel.textFieldLoginId.value!!
        val userPw = loginViewModel.textFieldLoginPw.value!!

        if(userId.isEmpty()){
            Tools.showErrorDialog(loginActivity, fragmentLoginBinding.textFieldLoginId, "아이디 입력 오류",
                "아이디를 입력해주세요")
            return false
        }

        if(userPw.isEmpty()){
            Tools.showErrorDialog(loginActivity, fragmentLoginBinding.textFieldLoginPw, "비밀번호 입력 오류",
                "비밀번호를 입력해주세요")
            return false
        }

        return true

    }

    private fun loginPro(){
        // 사용자가 입력한 정보를 가져온다.
        val userId = loginViewModel.textFieldLoginId.value!!
        val userPw = loginViewModel.textFieldLoginPw.value!!

        CoroutineScope(Dispatchers.Main).launch {
            val loginUserModel = UserDao.getUserDataById(userId)

            // 만약 null 이라면..
            if(loginUserModel == null){
                Tools.showErrorDialog(loginActivity, fragmentLoginBinding.textFieldLoginId, "로그인 오류",
                    "존재하지 않는 아이디 입니다")
            }
            // 만약 정보를 가져온 것이 있다면
            else {
                // 입력한 비밀번호와 서버에서 받아온 사용자의 비밀호가 다르다면..
                if(userPw != loginUserModel.user_pw){
                    Tools.showErrorDialog(loginActivity, fragmentLoginBinding.textFieldLoginPw, "로그인 오류",
                        "비밀번호가 잘못되었습니다")
                }
                // 비밀번호가 일치한다면
                else {
                    // 회원이 탈퇴 상태라면
                    if(loginUserModel.user_state == UserState.USER_STATE_SIGNOUT.num){
                        MaterialAlertDialogBuilder(loginActivity).apply {
                            setTitle("로그인 오류")
                            setMessage("탈퇴한 회원입니다")
                            setPositiveButton("확인"){ _: DialogInterface, _: Int ->
                                loginViewModel.textFieldLoginId.value = ""
                                loginViewModel.textFieldLoginPw.value = ""
                                Tools.showSoftInput(loginActivity, fragmentLoginBinding.textFieldLoginId)
                            }
                            show()
                        }
                    } else {
                        // 자동 로그인 부분 (연결 필요)
//                        val sharedPreferences = loginActivity.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE)
//                        val editor = sharedPreferences.edit()
//                        editor.putInt("loginUserIdx", loginUserModel.user_idx)
//                        editor.putString("loginUserNickName", loginUserModel.user_nickname)
//                        editor.apply()

                        // ContentActivity를 실행한다.
                        val intent = Intent(loginActivity, MainActivity::class.java)

                        // 로그인한 사용자의 정보를 전달해준다.
//                        intent.putExtra("loginUserIdx", loginUserModel.user_idx)
//                        intent.putExtra("loginUserNickName", loginUserModel.user_nickname)

                        startActivity(intent)
                        // LoginActivity를 종료한다.
                        loginActivity.finish()
                    }
                }
            }
        }
    }
}