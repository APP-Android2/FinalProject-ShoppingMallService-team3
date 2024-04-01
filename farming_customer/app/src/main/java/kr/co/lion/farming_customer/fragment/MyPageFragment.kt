package kr.co.lion.farming_customer.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.PointActivity
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

        return fragmentMyPageBinding.root
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

}