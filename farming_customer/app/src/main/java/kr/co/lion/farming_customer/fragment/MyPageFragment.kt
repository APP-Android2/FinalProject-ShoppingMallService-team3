package kr.co.lion.farming_customer.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.myPageManagement.MyPageManagementActivity
import kr.co.lion.farming_customer.activity.myPageMyPost.MyPageMyPostActivity
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterActivity
import kr.co.lion.farming_customer.activity.myPageSetting.MyPageSettingActivity
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.activity.point.PointActivity
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.FragmentMyPageBinding
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.MyPageViewModel
import java.text.DecimalFormat

class MyPageFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentMyPageBinding: FragmentMyPageBinding
    lateinit var mainActivity: MainActivity
    lateinit var myPageViewModel : MyPageViewModel

    var userModel : UserModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMyPageBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_page, container, false)
        myPageViewModel = MyPageViewModel()
        fragmentMyPageBinding.myPageViewModel = myPageViewModel
        fragmentMyPageBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingUserData()
        settingTextViewMyPageFirstReview()
        settingTextViewMyPageFirstCart()
        settingMyPageManagement()
        settingTextViewMyPageFirstCS()
        settingTextViewMyPageFirstSetting()
        settingTextViewMyPageFirstPost()
        settingLogout()


        return fragmentMyPageBinding.root
    }

    private fun settingLogout() {
        // 로그아웃
        fragmentMyPageBinding.textViewMyPageFirstLogout.setOnClickListener {
            val dialog = DialogYesNo(this@MyPageFragment, null, "로그아웃 하시겠습니까?", mainActivity, null)
            dialog.show(mainActivity.supportFragmentManager, "DialogYesNo")
        }
    }

    private fun settingUserData() {
        val sharedPreferences = mainActivity.getSharedPreferences("AutoLogin",
            Context.MODE_PRIVATE)
        val userIdx = sharedPreferences.getInt("loginUserIdx", -1)

        CoroutineScope(Dispatchers.Main).launch {
            userModel = UserDao.gettingUserInfoByUserIdx(userIdx)
            settingCardViewMyPageFirstPoint()
        }

    }

    private fun settingTextViewMyPageFirstCS() {
        fragmentMyPageBinding.apply {
            textViewMyPageFirstCS.setOnClickListener {
                val intent = Intent(mainActivity, MyPageServiceCenterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun settingMyPageManagement() {
        fragmentMyPageBinding.apply {
            imageViewMyPageFirstInformation.setOnClickListener {
                val intent = Intent(mainActivity, MyPageManagementActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 포인트
    fun settingCardViewMyPageFirstPoint() {
        fragmentMyPageBinding.apply {
            myPageViewModel?.apply {
                textViewMyPageFirstPointNumber.value = DecimalFormat("#,###").format(userModel!!.user_point)+"P"
                textViewMyPageFirstNickName.value = userModel!!.user_nickname
            }

            cardViewMyPageFirstPoint.setOnClickListener {
                val pointIntent = Intent(mainActivity, PointActivity::class.java)
                startActivity(pointIntent)
            }
            CoroutineScope(Dispatchers.Main).launch {
                UserDao.gettingUserImage(requireContext(), userModel!!.user_profile_image, imageViewMyPageFirstProfile)
            }
        }
    }

    // 리뷰 내역
    fun settingTextViewMyPageFirstReview() {
        fragmentMyPageBinding.apply {
            textViewMyPageFirstReview.setOnClickListener {
                val reviewIntent = Intent(mainActivity, ReviewActivity::class.java)
                startActivity(reviewIntent)
            }
        }
    }

    // 게시글 내역
    fun settingTextViewMyPageFirstPost() {
        fragmentMyPageBinding.apply {
            textViewMyPageFirstPost.setOnClickListener {
                val postIntent = Intent(mainActivity, MyPageMyPostActivity::class.java)
                startActivity(postIntent)
            }
        }
    }

    // 장바구니
    fun settingTextViewMyPageFirstCart() {
        fragmentMyPageBinding.apply {
            textViewMyPageFirstCart.setOnClickListener {
                val cartIntent = Intent(mainActivity, CartActivity::class.java)
                startActivity(cartIntent)
            }
            // 농산물 주문 내역
            textViewMyPageFirstCrop.setOnClickListener {
                val intent = Intent(mainActivity, OrderHistoryActivity::class.java)
                intent.putExtra("fragmentName", OrderHistoryFragmentName.ORDER_HISTORY_CROP_FRAGMENT)
                startActivity(intent)
            }
            // 주말농장 주문 내역
            textViewMyPageFirstFarm.setOnClickListener {
                val intent = Intent(mainActivity, OrderHistoryActivity::class.java)
                intent.putExtra("fragmentName", OrderHistoryFragmentName.ORDER_HISTORY_FARM_FRAMGNET)
                startActivity(intent)
            }
            //체험활동 주문 내역
            textViewMyPageFirstActivity.setOnClickListener {
                val intent = Intent(mainActivity, OrderHistoryActivity::class.java)
                intent.putExtra("fragmentName", OrderHistoryFragmentName.ORDER_HISTORY_ACTIVITY_FRAGMENT)
                startActivity(intent)
            }
        }
    }

    // 설정
    fun settingTextViewMyPageFirstSetting(){
        fragmentMyPageBinding.apply {
            textViewMyPageFirstSetting.setOnClickListener {
                val settingIntent = Intent(mainActivity, MyPageSettingActivity::class.java)
                startActivity(settingIntent)
            }
        }
    }

    override fun onYesButtonClick(id: Int) {
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {
        // 로그아웃 확인 눌렀을 때
        val prefs : SharedPreferences = mainActivity.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.remove("AutoLogin")
        editor.clear()
        editor.commit()
        mainActivity.settingBottomNavigation()
        //mainActivity.replaceFragment(MainFragmentName.HOME_FRAGMENT, false, false, null)
    }

}