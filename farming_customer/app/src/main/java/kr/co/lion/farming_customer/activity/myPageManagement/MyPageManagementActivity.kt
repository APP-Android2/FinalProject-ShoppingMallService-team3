package kr.co.lion.farming_customer.activity.myPageManagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import kr.co.lion.farming_customer.MyPageManagementName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.ActivityMyPageManagementBinding
import kr.co.lion.farming_customer.fragment.myPageManagement.MyPageManagementAddDeliveryAddressFragment
import kr.co.lion.farming_customer.fragment.myPageManagement.MyPageManagementCheckUserInfoModifyFragment
import kr.co.lion.farming_customer.fragment.myPageManagement.MyPageManagementDeliveryAddressFragment
import kr.co.lion.farming_customer.fragment.myPageManagement.MyPageManagementMainFragment
import kr.co.lion.farming_customer.fragment.myPageManagement.MyPageManagementModifyDeliveryAddressFragment
import kr.co.lion.farming_customer.fragment.myPageManagement.MyPageManagementProfileModifyFragment
import kr.co.lion.farming_customer.fragment.myPageManagement.MyPageManagementUserInfoModifyFragment

class MyPageManagementActivity : AppCompatActivity() {

    lateinit var activityMyPageManagementBinding: ActivityMyPageManagementBinding

    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMyPageManagementBinding = ActivityMyPageManagementBinding.inflate(layoutInflater)

        replaceFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_MAIN,false,false,null)

        setContentView(activityMyPageManagementBinding.root)
    }

    fun replaceFragment(name: MyPageManagementName, addToBackStack:Boolean, isAnimate:Boolean, data:Bundle?){
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
            MyPageManagementName.MY_PAGE_MANAGEMENT_MAIN -> {
                newFragment = MyPageManagementMainFragment()
            }
            MyPageManagementName.MY_PAGE_MANAGEMENT_PROFILE_MODIFY -> {
                newFragment = MyPageManagementProfileModifyFragment()
            }
            MyPageManagementName.MY_PAGE_MANAGEMENT_CHECK_USER_INFO_MODIFY -> {
                newFragment = MyPageManagementCheckUserInfoModifyFragment()
            }
            MyPageManagementName.MY_PAGE_MANAGEMENT_USER_INFO_MODIFY -> {
                newFragment = MyPageManagementUserInfoModifyFragment()
            }
            MyPageManagementName.MY_PAGE_MANAGEMENT_DELIVERY_ADDRESS -> {
                newFragment = MyPageManagementDeliveryAddressFragment()
            }
            MyPageManagementName.MY_PAGE_MANAGEMENT_ADD_DELIVERY_ADDRESS -> {
                newFragment = MyPageManagementAddDeliveryAddressFragment()
            }
            MyPageManagementName.MY_PAGE_MANAGEMENT_MODIFY_DELIVERY_ADDRESS -> {
                newFragment = MyPageManagementModifyDeliveryAddressFragment()
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
            fragmentTransaction.replace(R.id.containerMyPageManagement, newFragment!!)

            // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
            if(addToBackStack){
                fragmentTransaction.addToBackStack(name.str)
            }
            // Fragment 교체를 확정한다.
            fragmentTransaction.commit()
        }
    }

    fun removeFragment(name:MyPageManagementName){
        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        SystemClock.sleep(200)
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}