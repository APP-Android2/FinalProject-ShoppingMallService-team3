package kr.co.lion.farming_customer.fragment.loginRegister

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentFindPwDoneBinding
import kr.co.lion.farming_customer.viewmodel.loginRegister.FindPwDoneViewModel

class FindPwDoneFragment : Fragment() {

    lateinit var fragmentFindPwDoneBinding: FragmentFindPwDoneBinding
    lateinit var loginActivity: LoginActivity

    private lateinit var findPwDoneViewModel: FindPwDoneViewModel

    private var checkPw = false
    private var checkPw2 = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindPwDoneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_pw_done, container, false)
        findPwDoneViewModel = FindPwDoneViewModel()
        fragmentFindPwDoneBinding.findPwDoneViewModel = findPwDoneViewModel
        fragmentFindPwDoneBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        // 버튼을 비활성화 시킴
        fragmentFindPwDoneBinding.buttonFindPwDone.isEnabled = false

        settingButton()
        settingTextView()
        settingPassword()
        isRegisterButtonEnabled()

        return fragmentFindPwDoneBinding.root
    }

    // 버튼 설정
    fun settingButton(){
        fragmentFindPwDoneBinding.apply {

            // 비밀번호 변경 버튼을 누르면
            buttonFindPwDone.setOnClickListener {
                // Bundle로부터 받은 userId를 가져온다.
                val userId = arguments?.getString("UserId")!!
                // 버튼은 유효성 검사가 되어야 활성화 되므로 null이나 다른 값 고려 안함
                val userPw = findPwDoneViewModel?.userPw?.value!!

                // ID를 통해 userModel을 가져오고 비밀번호를 수정 후 update
                CoroutineScope(Dispatchers.Main).launch {
                    val userModel = UserDao.getUserDataById(userId)
                    userModel!!.user_pw = userPw
                    UserDao.updateUserData(userModel)
                }
                loginActivity.replaceFragment(
                    LoginFragmentName.FIND_PW_DONE2_FRAGMENT, addToBackStack = true, isAnimate = true, data = null)
            }

        }
    }

    // 텍스트 설정
    private fun settingTextView(){
        fragmentFindPwDoneBinding.apply {
            val fullText = textViewFindPwDone.text.toString()
            val spannableString = SpannableString(fullText)

            // 색상 리소스 값 가져오기
            val greenColor = ContextCompat.getColor(loginActivity, R.color.green_main)

            // "비밀번호" 부분의 시작과 끝 인덱스 값을 찾음
            val start = fullText.indexOf("비밀번호")
            val end = start + "비밀번호".length

            // SpannableString에 색상을 적용
            spannableString.setSpan(ForegroundColorSpan(greenColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            textViewFindPwDone.text = spannableString
        }
    }

    // Text를 감시하는 textWatcher 객체 생성
    private val textWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            // 둘다 true일 경우에만 버튼 활성화
            fragmentFindPwDoneBinding.buttonFindPwDone.isEnabled = checkPw && checkPw2
        }
    }

    // 비밀번호, 비밀번호 확인에 감시자를 붙임
    private fun isRegisterButtonEnabled(){
        fragmentFindPwDoneBinding.apply {
            textFieldFindPw.addTextChangedListener(textWatcher)
            textFieldFindPw2.addTextChangedListener(textWatcher)
            //TODO("인증코드 확인 필요")
        }
    }

    // 텍스트 관련 설정
    private fun settingPassword(){
        fragmentFindPwDoneBinding.apply {


            // 비밀번호 검사
            textFieldFindPw.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    // 변경 전 호출 내용
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 변경할 때 호출
                }

                override fun afterTextChanged(s: Editable?) {
                    // 변경된 후에 호출
                    val input = s.toString()

                    // 길이 검사 6~20자리인지
                    val isValidLength = input.length in 6..20

                    // 조합 검사
                    val hasUpperCase = input.any {it.isUpperCase() }
                    val hasLowerCase = input.any { it.isLowerCase() }
                    val hasDigit = input.any { it.isDigit() }
                    val hasSpecial = input.any {!it.isLetterOrDigit() }

                    // 조합 조건 충족 여부 계산
                    val validConditions = listOf(hasUpperCase, hasLowerCase, hasDigit, hasSpecial).count { it }

                    // 글씨가 6~20자리인지, 조합 조건 2개 이상 만족하는지 확인
                    val isValidPassword = isValidLength && validConditions >= 2

                    // 조건에 따라 UI 업데이트나 에러 메시지 표시
                    if (!isValidPassword){
                        // 비밀번호 조건을 충족하지 않을 시
//                        textInputLayout5.helperText = "6~20자, 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합"
                        textInputLayout5.error = "6~20자, 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합"
                        checkPw = false
                    } else {
                        // 조건 충족
//                        textInputLayout5.isHelperTextEnabled = false
                        textInputLayout5.error = null
                        textInputLayout5.isErrorEnabled = false
                        checkPw = true
                    }
                }
            })

            // 비밀번호 확인 검사
            textFieldFindPw2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    // TODO("Not yet implemented")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // TODO("Not yet implemented")
                }

                override fun afterTextChanged(s: Editable?) {
                    val password = textFieldFindPw.text.toString()
                    val confirmPassword = textFieldFindPw2.text.toString()

                    if (password != confirmPassword) {
//                        fragmentRegister2Binding.textInputLayout6.isHelperTextEnabled = true
//                        fragmentRegister2Binding.textInputLayout6.helperText = "비밀번호가 일치하지 않습니다."
                        fragmentFindPwDoneBinding.textInputLayout6.error = "비밀번호가 일치하지 않습니다."
                        checkPw2 = false

                    } else {
                        // 오류 제거
//                        fragmentRegister2Binding.textInputLayout6.isHelperTextEnabled = false
//                        fragmentRegister2Binding.textInputLayout6.helperText = null
                        fragmentFindPwDoneBinding.textInputLayout6.error = null
                        fragmentFindPwDoneBinding.textInputLayout6.isErrorEnabled = false
                        checkPw2 = true
                    }
                }

            })

        }
    }
}