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
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.FragmentCommunityTabInformationBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabAllBinding
import kr.co.lion.farming_customer.databinding.RowCommunityTabInformationBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel


class CommunityTabInformationFragment : Fragment() {
    lateinit var fragmentCommunityTabInformationBinding: FragmentCommunityTabInformationBinding
    lateinit var mainActivity: MainActivity
    lateinit var communityViewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityTabInformationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_tab_information, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityTabInformationBinding.communityViewModel = communityViewModel
        fragmentCommunityTabInformationBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingRecyclerViewCommunityTabInformation()

        return fragmentCommunityTabInformationBinding.root
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
            return 100
        }

        override fun onBindViewHolder(holder: CommunityTabInformationViewHolder, position: Int) {
            holder.rowCommunityTabInformationBinding.textViewTemp2.text = "임시 데이터 2"

            holder.rowCommunityTabInformationBinding.textViewTemp2.setOnClickListener {
                val communityIntent = Intent(mainActivity, CommunityActivity::class.java)
                startActivity(communityIntent)
            }
        }
    }

}