package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.ReviewActivity
import kr.co.lion.farming_customer.databinding.FragmentReviewTabCropBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryCropBinding
import kr.co.lion.farming_customer.viewmodel.MyPageReviewViewModel


class ReviewTabCropFragment : Fragment() {
    lateinit var fragmentReviewTabCropBinding: FragmentReviewTabCropBinding
    lateinit var reviewActivity: ReviewActivity
    lateinit var myPageReviewViewModel: MyPageReviewViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentReviewTabCropBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_tab_crop, container, false)
        myPageReviewViewModel = MyPageReviewViewModel()
        fragmentReviewTabCropBinding.myPageReviewViewModel = myPageReviewViewModel
        fragmentReviewTabCropBinding.lifecycleOwner = this

        reviewActivity = activity as ReviewActivity

        settingWrittenCropReviewCount()
        settingRecyclerViewReviewTabCrop()

        return fragmentReviewTabCropBinding.root
    }

    // 쓴 농산품 리뷰 개수
    fun settingWrittenCropReviewCount() {
        fragmentReviewTabCropBinding.myPageReviewViewModel?.textViewReviewTabCropCount?.value = "내가 쓴 리뷰 총 100개"
    }

    // 리뷰 농산물 탭 리사이클러뷰 설정
    fun settingRecyclerViewReviewTabCrop() {
        fragmentReviewTabCropBinding.apply {
            recyclerViewReviewTabCrop.apply {
                adapter = ReviewTabCropRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity)
                val deco = MaterialDividerItemDecoration(reviewActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 리뷰 농산물 탭 리사이클러뷰 어댑터 설정
    inner class ReviewTabCropRecyclerViewAdapter : RecyclerView.Adapter<ReviewTabCropRecyclerViewAdapter.ReviewTabCropViewHolder>() {
        inner class ReviewTabCropViewHolder(rowReviewHistoryCropBinding: RowReviewHistoryCropBinding) : RecyclerView.ViewHolder(rowReviewHistoryCropBinding.root) {
            val rowReviewHistoryCropBinding: RowReviewHistoryCropBinding

            init {
                this.rowReviewHistoryCropBinding = rowReviewHistoryCropBinding

                this.rowReviewHistoryCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewTabCropViewHolder {
            val rowReviewHistoryCropBinding = DataBindingUtil.inflate<RowReviewHistoryCropBinding>(layoutInflater, R.layout.row_review_history_crop, parent, false)
            val myPageReviewViewModel = MyPageReviewViewModel()
            rowReviewHistoryCropBinding.myPageReviewViewModel = myPageReviewViewModel
            rowReviewHistoryCropBinding.lifecycleOwner = this@ReviewTabCropFragment

            val reviewTabCropViewHolder = ReviewTabCropViewHolder(rowReviewHistoryCropBinding)

            return reviewTabCropViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: ReviewTabCropViewHolder, position: Int) {
            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropDate?.value = "2024.04.01"
            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropName?.value = "파밍이네 감자"
            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropText?.value = "리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다."
            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropLabel?.value = "파밍이네 감자 10kg"

            holder.rowReviewHistoryCropBinding.buttonReviewTabCropDelete.setOnClickListener {
                val dialog = DialogYesNo(null, "리뷰를 삭제하시면 재작성이 불가합니다.\n" +
                        "삭제하시겠습니까?", reviewActivity)
                dialog.show(this@ReviewTabCropFragment?.parentFragmentManager!!, "DialogYesNo")
            }
        }
    }

}