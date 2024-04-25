package kr.co.lion.farming_customer.fragment.point

import android.content.Context
import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYes
import kr.co.lion.farming_customer.PointType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.point.PointActivity
import kr.co.lion.farming_customer.dao.loginRegister.UserDao
import kr.co.lion.farming_customer.dao.myPagePoint.myPagePointDao
import kr.co.lion.farming_customer.databinding.FragmentPointHistoryBinding
import kr.co.lion.farming_customer.databinding.RowPointHistoryBinding
import kr.co.lion.farming_customer.model.myPagePoint.PointModel
import kr.co.lion.farming_customer.model.user.UserModel
import kr.co.lion.farming_customer.viewmodel.point.MyPagePointViewModel

class PointHistoryFragment : Fragment() {
    lateinit var fragmentPointHistoryBinding: FragmentPointHistoryBinding
    lateinit var pointActivity: PointActivity
    lateinit var myPagePointViewModel: MyPagePointViewModel


    var pointList = mutableListOf<PointModel>()
    var pointList_all = mutableListOf<PointModel>()
    var pointList_save = mutableListOf<PointModel>()
    var pointList_use = mutableListOf<PointModel>()
    var pointList_extinction = mutableListOf<PointModel>()
    var userModel : UserModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentPointHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_point_history, container, false)
        myPagePointViewModel = MyPagePointViewModel()
        fragmentPointHistoryBinding.myPagePointViewModel = myPagePointViewModel
        fragmentPointHistoryBinding.lifecycleOwner = this

        pointActivity = activity as PointActivity

        settingUserData()
        settingToolbar()
        settingImageViewPointHistoryCaution()
        settingToggleButton()


        return fragmentPointHistoryBinding.root
    }

    private fun settingUserData() {
        val sharedPreferences = pointActivity.getSharedPreferences("AutoLogin",
            Context.MODE_PRIVATE)
        val userIdx = sharedPreferences.getInt("loginUserIdx", -1)

        CoroutineScope(Dispatchers.Main).launch {
            userModel = UserDao.gettingUserInfoByUserIdx(userIdx)
            settingInitData()
        }
    }

    private fun settingToggleButton() {
        fragmentPointHistoryBinding.materialButtonToggleGroupWithRadius.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                when(checkedId){
                    R.id.buttonPointHistoryAll -> {
                        pointList = pointList_all
                        fragmentPointHistoryBinding.recyclerViewPointHistory.adapter?.notifyDataSetChanged()
                    }
                    R.id.buttonPointHistoryAccumulate -> {
                        pointList = pointList_save
                        fragmentPointHistoryBinding.recyclerViewPointHistory.adapter?.notifyDataSetChanged()
                    }
                    R.id.buttonPointHistoryUse -> {
                        pointList = pointList_use
                        fragmentPointHistoryBinding.recyclerViewPointHistory.adapter?.notifyDataSetChanged()
                    }
                    R.id.buttonPointHistoryDelete -> {
                        pointList = pointList_extinction
                        fragmentPointHistoryBinding.recyclerViewPointHistory.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            pointList_all = myPagePointDao.gettingPointList(userModel!!.user_idx)
            var remain_point = 0
            pointList_all.forEach {
                if(it.point_type == PointType.POINT_TYPE_SAVE.number){
                    pointList_save.add(it)
                }else if(it.point_type == PointType.POINT_TYPE_USE.number){
                    pointList_use.add(it)
                }else{
                    pointList_extinction.add(it)
                }
                remain_point += it.point_changed
            }

            myPagePointViewModel.textViewPointHistoryRemainPoint.value = "${DecimalFormat("#,###").format(remain_point)}P"
            pointList = pointList_all
            settingButtonPointHistoryAll()
            settingRecyclerViewPointHistory()
        }
    }

    // 툴바 설정
    fun settingToolbar() {
        fragmentPointHistoryBinding.apply {
            toolbarPoint.apply {
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    pointActivity.finish()
                }
            }
        }
    }

    // 포인트 주의사항
    fun settingImageViewPointHistoryCaution() {
        fragmentPointHistoryBinding.apply {
            imageViewPointHistoryCaution.setOnClickListener {
                val dialog = DialogYes("포인트 이용안내", "포인트 사용 기간은 적입일 이후 90일 까지이며,\n" +
                        "90일 이후 잔여 포인트는 자동 소멸 됩니다.", "*포인트를 사용한 상품을 포인트 기간 내에 취소하시면\n" +
                        "환급되오나, 기간이 지난 이후 취소시 환급되지 않습니다.", pointActivity)
                dialog.show(this@PointHistoryFragment?.parentFragmentManager!!, "DialogYes")
            }
        }
    }

    // 포인트 탭
    fun settingButtonPointHistoryAll() {
        fragmentPointHistoryBinding.apply {
            buttonPointHistoryAll.isChecked = true
        }
    }

    // 포인트 리사이클러뷰 설정
    fun settingRecyclerViewPointHistory() {
        fragmentPointHistoryBinding.apply {
            recyclerViewPointHistory.apply {
                adapter = PointHistoryRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(pointActivity)
                val deco = MaterialDividerItemDecoration(pointActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 포인트 리사이클러뷰 어댑터 설정
    inner class PointHistoryRecyclerViewAdapter : RecyclerView.Adapter<PointHistoryRecyclerViewAdapter.PointHistoryViewHolder>() {
        inner class PointHistoryViewHolder(rowPointHistoryBinding: RowPointHistoryBinding) : RecyclerView.ViewHolder(rowPointHistoryBinding.root) {
            val rowPointHistoryBinding:RowPointHistoryBinding

            init {
                this.rowPointHistoryBinding = rowPointHistoryBinding

                this.rowPointHistoryBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHistoryViewHolder {
            // val rowPointHistoryBinding = RowPointHistoryBinding.inflate(layoutInflater)
            val rowPointHistoryBinding = DataBindingUtil.inflate<RowPointHistoryBinding>(layoutInflater,R.layout.row_point_history, parent, false)
            val myPagePointViewModel = MyPagePointViewModel()
            rowPointHistoryBinding.myPagePointViewModel = myPagePointViewModel
            rowPointHistoryBinding.lifecycleOwner = this@PointHistoryFragment

            val pointHistoryViewHolder = PointHistoryViewHolder(rowPointHistoryBinding)

            return pointHistoryViewHolder
        }

        override fun getItemCount(): Int {
            return pointList.size
        }

        override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
            holder.rowPointHistoryBinding.apply {
                myPagePointViewModel!!.apply {
                    textViewRowPointHistoryDate.value = pointList[position].point_date
                    textViewRowPointHistoryName.value = pointList[position].point_reason
                    textViewRowPointHistoryNumber.value = "${pointList[position].point_changed}P"
                }
                if(pointList[position].point_type == PointType.POINT_TYPE_SAVE.number){
                    // 적립
                    imageViewRowPointHistory.setImageResource(R.drawable.point_save)
                    textViewRowPointHistoryNumber.setTextColor(resources.getColor(R.color.green_main))
                }else if(pointList[position].point_type == PointType.POINT_TYPE_USE.number){
                    // 사용
                    imageViewRowPointHistory.setImageResource(R.drawable.point_use)
                    textViewRowPointHistoryNumber.setTextColor(resources.getColor(R.color.orange_01))
                }else{
                    // 소멸
                    imageViewRowPointHistory.setImageResource(R.drawable.point_gone)
                    textViewRowPointHistoryNumber.setTextColor(resources.getColor(R.color.orange_01))
                }
            }



        }
    }


}