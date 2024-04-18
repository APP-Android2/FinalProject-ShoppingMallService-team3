package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.CommunityAddActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabSocialBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabInformationBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabSocialBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel

class CommunityTabSocialFragment : Fragment() {
    lateinit var fragmentCommunityTabSocialBinding: FragmentCommunityTabSocialBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabSocialBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_social, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabSocialBinding.communityViewModel = communityViewModel
        fragmentCommunityTabSocialBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingButtonCommunityTabSocialPopularity()
        settingRecyclerViewCommunityTabSocial()
        settingFloatingActionButtonCommunitySocialAdd()

        return fragmentCommunityTabSocialBinding.root
    }

    // 커뮤니티 소통 탭
    fun settingButtonCommunityTabSocialPopularity() {
        fragmentCommunityTabSocialBinding.apply {
            buttonCommunityTabSocialPopularity.isChecked = true
        }
    }

    // 커뮤니티 소통 탭 게시글 작성
    fun settingFloatingActionButtonCommunitySocialAdd() {
        fragmentCommunityTabSocialBinding.apply {
            floatingActionButtonCommunitySocialAdd.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityAddActivity::class.java)
                startActivity(communityIntent)
            }
        }
    }

    // 커뮤니티 탭 소통 리사이클러뷰 설정
    fun settingRecyclerViewCommunityTabSocial() {
        fragmentCommunityTabSocialBinding.apply {
            recyclerViewCommunityTabSocial.apply {
                adapter = CommunityTabSocialRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 커뮤니티 탭 소통 리사이클러뷰 어댑터 설정
    inner class CommunityTabSocialRecyclerViewAdapter : RecyclerView.Adapter<CommunityTabSocialRecyclerViewAdapter.CommunityTabSocialViewHolder>() {
        inner class CommunityTabSocialViewHolder(rowCommunityTabSocialBinding: RowCommunityTabSocialBinding) : RecyclerView.ViewHolder(rowCommunityTabSocialBinding.root) {
            val rowCommunityTabSocialBinding: RowCommunityTabSocialBinding

            init {
                this.rowCommunityTabSocialBinding = rowCommunityTabSocialBinding

                this.rowCommunityTabSocialBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityTabSocialViewHolder {
            val rowCommunityTabSocialBinding = DataBindingUtil.inflate<RowCommunityTabSocialBinding>(layoutInflater, R.layout.row_community_tab_social, parent,false)
            val communityViewModel = CommunityViewModel()
            rowCommunityTabSocialBinding.communityViewModel = communityViewModel
            rowCommunityTabSocialBinding.lifecycleOwner = this@CommunityTabSocialFragment

            val communityTabSocialViewHolder = CommunityTabSocialViewHolder(rowCommunityTabSocialBinding)

            return communityTabSocialViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: CommunityTabSocialViewHolder, position: Int) {
            holder.rowCommunityTabSocialBinding.communityViewModel?.textViewCommunityListLabelSocial?.value = "소통"
            holder.rowCommunityTabSocialBinding.communityViewModel?.textViewCommunityListTitleSocial?.value = "글 제목 $position"
            holder.rowCommunityTabSocialBinding.communityViewModel?.textViewCommunityListContentSocial?.value = "글 내용입니다 글 내용입니다 글 내용입니다\n" +
                    "글 내용입니다"
            holder.rowCommunityTabSocialBinding.communityViewModel?.textViewCommunityListViewCntSocial?.value = "999+"
            holder.rowCommunityTabSocialBinding.communityViewModel?.textViewCommunityListCommentCntSocial?.value = "999+"
            holder.rowCommunityTabSocialBinding.communityViewModel?.textViewCommunityListDateSocial?.value = "2024.03.01"
            holder.rowCommunityTabSocialBinding.communityViewModel?.textViewCommunityListLikeCntSocial?.value = "999+"

            holder.rowCommunityTabSocialBinding.linearLayoutCommunityListSocial.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                startActivity(communityIntent)
            }

            holder.rowCommunityTabSocialBinding.apply {
                imageViewCommunityListLikeSocial.setOnClickListener {
                    imageViewCommunityListLikeSocial.isSelected = !imageViewCommunityListLikeSocial.isSelected
                    textViewCommunityListLikeCntSocial.isSelected = !textViewCommunityListLikeCntSocial.isSelected
                }
            }

            if (position == 9) {
                fragmentCommunityTabSocialBinding.floatingActionButtonCommunitySocialAdd.isVisible = false
            } else {
                fragmentCommunityTabSocialBinding.floatingActionButtonCommunitySocialAdd.isVisible = true
            }
        }
    }
}