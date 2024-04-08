package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.LikeType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentLikeBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.databinding.RowLikePostBinding
import kr.co.lion.farming_customer.viewmodel.LikeViewModel

class LikeFragment : Fragment() {
    lateinit var fragmentLikeBinding: FragmentLikeBinding
    lateinit var mainActivity: MainActivity

    lateinit var likeViewModel : LikeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLikeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_like, container, false)
        likeViewModel = LikeViewModel()
        fragmentLikeBinding.likeViewModel = likeViewModel
        fragmentLikeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        settingUI()
        settingToggleButton()


        return fragmentLikeBinding.root
    }

    // 초기 설정
    fun settingUI(){
        likeViewModel.settingLikeType(LikeType.CROP)
        settingRecyclerViewLikeCrop()
    }

    // toggleButton 선택에 따라 recyclerView가 바뀌는 메서드
    fun settingToggleButton(){
        fragmentLikeBinding.apply {
            toggleGroupLikeType.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if(isChecked){
                    when(likeViewModel?.toggleLikeType?.value){
                        R.id.buttonLikeCrop -> {
                            settingRecyclerViewLikeCrop()
                        }
                        R.id.buttonLikePost -> {
                            settingRecyclerViewLikePost()
                        }
                    }
                }
            }

        }
    }
    // 농산물 recyclerView 설정
    fun settingRecyclerViewLikeCrop(){
        fragmentLikeBinding.apply {
            recyclerViewLike.apply {
                adapter = LikeCropRecyclerViewAdapter()
                layoutManager = GridLayoutManager(mainActivity,2)

            }
        }
    }

    fun settingRecyclerViewLikePost(){
        fragmentLikeBinding.apply {
            recyclerViewLike.apply {
                adapter = LikePostRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 농산물 recyclerView의 Adapter
    inner class LikeCropRecyclerViewAdapter:RecyclerView.Adapter<LikeCropRecyclerViewAdapter.LikeCropViewHolder>(){
        inner class LikeCropViewHolder(rowLikeCropBinding:RowLikeCropBinding):RecyclerView.ViewHolder(rowLikeCropBinding.root){
            val rowLikeCropBinding:RowLikeCropBinding

            init {
                this.rowLikeCropBinding = rowLikeCropBinding
                this.rowLikeCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeCropViewHolder {
            val rowLikeCropBinding = RowLikeCropBinding.inflate(layoutInflater)
            val likeCropViewHolder = LikeCropViewHolder(rowLikeCropBinding)
            return likeCropViewHolder
        }

        override fun getItemCount(): Int {
            return 9
        }

        override fun onBindViewHolder(holder: LikeCropViewHolder, position: Int) {
            holder.rowLikeCropBinding.textViewLikeCropName.text = "못난이 감자$position"
            holder.rowLikeCropBinding.textViewLikeCropPrice.text = "${position}0,000원"
        }
    }

    // 게시판 recyclerView 설정

    // 게시판 recyclerView의 Adapter
    inner class LikePostRecyclerViewAdapter:RecyclerView.Adapter<LikePostRecyclerViewAdapter.LikePostViewHolder>(){
        inner class LikePostViewHolder(rowLikePostBinding: RowLikePostBinding):RecyclerView.ViewHolder(rowLikePostBinding.root){
            val rowLikePostBinding:RowLikePostBinding

            init{
                this.rowLikePostBinding = rowLikePostBinding
                this.rowLikePostBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikePostViewHolder {
            val rowLikePostBinding = RowLikePostBinding.inflate(layoutInflater)
            val likePostViewHolder = LikePostViewHolder(rowLikePostBinding)
            return likePostViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: LikePostViewHolder, position: Int) {
            holder.rowLikePostBinding.textView.text = "게시판 리스트 $position"
        }
    }
}

