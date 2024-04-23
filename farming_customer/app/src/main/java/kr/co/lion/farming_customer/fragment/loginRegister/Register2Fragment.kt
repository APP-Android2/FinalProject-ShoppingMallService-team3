package kr.co.lion.farming_customer.fragment.loginRegister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.Gender
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.UserState
import kr.co.lion.farming_customer.UserType
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentRegister2Binding
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.loginRegister.Register2ViewModel

class Register2Fragment : Fragment() {

    lateinit var fragmentRegister2Binding: FragmentRegister2Binding
    lateinit var loginActivity: LoginActivity

    private lateinit var register2ViewModel: Register2ViewModel

    // 아이디, 닉네임 중복 확인 검사를 했는지
    // true면 아이디 중복 확인 겁사를 완료한 것으로 간주한다.
    private var checkUserIdExist = false
    private var checkUserNickNameExist = false
    private var checkPwError = false
    private var checkPw2Error = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        fragmentRegister2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register2, container, false)
        register2ViewModel = Register2ViewModel()
        fragmentRegister2Binding.register2ViewModel = register2ViewModel
        fragmentRegister2Binding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        // 회원가입 버튼을 비활성화
        fragmentRegister2Binding.buttonReg2Reg.isEnabled = false
        // 성별 기본값 남자로 설정
        register2ViewModel.userGender.value = Gender.MALE.num

        settingToolbar()
        settingButton()
        settingText()
        isRegisterButtonEnabled()

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
    private fun settingButton(){
        fragmentRegister2Binding.apply {

            // 닉네임 중복 확인 버튼
            buttonReg2NickName.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    checkUserNickNameExist = UserDao.checkUserNickNameExist(register2ViewModel?.userNickname?.value!!)

                    // 닉네임이 존재한다면
                    if(!checkUserNickNameExist) {
                        // 입력 값을 초기화 후 다이얼로그 출력
                        register2ViewModel?.userNickname?.value = ""
                        Tools.showErrorDialog(loginActivity, fragmentRegister2Binding.textFieldReg2Id,
                            "닉네임 입력 오류", "존재하는 닉네임입니다\n다른 닉네임을 입력해주세요")
                    } else {
                        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(loginActivity)
                        materialAlertDialogBuilder.setTitle("닉네임 사용 가능")
                        materialAlertDialogBuilder.setMessage("사용 가능한 닉네임 입니다")
                        materialAlertDialogBuilder.setPositiveButton("확인", null)
                        materialAlertDialogBuilder.show()
                    }

                }
            }

            // 아이디 중복 확인 버튼
            buttonReg2Id.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    checkUserIdExist = UserDao.checkUserIdExist(register2ViewModel?.userId?.value!!)

                    // 아이디가 존재한다면
                    if(!checkUserIdExist) {
                        // 입력 값을 초기화 후 다이얼로그 출력
                        register2ViewModel?.userId?.value = ""
                        Tools.showErrorDialog(loginActivity, fragmentRegister2Binding.textFieldReg2Id,
                            "아이디 입력 오류", "존재하는 아이디입니다\n다른 아이디를 입력해주세요")
                    } else {
                        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(loginActivity)
                        materialAlertDialogBuilder.setTitle("아이디 사용 가능")
                        materialAlertDialogBuilder.setMessage("사용 가능한 아이디 입니다")
                        materialAlertDialogBuilder.setPositiveButton("확인", null)
                        materialAlertDialogBuilder.show()
                    }

                }
            }

            // 성별 버튼
            toggleButtonReg2Gender.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.buttonReg2GenderMale -> register2ViewModel?.settingGender(R.id.buttonReg2GenderMale)
                        R.id.buttonReg2GenderFemale -> register2ViewModel?.settingGender(R.id.buttonReg2GenderFemale)
                    }
                }
            }

            // 휴대폰 본인 인증 버튼
            buttonReg2Authorize.setOnClickListener {
                // TODO("본인 인증 버튼 클릭 시 본인 인증 진행")
            }



            // 회원가입 버튼
            buttonReg2Reg.setOnClickListener {
                val chk = checkButton()
                if (chk){
                    // 데이터 처리
                    registerComplete()
                }
            }
        }
    }

    // 텍스트 관련 설정
    private fun settingText(){
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
                        textInputLayout5.error = "6~20자, 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합"
                        checkPwError = false
                    } else {
                        // 조건 충족
//                        textInputLayout5.isHelperTextEnabled = false
                        textInputLayout5.error = null
                        textInputLayout5.isErrorEnabled = false
                        checkPwError = true
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
                        fragmentRegister2Binding.textInputLayout6.error = "비밀번호가 일치하지 않습니다."
                        checkPw2Error = false

                    } else {
                        // 오류 제거
                        fragmentRegister2Binding.textInputLayout6.error = null
                        fragmentRegister2Binding.textInputLayout6.isErrorEnabled = false
                        checkPw2Error = true
                    }
                }

            })
        }
    }

    private val textWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // TODO("Not yet implemented")
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            setRegisterButtonEnabled()
        }
    }

    private fun isRegisterButtonEnabled(){
        fragmentRegister2Binding.apply {
            textFieldReg2NickName.addTextChangedListener(textWatcher)
            textFieldReg2Id.addTextChangedListener(textWatcher)
            textFieldReg2Pw.addTextChangedListener(textWatcher)
            textFieldReg2Pw2.addTextChangedListener(textWatcher)
            textFieldReg2Name.addTextChangedListener(textWatcher)
            textFieldReg2Birth.addTextChangedListener(textWatcher)
            textFieldReg2Phone.addTextChangedListener(textWatcher)
            //TODO("인증코드 확인 필요")
        }
    }


    private fun setRegisterButtonEnabled(){
        val allFieldsFilled = register2ViewModel.userNickname.value?.isNotEmpty() == true &&
                register2ViewModel.userId.value?.isNotEmpty() == true &&
                register2ViewModel.userName.value?.isNotEmpty() == true &&
                register2ViewModel.userBirthDate.value?.isNotEmpty() == true &&
                register2ViewModel.userPhoneNumber.value?.isNotEmpty() == true &&
                checkPwError && checkPw2Error

        // 모든 필드가 채워져 있고, 비밀번호 확인 로직도 통과했을 때만 버튼 활성화
        fragmentRegister2Binding.buttonReg2Reg.isEnabled = allFieldsFilled
    }

    // 활성화된 회원가입 버튼을 눌렀을 때
    private fun checkButton(): Boolean{

        // 닉네임 중복 확인을 하지 않았다면
        if (!checkUserNickNameExist) {
            Tools.showErrorDialog(loginActivity, fragmentRegister2Binding.textFieldReg2NickName, "닉네임 중복 확인 오류",
                "닉네임 중복 확인을 해주세요")
            return false
        }

        // 아이디 중복 확인을 하지 않았다면
        if (!checkUserIdExist) {
            Tools.showErrorDialog(loginActivity, fragmentRegister2Binding.textFieldReg2Id, "아이디 중복 확인 오류",
                "아이디 중복 확인을 해주세요")
            return false
        }

        return true
    }

    // 모든 조건 검사 완료 후
    private fun registerComplete(){
        CoroutineScope(Dispatchers.Main).launch {
            // 사용자 번호 시퀀스 값을 가져온다.
            val userSequence = UserDao.getUserSequence()
            // 시퀀스 값을 1 증가시켜 저장
            UserDao.updateUserSequence(userSequence + 1)

            // 저장할 데이터를 가져온다.
            val userIdx = userSequence + 1
            val userId = register2ViewModel.userId.value!!
            val userPw = register2ViewModel.userPw.value!!
            val userBirth = register2ViewModel.userBirthDate.value!!
            val userNickName = register2ViewModel.userNickname.value!!
            val userName = register2ViewModel.userName.value!!
            val userPhone = register2ViewModel.userPhoneNumber.value!!
            val userGender = register2ViewModel.userGender.value!!
            val userServiceAgree = arguments?.getBoolean("registerUserService")!!
            val userPersonalAgree = arguments?.getBoolean("registerUserPersonal")!!
            val userAlertAgree = arguments?.getBoolean("registerUserAlarm")!!
            val userType = UserType.USER_BUYER.num
            val userState = UserState.USER_STATE_NORMAL.num
            val imageFileName = "image/user/profile.jpg"  // 이미지 파일 경로를 설정

            val userModel = UserModel(userIdx, userName, userNickName, userId, userPw, userBirth, userGender, userPhone,
                "", userServiceAgree, userPersonalAgree, userAlertAgree, userType, 0, userState, imageFileName)


            // 사용자 정보를 저장한다.
            UserDao.insertUserData(userModel)

            val registerBundle = Bundle()
            registerBundle.putInt("registerUserIdx", userIdx)
            registerBundle.putString("registerUserNickName",register2ViewModel.userNickname.value!!)

            loginActivity.replaceFragment(LoginFragmentName.REGISTER3_FRAGMENT, addToBackStack = true,
                isAnimate = true, data = registerBundle)
        }
    }
}