package kr.co.lion.farming_customer.activity.myPageServiceCenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.ActivityMyPageServiceCenterReplyBinding

class MyPageServiceCenterReplyActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPageServiceCenterReplyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageServiceCenterReplyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingToolbar()
    }

    fun settingToolbar() {
        binding.serviceCenterReplyToolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }
}