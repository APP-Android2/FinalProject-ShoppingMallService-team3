package kr.co.lion.farming_customer.fragment.myPageManagement

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.MyPageManagementName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.myPageManagement.MyPageManagementActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementMainBinding
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.myPageManagement.MyPageManagementMainViewModel
import org.apache.commons.lang3.mutable.Mutable

class MyPageManagementMainFragment : Fragment() {

    lateinit var fragmentMyPageManagementMainBinding: FragmentMyPageManagementMainBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity
    lateinit var myPageManagementMainViewModel : MyPageManagementMainViewModel

    var userModel : UserModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentMyPageManagementMainBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_main, container, false)
        myPageManagementMainViewModel = MyPageManagementMainViewModel()
        fragmentMyPageManagementMainBinding.myPageManagementMainViewModel = myPageManagementMainViewModel
        fragmentMyPageManagementMainBinding.lifecycleOwner = this
        myPageManagementActivity = activity as MyPageManagementActivity

        settingUserData()
        settingToolbar()
        buttonModifyProfile()

        return fragmentMyPageManagementMainBinding.root
    }

    private fun settingUserData() {
        val sharedPreferences = myPageManagementActivity.getSharedPreferences("AutoLogin",
            Context.MODE_PRIVATE)
        val userIdx = sharedPreferences.getInt("loginUserIdx", -1)
        CoroutineScope(Dispatchers.Main).launch {
            userModel = UserDao.gettingUserInfoByUserIdx(userIdx)
            settingView()
            UserDao.gettingUserImage(requireContext(), userModel!!.user_profile_image, fragmentMyPageManagementMainBinding.imageViewMyPageManagementMainProfile)
        }
    }

    private fun settingView() {
        fragmentMyPageManagementMainBinding.apply {
            myPageManagementMainViewModel!!.apply {
                textViewMyPageManagementMain_NickName.value = userModel!!.user_nickname
                textViewMyPageManagementMain_UserName.value = userModel!!.user_name
                textViewMyPageManagementMain_UserId.value = userModel!!.user_id
                textViewMyPageManagementMain_UserPhoneNumber.value = userModel!!.user_phone
                textViewMyPageManagementMain_RecipientName.value = userModel!!.user_name
                textViewMyPageManagementMain_AddressName.value = userModel!!.user_address
                textViewMyPageManagementMain_AddressPhoneNumber.value = userModel!!.user_phone
            }
        }
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