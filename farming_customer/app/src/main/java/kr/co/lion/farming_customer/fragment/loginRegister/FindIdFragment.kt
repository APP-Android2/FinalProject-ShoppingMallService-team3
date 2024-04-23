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
import kr.co.lion.farming_customer.databinding.FragmentFindIdBinding
import kr.co.lion.farming_customer.viewmodel.loginRegister.FindIdViewModel

class FindIdFragment : Fragment() {
    private lateinit var fragmentFindIdBinding: FragmentFindIdBinding
    lateinit var loginActivity: LoginActivity

    private lateinit var findIdViewModel: FindIdViewModel

    private var userId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindIdBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_id, container, false)
        findIdViewModel = FindIdViewModel()
        fragmentFindIdBinding.findIdViewModel = findIdViewModel
        fragmentFindIdBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()
        //TODO("휴대폰 인증 구현 필요")

        return fragmentFindIdBinding.root
    }

    private fun settingButton(){
        fragmentFindIdBinding.apply {
            // 아이디 찾기 버튼을 누르면
            buttonFindId.setOnClickListener {

                val chk = checkInputForm()
                // 아이디, 휴대폰이 다 입력 돼 있다면
                if (chk){
                    val userName = findIdViewModel?.userName?.value!!
                    val userPhone = findIdViewModel?.userPhoneNumber?.value!!

                    CoroutineScope(Dispatchers.Main).launch{
                        userId = UserDao.findUserID(userName, userPhone)
                        if (userId != null) {
                            // 결과가 있을 경우, 결과를 Bundle에 담아 Fragment 전환
                            val bundle = Bundle().apply {
                                putString("foundUserId", userId)
                            }
                            loginActivity.replaceFragment(LoginFragmentName.FIND_ID_DONE_FRAGMENT,
                                addToBackStack = true, isAnimate = true, data = bundle)
                        } else {
                            // 결과가 없을 경우, 다른 Fragment로 전환
                            loginActivity.replaceFragment(LoginFragmentName.CANT_FIND_ID_FRAGMENT,
                                addToBackStack = true, isAnimate = true, data = null)
                        }
                    }
                }

            }
        }
    }

    // 이름, 휴대폰 입력이 되어있는지 확인하는 메서드
    private fun checkInputForm(): Boolean{
        // 입력한 값들을 가져온다.
        val userName = findIdViewModel.userName.value
        val userPhone = findIdViewModel.userPhoneNumber.value

        if(userName.isNullOrEmpty()){
            Tools.showErrorDialog(loginActivity, fragmentFindIdBinding.textFieldFindIdName, "이름 입력 오류",
                "이름을 입력해주세요")
            return false
        }

        if(userPhone.isNullOrEmpty()){
            Tools.showErrorDialog(loginActivity, fragmentFindIdBinding.textFieldFindIdPhone, "휴대폰 입력 오류",
                "휴대폰 번호를 입력해주세요")
            return false
        }

        return true

    }
}