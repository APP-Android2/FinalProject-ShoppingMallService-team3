package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentFindAccountBinding
import kr.co.lion.farming_customer.viewmodel.FindAccountViewModel


class FindAccountFragment : Fragment() {

    lateinit var fragmentFindAccountBinding: FragmentFindAccountBinding
    lateinit var loginActivity: LoginActivity

    lateinit var findAccountViewModel: FindAccountViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_account, container, false)
        findAccountViewModel = FindAccountViewModel()
        fragmentFindAccountBinding.findAccountViewModel = findAccountViewModel
        fragmentFindAccountBinding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingToolbar()
        setupViewPagerAndTabs()

        return fragmentFindAccountBinding.root
    }

    // 툴바 관련 함수
    fun settingToolbar(){
        fragmentFindAccountBinding.apply {
            toolbarFindAccount.apply {
                // 제목
                title = "아이디/비밀번호 찾기"

                // 뒤로가기
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    loginActivity.removeFragment(LoginFragmentName.FIND_ACCOUNT_FRAGMENT)
                }
            }
        }
    }


    class FindAccountPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2 // 탭의 수

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FindIdFragment() // "아이디 찾기" 탭의 프래그먼트
                1 -> FindPasswordFragment() // "비밀번호 찾기" 탭의 프래그먼트
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

    private fun setupViewPagerAndTabs() {
        val adapter = FindAccountPagerAdapter(this)
        fragmentFindAccountBinding.viewPager.adapter = adapter

        TabLayoutMediator(fragmentFindAccountBinding.tabLayoutFindAccount, fragmentFindAccountBinding.viewPager) { tab, position ->
            tab.text = if (position == 0) "아이디" else "비밀번호"
        }.attach()
    }

}