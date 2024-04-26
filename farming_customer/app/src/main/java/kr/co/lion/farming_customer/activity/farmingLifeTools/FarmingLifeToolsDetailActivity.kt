package kr.co.lion.farming_customer.activity.farmingLifeTools

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.farmingLifeTools.adapter.ToolsNameRVAdapter
import kr.co.lion.farming_customer.dao.farmingLifeTools.RentalDao
import kr.co.lion.farming_customer.databinding.ActivityFarmingLifeToolsDetailBinding
import kr.co.lion.farming_customer.model.farminLifeTools.RentalModel
import kr.co.lion.farming_customer.viewmodel.farminLifeTools.FarmingLifeToolsDetailViewModel

class FarmingLifeToolsDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityFarmingLifeToolsDetailBinding
    lateinit var viewModel: FarmingLifeToolsDetailViewModel
    lateinit var toolsNameRVAdapter: ToolsNameRVAdapter
    private var isHeartBtn : Boolean = true

    var rentalModel : RentalModel? = null
    var rentalList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_farming_life_tools_detail)
        viewModel = FarmingLifeToolsDetailViewModel()
        binding.farmingLifeToolsDetailViewModel = viewModel
        binding.lifecycleOwner = this

        settingInitData()
        settingDetailToolbar()
        settingHeartBtn()
        settingCallBtn()
    }

    private fun settingCallBtn() {
        binding.farmingLifeToolsDetailCallBtn.setOnClickListener {
            // 전화 걸기 화면으로 이동
            val phoneNumber = binding.farmingLifeToolsDetailViewModel!!.farming_life_tools_detail_phone_tv.value // 전화번호
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            val idx = intent.getStringExtra("rentalIdx")
            if (idx != null) {
                rentalModel = RentalDao.selectRentalData(idx)
                rentalModel?.rental_machines?.forEach {
                    rentalList.add(it.values.first())
                }
                settingInitView()

                toolsNameRVAdapter = ToolsNameRVAdapter(rentalList)
                binding.farmingLifeToolsDetailToolRv.adapter = toolsNameRVAdapter
                binding.farmingLifeToolsDetailToolRv.layoutManager = LinearLayoutManager(applicationContext)
            }
        }
    }

    private fun settingInitView() {
        viewModel.apply {
            farming_life_tools_detail_rental_tv.value = rentalModel?.rental_name
            farming_life_tools_detail_phone_tv.value = rentalModel?.rental_phone
            farming_life_tools_detail_address_tv.value = rentalModel?.rental_address
            farming_life_tools_detail_explain_title.value = "총 농기계 보유 갯수 : ${rentalModel?.rental_machines?.size}개"
        }
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