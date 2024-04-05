package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentLikeBinding
import kr.co.lion.farming_customer.databinding.RowLikeBinding
import kr.co.lion.farming_customer.viewmodel.LikeViewModel

class LikeFragment : Fragment() {
    lateinit var fragmentLikeBinding: FragmentLikeBinding
    lateinit var mainActivity: MainActivity

    lateinit var likeViewModel : LikeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLikeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_like, container, false)
        likeViewModel = LikeViewModel()
        fragmentLikeBinding.likeViewModel = likeViewModel
        fragmentLikeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        settingRecyclerViewLike()

        return fragmentLikeBinding.root
    }

    // recyclerView 설정
    fun settingRecyclerViewLike(){
        fragmentLikeBinding.apply {
            recyclerViewLike.apply {
                adapter = LikeRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity,2)
            }
        }
    }

    // recyclerView의 Adapter
    inner class LikeRecyclerViewAdapter:RecyclerView.Adapter<LikeRecyclerViewAdapter.LikeViewHolder>(){
        inner class LikeViewHolder(rowLikeBinding:RowLikeBinding):RecyclerView.ViewHolder(rowLikeBinding.root){
            val rowLikeBinding:RowLikeBinding

            init {
                this.rowLikeBinding = rowLikeBinding
                this.rowLikeBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
            val rowLikeBinding = RowLikeBinding.inflate(layoutInflater)
            val likeViewHolder = LikeViewHolder(rowLikeBinding)
            return likeViewHolder
        }

        override fun getItemCount(): Int {
            return 9
        }

        override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {

        }
    }

}