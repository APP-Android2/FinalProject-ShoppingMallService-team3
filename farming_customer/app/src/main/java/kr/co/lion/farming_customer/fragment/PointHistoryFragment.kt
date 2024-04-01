package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.DialogYes
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.PointFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
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
                setNavigationIcon(R.drawable.ic_launcher_background)
                setNavigationOnClickListener {
                    pointActivity.removeFragment(PointFragmentName.POINT_HISTORY_FRAGMENT)
                }
            }
        }
    }

    // 포인트 주의사항
    fun settingImageViewPointHistoryCaution() {
        fragmentPointHistoryBinding.apply {
            imageViewPointHistoryCaution.setOnClickListener {
                val dialog = DialogYes()
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
            val rowPointHistoryBinding = RowPointHistoryBinding.inflate(layoutInflater)
            val pointHistoryViewHolder = PointHistoryViewHolder(rowPointHistoryBinding)

            return pointHistoryViewHolder
        }

        override fun getItemCount(): Int {
            return 20
        }

        override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
            holder.rowPointHistoryBinding.textViewRowPointHistoryDate.text = "2024.03.01"
            holder.rowPointHistoryBinding.textViewRowPointHistoryName.text = "농산품 이름 $position"
            holder.rowPointHistoryBinding.textViewRowPointHistoryNumber.text = "+100P"
        }
    }


}