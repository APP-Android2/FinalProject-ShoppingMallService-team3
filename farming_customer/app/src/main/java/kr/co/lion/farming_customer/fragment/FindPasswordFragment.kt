package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentFindPasswordBinding
import kr.co.lion.farming_customer.viewmodel.FindPwViewModel

class FindPasswordFragment : Fragment() {

    lateinit var fragmentFindPwBinding: FragmentFindPasswordBinding
    lateinit var loginActivity: LoginActivity

    lateinit var findPwViewModel: FindPwViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        fragmentFindPwBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password, container, false)
        findPwViewModel = FindPwViewModel()
        fragmentFindPwBinding.findPwViewModel = findPwViewModel
        fragmentFindPwBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        return fragmentFindPwBinding.root
    }

}