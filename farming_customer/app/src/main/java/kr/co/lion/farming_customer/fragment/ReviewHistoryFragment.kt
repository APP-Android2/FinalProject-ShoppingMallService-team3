package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.ReviewFragmentName
import kr.co.lion.farming_customer.activity.ReviewActivity
import kr.co.lion.farming_customer.databinding.FragmentReviewHistoryBinding

class ReviewHistoryFragment : Fragment() {
    lateinit var fragmentReviewHistoryBinding: FragmentReviewHistoryBinding
    lateinit var reviewActivity: ReviewActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentReviewHistoryBinding = FragmentReviewHistoryBinding.inflate(inflater)
        reviewActivity = activity as ReviewActivity

        settingToolbar()
        settingTabLayoutReviewHistory()
        reviewActivity.replaceFragment(ReviewFragmentName.REVIEW_TAB_CROP_FRAGMENT, false, false, null, container = R.id.containerReviewHistoryTab)

        return fragmentReviewHistoryBinding.root
    }

    // 툴바 설정
    fun settingToolbar() {
        fragmentReviewHistoryBinding.apply {
            toolbarReview.apply {
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    reviewActivity.finish()
                }
            }
        }
    }

    // 탭 설정
    fun settingTabLayoutReviewHistory() {
        fragmentReviewHistoryBinding.apply {
            tabLayoutReviewHistory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        // 농산물 탭
                        0 -> {
                            reviewActivity.replaceFragment(ReviewFragmentName.REVIEW_TAB_CROP_FRAGMENT, false, true, null, R.id.containerReviewHistoryTab)
                        }
                        // 주말농장 탭
                        1 -> {
                            reviewActivity.replaceFragment(ReviewFragmentName.REVIEW_TAB_FARM_FRAGMENT, false, true, null, R.id.containerReviewHistoryTab)
                        }
                        // 체험활동 탭
                        2 -> {
                            reviewActivity.replaceFragment(ReviewFragmentName.REVIEW_TAB_ACTIVITY_FRAGMENT, false, true, null, R.id.containerReviewHistoryTab)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // 탭이 선택되지 않았을 때
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // 탭이 다시 선택되었을 때
                }
            })
        }
    }

}