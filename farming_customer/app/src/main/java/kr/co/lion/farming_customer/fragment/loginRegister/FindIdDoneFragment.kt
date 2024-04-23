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
import kr.co.lion.farming_customer.databinding.FragmentFindIdDoneBinding
import kr.co.lion.farming_customer.viewmodel.loginRegister.FindIdDoneViewModel

class FindIdDoneFragment : Fragment() {

    private lateinit var fragmentFindIdDoneBinding: FragmentFindIdDoneBinding
    lateinit var loginActivity: LoginActivity

    private lateinit var findIdDoneViewModel: FindIdDoneViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindIdDoneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_id_done, container, false)
        findIdDoneViewModel = FindIdDoneViewModel()
        fragmentFindIdDoneBinding.findIdDoneViewModel = findIdDoneViewModel
        fragmentFindIdDoneBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()
        settingText()

        return fragmentFindIdDoneBinding.root
    }

    // 버튼 설정
    fun settingButton(){
        fragmentFindIdDoneBinding.apply {
            buttonFindIdDone.setOnClickListener {
                loginActivity.replaceFragment(LoginFragmentName.LOGIN_FRAGMENT,
                    addToBackStack = false, isAnimate = false, data = null)
            }
        }
    }

    // 텍스트 설정
    private fun settingText(){
        val userId = arguments?.getString("foundUserId")
        findIdDoneViewModel.userId.value = userId
    }
}