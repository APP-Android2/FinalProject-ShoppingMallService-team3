package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentRegisterBinding
import kr.co.lion.farming_customer.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    lateinit var fragmentRegisterBinding: FragmentRegisterBinding
    lateinit var loginActivity: LoginActivity

    lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        fragmentRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        registerViewModel = RegisterViewModel()
        fragmentRegisterBinding.registerViewModel = registerViewModel
        fragmentRegisterBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingToolbar()
        settingCheckBox()

        return fragmentRegisterBinding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentRegisterBinding.apply {
            toolbarRegister.apply {
                //제목
                title = "약관동의"
                setNavigationIcon(R.drawable.arrow_back_24px)

                setNavigationOnClickListener {
                    // 클릭 시 현재 프래그먼트 제거
                    loginActivity.removeFragment(LoginFragmentName.REGISTER_FRAGMENT)
                }
            }
        }
    }

    // 체크박스 설정
    fun settingCheckBox(){
        fragmentRegisterBinding.apply {
            // 먼저 버튼을 비활성화 상태로 만듦
            buttonRegisterNext.isEnabled = false

            // 모두 동의 체크박스 선택 시 다른 체크 박스의 상태를 isChecked로 바꾼다.
            checkBoxTermsAll.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBoxRegPersonalInfo.isChecked = isChecked
                checkBoxRegServiceTerm.isChecked = isChecked
                checkBoxRegAlertServ.isChecked = isChecked

                buttonRegisterNext.isEnabled = checkBoxRegPersonalInfo.isChecked && checkBoxRegServiceTerm.isChecked
            }

            // 개별 필수 체크박스 상태 변경 시 다음 버튼 활성화 상태 업데이트
            val mandatoryCheckboxListener = CompoundButton.OnCheckedChangeListener { _, _ ->
                buttonRegisterNext.isEnabled = checkBoxRegPersonalInfo.isChecked && checkBoxRegServiceTerm.isChecked
            }

            checkBoxRegPersonalInfo.setOnCheckedChangeListener(mandatoryCheckboxListener)
            checkBoxRegServiceTerm.setOnCheckedChangeListener(mandatoryCheckboxListener)

            // 선택 체크박스는 다음 버튼 활성화 상태에 영향을 주지 않으므로 리스너 설정 생략
        }
    }
}