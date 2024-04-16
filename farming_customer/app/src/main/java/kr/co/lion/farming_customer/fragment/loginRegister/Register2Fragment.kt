package kr.co.lion.farming_customer.fragment.loginRegister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentRegister2Binding
import kr.co.lion.farming_customer.viewmodel.loginRegister.Register2ViewModel

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
        settingText()

        return fragmentRegister2Binding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentRegister2Binding.apply {
            toolbarRegister2.apply {
                // 제목
                title = "회원가입"

                // back 버튼
                setNavigationIcon(R.drawable.back)
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

    // 텍스트 관련 설정
    fun settingText(){
        fragmentRegister2Binding.apply {


            // 비밀번호 검사
            textFieldReg2Pw.addTextChangedListener(object : TextWatcher {
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
            textFieldReg2Pw2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    // TODO("Not yet implemented")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // TODO("Not yet implemented")
                }

                override fun afterTextChanged(s: Editable?) {
                    val password = textFieldReg2Pw.text.toString()
                    val confirmPassword = textFieldReg2Pw2.text.toString()

                    if (password != confirmPassword) {
//                        fragmentRegister2Binding.textInputLayout6.isHelperTextEnabled = true
//                        fragmentRegister2Binding.textInputLayout6.helperText = "비밀번호가 일치하지 않습니다."
                        fragmentRegister2Binding.textInputLayout6.error = "비밀번호가 일치하지 않습니다."

                    } else {
                        // 오류 제거
//                        fragmentRegister2Binding.textInputLayout6.isHelperTextEnabled = false
//                        fragmentRegister2Binding.textInputLayout6.helperText = null
                        fragmentRegister2Binding.textInputLayout6.error = null
                        fragmentRegister2Binding.textInputLayout6.isErrorEnabled = false
                    }
                }

            })
        }
    }
}