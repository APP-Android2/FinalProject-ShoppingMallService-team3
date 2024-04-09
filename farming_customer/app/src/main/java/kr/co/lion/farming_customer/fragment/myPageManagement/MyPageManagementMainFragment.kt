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
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementMainBinding

class MyPageManagementMainFragment : Fragment() {

    lateinit var fragmentMyPageManagementMainBinding: FragmentMyPageManagementMainBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentMyPageManagementMainBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_main, container, false)
        myPageManagementActivity = activity as MyPageManagementActivity


        settingToolbar()
        buttonModifyProfile()

        return fragmentMyPageManagementMainBinding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageManagementMainBinding.apply {
            toolbarMyPageManagementMain.apply {
                // 네비게이션
                setNavigationOnClickListener {
                    myPageManagementActivity.finish()
                }
            }
        }
    }

    // 버튼 이벤트 설정
    fun buttonModifyProfile(){
        fragmentMyPageManagementMainBinding.apply {
            // 프로필 수정 버튼
            buttonMyPageManagementMainProfileModify.apply {
                setOnClickListener {
                    myPageManagementActivity.replaceFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_PROFILE_MODIFY,true,true,null)
                }
            }
            // 회원정보 버튼
            buttonMyPageManagementMainUserInfoDetail.apply {
                setOnClickListener {
                    myPageManagementActivity.replaceFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_CHECK_USER_INFO_MODIFY,true,true,null)
                }
            }
            // 기본 배송지 버튼
            buttonMyPageManagementMainBasicDeliveryAddressDetail.apply {
                setOnClickListener {
                    myPageManagementActivity.replaceFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_DELIVERY_ADDRESS,true,true,null)
                }
            }
        }
    }
}