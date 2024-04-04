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
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementProfileModifyBinding

class MyPageManagementProfileModifyFragment : Fragment() {

    lateinit var fragmentMyPageManagementProfileModifyBinding: FragmentMyPageManagementProfileModifyBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentMyPageManagementProfileModifyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_profile_modify,container,false)
        myPageManagementActivity = activity as MyPageManagementActivity

        settingToolbar()
        modifyNickName()


        return fragmentMyPageManagementProfileModifyBinding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageManagementProfileModifyBinding.apply {
            toolbarMyPageManagementProfileModify.apply {
                // 네비게이션 Back
                setNavigationOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_PROFILE_MODIFY)
                }
            }
        }
    }

    // 닉네임을 수정했을 때 완료버튼 비활성화 메서드
    fun modifyNickName(){
        fragmentMyPageManagementProfileModifyBinding.apply {
            textFieldMyPageManagementProfileModifyModifyUserNickName.addTextChangedListener {

                if(it!!.isEmpty()){
                    textInputLayoutCheckNickName.error = "닉네임이 중복됩니다"
                    // 완료버튼 비활성화
                    buttonMyPageManagementProfileModifyDone.isEnabled = false
                }
                else{
                    textInputLayoutCheckNickName.error = null
                    // 완료버튼 활성화
                    buttonMyPageManagementProfileModifyDone.isEnabled = true
                    buttonMyPageManagementProfileModifyDone.setOnClickListener {
                        myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_PROFILE_MODIFY)
                    }
                }
            }
        }
    }
}