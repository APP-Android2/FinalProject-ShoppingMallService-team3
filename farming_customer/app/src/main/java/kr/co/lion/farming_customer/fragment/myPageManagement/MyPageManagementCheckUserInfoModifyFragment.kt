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
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementCheckUserInfoModifyBinding

class MyPageManagementCheckUserInfoModifyFragment : Fragment() {

    lateinit var fragmentMyPageManagementCheckUserInfoModifyBinding: FragmentMyPageManagementCheckUserInfoModifyBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentMyPageManagementCheckUserInfoModifyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_check_user_info_modify,container,false)
        myPageManagementActivity = activity as MyPageManagementActivity

        settingToolbar()
        settingButtonDone()

        return fragmentMyPageManagementCheckUserInfoModifyBinding.root

    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageManagementCheckUserInfoModifyBinding.apply {
            toolbarMyPageManagementCheckUserInfoModify.apply {
                // 네비게이션 Back
                setNavigationOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_CHECK_USER_INFO_MODIFY)
                }
            }
        }
    }

    // 확인 버튼을 눌렀을 때
    fun settingButtonDone(){
        fragmentMyPageManagementCheckUserInfoModifyBinding.apply {
            buttonMyPageManagementCheckUserInfoModifyDone.setOnClickListener {
                myPageManagementActivity.replaceFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_USER_INFO_MODIFY,true,true,null)
            }
        }
    }
}