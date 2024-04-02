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
import kr.co.lion.farming_customer.viewmodel.FindAccountViewModel
import kr.co.lion.farming_customer.viewmodel.FindIdViewModel

class FindIdFragment : Fragment() {
    lateinit var fragmentFindIdBinding: FragmentFindIdBinding
    lateinit var loginActivity: LoginActivity

    lateinit var findIdViewModel: FindIdViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindIdBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_id, container, false)
        findIdViewModel = FindIdViewModel()
        fragmentFindIdBinding.findIdViewModel = findIdViewModel
        fragmentFindIdBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()

        return fragmentFindIdBinding.root
    }

    fun settingButton(){
        fragmentFindIdBinding.apply {
            buttonFindId.setOnClickListener {
                loginActivity.replaceFragment(LoginFragmentName.FIND_ID_DONE_FRAGMENT,
                    addToBackStack = true, isAnimate = true, data = null)
            }
        }
    }
}