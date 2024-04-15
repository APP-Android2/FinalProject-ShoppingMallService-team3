package kr.co.lion.farming_customer.fragment.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.databinding.FragmentReviewTabFarmBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryFarmBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryImageFarmBinding
import kr.co.lion.farming_customer.viewmodel.review.MyPageReviewViewModel

class ReviewTabFarmFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentReviewTabFarmBinding: FragmentReviewTabFarmBinding
    lateinit var reviewActivity: ReviewActivity
    lateinit var myPageReviewViewModel: MyPageReviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentReviewTabFarmBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_tab_farm, container, false)
        myPageReviewViewModel = MyPageReviewViewModel()
        fragmentReviewTabFarmBinding.myPageReviewViewModel = myPageReviewViewModel
        fragmentReviewTabFarmBinding.lifecycleOwner = this

        reviewActivity = activity as ReviewActivity

        settingWrittenFarmReviewCount()
        settingRecyclerViewReviewTabFarm()

        return fragmentReviewTabFarmBinding.root
    }

    // 쓴 주말농장 리뷰 개수
    fun settingWrittenFarmReviewCount() {
        fragmentReviewTabFarmBinding.myPageReviewViewModel?.textViewReviewTabFarmCount?.value = "내가 쓴 리뷰 총 90개"
    }

    // 리뷰 주말농장 탭 리사이클러뷰 설정
    fun settingRecyclerViewReviewTabFarm() {
        fragmentReviewTabFarmBinding.apply {
            recyclerViewReviewTabFarm.apply {
                adapter = ReviewTabFarmRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity)
                val deco = MaterialDividerItemDecoration(reviewActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    // 리뷰 주말농장 탭 리사이클러뷰 어댑터 설정
    inner class ReviewTabFarmRecyclerViewAdapter : RecyclerView.Adapter<ReviewTabFarmRecyclerViewAdapter.ReviewTabFarmViewHolder>() {
        inner class ReviewTabFarmViewHolder(rowReviewHistoryFarmBinding: RowReviewHistoryFarmBinding) : RecyclerView.ViewHolder(rowReviewHistoryFarmBinding.root) {
            val rowReviewHistoryFarmBinding: RowReviewHistoryFarmBinding

            init {
                this.rowReviewHistoryFarmBinding = rowReviewHistoryFarmBinding

                this.rowReviewHistoryFarmBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewTabFarmViewHolder {
            val rowReviewHistoryFarmBinding = DataBindingUtil.inflate<RowReviewHistoryFarmBinding>(layoutInflater, R.layout.row_review_history_farm, parent, false)
            val myPageReviewViewModel = MyPageReviewViewModel()
            rowReviewHistoryFarmBinding.myPageReviewViewModel = myPageReviewViewModel
            rowReviewHistoryFarmBinding.lifecycleOwner = this@ReviewTabFarmFragment

            val reviewTabFarmViewHolder = ReviewTabFarmViewHolder(rowReviewHistoryFarmBinding)

            // 리뷰 주말농장 탭 이미지 리사이클러뷰 설정
            rowReviewHistoryFarmBinding.recyclerViewReviewImageFarm.apply {
                adapter = ReviewImageFarmRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity, LinearLayoutManager.HORIZONTAL, false)
            }

            return reviewTabFarmViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: ReviewTabFarmViewHolder, position: Int) {
            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmDate?.value = "2024.04.02"
            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmName?.value = "파밍이네 주말농장"
            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmText?.value = "리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다.리뷰내용입니다."
            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmLabel?.value = "파밍이네 감자 10kg"

            holder.rowReviewHistoryFarmBinding.buttonReviewTabFarmDelete.setOnClickListener {
                val dialog = DialogYesNo(
                    this@ReviewTabFarmFragment, null, "리뷰를 삭제하시면 재작성이 불가합니다.\n" +
                            "삭제하시겠습니까?", reviewActivity, position
                )
                dialog.show(this@ReviewTabFarmFragment?.parentFragmentManager!!, "DialogYesNo")
            }
        }

        // 리뷰 주말농장 탭 이미지 리사이클러뷰 설정
        inner class ReviewImageFarmRecyclerViewAdapter : RecyclerView.Adapter<ReviewImageFarmRecyclerViewAdapter.ReviewImageFarmViewHolder>() {
            inner class ReviewImageFarmViewHolder(rowReviewHistoryImageFarmBinding: RowReviewHistoryImageFarmBinding) : RecyclerView.ViewHolder(rowReviewHistoryImageFarmBinding.root) {
                val rowReviewHistoryImageFarmBinding: RowReviewHistoryImageFarmBinding

                init {
                    this.rowReviewHistoryImageFarmBinding = rowReviewHistoryImageFarmBinding

                    this.rowReviewHistoryImageFarmBinding.root.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ReviewImageFarmViewHolder {
                val rowReviewHistoryImageFarmBinding = DataBindingUtil.inflate<RowReviewHistoryImageFarmBinding>(layoutInflater, R.layout.row_review_history_image_farm, parent, false)
                val myPageReviewViewModel = MyPageReviewViewModel()
                rowReviewHistoryImageFarmBinding.myPageReviewViewModel = myPageReviewViewModel
                rowReviewHistoryImageFarmBinding.lifecycleOwner = this@ReviewTabFarmFragment

                val reviewImageFarmViewHolder = ReviewImageFarmViewHolder(rowReviewHistoryImageFarmBinding)

                return reviewImageFarmViewHolder
            }

            override fun getItemCount(): Int {
                return 4
            }

            override fun onBindViewHolder(holder: ReviewImageFarmViewHolder, position: Int) {
                holder.rowReviewHistoryImageFarmBinding.imageViewRowReviewTabFarm.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }

    override fun onYesButtonClick(id: Int) {
        fragmentReviewTabFarmBinding.recyclerViewReviewTabFarm.adapter!!.notifyItemRemoved(id)
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }
}