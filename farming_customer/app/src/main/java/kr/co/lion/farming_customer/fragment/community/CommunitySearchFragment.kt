package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.CommunitySearchActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunitySearchBinding
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowCommunitySearchBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabAllBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel

class CommunitySearchFragment : Fragment() {
    lateinit var fragmentCommunitySearchBinding: FragmentCommunitySearchBinding
    lateinit var communitySearchActivity: CommunitySearchActivity
    lateinit var communityViewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunitySearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_search, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunitySearchBinding.communityViewModel = communityViewModel
        fragmentCommunitySearchBinding.lifecycleOwner = this

        communitySearchActivity = activity as CommunitySearchActivity

        settingRecyclerViewCommunitySearch()

        return fragmentCommunitySearchBinding.root
    }

    // 커뮤니티 검색 리사이클러뷰 설정
    fun settingRecyclerViewCommunitySearch() {
        fragmentCommunitySearchBinding.apply {
            recyclerViewCommunitySearch.apply {
                adapter = CommunitySearchRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(communitySearchActivity)
                val deco = MaterialDividerItemDecoration(communitySearchActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 커뮤니티 검색 리사이클러뷰 어댑터 설정
    inner class CommunitySearchRecyclerViewAdapter : RecyclerView.Adapter<CommunitySearchRecyclerViewAdapter.CommunitySearchViewHolder>() {
        inner class CommunitySearchViewHolder(rowCommunitySearchBinding: RowCommunitySearchBinding) : RecyclerView.ViewHolder(rowCommunitySearchBinding.root) {
            val rowCommunitySearchBinding: RowCommunitySearchBinding

            init {
                this.rowCommunitySearchBinding = rowCommunitySearchBinding

                this.rowCommunitySearchBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunitySearchViewHolder {
            val rowCommunitySearchBinding = DataBindingUtil.inflate<RowCommunitySearchBinding>(layoutInflater, R.layout.row_community_search, parent,false)
            val communityViewModel = CommunityViewModel()
            rowCommunitySearchBinding.communityViewModel = communityViewModel
            rowCommunitySearchBinding.lifecycleOwner = this@CommunitySearchFragment

            val communitySearchViewHolder = CommunitySearchViewHolder(rowCommunitySearchBinding)

            return communitySearchViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CommunitySearchViewHolder, position: Int) {
            holder.rowCommunitySearchBinding.communityViewModel?.textViewCommunityListLabelSearch?.value = "정보"
            holder.rowCommunitySearchBinding.communityViewModel?.textViewCommunityListTitleSearch?.value = "글 제목"
            holder.rowCommunitySearchBinding.communityViewModel?.textViewCommunityListContentSearch?.value = "글 내용입니다 글 내용입니다 글 내용입니다\n" +
                    "글 내용입니다"
            holder.rowCommunitySearchBinding.communityViewModel?.textViewCommunityListViewCntSearch?.value = "999+"
            holder.rowCommunitySearchBinding.communityViewModel?.textViewCommunityListCommentCntSearch?.value = "999+"
            holder.rowCommunitySearchBinding.communityViewModel?.textViewCommunityListDateSearch?.value = "2024.03.01"
            holder.rowCommunitySearchBinding.communityViewModel?.textViewCommunityListLikeCntSearch?.value = "999+"

            holder.rowCommunitySearchBinding.linearLayoutCommunityListSearch.setOnClickListener {
                val communityIntent = Intent(communitySearchActivity, CommunityActivity::class.java)
                startActivity(communityIntent)
            }

            holder.rowCommunitySearchBinding.apply {
                imageViewCommunityListLikeSearch.setOnClickListener {
                    imageViewCommunityListLikeSearch.isSelected = !imageViewCommunityListLikeSearch.isSelected
                    textViewCommunityListLikeCntSearch.isSelected = !textViewCommunityListLikeCntSearch.isSelected
                }
            }

        }
    }
}