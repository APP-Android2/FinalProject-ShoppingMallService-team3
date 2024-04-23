package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.graphics.Color
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
import kr.co.lion.farming_customer.dao.CommunityCommentDao
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabJobBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabJobBinding
import kr.co.lion.farming_customer.model.CommunityCommentModel
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel

class CommunityTabJobFragment : Fragment() {
    lateinit var fragmentCommunityTabJobBinding: FragmentCommunityTabJobBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    var allList = mutableListOf<CommunityModel>()
    // 구인구직 탭의 리사이클러뷰 구성을 위한 리스트
    var jobList = mutableListOf<CommunityModel>()
    // 댓글 정보를 가지고 있는 리스트
    var commentList = mutableListOf<CommunityCommentModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabJobBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_job, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabJobBinding.communityViewModel = communityViewModel
        fragmentCommunityTabJobBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingToggleButtonCommunityTabJob()
        settingRecyclerViewCommunityTabJob()
        settingFloatingActionButtonCommunityJobAdd()
        settingFloatingButton()

        return fragmentCommunityTabJobBinding.root
    }

    override fun onResume() {
        super.onResume()

        settingToggleButtonCommunityTabJob()
    }

    // 커뮤니티 구인구직 탭의 정렬 토글
    fun settingToggleButtonCommunityTabJob() {
        fragmentCommunityTabJobBinding.apply {
            buttonCommunityTabJobPopularity.isChecked = true
            handleToggleSelected("인기순")

            buttonCommunityTabJobPopularity.setOnClickListener {
                buttonCommunityTabJobPopularity.isChecked = true
                handleToggleSelected("인기순")
            }
            buttonCommunityTabJobRecent.setOnClickListener {
                buttonCommunityTabJobRecent.isChecked = true
                handleToggleSelected("최신순")
            }
            buttonCommunityTabJobLook.setOnClickListener {
                buttonCommunityTabJobLook.isChecked = true
                handleToggleSelected("조회순")
            }
        }
    }

    // 정렬 토글 눌렀을 때
    fun handleToggleSelected(toggleType:String) {
        when(toggleType) {
            "인기순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("인기순")
                    jobList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "구인구직" -> jobList.add(it)
                        }
                    }
                    fragmentCommunityTabJobBinding.recyclerViewCommunityTabJob.adapter?.notifyDataSetChanged()
                }
            }
            "최신순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("최신순")
                    jobList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "구인구직" -> jobList.add(it)
                        }
                    }
                    fragmentCommunityTabJobBinding.recyclerViewCommunityTabJob.adapter?.notifyDataSetChanged()
                }
            }
            "조회순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("조회순")
                    jobList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "구인구직" -> jobList.add(it)
                        }
                    }
                    fragmentCommunityTabJobBinding.recyclerViewCommunityTabJob.adapter?.notifyDataSetChanged()
                }
            }
            "번호순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("번호순")
                    jobList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "구인구직" -> jobList.add(it)
                        }
                    }
                    fragmentCommunityTabJobBinding.recyclerViewCommunityTabJob.adapter?.notifyDataSetChanged()
                }
            }
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

    // 플로팅 버튼 리사이클러뷰 마지막에만 안 보이게
    fun settingFloatingButton() {
        fragmentCommunityTabJobBinding.apply {
            recyclerViewCommunityTabJob.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var temp: Int = 0

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (temp == 1) {
                        super.onScrolled(recyclerView, dx, dy)
                    }
                    floatingActionButtonCommunityJobAdd.visibility = View.GONE
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    floatingActionButtonCommunityJobAdd.visibility = View.VISIBLE
                    temp = 1
                }
            })
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
                communityViewModel?.textViewCommunityListDateJob?.value = jobList[position].postRegDt

                if (jobList[position].postLikeState == true) {
                    imageViewCommunityListLikeJob.setImageResource(R.drawable.heart_01)
                    textViewCommunityListLikeCntJob.setTextColor(Color.parseColor("#FFFFFFFF"))
                    communityViewModel?.textViewCommunityListLikeCntJob?.value = jobList[position].postLikeCnt.toString()
                } else {
                    imageViewCommunityListLikeJob.setImageResource(R.drawable.heart_04)
                    textViewCommunityListLikeCntJob.setTextColor(Color.parseColor("#413514"))
                    communityViewModel?.textViewCommunityListLikeCntJob?.value = jobList[position].postLikeCnt.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    // 댓글 정보를 가져온다
                    commentList = CommunityCommentDao.gettingCommunityCommentList(jobList[position].postIdx)
                    communityViewModel?.textViewCommunityListCommentCntJob?.value = commentList.size.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (jobList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, jobList[position].postImages!![0], imageViewCommunityListJob)
                    } else {
                        holder.rowCommunityTabJobBinding.imageViewCommunityListJob.visibility = View.GONE
                    }
                }

                linearLayoutCommunityListJob.setOnClickListener {
                    // 조회수
                    CoroutineScope(Dispatchers.Main).launch {
                        jobList[position].postViewCnt += 1
                        CommunityPostDao.updateCommunityPostViewCnt(jobList[position].postIdx, jobList[position].postViewCnt)
                    }

                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", jobList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeJob.setOnClickListener {
                    if (jobList[position].postLikeState == false) {
                        jobList[position].postLikeState = true
                        imageViewCommunityListLikeJob.setImageResource(R.drawable.heart_01)
                        textViewCommunityListLikeCntJob.setTextColor(Color.parseColor("#FFFFFFFF"))
                        jobList[position].postLikeCnt += 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(jobList[position], jobList[position].postLikeState)
                        }
                    } else {
                        jobList[position].postLikeState = false
                        imageViewCommunityListLikeJob.setImageResource(R.drawable.heart_04)
                        textViewCommunityListLikeCntJob.setTextColor(Color.parseColor("#413514"))
                        jobList[position].postLikeCnt -= 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(jobList[position], jobList[position].postLikeState)
                        }
                    }
                    communityViewModel?.textViewCommunityListLikeCntJob?.value = jobList[position].postLikeCnt.toString()
                }

            }
        }
    }
}