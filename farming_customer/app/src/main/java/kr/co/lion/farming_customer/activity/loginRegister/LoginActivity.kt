package kr.co.lion.farming_customer.activity.loginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.ActivityLoginBinding
import kr.co.lion.farming_customer.fragment.loginRegister.CantFindIdFragment
import kr.co.lion.farming_customer.fragment.loginRegister.FindAccountFragment
import kr.co.lion.farming_customer.fragment.loginRegister.FindIdDoneFragment
import kr.co.lion.farming_customer.fragment.loginRegister.FindPwDone2Fragment
import kr.co.lion.farming_customer.fragment.loginRegister.FindPwDoneFragment
import kr.co.lion.farming_customer.fragment.loginRegister.LoginFragment
import kr.co.lion.farming_customer.fragment.loginRegister.Register2Fragment
import kr.co.lion.farming_customer.fragment.loginRegister.Register3Fragment
import kr.co.lion.farming_customer.fragment.loginRegister.RegisterFragment

class LoginActivity : AppCompatActivity() {

    lateinit var activityLoginBinding: ActivityLoginBinding

    // 프래그먼트 객체를 담을 변수
    var oldFragment: Fragment? = null
    var newFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        // 자동 로그인 설정
        val sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE)
        val loginUserIdx = sharedPreferences.getInt("loginUserIdx", -1)
        val loginUserNickName = sharedPreferences.getString("loginUserNickName", null)

        // LoginUserIdx 값이 없다면 (로그인 된 기록이 없다면)
        if (loginUserIdx == -1) {
            // 첫 화면을 띄워준다.
            replaceFragment(LoginFragmentName.LOGIN_FRAGMENT
            ,addToBackStack = false, isAnimate = false, data = null)
        } // 로그인 된 기록이 있다면
        else {
            // MainActivity를 실행한다.
            val intent = Intent(this, MainActivity::class.java)

            // 로그인한 사용자 정보를 전달해준다.
            intent.putExtra("loginUserIdx", loginUserIdx)
            intent.putExtra("loginUserNickName", loginUserNickName)

            startActivity(intent)
            // LoginActivity 종료
            finish()
        }
    }

    fun replaceFragment(name: LoginFragmentName, addToBackStack:Boolean, isAnimate:Boolean, data:Bundle?){
        SystemClock.sleep(200)
        // Fragment를 교체할 수 있는 객체를 추출한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // oldFragment에 newFragment가 가지고 있는 Fragment 객체를 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 이름으로 분기한다.
        // Fragment의 객체를 생성하여 변수에 담아준다.
        newFragment = when(name){
            LoginFragmentName.LOGIN_FRAGMENT -> {
                LoginFragment()
            }

            LoginFragmentName.REGISTER_FRAGMENT -> {
                RegisterFragment()
            }

            LoginFragmentName.REGISTER2_FRAGMENT -> {
                Register2Fragment()
            }
            LoginFragmentName.REGISTER3_FRAGMENT -> {
                Register3Fragment()
            }
            LoginFragmentName.FIND_ACCOUNT_FRAGMENT -> {
                FindAccountFragment()
            }
            LoginFragmentName.FIND_ID_DONE_FRAGMENT -> {
                FindIdDoneFragment()
            }
            LoginFragmentName.FIND_PW_DONE_FRAGMENT -> {
                FindPwDoneFragment()
            }
            LoginFragmentName.FIND_PW_DONE2_FRAGMENT -> {
                FindPwDone2Fragment()
            }
            LoginFragmentName.CANT_FIND_ID_FRAGMENT -> {
                CantFindIdFragment()
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
            fragmentTransaction.replace(R.id.containerLogin, newFragment!!)

            // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시켜 준다.
            if(addToBackStack){
                fragmentTransaction.addToBackStack(name.str)
            }
            // Fragment 교체를 확정한다.
            fragmentTransaction.commit()
        }
    }

    // BackStack에서 Fragment를 제거한다.
    fun removeFragment(name: LoginFragmentName){
        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거한다.
        SystemClock.sleep(200)
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}