package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowPointHistoryBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel
import kr.co.lion.farming_customer.viewmodel.FarminglLifeViewModel
import kr.co.lion.farming_customer.viewmodel.point.MyPagePointViewModel


class CommunityTabAllFragment : Fragment() {
    lateinit var fragmentCommunityTabAllBinding: FragmentCommunityTabAllBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabAllBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_all, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabAllBinding.communityViewModel = communityViewModel
        fragmentCommunityTabAllBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingButtonCommunityTabAllPopularity()
        settingRecyclerViewCommunityTabAll()

        return fragmentCommunityTabAllBinding.root
    }

    // 커뮤니티 전체 탭
    fun settingButtonCommunityTabAllPopularity() {
        fragmentCommunityTabAllBinding.apply {
            buttonCommunityTabAllPopularity.isChecked = true

        }
    }

    // 커뮤니티 탭 전체 리사이클러뷰 설정
    fun settingRecyclerViewCommunityTabAll() {
        fragmentCommunityTabAllBinding.apply {
            recyclerViewCommunityTabAll.apply {
                adapter = CommunityTabAllRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 커뮤니티 탭 전체 리사이클러뷰 어댑터 설정
    inner class CommunityTabAllRecyclerViewAdapter : RecyclerView.Adapter<CommunityTabAllRecyclerViewAdapter.CommunityTabAllViewHolder>() {
        inner class CommunityTabAllViewHolder(rowCommunityTabAllBinding: RowCommunityTabAllBinding) : RecyclerView.ViewHolder(rowCommunityTabAllBinding.root) {
            val rowCommunityTabAllBinding: RowCommunityTabAllBinding

            init {
                this.rowCommunityTabAllBinding = rowCommunityTabAllBinding

                this.rowCommunityTabAllBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityTabAllViewHolder {
            val rowCommunityTabAllBinding = DataBindingUtil.inflate<RowCommunityTabAllBinding>(layoutInflater, R.layout.row_community_tab_all, parent,false)
            val communityViewModel = CommunityViewModel()
            rowCommunityTabAllBinding.communityViewModel = communityViewModel
            rowCommunityTabAllBinding.lifecycleOwner = this@CommunityTabAllFragment

            val communityTabAllViewHolder = CommunityTabAllViewHolder(rowCommunityTabAllBinding)

            return communityTabAllViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CommunityTabAllViewHolder, position: Int) {
            holder.rowCommunityTabAllBinding.communityViewModel?.textViewCommunityListLabelAll?.value = "정보"
            holder.rowCommunityTabAllBinding.communityViewModel?.textViewCommunityListTitleAll?.value = "글 제목"
            holder.rowCommunityTabAllBinding.communityViewModel?.textViewCommunityListContentAll?.value = "글 내용입니다 글 내용입니다 글 내용입니다\n" +
                    "글 내용입니다"
            holder.rowCommunityTabAllBinding.communityViewModel?.textViewCommunityListViewCntAll?.value = "999+"
            holder.rowCommunityTabAllBinding.communityViewModel?.textViewCommunityListCommentCntAll?.value = "999+"
            holder.rowCommunityTabAllBinding.communityViewModel?.textViewCommunityListDateAll?.value = "2024.03.01"
            holder.rowCommunityTabAllBinding.communityViewModel?.textViewCommunityListLikeCntAll?.value = "999+"

            holder.rowCommunityTabAllBinding.linearLayoutCommunityListAll.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                startActivity(communityIntent)
            }

            holder.rowCommunityTabAllBinding.apply {
                imageViewCommunityListLikeAll.setOnClickListener {
                    imageViewCommunityListLikeAll.isSelected = !imageViewCommunityListLikeAll.isSelected
                    textViewCommunityListLikeCntAll.isSelected = !textViewCommunityListLikeCntAll.isSelected
                }
            }

        }
    }
}