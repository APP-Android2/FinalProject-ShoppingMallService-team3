package kr.co.lion.farming_customer.fragment.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentFindPasswordBinding
import kr.co.lion.farming_customer.viewmodel.loginRegister.FindPwViewModel

class FindPasswordFragment : Fragment() {

    private lateinit var fragmentFindPwBinding: FragmentFindPasswordBinding
    lateinit var loginActivity: LoginActivity

    private lateinit var findPwViewModel: FindPwViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        fragmentFindPwBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password, container, false)
        findPwViewModel = FindPwViewModel()
        fragmentFindPwBinding.findPwViewModel = findPwViewModel
        fragmentFindPwBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()

        return fragmentFindPwBinding.root
    }

    fun settingButton(){
        fragmentFindPwBinding.apply {
            // 비밀번호 찾기 버튼을 눌렀을 때
            buttonFindPw.setOnClickListener {
                val chk = checkInputForm()
                // 아이디, 이름, 휴대폰 번호가 정상적으로 입력 돼었다면
                if (chk) {
                    val userName = findPwViewModel?.userName?.value!!
                    val userId = findPwViewModel?.userId?.value!!
                    val userPhone = findPwViewModel?.userPhoneNumber?.value!!

                    CoroutineScope(Dispatchers.Main).launch {
                        val check = UserDao.verifyUserPasswordReset(userName, userId, userPhone)
                        // 일치하는 정보가 있으면 비밀번호 변경 창으로 이동, 이 때, ID를 Bundle에 넣어서
                        // ID를 통해 userModel에 접근 -> 비밀번호를 변경 후 update
                        if (check){
                            val bundle = Bundle().apply {
                                putString("UserId", userId)
                            }
                            loginActivity.replaceFragment(LoginFragmentName.FIND_PW_DONE_FRAGMENT,
                                addToBackStack = true, isAnimate = true, data = bundle)
                        } else {
                            // 일치하는 정보가 없으면
                            loginActivity.replaceFragment(LoginFragmentName.CANT_FIND_ID_FRAGMENT,
                                addToBackStack = true, isAnimate = true, data = null)
                        }
                    }
                }
            }
        }
    }

    // 이름, 아이디, 휴대폰이 되어있는지 확인하는 메서드
    private fun checkInputForm(): Boolean{
        // 입력한 값들을 가져온다.
        val userName = findPwViewModel.userName.value
        val userId = findPwViewModel.userId.value
        val userPhone = findPwViewModel.userPhoneNumber.value

        if(userName.isNullOrEmpty()){
            Tools.showErrorDialog(loginActivity, fragmentFindPwBinding.textFieldFindPwName, "이름 입력 오류",
                "이름을 입력해주세요")
            return false
        }

        if(userId.isNullOrEmpty()) {
            Tools.showErrorDialog(loginActivity, fragmentFindPwBinding.textFieldFindPwId, "아이디 입력 오류",
                "아이디를 입력해주세요")
            return false
        }

        if(userPhone.isNullOrEmpty()){
            Tools.showErrorDialog(loginActivity, fragmentFindPwBinding.textFieldFindPwPhone, "휴대폰 번호 입력 오류",
                "휴대폰 번호를 입력해주세요")
            return false
        }

        return true

    }

}