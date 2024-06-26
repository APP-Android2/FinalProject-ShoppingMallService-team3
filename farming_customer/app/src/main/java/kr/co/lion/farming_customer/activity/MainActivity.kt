package kr.co.lion.farming_customer.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.databinding.ActivityMainBinding
import kr.co.lion.farming_customer.fragment.HomeFragment
import kr.co.lion.farming_customer.fragment.LikeFragment
import kr.co.lion.farming_customer.fragment.MyPageFragment
import kr.co.lion.farming_customer.fragment.community.CommunityFragment
import kr.co.lion.farming_customer.fragment.farmingLife.FarmingLifeFarmAndActivityFragment
import kr.co.lion.farming_customer.fragment.farmingLife.TapActivityFragment
import kr.co.lion.farming_customer.fragment.farmingLife.TapFarmFragment
import kr.co.lion.farming_customer.fragment.famingLifeTools.FarmingLifeToolsFragment
import kr.co.lion.farming_customer.fragment.tradeCrop.TradeFragment
import kr.co.lion.farming_customer.fragment.tradeCrop.TradeTabDetailFragment
import kr.co.lion.farming_customer.model.user.UserModel

class MainActivity : AppCompatActivity(), DialogYesNoInterface {
    lateinit var activityMainBinding: ActivityMainBinding

    // 프래그먼트 객체를 담을 변수
    var oldFragment: Fragment? = null
    var newFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(activityMainBinding.root)

        settingBottomNavigation()

        replaceFragment(MainFragmentName.HOME_FRAGMENT, false, false, null)
    }

    // 네비게이션 세팅
    fun settingBottomNavigation() {
        activityMainBinding.apply {
            bottomNavigation.apply {
                // 초기화면 홈으로 세팅
                bottomNavigation.setItemIconTintList(null)
                selectedItemId = R.id.menuItemBottomNavigation_Home
                setOnItemSelectedListener {
                    when(it.itemId){
                        R.id.menuItemBottomNavigation_Trade -> {
                            replaceFragment(MainFragmentName.TRADE_FRAGMENT, false, false, null)
                        }
                        R.id.menuItemBottonNavigation_Farming_Life -> {
                            replaceFragment(MainFragmentName.COMMUNITY_FRAGMENT, false, false, null)
                        }

                        R.id.menuItemBottomNavigation_Home -> {
                            replaceFragment(MainFragmentName.HOME_FRAGMENT, false, false, null)

                        }
                        R.id.menuItemBottonNavigation_Like -> {
                            replaceFragment(MainFragmentName.LIKE_FRAGMENT, false, false, null)
                        }
                        R.id.menuItemBottonNavigation_MyPage -> {
                            val sharedPreferences = this@MainActivity.getSharedPreferences("AutoLogin",
                                Context.MODE_PRIVATE)
                            val userIdx = sharedPreferences.getInt("loginUserIdx", -1)
                            if(userIdx == -1){
                                // 사용자가 로그인 하지 않은 상태인 경우
                                val dialog = DialogYesNo(this@MainActivity,"로그인이 필요한 기능입니다.", "로그인 하시겠습니까?", this@MainActivity, yes_text = "로그인")
                                dialog.show(this@MainActivity.supportFragmentManager, "DialogYesNo")
                                return@setOnItemSelectedListener false
                            }else{
                                // 사용자가 로그인 한 상태인 경우
                                replaceFragment(MainFragmentName.MY_PAGE_FRAGMENT, false, false, null)
                            }
                        }
                    }
                    true
                }
            }
        }
    }

    fun replaceFragment(name:MainFragmentName, addToBackStack:Boolean, isAnimate:Boolean, data:Bundle?, container : Int = R.id.containerMain){
        SystemClock.sleep(200)
        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // oldFragment에 newFragment가 가지고 있는 Fragment 객체를 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 이름으로 분기한다.
        // Fragment의 객체를 생성하여 변수에 담아준다.
        when(name){
            MainFragmentName.TRADE_FRAGMENT -> {
                newFragment = TradeFragment()
            }
            MainFragmentName.FARMING_LIFE_TOOLS_FRAGMENT -> {
                newFragment = FarmingLifeToolsFragment()
            }
            MainFragmentName.HOME_FRAGMENT -> {
                newFragment = HomeFragment()
            }
            MainFragmentName.LIKE_FRAGMENT -> {
                newFragment = LikeFragment()
            }
            MainFragmentName.MY_PAGE_FRAGMENT -> {
                newFragment = MyPageFragment()
            }
            MainFragmentName.COMMUNITY_FRAGMENT -> {
                newFragment = CommunityFragment()
            }
            MainFragmentName.FARMING_LIFE_FARM_AND_ACTIVITY_FRAGMENT -> {
                newFragment = FarmingLifeFarmAndActivityFragment()
            }
            MainFragmentName.TAP_FARM_FRAGMENT -> {
                newFragment = TapFarmFragment()
            }
            MainFragmentName.TAP_ACTIVITY_FRAGMENT -> {
                newFragment = TapActivityFragment()
            }
            MainFragmentName.TRADE_TAB_DETAIL_FRAGMENT -> {
                newFragment = TradeTabDetailFragment()
            }
        }
        if(data != null){
            newFragment?.arguments = data
        }

        if(newFragment != null){
            // 애니메이션 설정
            if(isAnimate){
                // oldFragment -> newFragment
                // oldFragment : exit
                // newFragment : enter

                // newFragment -> oldFragment
                // oldFragment : reenter
                // newFragment : return

                // MaterialSharedAxis : 좌우, 위아래, 공중 바닥 사이로 이동하는 애니메이션 효과
                // X - 좌우
                // Y - 위아래
                // Z - 공중 바닥
                // 두 번째 매개변수 : 새로운 화면이 나타나는 것인지 여부를 설정해준다.
                // true : 새로운 화면이 나타나는 애니메이션이 동작한다.
                // false : 이전으로 되돌아가는 애니메이션이 동작한다.
                if(oldFragment != null){
                    // old에서 new가 새롭게 보여질 때 old의 애니메이션
                    oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                    // new에서 old로 되돌아갈때 old의 애니메이션
                    oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

                    oldFragment?.enterTransition = null
                    oldFragment?.returnTransition = null
                }

                // old에서 new가 새롭게 보여질 때 new의 애니메이션
                newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                // new에서 old로 되돌아갈때 new의 애니메이션
                newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

                newFragment?.exitTransition = null
                newFragment?.reenterTransition = null
            }
            // Fragment를 교체한다.(이전 Fragment가 없으면 새롭게 추가하는 역할을 수행한다)
            // 첫 번째 매개 변수 : Fragment를 배치할 FragmentContainerView의 ID
            // 두 번째 매개 변수 : 보여주고하는 Fragment 객체를
            fragmentTransaction.replace(container, newFragment!!)

            // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
            if(addToBackStack){
                fragmentTransaction.addToBackStack(name.str)
            }
            // Fragment 교체를 확정한다.
            fragmentTransaction.commit()
        }
    }

    // BackStack에서 Fragment를 제거한다.
    fun removeFragment(name:MainFragmentName){
        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        SystemClock.sleep(200)
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}