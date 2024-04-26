package kr.co.lion.farming_customer.activity.myPageServiceCenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.dao.myPageServiceCenter.MyPageServiceCenterInquiryDao
import kr.co.lion.farming_customer.databinding.ActivityMyPageServiceCenterReplyBinding
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterInquiryViewModel

class MyPageServiceCenterReplyActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPageServiceCenterReplyBinding
    lateinit var viewModel: ServiceCenterInquiryViewModel
    var inquiryIdx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_page_service_center_reply)
        viewModel = ServiceCenterInquiryViewModel()
        binding.serviceCenterInquiryViewModel = viewModel
        binding.lifecycleOwner = this

        inquiryIdx = intent.getIntExtra("inquiryIdx", 0)

        settingToolbar()
        gettingReplyData()
    }

    fun settingToolbar() {
        binding.serviceCenterReplyToolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    fun gettingReplyData() {
        CoroutineScope(Dispatchers.Main).launch {
            val inquiryModel = MyPageServiceCenterInquiryDao.selectInquiryData(inquiryIdx)

            viewModel.inquiryType.value = when (inquiryModel?.inquiry_type) {
                0 -> "농산물"
                1 -> "주말농장"
                2 -> "체험활동"
                3 -> "구인구직"
                4 -> "농기구"
                5 -> "커뮤니티"
                6 -> "기타"
                else -> "기타"
            }

            viewModel.inquiryContent.value = inquiryModel?.inquiry_content
            viewModel.inquiryAnswer.value = when (inquiryModel?.inquiry_answer) {
                "" -> "답변 대기중 입니다"
                else -> inquiryModel?.inquiry_answer
            }
        }
    }
}