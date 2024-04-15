package kr.co.lion.farming_customer.activity.farmingLifeTools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.farmingLifeTools.adapter.ToolsNameRVAdapter
import kr.co.lion.farming_customer.databinding.ActivityFarmingLifeToolsDetailBinding

class FarmingLifeToolsDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityFarmingLifeToolsDetailBinding
    lateinit var toolsNameRVAdapter: ToolsNameRVAdapter
    private var isHeartBtn : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmingLifeToolsDetailBinding.inflate(layoutInflater)

        toolsNameRVAdapter = ToolsNameRVAdapter()
        binding.farmingLifeToolsDetailToolRv.adapter = toolsNameRVAdapter
        binding.farmingLifeToolsDetailToolRv.layoutManager = LinearLayoutManager(applicationContext)

        settingDetailToolbar()
        settingHeartBtn()

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

    fun settingHeartBtn() {
        binding.farmingLifeToolsDetailHeartBtn.setOnClickListener {
            if (isHeartBtn) {
                binding.farmingLifeToolsDetailHeartBtn.setImageResource(R.drawable.heart_01)
            }
            else {
                binding.farmingLifeToolsDetailHeartBtn.setImageResource(R.drawable.heart_02)
            }
            // 상태 변경
            isHeartBtn = !isHeartBtn
        }
    }
}