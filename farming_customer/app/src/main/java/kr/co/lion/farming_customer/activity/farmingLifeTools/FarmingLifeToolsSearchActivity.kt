package kr.co.lion.farming_customer.activity.farmingLifeTools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.lion.farming_customer.activity.farmingLifeTools.adapter.ToolsSearchRVAdapter
import kr.co.lion.farming_customer.databinding.ActivityFarmingLifeToolsSearchBinding

class FarmingLifeToolsSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityFarmingLifeToolsSearchBinding
    lateinit var toolsSearchRVAdapter: ToolsSearchRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmingLifeToolsSearchBinding.inflate(layoutInflater)

        toolsSearchRVAdapter = ToolsSearchRVAdapter(this)
        binding.farmingLifeToolsSearchRv.adapter = toolsSearchRVAdapter
        binding.farmingLifeToolsSearchRv.layoutManager = LinearLayoutManager(applicationContext)

        settingEvent()

        setContentView(binding.root)
    }

    private fun settingEvent() {
        binding.farmingLifeToolsSearchLayout.setStartIconOnClickListener {
            finish()
        }
    }
}