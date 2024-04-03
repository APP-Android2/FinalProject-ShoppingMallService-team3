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
import kr.co.lion.farming_customer.databinding.FragmentFindIdBinding
import kr.co.lion.farming_customer.databinding.FragmentFindIdDoneBinding
import kr.co.lion.farming_customer.viewmodel.FindIdDoneViewModel
import kr.co.lion.farming_customer.viewmodel.FindIdViewModel

class FindIdDoneFragment : Fragment() {

    lateinit var fragmentFindIdDoneBinding: FragmentFindIdDoneBinding
    lateinit var loginActivity: LoginActivity

    lateinit var findIdDoneViewModel: FindIdDoneViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindIdDoneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_id_done, container, false)
        findIdDoneViewModel = FindIdDoneViewModel()
        fragmentFindIdDoneBinding.findIdDoneViewModel = findIdDoneViewModel
        fragmentFindIdDoneBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()

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
}