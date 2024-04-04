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
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementUserInfoModifyBinding

class MyPageManagementUserInfoModifyFragment : Fragment() {

    lateinit var fragmentMyPageManagementUserInfoModifyBinding: FragmentMyPageManagementUserInfoModifyBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentMyPageManagementUserInfoModifyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_user_info_modify,container,false)
        myPageManagementActivity = activity as MyPageManagementActivity

        settingToolbar()
        settingButtonDone()

        return fragmentMyPageManagementUserInfoModifyBinding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageManagementUserInfoModifyBinding.apply {
            toolbarMyPageManagementUserInfoModify.apply {
                // 네비게이션 Back
                setNavigationOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_USER_INFO_MODIFY)
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_CHECK_USER_INFO_MODIFY)
                }
            }
        }
    }

    // 완료 버튼 메서드
    fun settingButtonDone(){
        fragmentMyPageManagementUserInfoModifyBinding.apply {
            buttonMyPageManagementUserInfoModifyDone.setOnClickListener {
                myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_USER_INFO_MODIFY)
                myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_CHECK_USER_INFO_MODIFY)
            }
        }
    }
}