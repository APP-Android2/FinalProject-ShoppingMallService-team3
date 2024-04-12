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
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabJobBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabJobBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabSocialBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel

class CommunityTabJobFragment : Fragment() {
    lateinit var fragmentCommunityTabJobBinding: FragmentCommunityTabJobBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabJobBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_job, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabJobBinding.communityViewModel = communityViewModel
        fragmentCommunityTabJobBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingButtonCommunityTabJobPopularity()
        settingRecyclerViewCommunityTabJob()

        return fragmentCommunityTabJobBinding.root
    }

    // 커뮤니티 구인구직 탭
    fun settingButtonCommunityTabJobPopularity() {
        fragmentCommunityTabJobBinding.apply {
            buttonCommunityTabJobPopularity.isChecked = true

        }
    }

    // 커뮤니티 탭 소통 리사이클러뷰 설정
    fun settingRecyclerViewCommunityTabJob() {
        fragmentCommunityTabJobBinding.apply {
            recyclerViewCommunityTabJob.apply {
                adapter = CommunityTabJoblRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 커뮤니티 탭 소통 리사이클러뷰 어댑터 설정
    inner class CommunityTabJoblRecyclerViewAdapter : RecyclerView.Adapter<CommunityTabJoblRecyclerViewAdapter.CommunityTabJobViewHolder>() {
        inner class CommunityTabJobViewHolder(rowCommunityTabJobBinding: RowCommunityTabJobBinding) : RecyclerView.ViewHolder(rowCommunityTabJobBinding.root) {
            val rowCommunityTabJobBinding: RowCommunityTabJobBinding

            init {
                this.rowCommunityTabJobBinding = rowCommunityTabJobBinding

                this.rowCommunityTabJobBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityTabJobViewHolder {
            val rowCommunityTabJobBinding = DataBindingUtil.inflate<RowCommunityTabJobBinding>(layoutInflater, R.layout.row_community_tab_job, parent,false)
            val communityViewModel = CommunityViewModel()
            rowCommunityTabJobBinding.communityViewModel = communityViewModel
            rowCommunityTabJobBinding.lifecycleOwner = this@CommunityTabJobFragment

            val communityTabJobViewHolder = CommunityTabJobViewHolder(rowCommunityTabJobBinding)

            return communityTabJobViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CommunityTabJobViewHolder, position: Int) {
            holder.rowCommunityTabJobBinding.communityViewModel?.textViewCommunityListLabelJob?.value = "구인구직"
            holder.rowCommunityTabJobBinding.communityViewModel?.textViewCommunityListTitleJob?.value = "글 제목"
            holder.rowCommunityTabJobBinding.communityViewModel?.textViewCommunityListContentJob?.value = "글 내용입니다 글 내용입니다 글 내용입니다\n" +
                    "글 내용입니다"
            holder.rowCommunityTabJobBinding.communityViewModel?.textViewCommunityListViewCntJob?.value = "999+"
            holder.rowCommunityTabJobBinding.communityViewModel?.textViewCommunityListCommentCntJob?.value = "999+"
            holder.rowCommunityTabJobBinding.communityViewModel?.textViewCommunityListDateJob?.value = "2024.03.01"
            holder.rowCommunityTabJobBinding.communityViewModel?.textViewCommunityListLikeCntJob?.value = "999+"

            holder.rowCommunityTabJobBinding.linearLayoutCommunityListJob.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                startActivity(communityIntent)
            }

            holder.rowCommunityTabJobBinding.apply {
                imageViewCommunityListLikeJob.setOnClickListener {
                    imageViewCommunityListLikeJob.isSelected = !imageViewCommunityListLikeJob.isSelected
                    textViewCommunityListLikeCntJob.isSelected = !textViewCommunityListLikeCntJob.isSelected
                }
            }
        }
    }
}