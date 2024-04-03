package kr.co.lion.farming_customer.fragment.myPageServiceCenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterActivity
import kr.co.lion.farming_customer.databinding.FragmentMyPageServiceCenterFaqBinding
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterFaqViewModel

class MyPageServiceCenterFaqFragment : Fragment() {
    lateinit var binding : FragmentMyPageServiceCenterFaqBinding
    lateinit var myPageServiceCenterActivity: MyPageServiceCenterActivity
    lateinit var viewModel : ServiceCenterFaqViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_page_service_center_faq, container, false)
        viewModel = ServiceCenterFaqViewModel()
        binding.serviceCenterFaqViewModel = viewModel
        binding.lifecycleOwner = this
        myPageServiceCenterActivity = activity as MyPageServiceCenterActivity

        return binding.root
    }

}