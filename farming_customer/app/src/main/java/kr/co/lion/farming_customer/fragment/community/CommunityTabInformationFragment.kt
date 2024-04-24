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
import kr.co.lion.farming_customer.PostType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.community.CommunityAddActivity
import kr.co.lion.farming_customer.dao.CommunityCommentDao
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabInformationBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabInformationBinding
import kr.co.lion.farming_customer.model.CommunityCommentModel
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel
import kotlin.concurrent.thread


class CommunityTabInformationFragment : Fragment() {
    lateinit var fragmentCommunityTabInformationBinding: FragmentCommunityTabInformationBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    var allList = mutableListOf<CommunityModel>()
    // 정보 탭의 리사이클러뷰 구성을 위한 리스트
    var informationList = mutableListOf<CommunityModel>()
    // 댓글 정보를 가지고 있는 리스트
    var commentList = mutableListOf<CommunityCommentModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabInformationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_information, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabInformationBinding.communityViewModel = communityViewModel
        fragmentCommunityTabInformationBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingToggleButtonCommunityTabInformation()
        settingRecyclerViewCommunityTabInformation()
        settingFloatingActionButtonCommunityInformationAdd()
        settingFloatingButton()

        return fragmentCommunityTabInformationBinding.root
    }

    override fun onResume() {
        super.onResume()

        settingToggleButtonCommunityTabInformation()
    }

    // 커뮤니티 정보 탭의 정렬 토글
    fun settingToggleButtonCommunityTabInformation() {
        fragmentCommunityTabInformationBinding.apply {
            buttonCommunityTabInformationPopularity.isChecked = true
            handleToggleSelected("인기순")

            buttonCommunityTabInformationPopularity.setOnClickListener {
                buttonCommunityTabInformationPopularity.isChecked = true
                handleToggleSelected("인기순")
            }
            buttonCommunityTabInformationRecent.setOnClickListener {
                buttonCommunityTabInformationRecent.isChecked = true
                handleToggleSelected("최신순")
            }
            buttonCommunityTabInformationLook.setOnClickListener {
                buttonCommunityTabInformationLook.isChecked = true
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
                    informationList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "정보" -> informationList.add(it)
                        }
                    }
                    fragmentCommunityTabInformationBinding.recyclerViewCommunityTabInformation.adapter?.notifyDataSetChanged()
                }
            }
            "최신순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("최신순")
                    informationList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "정보" -> informationList.add(it)
                        }
                    }
                    fragmentCommunityTabInformationBinding.recyclerViewCommunityTabInformation.adapter?.notifyDataSetChanged()
                }
            }
            "조회순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("조회순")
                    informationList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "정보" -> informationList.add(it)
                        }
                    }
                    fragmentCommunityTabInformationBinding.recyclerViewCommunityTabInformation.adapter?.notifyDataSetChanged()
                }
            }
            "번호순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("번호순")
                    informationList.clear()
                    allList.forEach {
                        when(it.postType) {
                            "정보" -> informationList.add(it)
                        }
                    }
                    fragmentCommunityTabInformationBinding.recyclerViewCommunityTabInformation.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    // 커뮤니티 정보 탭 게시글 작성
    fun settingFloatingActionButtonCommunityInformationAdd() {
        fragmentCommunityTabInformationBinding.apply {
            floatingActionButtonCommunityInformationAdd.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityAddActivity::class.java)
                startActivity(communityIntent)

            }
        }
    }

    // 플로팅 버튼 리사이클러뷰 마지막에만 안 보이게
    fun settingFloatingButton() {
        fragmentCommunityTabInformationBinding.apply {
            recyclerViewCommunityTabInformation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var temp: Int = 0

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (temp == 1) {
                        super.onScrolled(recyclerView, dx, dy)
                    }
                    floatingActionButtonCommunityInformationAdd.visibility = View.GONE
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    floatingActionButtonCommunityInformationAdd.visibility = View.VISIBLE
                    temp = 1
                }
            })
        }
    }

    // 커뮤니티 탭 정보 리사이클러뷰 설정
    fun settingRecyclerViewCommunityTabInformation() {
        fragmentCommunityTabInformationBinding.apply {
            recyclerViewCommunityTabInformation.apply {
                adapter = CommunityTabInformationRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 커뮤니티 탭 정보 리사이클러뷰 어댑터 설정
    inner class CommunityTabInformationRecyclerViewAdapter : RecyclerView.Adapter<CommunityTabInformationRecyclerViewAdapter.CommunityTabInformationViewHolder>() {
        inner class CommunityTabInformationViewHolder(rowCommunityTabInformationBinding: RowCommunityTabInformationBinding) : RecyclerView.ViewHolder(rowCommunityTabInformationBinding.root) {
            val rowCommunityTabInformationBinding: RowCommunityTabInformationBinding

            init {
                this.rowCommunityTabInformationBinding = rowCommunityTabInformationBinding

                this.rowCommunityTabInformationBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityTabInformationViewHolder {
            val rowCommunityTabInformationBinding = DataBindingUtil.inflate<RowCommunityTabInformationBinding>(layoutInflater, R.layout.row_community_tab_information, parent,false)
            val communityViewModel = CommunityViewModel()
            rowCommunityTabInformationBinding.communityViewModel = communityViewModel
            rowCommunityTabInformationBinding.lifecycleOwner = this@CommunityTabInformationFragment

            val communityTabInformationViewHolder = CommunityTabInformationViewHolder(rowCommunityTabInformationBinding)

            return communityTabInformationViewHolder
        }

        override fun getItemCount(): Int {
            return informationList!!.size
        }

        override fun onBindViewHolder(holder: CommunityTabInformationViewHolder, position: Int) {

            holder.rowCommunityTabInformationBinding.apply {
                communityViewModel?.textViewCommunityListLabelInformation?.value = informationList[position].postType
                communityViewModel?.textViewCommunityListTitleInformation?.value = informationList[position].postTitle
                communityViewModel?.textViewCommunityListContentInformation?.value = informationList[position].postContent
                communityViewModel?.textViewCommunityListViewCntInformation?.value = informationList[position].postViewCnt.toString()
                communityViewModel?.textViewCommunityListCommentCntInformation?.value = informationList[position].postCommentCnt.toString()
                communityViewModel?.textViewCommunityListDateInformation?.value = informationList[position].postRegDt

                if (informationList[position].postLikeState == true) {
                    imageViewCommunityListLikeInformation.setImageResource(R.drawable.heart_01)
                    textViewCommunityListLikeCntInformation.setTextColor(Color.parseColor("#FFFFFFFF"))
                    communityViewModel?.textViewCommunityListLikeCntInformation?.value = informationList[position].postLikeCnt.toString()
                } else {
                    imageViewCommunityListLikeInformation.setImageResource(R.drawable.heart_04)
                    textViewCommunityListLikeCntInformation.setTextColor(Color.parseColor("#413514"))
                    communityViewModel?.textViewCommunityListLikeCntInformation?.value = informationList[position].postLikeCnt.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    // 댓글 정보를 가져온다
                    commentList = CommunityCommentDao.gettingCommunityCommentList(informationList[position].postIdx)
                    communityViewModel?.textViewCommunityListCommentCntInformation?.value = commentList.size.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (informationList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, informationList[position].postImages!![0], imageViewCommunityListInformation)
                    } else {
                        holder.rowCommunityTabInformationBinding.imageViewCommunityListInformation.setImageResource(R.color.white)
                    }
                }

                linearLayoutCommunityListInformation.setOnClickListener {
                    // 조회수
                    CoroutineScope(Dispatchers.Main).launch {
                        informationList[position].postViewCnt += 1
                        CommunityPostDao.updateCommunityPostViewCnt(informationList[position].postIdx, informationList[position].postViewCnt)
                    }

                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", informationList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeInformation.setOnClickListener {
                    if (informationList[position].postLikeState == false) {
                        informationList[position].postLikeState = true
                        imageViewCommunityListLikeInformation.setImageResource(R.drawable.heart_01)
                        textViewCommunityListLikeCntInformation.setTextColor(Color.parseColor("#FFFFFFFF"))
                        informationList[position].postLikeCnt += 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(informationList[position], informationList[position].postLikeState)
                        }
                    } else {
                        informationList[position].postLikeState = false
                        imageViewCommunityListLikeInformation.setImageResource(R.drawable.heart_04)
                        textViewCommunityListLikeCntInformation.setTextColor(Color.parseColor("#413514"))
                        informationList[position].postLikeCnt -= 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(informationList[position], informationList[position].postLikeState)
                        }
                    }
                    communityViewModel?.textViewCommunityListLikeCntInformation?.value = informationList[position].postLikeCnt.toString()
                }

            }
        }
    }

}