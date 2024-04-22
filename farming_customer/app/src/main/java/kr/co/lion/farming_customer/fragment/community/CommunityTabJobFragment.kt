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
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabJobBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabJobBinding
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel

class CommunityTabJobFragment : Fragment() {
    lateinit var fragmentCommunityTabJobBinding: FragmentCommunityTabJobBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    var allList = mutableListOf<CommunityModel>()
    // 구인구직 탭의 리사이클러뷰 구성을 위한 리스트
    var jobList = mutableListOf<CommunityModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabJobBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_job, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabJobBinding.communityViewModel = communityViewModel
        fragmentCommunityTabJobBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingButtonCommunityTabJobPopularity()
        settingRecyclerViewCommunityTabJob()
        settingFloatingActionButtonCommunityJobAdd()

        return fragmentCommunityTabJobBinding.root
    }

    override fun onResume() {
        super.onResume()

        settingList()
    }

    // 커뮤니티 구인구직 탭
    fun settingButtonCommunityTabJobPopularity() {
        fragmentCommunityTabJobBinding.apply {
            buttonCommunityTabJobPopularity.isChecked = true

        }
    }

    // 리스트에 받아오기
    fun settingList() {
        CoroutineScope(Dispatchers.Main).launch {
            allList = CommunityPostDao.gettingCommunityPostList()

            allList.forEach {
                when(it.postType) {
                    "구인구직" -> jobList.add(it)
                }
            }

            fragmentCommunityTabJobBinding.recyclerViewCommunityTabJob.adapter?.notifyDataSetChanged()
        }
    }

    // 커뮤니티 구인구직 탭 게시글 작성
    fun settingFloatingActionButtonCommunityJobAdd() {
        fragmentCommunityTabJobBinding.apply {
            floatingActionButtonCommunityJobAdd.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityAddActivity::class.java)
                startActivity(communityIntent)

            }
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
            return jobList!!.size
        }

        override fun onBindViewHolder(holder: CommunityTabJobViewHolder, position: Int) {

            holder.rowCommunityTabJobBinding.apply {
                communityViewModel?.textViewCommunityListLabelJob?.value = jobList[position].postType
                communityViewModel?.textViewCommunityListTitleJob?.value = jobList[position].postTitle
                communityViewModel?.textViewCommunityListContentJob?.value = jobList[position].postContent
                communityViewModel?.textViewCommunityListViewCntJob?.value = jobList[position].postViewCnt.toString()
                communityViewModel?.textViewCommunityListCommentCntJob?.value = jobList[position].postCommentCnt.toString()
                communityViewModel?.textViewCommunityListLikeCntJob?.value = jobList[position].postLikeCnt.toString()
                communityViewModel?.textViewCommunityListDateJob?.value = jobList[position].postRegDt

                CoroutineScope(Dispatchers.Main).launch {
                    if (jobList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, jobList[position].postImages!![0], imageViewCommunityListJob)
                    } else {
                        holder.rowCommunityTabJobBinding.imageViewCommunityListJob.visibility = View.GONE
                    }
                }

                linearLayoutCommunityListJob.setOnClickListener {
                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", jobList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeJob.setOnClickListener {
                    imageViewCommunityListLikeJob.isSelected = !imageViewCommunityListLikeJob.isSelected
                    textViewCommunityListLikeCntJob.isSelected = !textViewCommunityListLikeCntJob.isSelected
                }
            }

            if (position == jobList.size) {
                fragmentCommunityTabJobBinding.floatingActionButtonCommunityJobAdd.isVisible = false
            } else {
                fragmentCommunityTabJobBinding.floatingActionButtonCommunityJobAdd.isVisible = true
            }
        }
    }
}