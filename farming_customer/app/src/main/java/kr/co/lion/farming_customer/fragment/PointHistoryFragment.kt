package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.DialogYes
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.PointActivity
import kr.co.lion.farming_customer.databinding.FragmentPointHistoryBinding
import kr.co.lion.farming_customer.databinding.RowPointHistoryBinding
import kr.co.lion.farming_customer.viewmodel.MyPagePointViewModel

class PointHistoryFragment : Fragment() {
    lateinit var fragmentPointHistoryBinding: FragmentPointHistoryBinding
    lateinit var pointActivity: PointActivity
    lateinit var myPagePointViewModel: MyPagePointViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentPointHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_point_history, container, false)
        myPagePointViewModel = MyPagePointViewModel()
        fragmentPointHistoryBinding.myPagePointViewModel = myPagePointViewModel
        fragmentPointHistoryBinding.lifecycleOwner = this

        pointActivity = activity as PointActivity

        settingToolbar()
        settingImageViewPointHistoryCaution()
        settingTextViewPointHistoryRemainPoint()
        settingRecyclerViewPointHistory()

        return fragmentPointHistoryBinding.root
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

    // 현재 남은포인트 양
    fun settingTextViewPointHistoryRemainPoint() {
        myPagePointViewModel.textViewPointHistoryRemainPoint.value = "1000P"
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
            return 100
        }

        override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
            holder.rowPointHistoryBinding.myPagePointViewModel?.textViewRowPointHistoryDate?.value = "2024.03.01"
            holder.rowPointHistoryBinding.myPagePointViewModel?.textViewRowPointHistoryName?.value = "농산품 이름 $position"
            holder.rowPointHistoryBinding.myPagePointViewModel?.textViewRowPointHistoryNumber?.value = "+100P"
        }
    }


}