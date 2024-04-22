package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
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
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabAllBinding
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel


class CommunityTabAllFragment : Fragment() {
    lateinit var fragmentCommunityTabAllBinding: FragmentCommunityTabAllBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    // 전체 탭의 리사이클러뷰 구성을 위한 리스트
    var allList = mutableListOf<CommunityModel>()
    // 조회 수
    var communityReadAllViewCnt = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabAllBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_all, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabAllBinding.communityViewModel = communityViewModel
        fragmentCommunityTabAllBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingButtonCommunityTabAllPopularity()
        settingList()
        settingRecyclerViewCommunityTabAll()
        settingFloatingActionButtonCommunityAllAdd()

        return fragmentCommunityTabAllBinding.root
    }

    override fun onResume() {
        super.onResume()

        settingList()
    }

    // 커뮤니티 전체 탭
    fun settingButtonCommunityTabAllPopularity() {
        fragmentCommunityTabAllBinding.apply {
            buttonCommunityTabAllPopularity.isChecked = true
        }
    }

    // 리스트에 받아오기
    fun settingList() {
        CoroutineScope(Dispatchers.Main).launch {
            allList = CommunityPostDao.gettingCommunityPostList()
            fragmentCommunityTabAllBinding.recyclerViewCommunityTabAll.adapter?.notifyDataSetChanged()

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
                communityViewModel?.textViewCommunityListCommentCntAll?.value = allList[position].postCommentCnt.toString()
                communityViewModel?.textViewCommunityListLikeCntAll?.value = allList[position].postLikeCnt.toString()
                communityViewModel?.textViewCommunityListDateAll?.value = allList[position].postRegDt

                CoroutineScope(Dispatchers.Main).launch {
                    if (allList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, allList[position].postImages!![0], imageViewCommunityListAll)
                    } else {
                        holder.rowCommunityTabAllBinding.imageViewCommunityListAll.visibility = View.GONE
                    }
                }

                linearLayoutCommunityListAll.setOnClickListener {

                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", allList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeAll.setOnClickListener {
                    imageViewCommunityListLikeAll.isSelected = !imageViewCommunityListLikeAll.isSelected
                    textViewCommunityListLikeCntAll.isSelected = !textViewCommunityListLikeCntAll.isSelected
                }
            }

            if (position == allList.size) {
                fragmentCommunityTabAllBinding.floatingActionButtonCommunityAllAdd.isVisible = false
            } else {
                fragmentCommunityTabAllBinding.floatingActionButtonCommunityAllAdd.isVisible = true
            }

        }
    }
}