package kr.co.lion.farming_customer.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.myPageManagement.MyPageManagementActivity
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterActivity
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.activity.point.PointActivity
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.databinding.FragmentMyPageBinding
import kr.co.lion.farming_customer.viewmodel.MyPageViewModel

class MyPageFragment : Fragment() {
    lateinit var fragmentMyPageBinding: FragmentMyPageBinding
    lateinit var mainActivity: MainActivity
    lateinit var myPageViewModel : MyPageViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMyPageBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_page, container, false)
        myPageViewModel = MyPageViewModel()
        fragmentMyPageBinding.myPageViewModel = myPageViewModel
        fragmentMyPageBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingCardViewMyPageFirstPoint()
        settingTextViewMyPageFirstReview()
        settingTextViewMyPageFirstCart()
        settingMyPageManagement()
        settingTextViewMyPageFirstCS()


        return fragmentMyPageBinding.root
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

            myPageViewModel?.textViewMyPageFirstPointNumber?.value = "1,000"

            cardViewMyPageFirstPoint.setOnClickListener {
                val pointIntent = Intent(mainActivity, PointActivity::class.java)
                startActivity(pointIntent)
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

}