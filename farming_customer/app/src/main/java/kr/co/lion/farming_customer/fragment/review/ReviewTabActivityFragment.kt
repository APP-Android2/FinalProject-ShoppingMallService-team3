package kr.co.lion.farming_customer.fragment.review

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
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.databinding.FragmentReviewTabActivityBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryActivityBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryImageActivityBinding
import kr.co.lion.farming_customer.viewmodel.review.MyPageReviewViewModel

class ReviewTabActivityFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentReviewTabActivityBinding: FragmentReviewTabActivityBinding
    lateinit var reviewActivity: ReviewActivity
    lateinit var myPageReviewViewModel: MyPageReviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentReviewTabActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_tab_activity, container, false)
        myPageReviewViewModel = MyPageReviewViewModel()
        fragmentReviewTabActivityBinding.myPageReviewViewModel = myPageReviewViewModel
        fragmentReviewTabActivityBinding.lifecycleOwner = this

        reviewActivity = activity as ReviewActivity

        settingWrittenActivityReviewCount()
        settingRecyclerViewReviewTabActivity()

        return fragmentReviewTabActivityBinding.root
    }

    // 쓴 체험활동 리뷰 개수
    fun settingWrittenActivityReviewCount() {
        fragmentReviewTabActivityBinding.myPageReviewViewModel?.textViewReviewTabActivityCount?.value = "내가 쓴 리뷰 총 80개"
    }

    // 리뷰 체험활동 탭 리사이클러뷰 설정
    fun settingRecyclerViewReviewTabActivity() {
        fragmentReviewTabActivityBinding.apply {
            recyclerViewReviewTabActivity.apply {
                adapter = ReviewTabActivityRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity)
                val deco = MaterialDividerItemDecoration(reviewActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 리뷰 체험활동 탭 리사이클러뷰 어댑터 설정
    inner class ReviewTabActivityRecyclerViewAdapter : RecyclerView.Adapter<ReviewTabActivityRecyclerViewAdapter.ReviewTabActivityViewHolder>() {
        inner class ReviewTabActivityViewHolder(rowReviewHistoryActivityBinding: RowReviewHistoryActivityBinding) : RecyclerView.ViewHolder(rowReviewHistoryActivityBinding.root) {
            val rowReviewHistoryActivityBinding: RowReviewHistoryActivityBinding

            init {
                this.rowReviewHistoryActivityBinding = rowReviewHistoryActivityBinding

                this.rowReviewHistoryActivityBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewTabActivityViewHolder {
            val rowReviewHistoryActivityBinding = DataBindingUtil.inflate<RowReviewHistoryActivityBinding>(layoutInflater, R.layout.row_review_history_activity, parent, false)
            val myPageReviewViewModel = MyPageReviewViewModel()
            rowReviewHistoryActivityBinding.myPageReviewViewModel = myPageReviewViewModel
            rowReviewHistoryActivityBinding.lifecycleOwner = this@ReviewTabActivityFragment

            val reviewTabActivityViewHolder = ReviewTabActivityViewHolder(rowReviewHistoryActivityBinding)

            // 리뷰 체험활동 탭 이미지 리사이클러뷰 설정
            rowReviewHistoryActivityBinding.recyclerViewReviewImageActivity.apply {
                adapter = ReviewImageActivityRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity, LinearLayoutManager.HORIZONTAL, false)
            }

            return reviewTabActivityViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: ReviewTabActivityViewHolder, position: Int) {
            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityDate?.value = "2024.04.03"
            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityName?.value = "파밍이네 체험활동"
            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityText?.value = "리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다."
            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityLabel?.value = "파밍이네 감자 10kg"

            holder.rowReviewHistoryActivityBinding.buttonReviewTabActivityDelete.setOnClickListener {
                val dialog = DialogYesNo(
                    this@ReviewTabActivityFragment, null, "리뷰를 삭제하시면 재작성이 불가합니다.\n" +
                            "삭제하시겠습니까?", reviewActivity, position
                )
                dialog.show(this@ReviewTabActivityFragment?.parentFragmentManager!!, "DialogYesNo")
            }
        }

        // 리뷰 체험활동 탭 이미지 리사이클러뷰 설정
        inner class ReviewImageActivityRecyclerViewAdapter : RecyclerView.Adapter<ReviewImageActivityRecyclerViewAdapter.ReviewImageActivityViewHolder>() {
            inner class ReviewImageActivityViewHolder(rowReviewHistoryImageActivityBinding: RowReviewHistoryImageActivityBinding) : RecyclerView.ViewHolder(rowReviewHistoryImageActivityBinding.root) {
                val rowReviewHistoryImageActivityBinding: RowReviewHistoryImageActivityBinding

                init {
                    this.rowReviewHistoryImageActivityBinding = rowReviewHistoryImageActivityBinding

                    this.rowReviewHistoryImageActivityBinding.root.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ReviewImageActivityViewHolder {
                val rowReviewHistoryImageActivityBinding = DataBindingUtil.inflate<RowReviewHistoryImageActivityBinding>(layoutInflater, R.layout.row_review_history_image_activity, parent, false)
                val myPageReviewViewModel = MyPageReviewViewModel()
                rowReviewHistoryImageActivityBinding.myPageReviewViewModel = myPageReviewViewModel
                rowReviewHistoryImageActivityBinding.lifecycleOwner = this@ReviewTabActivityFragment

                val reviewImageActivityViewHolder = ReviewImageActivityViewHolder(rowReviewHistoryImageActivityBinding)

                return reviewImageActivityViewHolder
            }

            override fun getItemCount(): Int {
                return 3
            }

            override fun onBindViewHolder(holder: ReviewImageActivityViewHolder, position: Int) {
                holder.rowReviewHistoryImageActivityBinding.imageViewRowReviewTabActivity.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }

    override fun onYesButtonClick(id: Int) {
        fragmentReviewTabActivityBinding.recyclerViewReviewTabActivity.adapter!!.notifyItemRemoved(id)
    }

}