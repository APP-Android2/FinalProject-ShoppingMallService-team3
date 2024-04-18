package kr.co.lion.farming_customer.fragment.myPageServiceCenter

import android.os.Bundle
import android.util.Log
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
import kr.co.lion.farming_customer.databinding.FragmentMyPageServiceCenterNoticeBinding
import kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter.NoticeRVAdapter
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.NoticeModel
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterNoticeViewModel
import kr.co.lion.farming_customer.dao.myPageServiceCenter.MyPageServiceCenterNoticeDao

class MyPageServiceCenterNoticeFragment : Fragment() {
    lateinit var binding: FragmentMyPageServiceCenterNoticeBinding
    lateinit var myPageServiceCenterActivity: MyPageServiceCenterActivity
    lateinit var viewModel: ServiceCenterNoticeViewModel
    lateinit var noticeRVAdapter: NoticeRVAdapter

    var noticeList = mutableListOf<NoticeModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_page_service_center_notice, container, false)
        viewModel = ServiceCenterNoticeViewModel()
        binding.serviceCenterNoticeViewModel = viewModel
        binding.lifecycleOwner = this
        myPageServiceCenterActivity = activity as MyPageServiceCenterActivity

        noticeRVAdapter = NoticeRVAdapter(noticeList)
        binding.serviceCenterNoticeRv.adapter = noticeRVAdapter
        binding.serviceCenterNoticeRv.layoutManager = LinearLayoutManager(context)

        gettingNoticeData()

        return binding.root
    }

    // 현재 게시판의 데이터를 가져와 메인 화면의 리사이클러뷰를 갱신한다.
    fun gettingNoticeData(){
        CoroutineScope(Dispatchers.Main).launch {
            // 서버에서 데이터를 가져온다.
            noticeList = MyPageServiceCenterNoticeDao.gettingNoticeList()

            // 어댑터에 데이터를 설정하고 갱신한다.
            noticeRVAdapter.setData(noticeList)
        }
    }

}