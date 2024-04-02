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

        return fragmentFindPwDoneBinding.root
    }

    // 버튼 설정
    fun settingButton(){
        fragmentFindPwDoneBinding.apply {
            buttonFindPwDone.setOnClickListener {
                loginActivity.replaceFragment(
                    LoginFragmentName.LOGIN_FRAGMENT, addToBackStack = false, isAnimate = true, data = null)
            }
        }
    }
}