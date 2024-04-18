package kr.co.lion.farming_customer.fragment.myPageServiceCenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterActivity
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterWriteInquiryActivity
import kr.co.lion.farming_customer.dao.myPageServiceCenter.MyPageServiceCenterInquiryDao
import kr.co.lion.farming_customer.databinding.FragmentMyPageServiceCenterInquiryBinding
import kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter.InquiryRVAdapter
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.InquiryModel
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterInquiryViewModel

class MyPageServiceCenterInquiryFragment : Fragment() {
    lateinit var binding: FragmentMyPageServiceCenterInquiryBinding
    lateinit var myPageServiceCenterActivity: MyPageServiceCenterActivity
    lateinit var viewModel: ServiceCenterInquiryViewModel

    lateinit var inquiryRVAdapter : InquiryRVAdapter

    var inquiryList = mutableListOf<InquiryModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_page_service_center_inquiry, container, false)
        viewModel = ServiceCenterInquiryViewModel()
        binding.serviceCenterInquiryViewModel = viewModel
        binding.lifecycleOwner = this
        myPageServiceCenterActivity = activity as MyPageServiceCenterActivity

        inquiryRVAdapter = InquiryRVAdapter(requireContext(), inquiryList)
        binding.serviceCenterInquiryRv.adapter = inquiryRVAdapter
        binding.serviceCenterInquiryRv.layoutManager = LinearLayoutManager(context)

        settingFloatingActionButton()
        gettingInquiryList()

        return binding.root
    }

    private fun settingFloatingActionButton() {
        binding.serviceCenterInquiryFab.setOnClickListener {
            val intent = Intent(context, MyPageServiceCenterWriteInquiryActivity::class.java)
            startActivity(intent)
        }
    }

    fun gettingInquiryList() {
        CoroutineScope(Dispatchers.Main).launch {
            inquiryList = MyPageServiceCenterInquiryDao.gettingInquiryList()
            inquiryRVAdapter.setInquiryListData(inquiryList)
        }
    }
}