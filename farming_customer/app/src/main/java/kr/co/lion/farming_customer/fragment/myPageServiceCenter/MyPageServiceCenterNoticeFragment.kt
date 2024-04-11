package kr.co.lion.farming_customer.fragment.myPageServiceCenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterActivity
import kr.co.lion.farming_customer.databinding.FragmentMyPageServiceCenterNoticeBinding
import kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter.NoticeRVAdapter
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterNoticeViewModel

class MyPageServiceCenterNoticeFragment : Fragment() {
    lateinit var binding: FragmentMyPageServiceCenterNoticeBinding
    lateinit var myPageServiceCenterActivity: MyPageServiceCenterActivity
    lateinit var viewModel: ServiceCenterNoticeViewModel

    lateinit var noticeRVAdapter: NoticeRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_page_service_center_notice, container, false)
        viewModel = ServiceCenterNoticeViewModel()
        binding.serviceCenterNoticeViewModel = viewModel
        binding.lifecycleOwner = this
        myPageServiceCenterActivity = activity as MyPageServiceCenterActivity

        noticeRVAdapter = NoticeRVAdapter()
        binding.serviceCenterNoticeRv.adapter = noticeRVAdapter
        binding.serviceCenterNoticeRv.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

}