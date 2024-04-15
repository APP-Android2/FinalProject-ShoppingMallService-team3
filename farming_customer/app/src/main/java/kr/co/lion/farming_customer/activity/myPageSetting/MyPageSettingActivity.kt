package kr.co.lion.farming_customer.activity.myPageSetting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kr.co.lion.farming_customer.databinding.ActivityMyPageSettingBinding

class MyPageSettingActivity : AppCompatActivity() {

    lateinit var activityMyPageSettingBinding: ActivityMyPageSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMyPageSettingBinding = ActivityMyPageSettingBinding.inflate(layoutInflater)


        buttonPermissionSettings()
        settingToolbar()


        setContentView(activityMyPageSettingBinding.root)
    }

    // 툴바 설정
    fun settingToolbar(){
        activityMyPageSettingBinding.apply {
            toolbarMyPageSetting.apply {
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }

    fun appPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val packageName = packageName

        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    // 접근 권한 버튼 메서드
    fun buttonPermissionSettings(){
        activityMyPageSettingBinding.apply {
            constraintLayoutAccessPermissionSettings.setOnClickListener {
                appPermissionSettings()
            }
        }
    }
}