package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentHomeBinding
import kr.co.lion.farming_customer.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        homeViewModel = HomeViewModel()
        fragmentHomeBinding.homeViewModel = homeViewModel
        fragmentHomeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        return fragmentHomeBinding.root
    }

//    // 추천 농산물 뷰 페이저 어댑터
//    inner class ViewPagerCropAdapter : RecyclerView.Adapter<ViewPagerCropAdapter.ViewPagerCropViewHolder>(){
//        inner class ViewPagerCropViewHolder() : RecyclerView.ViewHolder(){
//
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerCropViewHolder {
//
//        }
//
//        override fun getItemCount(): Int {
//
//        }
//
//        override fun onBindViewHolder(holder: ViewPagerCropViewHolder, position: Int) {
//
//        }
//    }
//
//    // 주말 농장, 체험활동 뷰 페이저 어댑터
//    inner class ViewPagerFarmAdapter : RecyclerView.Adapter<ViewPagerFarmAdapter.ViewPagerFarmViewHolder>(){
//        inner class ViewPagerFarmViewHolder() : RecyclerView.ViewHolder(){
//
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerFarmViewHolder {
//
//        }
//
//        override fun getItemCount(): Int {
//
//        }
//
//        override fun onBindViewHolder(holder: ViewPagerFarmViewHolder, position: Int) {
//
//        }
//    }
//
//    // 좋아요가 많은 게시판 리사이클러뷰 어댑터
//    inner class boardRecyClerViewAdatper : RecyclerView.Adapter<boardRecyClerViewAdatper.boardViewHolder>(){
//        inner class boardViewHolder() : RecyclerView.ViewHolder(){
//
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): boardViewHolder {
//
//        }
//
//        override fun getItemCount(): Int {
//
//        }
//
//        override fun onBindViewHolder(holder: boardViewHolder, position: Int) {
//
//        }
//    }
}