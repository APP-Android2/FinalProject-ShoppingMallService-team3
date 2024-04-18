package kr.co.lion.farming_customer.fragment.myPageServiceCenter

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
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterActivity
import kr.co.lion.farming_customer.dao.myPageServiceCenter.MyPageServiceCenterFaqDao
import kr.co.lion.farming_customer.dao.myPageServiceCenter.MyPageServiceCenterNoticeDao
import kr.co.lion.farming_customer.databinding.FragmentMyPageServiceCenterFaqBinding
import kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter.FaqRVAdapter
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.FaqModel
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterFaqViewModel

class MyPageServiceCenterFaqFragment : Fragment() {
    lateinit var binding : FragmentMyPageServiceCenterFaqBinding
    lateinit var myPageServiceCenterActivity: MyPageServiceCenterActivity
    lateinit var viewModel : ServiceCenterFaqViewModel
    lateinit var faqRVAdapter : FaqRVAdapter

    var faqList = mutableListOf<FaqModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_page_service_center_faq, container, false)
        viewModel = ServiceCenterFaqViewModel()
        binding.serviceCenterFaqViewModel = viewModel
        binding.lifecycleOwner = this
        myPageServiceCenterActivity = activity as MyPageServiceCenterActivity

        faqRVAdapter = FaqRVAdapter(faqList)
        binding.serviceCenterFaqRv.adapter = faqRVAdapter
        binding.serviceCenterFaqRv.layoutManager = LinearLayoutManager(context)

        gettingNoticeData()

        return binding.root
    }

    fun gettingNoticeData(){
        CoroutineScope(Dispatchers.Main).launch {
            // 서버에서 데이터를 가져온다.
            faqList = MyPageServiceCenterFaqDao.gettingFaqList()

            // 어댑터에 데이터를 설정하고 갱신한다.
            faqRVAdapter.setData(faqList)
        }
    }

}