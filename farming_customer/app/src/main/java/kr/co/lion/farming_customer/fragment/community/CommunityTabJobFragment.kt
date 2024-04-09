package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabJobBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabJobBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabSocialBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel

class CommunityTabJobFragment : Fragment() {
    lateinit var fragmentCommunityTabJobBinding: FragmentCommunityTabJobBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabJobBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_job, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabJobBinding.communityViewModel = communityViewModel
        fragmentCommunityTabJobBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingRecyclerViewCommunityTabJob()

        return fragmentCommunityTabJobBinding.root
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
            return 100
        }

        override fun onBindViewHolder(holder: CommunityTabJobViewHolder, position: Int) {
            holder.rowCommunityTabJobBinding.textViewTemp4.text = "임시 데이터 4"

            holder.rowCommunityTabJobBinding.textViewTemp4.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                startActivity(communityIntent)
            }
        }
    }
}