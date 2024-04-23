package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.community.CommunitySearchActivity
import kr.co.lion.farming_customer.dao.CommunityCommentDao
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunitySearchBinding
import kr.co.lion.farming_customer.databinding.RowCommunitySearchBinding
import kr.co.lion.farming_customer.model.CommunityCommentModel
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel

class CommunitySearchFragment : Fragment() {
    lateinit var fragmentCommunitySearchBinding: FragmentCommunitySearchBinding
    lateinit var communitySearchActivity: CommunitySearchActivity
    lateinit var communityViewModel: CommunityViewModel

    // 검색 화면의 RecyclerView 구성을 위한 리스트
    var searchList = mutableListOf<CommunityModel>()
    // 댓글 정보를 가지고 있는 리스트
    var commentList = mutableListOf<CommunityCommentModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunitySearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_search, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunitySearchBinding.communityViewModel = communityViewModel
        fragmentCommunitySearchBinding.lifecycleOwner = this

        communitySearchActivity = activity as CommunitySearchActivity

        settingEvent()
        settingTextInputCommunitySearch()
        settingRecyclerViewCommunitySearch()

        return fragmentCommunitySearchBinding.root
    }

    // 뒤로 가기
    private fun settingEvent() {
        fragmentCommunitySearchBinding.apply {
            textInputLayoutCommunitySearch.apply {
                setStartIconOnClickListener {
                    communitySearchActivity.finish()
                }
            }
        }
    }

    // 검색창 설정
    fun settingTextInputCommunitySearch() {
        fragmentCommunitySearchBinding.apply {
            textInputCommunitySearch.apply {
                setOnEditorActionListener { v, actionId, event ->
                    if (event != null && event.action == KeyEvent.ACTION_DOWN) {
                        // 검색 결과를 가져와 보여주는 메서드를 호출한다.
                        gettingSearchData()
                    }
                    true
                }
            }
        }
    }

    // 커뮤니티 검색 리사이클러뷰 설정
    fun settingRecyclerViewCommunitySearch() {
        fragmentCommunitySearchBinding.apply {
            recyclerViewCommunitySearch.apply {
                adapter = CommunitySearchRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(communitySearchActivity)
                val deco = MaterialDividerItemDecoration(communitySearchActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 커뮤니티 검색 리사이클러뷰 어댑터 설정
    inner class CommunitySearchRecyclerViewAdapter : RecyclerView.Adapter<CommunitySearchRecyclerViewAdapter.CommunitySearchViewHolder>() {
        inner class CommunitySearchViewHolder(rowCommunitySearchBinding: RowCommunitySearchBinding) : RecyclerView.ViewHolder(rowCommunitySearchBinding.root) {
            val rowCommunitySearchBinding: RowCommunitySearchBinding

            init {
                this.rowCommunitySearchBinding = rowCommunitySearchBinding

                this.rowCommunitySearchBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunitySearchViewHolder {
            val rowCommunitySearchBinding = DataBindingUtil.inflate<RowCommunitySearchBinding>(layoutInflater, R.layout.row_community_search, parent,false)
            val communityViewModel = CommunityViewModel()
            rowCommunitySearchBinding.communityViewModel = communityViewModel
            rowCommunitySearchBinding.lifecycleOwner = this@CommunitySearchFragment

            val communitySearchViewHolder = CommunitySearchViewHolder(rowCommunitySearchBinding)

            return communitySearchViewHolder
        }

        override fun getItemCount(): Int {
            return searchList.size
        }

        override fun onBindViewHolder(holder: CommunitySearchViewHolder, position: Int) {
            holder.rowCommunitySearchBinding.apply {
                communityViewModel?.textViewCommunityListLabelSearch?.value = searchList[position].postType
                communityViewModel?.textViewCommunityListTitleSearch?.value = searchList[position].postTitle
                communityViewModel?.textViewCommunityListContentSearch?.value = searchList[position].postContent
                communityViewModel?.textViewCommunityListViewCntSearch?.value = searchList[position].postViewCnt.toString()
                communityViewModel?.textViewCommunityListCommentCntSearch?.value = searchList[position].postCommentCnt.toString()
                communityViewModel?.textViewCommunityListDateSearch?.value = searchList[position].postRegDt

                CoroutineScope(Dispatchers.Main).launch {
                    // 댓글 정보를 가져온다
                    commentList = CommunityCommentDao.gettingCommunityCommentList(searchList[position].postIdx)
                    communityViewModel?.textViewCommunityListCommentCntSearch?.value = commentList.size.toString()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (searchList[position].postImages != null) {
                        CommunityPostDao.gettingCommunityPostImage(communitySearchActivity, searchList[position].postImages!![0], imageViewCommunityListSearch)
                    } else {
                        holder.rowCommunitySearchBinding.imageViewCommunityListSearch.visibility = View.GONE
                    }
                }

                linearLayoutCommunityListSearch.setOnClickListener {
                    // 조회수
                    CoroutineScope(Dispatchers.Main).launch {
                        searchList[position].postViewCnt += 1
                        CommunityPostDao.updateCommunityPostViewCnt(searchList[position].postIdx, searchList[position].postViewCnt)
                    }

                    val communityIntent = Intent(communitySearchActivity, CommunityActivity::class.java)
                    communityIntent.putExtra("postIdx", searchList[position].postIdx)
                    startActivity(communityIntent)
                }
            }

            holder.rowCommunitySearchBinding.apply {
                imageViewCommunityListLikeSearch.setOnClickListener {
                    imageViewCommunityListLikeSearch.isSelected = !imageViewCommunityListLikeSearch.isSelected
                    textViewCommunityListLikeCntSearch.isSelected = !textViewCommunityListLikeCntSearch.isSelected
                }
            }

        }
    }

    // 검색 결과의 데이터를 가져와 검색 화면의 리사이클러뷰를 갱신한다.
    fun gettingSearchData(){
        // 검색어를 가져온다.
        // SearchView에 있는 입력 요소(editText)를 추출하여 사용자가 입력한 내용을 가져온다
        val keyword = fragmentCommunitySearchBinding.textInputCommunitySearch.text.toString()

        // 검색 결과를 가지고 있는 리스트를 비워준다.
        searchList.clear()

        CoroutineScope(Dispatchers.Main).launch {
            // 현재 게시판에 해당하는 게시글을 모두 가져온다.
            val tempList = CommunityPostDao.gettingCommunityPostList("번호순")
            // 사용자 정보를 가져온다.
//            userList = UserDao.getUserAll()
            // 가져온 게시글 데이터 중에서 검색어를 포함하는 제목의 글 데이터만 담는다.
            tempList.forEach {
                if(it.postTitle.contains(keyword) || it.postContent.contains(keyword)){
                    // 검색 결과를 담는 리스트에 담아준다.
                    searchList.add(it)
                }
            }

            // 리사이클러 뷰를 갱신한다.
            fragmentCommunitySearchBinding.recyclerViewCommunitySearch.adapter?.notifyDataSetChanged()
        }
    }
}