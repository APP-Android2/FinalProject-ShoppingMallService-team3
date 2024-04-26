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
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementAddDeliveryAddressBinding

class MyPageManagementAddDeliveryAddressFragment : Fragment() {

    lateinit var fragmentMyPageAddDeliveryAddressBinding: FragmentMyPageManagementAddDeliveryAddressBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentMyPageAddDeliveryAddressBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_add_delivery_address,container,false)
        myPageManagementActivity = activity as MyPageManagementActivity

        settingToolbar()
        buttonSave()

        return fragmentMyPageAddDeliveryAddressBinding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageAddDeliveryAddressBinding.apply {
            toolbarMyPageManagementAddDeliveryAddress.apply {
                // 네비게이션 Back
                setNavigationOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_ADD_DELIVERY_ADDRESS)
                }
            }
        }
    }

    // 저장 버튼 메서드
    fun buttonSave(){
        fragmentMyPageAddDeliveryAddressBinding.apply {
            buttonMyPageManagementAddDeliveryAddressSaveDeliveryAddress.apply {
                setOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_ADD_DELIVERY_ADDRESS)
                }
            }
        }
    }
}