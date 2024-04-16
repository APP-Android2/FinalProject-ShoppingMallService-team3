package kr.co.lion.farming_customer.activity.myPageMyPost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.ActivityMyPageMyPostBinding
import kr.co.lion.farming_customer.fragment.myPageMyPost.MyPageMyPostBoardFragment
import kr.co.lion.farming_customer.fragment.myPageMyPost.MyPageMyPostCommentFragment

class MyPageMyPostActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyPageMyPostBinding


    private lateinit var myPageMyPostActivity: MyPageMyPostActivity
    private lateinit var myPostBoardFragment: MyPageMyPostBoardFragment
    private lateinit var myPostCommentFragment: MyPageMyPostCommentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPageMyPostActivity = MyPageMyPostActivity()
        myPostBoardFragment = MyPageMyPostBoardFragment()
        myPostCommentFragment = MyPageMyPostCommentFragment()

        binding.apply {
            myPageMyPostToolbar.apply {
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
//                    MyPageMyPostActivity.finish()
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.myPageMyPostfragmentContainerView, myPostBoardFragment).commit()
            binding.myPageMyPostTl.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val position = tab.position
                    val selected: Fragment? = when
                                                      (position) {
                        0 -> myPostBoardFragment
                        1 -> myPostCommentFragment
                        else -> null
                    }
                    selected?.let {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.myPageMyPostfragmentContainerView, it).commit()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

        }
    }

}




