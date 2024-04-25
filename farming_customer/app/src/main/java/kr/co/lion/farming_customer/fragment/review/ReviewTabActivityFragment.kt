package kr.co.lion.farming_customer.fragment.review

import android.annotation.SuppressLint
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.ReviewState
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.dao.myPageReview.MyPageReviewDao
import kr.co.lion.farming_customer.databinding.FragmentReviewTabActivityBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryActivityBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryImageActivityBinding
import kr.co.lion.farming_customer.model.myPageReview.ReviewModel
import kr.co.lion.farming_customer.viewmodel.review.MyPageReviewViewModel

class ReviewTabActivityFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentReviewTabActivityBinding: FragmentReviewTabActivityBinding
    lateinit var reviewActivity: ReviewActivity
    lateinit var myPageReviewViewModel: MyPageReviewViewModel

    var activityReviewList = mutableListOf<ReviewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentReviewTabActivityBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_review_tab_activity,
            container,
            false
        )
        myPageReviewViewModel = MyPageReviewViewModel()
        fragmentReviewTabActivityBinding.myPageReviewViewModel = myPageReviewViewModel
        fragmentReviewTabActivityBinding.lifecycleOwner = this

        reviewActivity = activity as ReviewActivity

        settingRecyclerViewReviewTabActivity()
        gettingActivityReviewData()

        return fragmentReviewTabActivityBinding.root
    }

    // 리뷰 체험활동 탭 리사이클러뷰 설정
    fun settingRecyclerViewReviewTabActivity() {
        fragmentReviewTabActivityBinding.apply {
            recyclerViewReviewTabActivity.apply {
                adapter = ReviewTabActivityRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity)
                val deco = MaterialDividerItemDecoration(
                    reviewActivity,
                    MaterialDividerItemDecoration.VERTICAL
                )
                addItemDecoration(deco)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun gettingActivityReviewData() {
        CoroutineScope(Dispatchers.Main).launch {
            activityReviewList = MyPageReviewDao.gettingActivityReviewList()

            fragmentReviewTabActivityBinding.recyclerViewReviewTabActivity.adapter?.notifyDataSetChanged()

            val reviewCount = activityReviewList.size
            fragmentReviewTabActivityBinding.myPageReviewViewModel?.textViewReviewTabActivityCount?.value =
                "내가 쓴 리뷰 총 ${reviewCount}개"
        }
    }

    // 리뷰 체험활동 탭 리사이클러뷰 어댑터 설정
    inner class ReviewTabActivityRecyclerViewAdapter :
        RecyclerView.Adapter<ReviewTabActivityRecyclerViewAdapter.ReviewTabActivityViewHolder>() {
        inner class ReviewTabActivityViewHolder(rowReviewHistoryActivityBinding: RowReviewHistoryActivityBinding) :
            RecyclerView.ViewHolder(rowReviewHistoryActivityBinding.root) {
            val rowReviewHistoryActivityBinding: RowReviewHistoryActivityBinding

            init {
                this.rowReviewHistoryActivityBinding = rowReviewHistoryActivityBinding

                this.rowReviewHistoryActivityBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ReviewTabActivityViewHolder {
            val rowReviewHistoryActivityBinding =
                DataBindingUtil.inflate<RowReviewHistoryActivityBinding>(
                    layoutInflater,
                    R.layout.row_review_history_activity,
                    parent,
                    false
                )
            val myPageReviewViewModel = MyPageReviewViewModel()
            rowReviewHistoryActivityBinding.myPageReviewViewModel = myPageReviewViewModel
            rowReviewHistoryActivityBinding.lifecycleOwner = this@ReviewTabActivityFragment

            val reviewTabActivityViewHolder =
                ReviewTabActivityViewHolder(rowReviewHistoryActivityBinding)

            return reviewTabActivityViewHolder
        }

        override fun getItemCount(): Int {
            return activityReviewList.size
        }

        override fun onBindViewHolder(holder: ReviewTabActivityViewHolder, position: Int) {
            val activityImageList = activityReviewList[position].review_images

            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityDate?.value =
                activityReviewList[position].review_reg_dt
            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityName?.value =
                activityReviewList[position].review_title
            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityText?.value =
                activityReviewList[position].review_content
            holder.rowReviewHistoryActivityBinding.myPageReviewViewModel?.textViewRowReviewTabActivityLabel?.value =
                activityReviewList[position].review_option
            holder.rowReviewHistoryActivityBinding.ratingBarRowReviewHistoryActivity.rating =
                activityReviewList[position].review_rate.toFloat()


            holder.rowReviewHistoryActivityBinding.buttonReviewTabActivityDelete.setOnClickListener {
                val dialog = DialogYesNo(
                    this@ReviewTabActivityFragment, null, "리뷰를 삭제하시면 재작성이 불가합니다.\n" +
                            "삭제하시겠습니까?", reviewActivity, position
                )
                dialog.show(this@ReviewTabActivityFragment?.parentFragmentManager!!, "DialogYesNo")
            }

            // 리뷰 체험활동 탭 이미지 리사이클러뷰 설정
            holder.rowReviewHistoryActivityBinding.recyclerViewReviewImageActivity.apply {
                adapter = ReviewImageActivityRecyclerViewAdapter(activityImageList)
                layoutManager =
                    LinearLayoutManager(reviewActivity, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        // 리뷰 체험활동 탭 이미지 리사이클러뷰 설정
        inner class ReviewImageActivityRecyclerViewAdapter(activityImageList: MutableList<String>) :
            RecyclerView.Adapter<ReviewImageActivityRecyclerViewAdapter.ReviewImageActivityViewHolder>() {
            var activityImages = activityImageList

            inner class ReviewImageActivityViewHolder(rowReviewHistoryImageActivityBinding: RowReviewHistoryImageActivityBinding) :
                RecyclerView.ViewHolder(rowReviewHistoryImageActivityBinding.root) {
                val rowReviewHistoryImageActivityBinding: RowReviewHistoryImageActivityBinding

                init {
                    this.rowReviewHistoryImageActivityBinding = rowReviewHistoryImageActivityBinding

                    this.rowReviewHistoryImageActivityBinding.root.layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ReviewImageActivityViewHolder {
                val rowReviewHistoryImageActivityBinding =
                    DataBindingUtil.inflate<RowReviewHistoryImageActivityBinding>(
                        layoutInflater,
                        R.layout.row_review_history_image_activity,
                        parent,
                        false
                    )
                val myPageReviewViewModel = MyPageReviewViewModel()
                rowReviewHistoryImageActivityBinding.myPageReviewViewModel = myPageReviewViewModel
                rowReviewHistoryImageActivityBinding.lifecycleOwner = this@ReviewTabActivityFragment

                val reviewImageActivityViewHolder =
                    ReviewImageActivityViewHolder(rowReviewHistoryImageActivityBinding)

                return reviewImageActivityViewHolder
            }

            override fun getItemCount(): Int {
                return activityImages.size
            }

            override fun onBindViewHolder(holder: ReviewImageActivityViewHolder, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    MyPageReviewDao.gettingCropReviewPostImage(
                        requireContext(),
                        activityImages[position],
                        holder.rowReviewHistoryImageActivityBinding.imageViewRowReviewTabActivity
                    )
                }
            }
        }
    }

    override fun onYesButtonClick(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            // 글 상태를 삭제 상태로 변경한다.
            MyPageReviewDao.updateReviewState(activityReviewList[id].review_idx, ReviewState.REVIEW_STATE_REMOVE)
            gettingActivityReviewData()
        }
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }
}