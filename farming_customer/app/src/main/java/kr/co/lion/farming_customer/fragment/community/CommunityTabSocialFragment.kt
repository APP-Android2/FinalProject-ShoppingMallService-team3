package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.os.Build
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.community.CommunityAddActivity
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabSocialBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabSocialBinding
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel

class CommunityTabSocialFragment : Fragment() {
    lateinit var fragmentCommunityTabSocialBinding: FragmentCommunityTabSocialBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    var allList = mutableListOf<CommunityModel>()
    // 소통 탭의 리사이클러뷰 구성을 위한 리스트
    var socialList = mutableListOf<CommunityModel>()

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

    override fun onResume() {
        super.onResume()

        settingList()
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

    // 리스트에 받아오기
    fun settingList() {
        CoroutineScope(Dispatchers.Main).launch {
            allList = CommunityPostDao.gettingCommunityPostList()

            allList.forEach {
                when(it.postType) {
                    "소통 게시판" -> socialList.add(it)
                }
            }

            fragmentCommunityTabSocialBinding.recyclerViewCommunityTabSocial.adapter?.notifyDataSetChanged()
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
            return socialList!!.size
        }

        override fun onBindViewHolder(holder: CommunityTabSocialViewHolder, position: Int) {

            holder.rowCommunityTabSocialBinding.apply {
                communityViewModel?.textViewCommunityListLabelSocial?.value = socialList[position].postType
                communityViewModel?.textViewCommunityListTitleSocial?.value = socialList[position].postTitle
                communityViewModel?.textViewCommunityListContentSocial?.value = socialList[position].postContent
                communityViewModel?.textViewCommunityListViewCntSocial?.value = socialList[position].postViewCnt.toString()
                communityViewModel?.textViewCommunityListCommentCntSocial?.value = socialList[position].postCommentCnt.toString()
                communityViewModel?.textViewCommunityListLikeCntSocial?.value = socialList[position].postLikeCnt.toString()
                communityViewModel?.textViewCommunityListDateSocial?.value = socialList[position].postRegDt

                CoroutineScope(Dispatchers.Main).launch {
                    if (socialList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, socialList[position].postImages!![0], imageViewCommunityListSocial)
                    } else {
                        holder.rowCommunityTabSocialBinding.imageViewCommunityListSocial.visibility = View.GONE
                    }
                }

                linearLayoutCommunityListSocial.setOnClickListener {
                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", socialList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeSocial.setOnClickListener {
                    imageViewCommunityListLikeSocial.isSelected = !imageViewCommunityListLikeSocial.isSelected
                    textViewCommunityListLikeCntSocial.isSelected = !textViewCommunityListLikeCntSocial.isSelected
                }
            }

            if (position == socialList.size) {
                fragmentCommunityTabSocialBinding.floatingActionButtonCommunitySocialAdd.isVisible = false
            } else {
                fragmentCommunityTabSocialBinding.floatingActionButtonCommunitySocialAdd.isVisible = true
            }
        }
    }
}