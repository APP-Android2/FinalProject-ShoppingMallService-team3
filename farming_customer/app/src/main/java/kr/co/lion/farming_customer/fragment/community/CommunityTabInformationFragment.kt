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
import kr.co.lion.farming_customer.PostType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.community.CommunityAddActivity
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabInformationBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabInformationBinding
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabInformationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_information, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabInformationBinding.communityViewModel = communityViewModel
        fragmentCommunityTabInformationBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingButtonCommunityTabInformationPopularity()
        settingFloatingActionButtonCommunityInformationAdd()
        settingRecyclerViewCommunityTabInformation()

        return fragmentCommunityTabInformationBinding.root
    }

    override fun onResume() {
        super.onResume()

        settingList()
    }

    // 커뮤니티 정보 탭 인기순이 초기 설정
    fun settingButtonCommunityTabInformationPopularity() {
        fragmentCommunityTabInformationBinding.apply {
            buttonCommunityTabInformationPopularity.isChecked = true
        }
    }

    // 리스트에 받아오기
    fun settingList() {
        CoroutineScope(Dispatchers.Main).launch {
            allList = CommunityPostDao.gettingCommunityPostList()

            allList.forEach {
                when(it.postType) {
                    "정보 게시판" -> informationList.add(it)
                }
            }

            fragmentCommunityTabInformationBinding.recyclerViewCommunityTabInformation.adapter?.notifyDataSetChanged()
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
                communityViewModel?.textViewCommunityListLikeCntInformation?.value = informationList[position].postLikeCnt.toString()
                communityViewModel?.textViewCommunityListDateInformation?.value = informationList[position].postRegDt

                CoroutineScope(Dispatchers.Main).launch {
                    if (informationList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(mainActivity, informationList[position].postImages!![0], imageViewCommunityListInformation)
                    } else {
                        holder.rowCommunityTabInformationBinding.imageViewCommunityListInformation.visibility = View.GONE
                    }
                }

                linearLayoutCommunityListInformation.setOnClickListener {
                    val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", informationList[position].postIdx)
                    startActivity(communityIntent)
                }

                imageViewCommunityListLikeInformation.setOnClickListener {
                    imageViewCommunityListLikeInformation.isSelected = !imageViewCommunityListLikeInformation.isSelected
                    textViewCommunityListLikeCntInformation.isSelected = !textViewCommunityListLikeCntInformation.isSelected
                }
            }

            if (position == informationList.size) {
                fragmentCommunityTabInformationBinding.floatingActionButtonCommunityInformationAdd.isVisible = false
            } else {
                fragmentCommunityTabInformationBinding.floatingActionButtonCommunityInformationAdd.isVisible = true
            }
        }
    }

}