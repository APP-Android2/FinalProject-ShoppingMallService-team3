package kr.co.lion.farming_customer.fragment.myPageManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.MyPageManagementName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.myPageManagement.MyPageManagementActivity
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementModifyDeliveryAddressBinding

class MyPageManagementModifyDeliveryAddressFragment : Fragment() {

    lateinit var fragmentMyPageModifyDeliveryAddressBinding: FragmentMyPageManagementModifyDeliveryAddressBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentMyPageModifyDeliveryAddressBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_modify_delivery_address,container,false)
        myPageManagementActivity = activity as MyPageManagementActivity

        settingToolbar()
        settingText()
        buttonModify()

        return fragmentMyPageModifyDeliveryAddressBinding.root
    }

    // 텍스트 기본세팅
    fun settingText(){
        fragmentMyPageModifyDeliveryAddressBinding.apply {
            textFieldMyPageManagementModifyDeliveryAddressName.setText("홍길동")
            textFieldMyPageManagementModifyDeliveryAddressPostNumber.setText("[121212] 서울특별시 00구 00동")
            textFieldMyPageManagementModifyDeliveryAddressAddressDetail.setText("101-101")
            textFieldMyPageManagementModifyDeliveryAddressPhoneNumber.setText("01012345678")
            textFieldMyPageManagementModifyDeliveryAddressRequest.setText("배송 전 연락주세요")
        }
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageModifyDeliveryAddressBinding.apply {
            toolbarMyPageManagementModifyDeliveryAddress.apply {
                // 네비게이션 Back
                setNavigationOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_MODIFY_DELIVERY_ADDRESS)
                }
            }
        }
    }

    // 수정 버튼 메서드
    fun buttonModify(){
        fragmentMyPageModifyDeliveryAddressBinding.apply {
            buttonMyPageManagementModifyDeliveryAddressSaveDeliveryAddress.setOnClickListener {
                myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_MODIFY_DELIVERY_ADDRESS)
            }
        }
    }
}