package kr.co.lion.farming_customer.fragment

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
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentFindIdDoneBinding
import kr.co.lion.farming_customer.databinding.FragmentFindPwDoneBinding
import kr.co.lion.farming_customer.viewmodel.FindIdDoneViewModel
import kr.co.lion.farming_customer.viewmodel.FindPwDoneViewModel

class FindPwDoneFragment : Fragment() {

    lateinit var fragmentFindPwDoneBinding: FragmentFindPwDoneBinding
    lateinit var loginActivity: LoginActivity

    lateinit var findPwDoneViewModel: FindPwDoneViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindPwDoneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_pw_done, container, false)
        findPwDoneViewModel = FindPwDoneViewModel()
        fragmentFindPwDoneBinding.findPwDoneViewModel = findPwDoneViewModel
        fragmentFindPwDoneBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()
        settingTextView()
        settingPassword()

        return fragmentFindPwDoneBinding.root
    }

    // 버튼 설정
    fun settingButton(){
        fragmentFindPwDoneBinding.apply {
            buttonFindPwDone.setOnClickListener {
                loginActivity.replaceFragment(
                    LoginFragmentName.FIND_PW_DONE2_FRAGMENT, addToBackStack = true, isAnimate = true, data = null)
            }
        }
    }

    // 텍스트 설정
    fun settingTextView(){
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

    // 텍스트 관련 설정
    fun settingPassword(){
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
                    } else {
                        // 조건 충족
//                        textInputLayout5.isHelperTextEnabled = false
                        textInputLayout5.error = null
                        textInputLayout5.isErrorEnabled = false
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

                    } else {
                        // 오류 제거
//                        fragmentRegister2Binding.textInputLayout6.isHelperTextEnabled = false
//                        fragmentRegister2Binding.textInputLayout6.helperText = null
                        fragmentFindPwDoneBinding.textInputLayout6.error = null
                        fragmentFindPwDoneBinding.textInputLayout6.isErrorEnabled = false
                    }
                }

            })
        }
    }
}