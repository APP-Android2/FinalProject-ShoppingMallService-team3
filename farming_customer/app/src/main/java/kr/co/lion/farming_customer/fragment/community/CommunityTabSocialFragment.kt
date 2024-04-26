package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabSocialBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabSocialBinding
import kr.co.lion.farming_customer.model.CommunityCommentModel
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel

class CommunityTabSocialFragment : Fragment() {
    lateinit var fragmentCommunityTabSocialBinding: FragmentCommunityTabSocialBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    var allList = mutableListOf<CommunityModel>()
    // 소통 탭의 리사이클러뷰 구성을 위한 리스트
    var socialList = mutableListOf<CommunityModel>()
    // 댓글 정보를 가지고 있는 리스트
    var commentList = mutableListOf<CommunityCommentModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabSocialBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_social, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabSocialBinding.communityViewModel = communityViewModel
        fragmentCommunityTabSocialBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingToggleButtonCommunityTabSocial()
        settingRecyclerViewCommunityTabSocial()
        settingFloatingActionButtonCommunitySocialAdd()
        settingFloatingButton()

        return fragmentCommunityTabSocialBinding.root
    }

    override fun onResume() {
        super.onResume()

        settingToggleButtonCommunityTabSocial()
    }

    // 커뮤니티 소통 탭의 정렬 토글
    fun settingToggleButtonCommunityTabSocial() {
        fragmentCommunityTabSocialBinding.apply {
            buttonCommunityTabSocialPopularity.isChecked = true
            handleToggleSelected("인기순")

            buttonCommunityTabSocialPopularity.setOnClickListener {
                buttonCommunityTabSocialPopularity.isChecked = true
                handleToggleSelected("인기순")
            }
            buttonCommunityTabSocialRecent.setOnClickListener {
                buttonCommunityTabSocialRecent.isChecked = true
                handleToggleSelected("최신순")
            }
            buttonCommunityTabSocialLook.setOnClickListener {
                buttonCommunityTabSocialLook.isChecked = true
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
                    socialList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "소통" -> socialList.add(it)
                        }
                    }
                    fragmentCommunityTabSocialBinding.recyclerViewCommunityTabSocial.adapter?.notifyDataSetChanged()
                }
            }
            "최신순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("최신순")
                    socialList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "소통" -> socialList.add(it)
                        }
                    }
                    fragmentCommunityTabSocialBinding.recyclerViewCommunityTabSocial.adapter?.notifyDataSetChanged()
                }
            }
            "조회순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("조회순")
                    socialList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "소통" -> socialList.add(it)
                        }
                    }
                    fragmentCommunityTabSocialBinding.recyclerViewCommunityTabSocial.adapter?.notifyDataSetChanged()
                }
            }
            "번호순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("번호순")
                    socialList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "소통" -> socialList.add(it)
                        }
                    }
                    fragmentCommunityTabSocialBinding.recyclerViewCommunityTabSocial.adapter?.notifyDataSetChanged()
                }
            }
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

    // 플로팅 버튼 리사이클러뷰 마지막에만 안 보이게
    fun settingFloatingButton() {
        fragmentCommunityTabSocialBinding.apply {
            recyclerViewCommunityTabSocial.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var temp: Int = 0

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (temp == 1) {
                        super.onScrolled(recyclerView, dx, dy)
                    }
                    floatingActionButtonCommunitySocialAdd.visibility = View.GONE
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    floatingActionButtonCommunitySocialAdd.visibility = View.VISIBLE
                    temp = 1
                }
            })
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
                communityViewModel?.textViewCommunityListDateSocial?.value = socialList[position].postRegDt

                if (socialList[position].postLikeState == true) {
                    imageViewCommunityListLikeSocial.setImageResource(R.drawable.heart_01)
                    textViewCommunityListLikeCntSocial.setTextColor(Color.parseColor("#FFFFFFFF"))
                    communityViewModel?.textViewCommunityListLikeCntSocial?.value = socialList[position].postLikeCnt.toString()
                } else {
                    imageViewCommunityListLikeSocial.setImageResource(R.drawable.heart_04)
                    textViewCommunityListLikeCntSocial.setTextColor(Color.parseColor("#413514"))
                    communityViewModel?.textViewCommunityListLikeCntSocial?.value = socialList[position].postLikeCnt.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    // 댓글 정보를 가져온다
                    commentList = CommunityCommentDao.gettingCommunityCommentList(socialList[position].postIdx)
                    communityViewModel?.textViewCommunityListCommentCntSocial?.value = commentList.size.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (socialList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, socialList[position].postImages!![0], imageViewCommunityListSocial)
                    } else {
                        holder.rowCommunityTabSocialBinding.imageViewCommunityListSocial.setImageResource(R.color.white)
                    }
                }

                linearLayoutCommunityListSocial.setOnClickListener {
                    // 조회수
                    CoroutineScope(Dispatchers.Main).launch {
                        socialList[position].postViewCnt += 1
                        CommunityPostDao.updateCommunityPostViewCnt(socialList[position].postIdx, socialList[position].postViewCnt)
                    }

                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", socialList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeSocial.setOnClickListener {
                    if (socialList[position].postLikeState == false) {
                        socialList[position].postLikeState = true
                        imageViewCommunityListLikeSocial.setImageResource(R.drawable.heart_01)
                        textViewCommunityListLikeCntSocial.setTextColor(Color.parseColor("#FFFFFFFF"))
                        socialList[position].postLikeCnt += 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(socialList[position], socialList[position].postLikeState)
                        }
                    } else {
                        socialList[position].postLikeState = false
                        imageViewCommunityListLikeSocial.setImageResource(R.drawable.heart_04)
                        textViewCommunityListLikeCntSocial.setTextColor(Color.parseColor("#413514"))
                        socialList[position].postLikeCnt -= 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(socialList[position], socialList[position].postLikeState)
                        }
                    }
                    communityViewModel?.textViewCommunityListLikeCntSocial?.value = socialList[position].postLikeCnt.toString()
                }
            }
        }
    }
}