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
}