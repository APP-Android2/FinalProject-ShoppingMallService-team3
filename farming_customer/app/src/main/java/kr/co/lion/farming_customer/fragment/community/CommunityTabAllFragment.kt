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
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabAllBinding
import kr.co.lion.farming_customer.model.CommunityCommentModel
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel


class CommunityTabAllFragment : Fragment() {
    lateinit var fragmentCommunityTabAllBinding: FragmentCommunityTabAllBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    // 전체 탭의 리사이클러뷰 구성을 위한 리스트
    var allList = mutableListOf<CommunityModel>()
    // 댓글 정보를 가지고 있는 리스트
    var commentList = mutableListOf<CommunityCommentModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabAllBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_all, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabAllBinding.communityViewModel = communityViewModel
        fragmentCommunityTabAllBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingToggleButtonCommunityTabAll()
        settingRecyclerViewCommunityTabAll()
        settingFloatingActionButtonCommunityAllAdd()
        settingFloatingButton()

        return fragmentCommunityTabAllBinding.root
    }

    override fun onResume() {
        super.onResume()

        settingToggleButtonCommunityTabAll()
    }

    // 커뮤니티 전체 탭의 정렬 토글
    fun settingToggleButtonCommunityTabAll() {
        fragmentCommunityTabAllBinding.apply {
            buttonCommunityTabAllPopularity.isChecked = true
            handleToggleSelected("인기순")

            buttonCommunityTabAllPopularity.setOnClickListener {
                buttonCommunityTabAllPopularity.isChecked = true
                handleToggleSelected("인기순")
            }
            buttonCommunityTabAllRecent.setOnClickListener {
                buttonCommunityTabAllRecent.isChecked = true
                handleToggleSelected("최신순")
            }
            buttonCommunityTabAllLook.setOnClickListener {
                buttonCommunityTabAllLook.isChecked = true
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
                    fragmentCommunityTabAllBinding.recyclerViewCommunityTabAll.adapter?.notifyDataSetChanged()
                }
            }
            "최신순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("최신순")
                    fragmentCommunityTabAllBinding.recyclerViewCommunityTabAll.adapter?.notifyDataSetChanged()
                }
            }
            "조회순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("조회순")
                    fragmentCommunityTabAllBinding.recyclerViewCommunityTabAll.adapter?.notifyDataSetChanged()
                }
            }
            "번호순" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    allList = CommunityPostDao.gettingCommunityPostList("번호순")
                    fragmentCommunityTabAllBinding.recyclerViewCommunityTabAll.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    // 커뮤니티 전체 탭 게시글 작성
    fun settingFloatingActionButtonCommunityAllAdd() {
        fragmentCommunityTabAllBinding.apply {
            floatingActionButtonCommunityAllAdd.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityAddActivity::class.java)
                startActivity(communityIntent)
            }
        }
    }

    // 플로팅 버튼 리사이클러뷰 마지막에만 안 보이게
    fun settingFloatingButton() {
        fragmentCommunityTabAllBinding.apply {
            recyclerViewCommunityTabAll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var temp: Int = 0

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (temp == 1) {
                        super.onScrolled(recyclerView, dx, dy)
                    }
                    floatingActionButtonCommunityAllAdd.visibility = View.GONE
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    floatingActionButtonCommunityAllAdd.visibility = View.VISIBLE
                    temp = 1
                }
            })
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
            return allList.size
        }

        override fun onBindViewHolder(holder: CommunityTabAllViewHolder, position: Int) {

            holder.rowCommunityTabAllBinding.apply {
                communityViewModel?.textViewCommunityListLabelAll?.value = allList[position].postType
                communityViewModel?.textViewCommunityListTitleAll?.value = allList[position].postTitle
                communityViewModel?.textViewCommunityListContentAll?.value = allList[position].postContent
                communityViewModel?.textViewCommunityListViewCntAll?.value = allList[position].postViewCnt.toString()
                communityViewModel?.textViewCommunityListDateAll?.value = allList[position].postRegDt

                if (allList[position].postLikeState == true) {
                    imageViewCommunityListLikeAll.setImageResource(R.drawable.heart_01)
                    textViewCommunityListLikeCntAll.setTextColor(Color.parseColor("#FFFFFFFF"))
                    communityViewModel?.textViewCommunityListLikeCntAll?.value = allList[position].postLikeCnt.toString()
                } else {
                    imageViewCommunityListLikeAll.setImageResource(R.drawable.heart_04)
                    textViewCommunityListLikeCntAll.setTextColor(Color.parseColor("#413514"))
                    communityViewModel?.textViewCommunityListLikeCntAll?.value = allList[position].postLikeCnt.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    // 댓글 정보를 가져온다
                    commentList = CommunityCommentDao.gettingCommunityCommentList(allList[position].postIdx)
                    communityViewModel?.textViewCommunityListCommentCntAll?.value = commentList.size.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (allList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, allList[position].postImages!![0], imageViewCommunityListAll)
                    } else {
                        holder.rowCommunityTabAllBinding.imageViewCommunityListAll.setImageResource(R.color.white)
                    }
                }

                linearLayoutCommunityListAll.setOnClickListener {

                    // 조회수
                    CoroutineScope(Dispatchers.Main).launch {
                        allList[position].postViewCnt += 1
                        CommunityPostDao.updateCommunityPostViewCnt(allList[position].postIdx, allList[position].postViewCnt)
                    }

                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", allList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeAll.setOnClickListener {
                    if (allList[position].postLikeState == false) {
                        allList[position].postLikeState = true
                        imageViewCommunityListLikeAll.setImageResource(R.drawable.heart_01)
                        textViewCommunityListLikeCntAll.setTextColor(Color.parseColor("#FFFFFFFF"))
                        allList[position].postLikeCnt += 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(allList[position], allList[position].postLikeState)
                        }
                    } else {
                        allList[position].postLikeState = false
                        imageViewCommunityListLikeAll.setImageResource(R.drawable.heart_04)
                        textViewCommunityListLikeCntAll.setTextColor(Color.parseColor("#413514"))
                        allList[position].postLikeCnt -= 1
                        CoroutineScope(Dispatchers.Main).launch {
                            CommunityPostDao.updateCommunityPostLikeState(allList[position], allList[position].postLikeState)
                        }
                    }
                    communityViewModel?.textViewCommunityListLikeCntAll?.value = allList[position].postLikeCnt.toString()
                }
            }
        }
    }
}