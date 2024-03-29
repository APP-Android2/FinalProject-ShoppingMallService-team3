package kr.co.lion.farming_customer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.ActivityMyPageManagementBinding

class MyPageManagementActivity : AppCompatActivity() {

    lateinit var activityMyPageManagementBinding: ActivityMyPageManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMyPageManagementBinding = ActivityMyPageManagementBinding.inflate(layoutInflater)
        setContentView(activityMyPageManagementBinding.root)
    }
}