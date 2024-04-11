package kr.co.lion.farming_customer.activity.farmingLifeTools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.ActivityFarmingLifeToolsDetailBinding

class FarmingLifeToolsDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityFarmingLifeToolsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmingLifeToolsDetailBinding.inflate(layoutInflater)

        settingDetailToolbar()

        setContentView(binding.root)
    }

    private fun settingDetailToolbar() {
        binding.apply {
            farmingLifeToolsDetailToolbar.apply {
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }
}