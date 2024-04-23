package kr.co.lion.farming_customer.fragment.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentCantFindIdBinding

class CantFindIdFragment : Fragment() {

    private lateinit var binding: FragmentCantFindIdBinding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cant_find_id, container, false)
        binding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()

        return binding.root
    }

    private fun settingButton(){
        // 버튼 클릭 시 회원가입 화면으로 이동
        binding.buttonToRegisterFragment.setOnClickListener {
            loginActivity.replaceFragment(LoginFragmentName.LOGIN_FRAGMENT,
                addToBackStack = false, isAnimate = true, null)
        }
    }
}