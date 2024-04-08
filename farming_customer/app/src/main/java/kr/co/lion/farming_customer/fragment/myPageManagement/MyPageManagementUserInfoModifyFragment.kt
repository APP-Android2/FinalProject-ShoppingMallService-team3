package kr.co.lion.farming_customer.fragment.myPageManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
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
        errorMessagePw()


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

    // 비밀번호 오류 메세지
    fun errorMessagePw(){
        fragmentMyPageManagementUserInfoModifyBinding.apply {
            // 첫번째 비밀번호 입력할 때 오류 메세지
            textFieldMyPageManagementUserInfoModifyUserPw.addTextChangedListener {
                if(textFieldMyPageManagementUserInfoModifyUserPw.length() <6 || textFieldMyPageManagementUserInfoModifyUserPw.length() > 20){
                    textInputLayoutUserPw.error = "6자 이상 20자 이하로 입력하세요"
                }
                else{
                    textInputLayoutUserPw.error = null
                    textInputLayoutUserPw.isErrorEnabled = false
                }
            }
            // 두번째 비밀번호가 첫번째 비밀번호와 다를 때
            textFieldMyPageManagementUserInfoModifyCheckUserPw.addTextChangedListener {
                val userPw = textFieldMyPageManagementUserInfoModifyUserPw.text.toString()
                val userPw2 = textFieldMyPageManagementUserInfoModifyCheckUserPw.text.toString()
                if(userPw != userPw2){
                    textInputLayoutUserPw2.error = "비밀번호가 다릅니다"
                }
                else{
                    textInputLayoutUserPw2.error = null
                    textInputLayoutUserPw2.isErrorEnabled = false
                }
            }
        }
    }
}